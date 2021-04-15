package com.simple.rpc.network.future;

import com.simple.rpc.network.msg.Request;
import com.simple.rpc.network.msg.Response;
import io.netty.channel.Channel;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWrite {

    /**
     * 发送请求并同步获取响应
     * @param channel
     * @param request
     * @param timeout
     * @return
     */
    public Response writeAndSync(final Channel channel, final Request request, final long timeout) throws Exception {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Response> future = new SyncWriteFuture(request.getRequestId());
        SyncWriteMap.syncKey.put(request.getRequestId(), future);

        Response response = doWriteAndSync(channel, request, timeout, future);

        SyncWriteMap.syncKey.remove(request.getRequestId());
        return response;

    }

    private Response doWriteAndSync(final Channel channel, final Request request, final long timeout, final WriteFuture<Response> writeFuture) throws Exception {
        channel.writeAndFlush(request).addListener(future -> {
            writeFuture.setWriteResult(future.isSuccess());
            writeFuture.setCause(future.cause());
            //失败移除
            if (!writeFuture.isWriteSuccess()) {
                SyncWriteMap.syncKey.remove(writeFuture.requestId());
            }
        });
        //阻塞到获取服务器的返回结果
        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException();
            } else {
                throw new Exception(writeFuture.cause());
            }
        }
        return response;
    }
}

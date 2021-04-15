package com.simple.rpc.network.client;

import com.simple.rpc.network.future.SyncWriteFuture;
import com.simple.rpc.network.future.SyncWriteMap;
import com.simple.rpc.network.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端的Handler，用于读取服务器返回回来的解完码后的数据
 * 设置到同步请求中
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        Response msg = (Response)obj;
        String requestId = msg.getRequestId();
        SyncWriteFuture future = (SyncWriteFuture) SyncWriteMap.syncKey.get(requestId);
        if (future != null) {
            future.setResponse(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

package com.zzus.netty.chatrom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author wangwei
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 保留所有与服务器建立连接的channel对象
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     *服务器收到任何一个客户端的消息都会触发这个方法
     * 连接的客户端向服务端发送消息，那么其他客户端都收到这个消息，自己收到[自己]+消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}

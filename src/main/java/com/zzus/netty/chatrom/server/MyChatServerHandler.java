package com.zzus.netty.chatrom.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;

/**
 * @author wangwei
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 保留所有与服务器建立连接的channel对象
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static HashMap<String,String> map = new HashMap<>();

    /**
     *服务器收到任何一个客户端的消息都会触发这个方法
     * 连接的客户端向服务端发送消息，那么其他客户端都收到这个消息，自己收到[自己]+消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString();
        if (msg.contains("##NICKNAME##")) {
            String nickName = msg.replace("##NICKNAME##","");
            map.put(address,nickName);
        } else {
            channelGroup.forEach(ch -> {
                if (channel != ch) {
                    String name = address;
                    if (map.get(address)!=null){
                        name =  map.get(address);
                    }
                    ch.writeAndFlush("[" + name + "]-" + msg + "\n");
                } else {
                    ch.writeAndFlush("[自己]-" + msg + "\n");
                }
            });
        }
    }

    /**
     *服务端已与客户端建立连接
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //channelGroup的writeAndFlush方法是向所有连接到服务器的客户端发送消息
        channelGroup.writeAndFlush("[服务器]-"+channel.remoteAddress()+"加入\n");
        //把自己加入channelGroup中
        channelGroup.add(channel);
    }

    /**
     *
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务器]-"+channel.remoteAddress()+"离开\n");
        //每一次客户端断开连接，打印一下channelGroup的大小
        System.out.println(channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线了");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }





}

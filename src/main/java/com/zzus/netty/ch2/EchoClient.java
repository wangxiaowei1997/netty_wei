package com.zzus.netty.ch2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host,int port){
        this.port = port;
        this.host = host;
    }

    public void  start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        EchoClientHandler echoClientHandler = new EchoClientHandler();
        echoClientHandler.setSendMessage("月薪五万带带我");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoClientHandler);
                        }
                    });
            ChannelFuture future = b.connect();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] s) throws Exception{
        if(s.length!=2){
            System.out.println("Usage: "+EchoClient.class.getSimpleName()+"<host> <post>");
            return;
        }

        String host = s[0];
        int port = Integer.parseInt(s[1]);
        new EchoClient(host,port).start();
    }
}

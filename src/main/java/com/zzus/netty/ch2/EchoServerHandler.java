package com.zzus.netty.ch2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.netty.handler.codec.DateFormatter.format;
import static java.time.LocalDate.now;

/**
 * wei.wang
 * 2019-06-06
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        ByteBuf in = (ByteBuf) msg;
        String inString = in.toString(CharsetUtil.UTF_8);
        System.out.println("服务器收到：" +inString);
        SimpleDateFormat myFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = myFormat.format(new Date());
        ByteBuf out = Unpooled.copiedBuffer(inString+date, CharsetUtil.UTF_8);
        ctx.write(out);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){

        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}

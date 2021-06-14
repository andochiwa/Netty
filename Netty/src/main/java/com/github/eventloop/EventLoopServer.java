package com.github.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * @author HAN
 * @version 1.0
 * @create 06-15-1:39
 */
public class EventLoopServer {
    public static void main(String[] args){
        new ServerBootstrap()
                // 两个group，一个负责accept，一个负责read/write
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            // Object msg --- ByteBuf
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf buf = (ByteBuf) msg;
                                // 把ByteBuf转成String
                                System.out.println(Thread.currentThread() +
                                        " --- " + buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(9876);
    }
}

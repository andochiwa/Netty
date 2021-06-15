package com.github.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * @author HAN
 * @version 1.0
 * @create 06-16-4:19
 */
public class Pipeline {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 拿到pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加自定义的三个处理器
                        // inbound read
                        pipeline.addLast("inbound1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "\tinbound1 " + msg);
                                // 处理第一道工序，加工成字符串
                                ByteBuf buf = (ByteBuf) msg;
                                String str = buf.toString(Charset.defaultCharset());
                                super.channelRead(ctx, str);
                            }
                        }).addLast("inbound2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object name) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "\tinbound2 " + name);
                                // 处理第二道工序，加工成Student对象，这个对象只有一个name变量
                                Student student = new Student((String) name);
                                super.channelRead(ctx, student);
                            }
                        }).addLast("inbound3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(Thread.currentThread().getName() + "\tinbound3 " + msg);
                                // 往buffer里写一些字节
                                // 注意，如果调用ctx.writeAndFlush()，那么处理器就会从当前位置往前找outbound，而不是从尾部
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("write...".getBytes()));
                            }
                        });

                        // outbound write
                        pipeline.addLast("outbound1", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "\toutbound1 " + msg);
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("outbound2", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "\toutbound2 " + msg);
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("outbound3", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "\toutbound3 " + msg);
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(9876);
    }

    @Data
    @AllArgsConstructor
    private static class Student {
        private String name;
    }
}

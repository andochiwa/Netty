package com.github.decoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author HAN
 * @version 1.0
 * @create 06-17-7:02
 */
public class NettyToHttp {
    public static void main(String[] args){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 处理Http请求
                        ch.pipeline().addLast(new HttpServerCodec());
                        // 可以绑定想要处理的数据
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) {
                                // 获取
                                System.out.println(Thread.currentThread().getName() + "\t" + msg.uri());

                                // 返回响应给客户端
                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                                                msg.protocolVersion(), HttpResponseStatus.OK);
                                byte[] bytes = "<h1>Netty!<h1>".getBytes();
                                // 把长度也响应给客户端
                                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
                                response.content().writeBytes(bytes);

                                // 写回响应
                                ctx.writeAndFlush(response);


                            }
                        });
                    }
                })
                .bind(9876)
                .channel()
                .closeFuture()
                .addListener((ChannelFutureListener) future -> {
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                });
    }
}

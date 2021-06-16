package com.github.decoder;

import com.github.util.Utils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author HAN
 * @version 1.0
 * @create 06-17-6:34
 */
public class NettyToRedis {
    /*
    我们可以根据redis的消息协议来给redis发送请求
    set name mary:
    *3 表示三段数据
    $3 表示长度为3
    set
    $4
    name
    $4
    mary
     */
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                final byte[] line = {'\r', '\n'}; // 回车 + 换行
                                ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
                                buffer.writeBytes("*3".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$3".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("set".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$4".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("name".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$4".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("mary".getBytes());
                                buffer.writeBytes(line);
                                Utils.log(buffer);
                                ctx.writeAndFlush(buffer);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf buffer = (ByteBuf) msg;
                                Utils.log(buffer);
                            }
                        });
                    }
                })
                .connect("localhost", 6379)
                .sync()
                .channel()
                .closeFuture()
                .sync();
        group.shutdownGracefully();
    }
}

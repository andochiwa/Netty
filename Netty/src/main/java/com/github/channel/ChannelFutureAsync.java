package com.github.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author HAN
 * @version 1.0
 * @create 06-15-2:47
 */
public class ChannelFutureAsync {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // connect操作是异步的，所以获取channel也要注意异步问题
                .connect(new InetSocketAddress(9876));

        // 同步获取channel
//        Channel channel = syncGetChannel(channelFuture);

        // 异步获取channel，使用addListener(回调对象)获取
        channelFuture.addListener((ChannelFutureListener) future -> {
            Channel channel = future.channel();
            Scanner scanner = new Scanner(System.in);
            String str;
            while (!Objects.equals(str = scanner.next(), "0")) {
                System.out.println(Thread.currentThread().getName() + " --- " + str);
                channel.writeAndFlush(str);
            }
        });
    }

    // 同步获取channel
    private static Channel syncGetChannel(ChannelFuture channelFuture) throws InterruptedException {
        // 同步阻塞，直到异步线程连接完成
        channelFuture.sync();
        return channelFuture.channel();
    }
}

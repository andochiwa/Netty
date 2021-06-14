package com.github.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author HAN
 * @version 1.0
 * @create 06-15-2:59
 */
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress(9876))
                .sync()
                .channel();

        // 异步处理消息
        // 注意，不能在新线程执行关闭channel后的善后操作
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String str;
            while (!Objects.equals(str = scanner.nextLine(), "q")) {
                channel.writeAndFlush(str);
            }
            channel.close();
            System.out.println(Thread.currentThread().getName() + "\t exit");
        }).start();

        // 关闭channel
        ChannelFuture channelFuture = channel.closeFuture();
        System.out.println(Thread.currentThread().getName() + "\t waiting close......");
        // 同步关闭
        // syncCloseChannel(channelFuture);

        // 异步关闭
        channelFuture.addListener((ChannelFutureListener) future -> {
            System.out.println(Thread.currentThread().getName() + "\t execute the aftermath......");
            // 优 雅 的 关 闭 group
            group.shutdownGracefully();
        });

    }

    @SneakyThrows
    private static void syncCloseChannel(ChannelFuture channelFuture) {
        channelFuture.sync();
        System.out.println(Thread.currentThread().getName() + "\t execute the aftermath......");
    }
}

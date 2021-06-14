package com.github.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
 * @create 06-14-21:14
 */
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
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

        Scanner scanner = new Scanner(System.in);
        String str;
        while (!Objects.equals(str = scanner.next(), "0")) {
            channel.writeAndFlush(str);
        }
    }

}

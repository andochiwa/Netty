package com.github.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author HAN
 * @version 1.0
 * @create 06-14-21:14
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        // 1. 启动类
        new Bootstrap()
                // 2. 添加EventLoop，客户端可以不用
                .group(new NioEventLoopGroup())
                // 3. 选择客户端Channel实现
                .channel(NioSocketChannel.class)
                // 4. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 在连接被建立后调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress(9876))
                // 阻塞，直到连接建立
                .sync()
                // 连接对象
                .channel()
                // 向服务器发送数据
                .writeAndFlush("hello world");

    }
}

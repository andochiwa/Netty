package com.github.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author HAN
 * @version 1.0
 * @create 06-14-20:56
 */
public class HelloServer {
    public static void main(String[] args){
        // 1.启动器，负责组装netty组件协调工作
        new ServerBootstrap()
                // 2. group组，包含Selector
                .group(new NioEventLoopGroup())
                // 3. 选择服务器的Channel实现，除了NIO还有OIO(BIO)等
                .channel(NioServerSocketChannel.class)
                // 4. 分工处理，决定worker能执行哪些操作
                // ChannelInitializer代表和客户端进行数据读写的通道的初始化，负责添加别的handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    // 在连接被建立后调用
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        // 添加具体的handler
                        // StringDecoder 将传输的byteBuf类型转为字符串
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        // 自定义handler
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            // 读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(9876);
    }
}

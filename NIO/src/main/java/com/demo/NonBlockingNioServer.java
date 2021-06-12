package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-23:21
 */
public class NonBlockingNioServer {
    public static void main(String[] args) throws IOException {
        // 使用Nio来理解阻塞模式

        // 创建服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9876));

        // 切换到非阻塞状态
        serverSocketChannel.configureBlocking(false);

        // 创建连接集合和buffer
        ArrayList<SocketChannel> socketChannels = new ArrayList<>();
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        while (true) {
            // accept建立与客户端的连接，socketChannel用来与客户端通信
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println("access connect...");
                // channel也切换成非阻塞
                socketChannel.configureBlocking(false);
                socketChannels.add(socketChannel);
            }

            // 接受客户端发送的数据
            for (SocketChannel channel : socketChannels) {
                if (channel.read(byteBuffer) > 0) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        System.out.print((char) byteBuffer.get());
                    }
                    byteBuffer.clear();
                }
            }

        }
    }
}

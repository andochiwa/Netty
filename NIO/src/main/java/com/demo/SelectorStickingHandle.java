package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author HAN
 * @version 1.0
 * @create 06-13-3:40
 */
public class SelectorStickingHandle {
    static void split(ByteBuffer buffer) {
        buffer.flip();

        for (int i = 0; i < buffer.limit(); i++) {
            // 如果是换行符，代表一条完整信息
            if (buffer.get(i) == '\n') {
                // 把这条信息放入新的buffer
                int length = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从buffer中读，写入target里
                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }
                target.flip();
                while (target.hasRemaining()) {
                    System.out.print((char)target.get());
                }
            }
        }

        buffer.compact();
    }

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9876));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (var iterator = selectionKeys.iterator(); iterator.hasNext();) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    System.out.println("accept event");
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    // 把buffer当成附件一同注册进selector里
                    ByteBuffer buffer = ByteBuffer.allocate(32);
                    socketChannel.register(selector, SelectionKey.OP_READ, buffer);
                } else if (key.isReadable()) {
                    System.out.println("read event");
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 获取附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) {
                            System.out.println("client close normally......");
                            key.cancel();
                            continue;
                        }
                        split(buffer);
                        // 说明没有分割，一个消息都没读取掉，需要扩容
                        if (buffer.position() == buffer.limit()) {
                            ByteBuffer newByteBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                            newByteBuffer.put(buffer.flip());
                            // 把新的buffer替换掉，这样会循环回去读没有读完的数据，然后在split方法中打印
                            key.attach(newByteBuffer);
                        }
                    } catch (IOException e) {
                        System.out.println("client close abnormally......");
                        key.cancel();
                    }
                }

            }
        }
    }
}

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
 * @create 06-13-0:49
 */
public class SelectorNioServer {
    public static void main(String[] args) throws IOException {
        // 创建selector，管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9876));
        serverSocketChannel.configureBlocking(false);

        // 把channel注册到selector，SelectionKey是事件发生后可以通过它知道是哪个channel发生的
        // 第二个参数是指定关心哪个事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(32);

        while (true) {
            System.out.println("wait event......");
            // 没有事件发生，线程阻塞，有事件发送才恢复运行
            selector.select();

            // 处理事件，先拿到所有可用的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (var iterator = selectionKeys.iterator(); iterator.hasNext();) {
                SelectionKey key = iterator.next();
                // 拿到key后需要手动删除，只是从集合删除并非真正的删除
                iterator.remove();
                // 区分事件
                if (key.isAcceptable()) {
                    System.out.println("accept event");
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("read event");
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 这个方法会在客户端关闭后抛出异常
                        channel.read(buffer);
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            System.out.print((char) buffer.get());
                        }
                        System.out.println();
                        buffer.clear();
                    } catch (IOException e) {
                        // 为了防止抛出异常导致程序中断，需要补货并且把这个key给取消掉
                        System.out.println("client close");
                        key.cancel();
                    }
                }

            }
        }
    }
}

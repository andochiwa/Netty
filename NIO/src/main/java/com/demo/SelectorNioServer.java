package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
        SelectionKey socketKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 没有事件发生，线程阻塞，有事件发送才恢复运行
            selector.select();

            // 处理事件，先拿到所有可用的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (var iterator = selectionKeys.iterator(); iterator.hasNext();) {
                SelectionKey key = iterator.next();
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                channel.accept();
            }
        }
    }
}

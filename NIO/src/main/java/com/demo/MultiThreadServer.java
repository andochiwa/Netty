package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HAN
 * @version 1.0
 * @create 06-14-2:02
 */
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(9876));

        // 获取cpu核心数
        int cpuCore = Runtime.getRuntime().availableProcessors();
        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < cpuCore; i++) {
            workers.add(new Worker());
        }
        AtomicInteger index = new AtomicInteger(0);
        while (true) {
            selector.select();
            for (var iter = selector.selectedKeys().iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println(Thread.currentThread().getName() + " connected...... " + socketChannel.getRemoteAddress());
                    // 与worker关联
                    workers.get(index.getAndIncrement() % workers.size()).register(socketChannel);

                }
            }
        }
    }

    // 检测读写事件
    private static class Worker implements Runnable {
        private Selector selector;
        private volatile boolean start = false;
        // 线程之间通信
        private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker() {
        }

        // 需要让我们的worker线程来注册selector，而不是主线程
        public void register(SocketChannel socketChannel) throws IOException {
            if (!start) {
                Thread thread = new Thread(this);
                selector = Selector.open();
                start = true;
                thread.start();
            }
            // 向队列中添加任务
            queue.add(() -> {
                try {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 唤醒Selector，在有注册事件发生时让Selector去注册
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }

                    for (var iter = selector.selectedKeys().iterator(); iter.hasNext();) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            try {
                                int read = socketChannel.read(buffer);
                                if (read == -1) {
                                    key.cancel();
                                    continue;
                                }
                                buffer.flip();
                                System.out.print(Thread.currentThread().getName() + " ");
                                while (buffer.hasRemaining()) {
                                    System.out.print((char) buffer.get());
                                }
                                System.out.println();
                            } catch (IOException e) {
                                key.cancel();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

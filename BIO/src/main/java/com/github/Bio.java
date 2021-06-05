package com.github;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HAN
 * @version 1.0
 * @create 06-05-20:29
 */
public class Bio {

    public static void main(String[] args) throws IOException {
        // 创建一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                5,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10));

        // 创建ServerSocket, 指定6666端口
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println(Thread.currentThread().getName() + " 服务器启动了");


        for (;;) {
            // accept方法会阻塞当前线程
            Socket socket = serverSocket.accept();
            System.out.println(Thread.currentThread().getName() + " 连接到一个客户端");

            threadPoolExecutor.execute(() -> {
                // 与客户端通讯
                byte[] bytes = new byte[1024];
                try {
                    InputStream inputStream = socket.getInputStream();

                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        System.out.println(Thread.currentThread().getName() + " 读取到客户端数据 = " + new String(bytes, 0, len));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

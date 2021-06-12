package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-23:33
 */
public class BlockingNioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9876));

        socketChannel.write(Charset.defaultCharset().encode("before"));

        System.in.read();

        socketChannel.write(Charset.defaultCharset().encode("after"));
    }
}

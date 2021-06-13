package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-23:33
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9876));

        socketChannel.write(Charset.defaultCharset().encode("before"));

        Scanner scanner = new Scanner(System.in);
        String s;
        while (!(s = scanner.next()).equals("0")) {
            socketChannel.write(Charset.defaultCharset().encode(s));
        }
        socketChannel.close();
    }
}

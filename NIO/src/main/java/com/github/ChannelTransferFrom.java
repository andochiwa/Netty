package com.github;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-4:26
 */
public class ChannelTransferFrom {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\code\\java\\Netty\\NIO\\src\\main\\resources\\file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\code\\java\\Netty\\NIO\\src\\main\\resources\\file02.txt");

        FileChannel fileInputChannel = fileInputStream.getChannel();
        FileChannel fileOutputChannel = fileOutputStream.getChannel();

        // 使用transferFrom拷贝
        fileOutputChannel.transferFrom(fileInputChannel, 0, fileInputChannel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }
}

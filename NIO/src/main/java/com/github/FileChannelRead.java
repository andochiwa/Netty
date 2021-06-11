package com.github;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-3:58
 */
public class FileChannelRead {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\code\\java\\Netty\\NIO\\src\\main\\resources\\file01.txt");
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 把文件数据读取到缓冲区中
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }
}

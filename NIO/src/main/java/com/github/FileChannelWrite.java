package com.github;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-3:35
 */
public class FileChannelWrite {

    public static void main(String[] args) throws IOException {
        String str = "hello world";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\code\\java\\Netty\\NIO\\src\\main\\resources\\file01.txt");

        // 获取对应的channel 真实类型为FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str放入byteBuffer
        byteBuffer.put(str.getBytes());

        // 对buffer反转
        byteBuffer.flip();

        // 将buffer数据写入到channel
        channel.write(byteBuffer);

        fileOutputStream.close();
    }
}

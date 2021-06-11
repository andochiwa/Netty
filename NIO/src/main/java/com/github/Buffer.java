package com.github;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author HAN
 * @version 1.0
 * @create 06-11-21:52
 */
public class Buffer {
    public static void main(String[] args){
        // Buffer的使用
        try (FileChannel channel = new FileInputStream("D:\\code\\java\\Netty\\NIO\\src\\main\\resources\\file01.txt")
                .getChannel()) {
            // 读取文件数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(5);
            // 重复去读取数据
            for (int read = channel.read(byteBuffer); read != -1; read = channel.read(byteBuffer)) {
                // 切换到读模式
                byteBuffer.flip();
                // 读取buffer内数据
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                // 切换到写模式
                byteBuffer.clear();
            }
        } catch (IOException ignored) {
        }

    }
}

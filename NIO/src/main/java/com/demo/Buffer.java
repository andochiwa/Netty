package com.demo;

import java.nio.ByteBuffer;

/**
 * @author HAN
 * @version 1.0
 * @create 06-12-7:01
 */
public class Buffer {
    public static void main(String[] args){
        /*
        黏包练习
        初始数据为
        Hello, world\n
        I'm mary\n
        How are you?

        黏包后
        Hello, world\nI'm mary\nHo
        w are you\n
        要求编写程序，将错乱的数据恢复正常
         */
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("Hello, world\nI'm mary\nHo".getBytes());
        split(buffer);
        buffer.put("w are you?\n".getBytes());
        split(buffer);


    }

    static void split(ByteBuffer buffer) {
        buffer.flip();

        for (int i = 0; i < buffer.limit(); i++) {
            // 如果是换行符，代表一条完整信息
            if (buffer.get(i) == '\n') {
                // 把这条信息放入新的buffer
                int length = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从buffer中读，写入target里
                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }
                target.flip();
                while (target.hasRemaining()) {
                    System.out.print((char)target.get());
                }
            }
        }

        buffer.compact();
    }
}

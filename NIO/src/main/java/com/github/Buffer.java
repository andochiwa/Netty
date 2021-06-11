package com.github;

import java.nio.IntBuffer;

/**
 * @author HAN
 * @version 1.0
 * @create 06-11-21:52
 */
public class Buffer {
    public static void main(String[] args){
        // Buffer的使用
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i);
        }
        // 将buffer转换，读写切换
        intBuffer.flip();
        // 取数据
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}

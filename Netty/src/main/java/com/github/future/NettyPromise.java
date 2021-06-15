package com.github.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.SneakyThrows;

/**
 * @author HAN
 * @version 1.0
 * @create 06-16-4:00
 */
public class NettyPromise {
    @SneakyThrows
    public static void main(String[] args){
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        // 主动创建promise，结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tcompute start...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 装入promise
            promise.setSuccess(100);
        }).start();

        System.out.println(Thread.currentThread().getName() + "\twait result...");
        // 接收结果
        Integer integer = promise.get();
        System.out.println(Thread.currentThread().getName() + "\tresult = " + integer);
    }
}

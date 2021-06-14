package com.github.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.SneakyThrows;

/**
 * @author HAN
 * @version 1.0
 * @create 06-15-5:05
 */
public class NettyFuture {
    @SneakyThrows
    public static void main(String[] args){
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventLoop = group.next();

        Future<Integer> future = eventLoop.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "\t进行计算");
            Thread.sleep(1000);
            return 100;
        });

        System.out.println(Thread.currentThread().getName() + "\t等待结果");

        // 同步方式
//        System.out.println(Thread.currentThread().getName() + "\t结果：" + future.get());

        // 异步方式
        future.addListener(submit -> {
            System.out.println(Thread.currentThread().getName() + "\t结果：" + submit.get());
        });

        System.out.println(Thread.currentThread().getName() + "\t结束");

    }
}

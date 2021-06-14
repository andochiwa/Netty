package com.github.eventloop;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * @author HAN
 * @version 1.0
 * @create 06-14-23:53
 */
public class CommonTask {
    public static void main(String[] args){
        // 1. 创建事件循环组
        // NioEventLoopGroup能处理普通任务，定时任务和IO事件
        NioEventLoopGroup group = new NioEventLoopGroup();
        // DefaultEventLoopGroup不能处理IO事件
        // DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        // 执行普通任务
        group.next().execute(() -> {
            System.out.println(Thread.currentThread().getName() + " --- common task ok");
        });

        // 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + " --- schedule task ok");
        }, 0, 1, TimeUnit.SECONDS);

    }
}

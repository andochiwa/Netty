package com.github.bytebuf;

import com.github.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author HAN
 * @version 1.0
 * @create 06-16-5:36
 */
public class NettyByteBuf {
    public static void main(String[] args){
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        Utils.log(buf);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("i".repeat(32));

        buf.writeBytes(stringBuilder.toString().getBytes());
        Utils.log(buf);
    }
}

package com.github.decoder;

import com.github.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author HAN
 * @version 1.0
 * @create 06-17-6:07
 */
public class NettyLengthFieldDecoder {
    public static void main(String[] args){
        EmbeddedChannel channel = new EmbeddedChannel(
                /*
                  maxFrameLength: 发送的数据帧的最大长度
                  lengthFieldOffset: 数据帧中发送的长度的下标位置
                  lengthFieldLength: 发送的长度的字节数
                  lengthAdjustment: 发送额外内容的字节数
                  initialBytesToStrip: 接收到的发送数据包，去掉前initialBytesToStrip位(例如可以把长度位去掉)
                 */
                new LengthFieldBasedFrameDecoder(
                        1024, 0, 4, 2, 4));

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "hello");
        send(buf, "hi");
        channel.writeInbound(buf);

        ByteBuf readInbound;
        while ((readInbound = channel.readInbound()) != null) {
            Utils.log(readInbound);
        }
    }

    private static void send(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes(); // 实际内容
        int length = bytes.length; // 实际内容长度
        buffer.writeInt(length);
        buffer.writeBytes("1:".getBytes()); // 额外内容
        buffer.writeBytes(bytes);
    }
}

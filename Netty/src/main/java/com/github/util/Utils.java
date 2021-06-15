package com.github.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;

/**
 * @author HAN
 * @version 1.0
 * @create 06-16-5:49
 */
public class Utils {
    public static void log(ByteBuf buf) {
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buffer = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity").append(buf.capacity())
                .append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(buffer, buf);
        System.out.println(buffer.toString());
    }
}

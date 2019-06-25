package test;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;
/**
 * Created by zhaoxiang.liu on 2017/3/20.
 */
public class ByteBufTest {
    Charset utf8 = Charset.forName("UTF-8");
    @Test
    public void test1() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = byteBuf.slice(0, 14);
        System.out.println(sliced.toString(utf8));
        byteBuf.setByte(0, (byte) 'J');
        assert byteBuf.getByte(0) == sliced.getByte(0);// 数据共享(尽量用此方式节省开销)
    }
    @Test
    public void test2() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = byteBuf.copy(0, 14);
        System.out.println(sliced.toString(utf8));
        byteBuf.setByte(0, (byte) 'J');
        assert byteBuf.getByte(0) == sliced.getByte(0);// copy数据非共享
    }
    @Test
    public void test3() {
        // 创建一个ByteBuf存字符串
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第n个字符
        System.out.println((char) byteBuf.getByte(0));
        // 保存当前读写index
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        // 将索引0更新
        byteBuf.setByte(0, (byte) 'B');
        // 打印第一字符，为之前更新的字符
        System.out.println((char) byteBuf.getByte(0));
        // 断言 set和get不改变索引
        assert readerIndex == byteBuf.readerIndex();
        assert writerIndex == byteBuf.writerIndex();
    }

    @Test
    public void test4() {
        // 创建一个ByteBuf存字符串
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第n个字符
        System.out.println((char) byteBuf.readByte());
        // 保存当前读写index
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        System.out.println(writerIndex);
        // 将索引0更新 不足时会增大写空间
        byteBuf.writeBytes("1234567890-1234567890-1234567890-1234567890--".getBytes());
        // 打印第一字符，为之前更新的字符
        System.out.println((char) byteBuf.getByte(0));
        System.out.println(byteBuf.writerIndex());
        System.out.println(byteBuf.writableBytes());
        System.out.println("capacity:"+byteBuf.capacity());
        // 断言 set和get不改变索引
        assert readerIndex == byteBuf.readerIndex();
        assert writerIndex != byteBuf.writerIndex();
    }
}

package test;

import com.tone.netty.inaction.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhaoxiang.liu on 2017/7/3.
 */
public class FixedLengthFrameDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        Assert.assertFalse(channel.writeInbound(input.readBytes(2))); // 4
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));
        Assert.assertTrue(channel.finish()); // 5
        ByteBuf read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        Assert.assertNull(channel.readInbound());
        buf.release();
    }
}

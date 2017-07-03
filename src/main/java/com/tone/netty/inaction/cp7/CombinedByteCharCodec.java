package com.tone.netty.inaction.cp7;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Created by echolau on 2017/6/24.
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}

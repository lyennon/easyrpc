package com.lyennon.remoting.netty;

import com.lyennon.common.protocol.EasyProtocol;
import com.lyennon.common.serialization.SerializerHolder;
import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yong.liang 2019/12/8
 */
public class RemotingTransporterEncoder extends MessageToByteEncoder<RemotingTransporter> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingTransporter msg, ByteBuf out) throws Exception {
//        byte[] bytes = SerializerHolder.get().writeObject(msg);
        byte[] bytes = msg.getBytes();
        out.writeShort(EasyProtocol.MAGIC)
            .writeByte(msg.getTransporterType())
            .writeByte(msg.getCode())
            .writeLong(msg.getOpaque())
            .writeInt(bytes.length)
            .writeBytes(bytes);
    }
}

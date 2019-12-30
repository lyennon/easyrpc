package com.lyennon.remoting.netty;

import com.lyennon.common.exception.RemotingContextException;
import com.lyennon.common.protocol.EasyProtocol;
import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/**
 * @author yong.liang 2019/12/11
 */

public class RemotingTransporterDecoder extends ReplayingDecoder<Void> {

    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short magic = in.readShort();
        checkMagic(magic);
        byte headType = in.readByte();
        byte sign = in.readByte();
        long id = in.readLong();
        int bodyLength = in.readInt();
        checkBodyLength(bodyLength);
        byte[] body = new byte[bodyLength];
        in.readBytes(body);
        RemotingTransporter transporter = RemotingTransporter.createTransporter(sign, id, headType, body);
        out.add(transporter);
    }

    private void checkMagic(short magic) throws RemotingContextException{
        if (EasyProtocol.MAGIC != magic){
            throw new RemotingContextException("magic value is not equal "+EasyProtocol.MAGIC );
        }
    }

    private void checkBodyLength(int bodyLength) throws RemotingContextException {
        if (bodyLength > MAX_BODY_SIZE) {
            throw new RemotingContextException("body of request is bigger than limit value " + MAX_BODY_SIZE);
        }
    }
}

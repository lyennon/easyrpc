package com.lyennon.remoting.model;

import com.lyennon.common.protocol.EasyProtocol;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yong.liang 2019/12/1
 */
public class RemotingTransporter extends ByteHolder {

    private static final AtomicLong requestId = new AtomicLong(0L);

    private byte code;

    private transient CommonCustomBody customBody;

    /**
     * 请求的时间戳
     */
    private transient long timestamp;

    /**
     * 定义该传输对象是请求还是响应信息
     */
    private byte transporterType;

    /**
     * 请求的id
     */
    private long opaque = requestId.getAndIncrement();


    protected RemotingTransporter() {
    }

    public static RemotingTransporter createRequestTransporter(byte code, CommonCustomBody customBody){
        RemotingTransporter transporter = new RemotingTransporter();
        transporter.setCode(code);
        transporter.setCustomBody(customBody);
        transporter.setTransporterType(EasyProtocol.REQUEST_REMOTING);
        return transporter;
    }

    public static RemotingTransporter createResponseTransporter(byte code, CommonCustomBody customBody,long opaque){
        RemotingTransporter transporter = new RemotingTransporter();
        transporter.setCode(code);
        transporter.setCustomBody(customBody);
        transporter.setTransporterType(EasyProtocol.RESPONSE_REMOTING);
        transporter.setOpaque(opaque);
        return transporter;
    }

    public static RemotingTransporter createTransporter(byte code, long opaque,byte type,byte[] body){
        RemotingTransporter transporter = new RemotingTransporter();
        transporter.setCode(code);
        transporter.setTransporterType(type);
        transporter.setOpaque(opaque);
        transporter.setBytes(body);
        return transporter;
    }
    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public CommonCustomBody getCustomBody() {
        return customBody;
    }

    public void setCustomBody(CommonCustomBody customBody) {
        this.customBody = customBody;
    }

    public byte getTransporterType() {
        return transporterType;
    }

    public void setTransporterType(byte transporterType) {
        this.transporterType = transporterType;
    }

    public long getOpaque() {
        return opaque;
    }

    public void setOpaque(long opaque) {
        this.opaque = opaque;
    }
}

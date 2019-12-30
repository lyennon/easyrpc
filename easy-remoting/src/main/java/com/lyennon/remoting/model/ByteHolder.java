package com.lyennon.remoting.model;

/**
 * @author yong.liang 2019/12/1
 */
public class ByteHolder {

    private transient byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int size() {
        return bytes == null ? 0 : bytes.length;
    }
}

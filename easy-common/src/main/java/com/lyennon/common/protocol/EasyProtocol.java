package com.lyennon.common.protocol;

/**
 * @author yong.liang 2019/12/1
 */
public class EasyProtocol {

    /** Magic */
    public static final short MAGIC = (short) 0xbabe;

    public static final int HEAD_LENGTH = 16;

    public static final byte REQUEST_REMOTING = 1;

    public static final byte RESPONSE_REMOTING = 2;
}

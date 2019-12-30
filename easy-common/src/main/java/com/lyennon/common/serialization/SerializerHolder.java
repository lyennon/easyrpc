package com.lyennon.common.serialization;

import com.lyennon.common.spi.BaseServiceLoader;

/**
 * @author yong.liang 2019/12/8
 */
public final class SerializerHolder {

    private static final Serializer serializer = BaseServiceLoader.load(Serializer.class);

    public static Serializer get(){
        return serializer;
    }
}

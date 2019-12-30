package com.lyennon.common.spi;

import java.util.ServiceLoader;

/**
 * @author yong.liang 2019/12/8
 */
public final class BaseServiceLoader {

    public static <S> S load(Class<S> serviceClass) {
        return ServiceLoader.load(serviceClass).iterator().next();
    }

}

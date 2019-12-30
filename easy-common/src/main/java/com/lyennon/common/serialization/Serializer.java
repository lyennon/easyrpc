package com.lyennon.common.serialization;

public interface Serializer {

    <T> byte[] writeObject(T object);

    <T> T readObject(byte[] bytes,Class<T> clz);
}

package com.lyennon.common.serialization;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * @author yong.liang 2019/12/8
 */
public class ProtobufSerializer implements Serializer{

    private static Map<Class<?>,Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    @Override
    public <T> byte[] writeObject(T object) {
        Class<T> clz = (Class<T>) object.getClass();
        Schema<T> schema = getSchema(clz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(object, schema, buffer);
    }

    @Override public <T> T readObject(byte[] bytes, Class<T> clz) {
        Schema<T> schema = getSchema(clz);
        T object = objenesis.newInstance(clz);
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }


    private static <T> Schema<T> getSchema(Class<T> clz) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(clz);
        if (schema == null){
            schema = RuntimeSchema.createFrom(clz);
            cachedSchema.put(clz,schema);
        }
        return schema;
    }

}

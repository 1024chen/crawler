package com.example.crawler.util.serializer;

import com.example.crawler.model.http.lingang.Empty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * 使用: @JsonSerialize(nullsUsing = NullObjectToEmptySerializer.class)
 * 效果：null对象会被转换为空对象 {}
 */
public class NullObjectToEmptySerializer extends JsonSerializer<Empty> {
    @Override
    public void serialize(Empty o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //创建空对象节点 效果为{}
        ObjectNode objectNode = objectMapper.createObjectNode();
        //如果处理的字段为数组，则需要创建数组节点 []
        //ArrayNode arrayNode = objectMapper.createArrayNode();
        jsonGenerator.writeObject(objectNode);
    }
}
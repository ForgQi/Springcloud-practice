package com.forgqi.resourcebaseserver.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@JsonComponent
public class DateFormatConfig extends JsonSerializer<Instant> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式化
     */
    @Override
    public void serialize(Instant date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(LocalDateTime.ofInstant(date, ZoneId.systemDefault()).format(formatter));
    }

}
//@JsonComponent
//public class DateFormatConfig {
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
//
//    /**
//     * 日期格式化
//     */
////    public static class DateJsonSerializer extends JsonSerializer<Instant> {
////        @Override
////        public void serialize(Instant date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
////            jsonGenerator.writeString(formatter.format(date));
////        }
////    }
//
//    /**
//     * 解析日期字符串
//     */
//    public static class DateJsonDeserializer extends JsonDeserializer<LocalDateTime> {
//        @Override
//        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
//            return LocalDateTime.parse(jsonParser.getText(), formatter);
//        }
//    }
//}

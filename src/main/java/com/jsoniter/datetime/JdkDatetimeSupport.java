package com.jsoniter.datetime;

import com.jsoniter.JsonException;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Decoder;
import com.jsoniter.spi.Encoder;
import com.jsoniter.spi.JsoniterSpi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * this is just an example
 */
public class JdkDatetimeSupport {

    private static String pattern;
    private final static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(pattern);
        }
    };

    public static void enable(String pattern) {
        JdkDatetimeSupport.pattern = pattern;
        JsoniterSpi.registerTypeEncoder(Date.class, new Encoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                stream.writeVal(sdf.get().format(obj));
            }

            @Override
            public Any wrap(Object obj) {
                return Any.wrap(sdf.get().format(obj));
            }
        });
        JsoniterSpi.registerTypeDecoder(Date.class, new Decoder() {
            @Override
            public Object decode(JsonIterator iter) throws IOException {
                try {
                    return sdf.get().parse(iter.readString());
                } catch (ParseException e) {
                    throw new JsonException(e);
                }
            }
        });
    }
}

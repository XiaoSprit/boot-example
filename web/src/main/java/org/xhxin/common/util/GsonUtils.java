package org.xhxin.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.xhxin.common.DateDeserializer;
import org.xhxin.common.DateSerializer;

import java.text.DateFormat;

public class GsonUtils {
    private static Gson instance = null;
    public synchronized static Gson getGson(){
        if(instance == null) {
            GsonBuilder builder = new GsonBuilder();
//            builder.addSerializationExclusionStrategy(new IgnoreStrategy());
            builder.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
            builder.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
            instance = builder.create();
        }
        return instance;
    }
}
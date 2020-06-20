package org.xhxin.common.util;


public class IDUtils {
    private static final IDGenerate worker = new IDGenerate();
    public static String generateId() {
        long id = worker.nextId();
        return String.valueOf(id);
    }



}
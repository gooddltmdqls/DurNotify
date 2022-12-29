package kr.icetang0123.durnotify.client.utils;

public abstract class Util {
    public static int integerOrDouble(Object something) {
        if (something instanceof Double) return ((Double) something).intValue();
        else return (Integer) something;
    }
}
package io.github.devbobos.quicksell.helper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.devbobos.quicksell.Base;

public class Utils {
    public static boolean notNull(Object object) {
        if(object == null) {
            return false;
        }
        else {
            return true;
        }
    }
    public static boolean isNull(Object object) {
        if(object == null) {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean hasItems(List list) {
        boolean hasItems = false;
        if(notNull(list)) {
            if(list.size() > 0) {
                hasItems = true;
            }
        }
        else {
            hasItems = false;
        }

        return hasItems;
    }
    public static boolean isEmpty(List list) {
        boolean isEmpty = false;
        if(isNull(list)) {
            isEmpty = true;
        }
        else {
            if(list.size() == 0) {
                isEmpty = true;
            }
        }
        return isEmpty;
    }
    public static boolean hasItems(Set list) {
        boolean hasItems = false;
        if(notNull(list)) {
            if(list.size() > 0) {
                hasItems = true;
            }
        }
        else {
            hasItems = false;
        }

        return hasItems;
    }
    public static boolean isEmpty(Set list) {
        boolean isEmpty = false;
        if(isNull(list)) {
            isEmpty = true;
        }
        else {
            if(list.size() == 0) {
                isEmpty = true;
            }
        }
        return isEmpty;
    }
    public static boolean hasItems(Map map)
    {
        return !map.isEmpty();
    }
    public static boolean isEmpty(Map map) { return map.isEmpty(); }

    public static boolean isEmpty(CharSequence text)
    {
        return TextUtils.isEmpty(text);
    }
    public static boolean hasText(CharSequence text) {
        return !TextUtils.isEmpty(text);
    }

    public static boolean not(boolean result){
        return !result;
    }
    public static String toString(Bundle bundle){
        if(isNull(bundle)){
            return "";
        }
        if(bundle.isEmpty()){
            return "";
        }
        Set<String> keySet = bundle.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : keySet){
            stringBuilder.append("[");
            stringBuilder.append(key);
            stringBuilder.append(":");
            stringBuilder.append(bundle.get(key));
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    //Preference
    public static void put(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(Base.context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static void put(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(Base.context).edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static void put(String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(Base.context).edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    public static void put(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(Base.context).edit();
        editor.putLong(key, value);
        editor.commit();
    }
    public static void put(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(Base.context).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static boolean get(String key, boolean defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(Base.context);
        return sharedPreferences.getBoolean(key, defValue);
    }
    public static int get(String key, int defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(Base.context);
        return sharedPreferences.getInt(key, defValue);
    }
    public static float get(String key, float defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(Base.context);
        return sharedPreferences.getFloat(key, defValue);
    }
    public static long get(String key, long defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(Base.context);
        return sharedPreferences.getLong(key, defValue);
    }
    public static String get(String key, String defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(Base.context);
        return sharedPreferences.getString(key, defValue);
    }

    private static SharedPreferences getSharedPreferences(Context context){
        final String name = context.getPackageName()+".preference";
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}

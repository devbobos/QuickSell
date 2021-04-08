package io.github.devbobos.quicksell;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
}

package com.example.william.my.library.utils;

import java.util.List;

public class StringUtils {

    public static String listToString(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.substring(0, sb.toString().length() - 1);
    }
}

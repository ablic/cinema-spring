package com.ablic.cinema.helpers;

import java.util.List;

public class StringListBuilder {
    public static String get(List<String> strs, char separator) {

        if (strs.isEmpty()) {
            return "";
        }

        if (strs.size() == 1) {
            return strs.get(0);
        }

        StringBuilder builder = new StringBuilder();

        for (String str : strs) {
            builder.append(str).append(separator).append(' ');
        }
        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }
}

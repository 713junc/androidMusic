package com.cse110.ucsd.flashbackmusicproject.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * StringArrayParser sets up strings for SQL queries.
 */

public class StringArrayParser {

    public static String toString(List<String> array) {
        String builder = "";
        for (int i = 0; i < array.size(); i++) {
            builder += array.get(i) + ",";
        }
        return builder;
    }

    public static List<String> toArray(String string) {
        List<String> array = new ArrayList<>();
        while (string.contains(",")) {
            array.add(string.substring(0, string.indexOf(",")));
            string = string.substring(string.indexOf(",") + 1);
        }
        return array;
    }
}

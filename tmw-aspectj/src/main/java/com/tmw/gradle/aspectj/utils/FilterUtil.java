package com.tmw.gradle.aspectj.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect默认过滤
 *
 * @author tanmingwu848
 * @since 2021/09/15
 */
public class FilterUtil {

    private static final List<String> CLASS_FILTERS = new ArrayList<>();
    private static final List<String> METHOD_FILTERS = new ArrayList<>();
    private static final String SPECIAL = "$";

    static {
        CLASS_FILTERS.add("BuildConfig.class");
        CLASS_FILTERS.add("R.class");
        CLASS_FILTERS.add("R$");

        METHOD_FILTERS.add("<init>");
        METHOD_FILTERS.add("<clinit>");
        METHOD_FILTERS.add("invoke");
    }

    private static boolean isFilter(String name) {
        for (String tag : CLASS_FILTERS) {
            if (name.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAvailable(String className) {
        if (isFilter(className)) {
            return false;
        }

        return className.endsWith(".class");
    }

    public static boolean isMethodAvailable(String methodName) {
        return !METHOD_FILTERS.contains(methodName) /*&& !methodName.contains(SPECIAL)*/;
    }


}

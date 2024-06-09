package com.example.demo.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomMap {

    public static <K, V> Map<K, V> of(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Số lượng key phải bằng số lượng value.");
        }
        Map<K, V> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put((K) keyValues[i], (V) keyValues[i + 1]);
        }
        return map;
    }
}

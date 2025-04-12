package com.zscore_api.zscore_api.helper;

import java.util.HashSet;
import java.util.Set;

public class SetOps {

    public static int numIntersecting(Set<String> setA, Set<String> setB) {
        Set<String> copy = new HashSet<>(setA);
        copy.retainAll(setB);
        return copy.size();
    }

    public static Set<String> union(Set<String> setA, Set<String> setB) {
        Set<String> copy = new HashSet<>(setA);
        copy.addAll(setB);
        return copy;
    }

    public static Set<String> subtract(Set<String> setA, Set<String> setB) {
        Set<String> copy = new HashSet<>(setA);
        copy.removeAll(setB);
        return copy;
    }
}

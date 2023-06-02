package com.betelgeuse.blockchain.core.indicator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
public enum Period {
    sevenDay(7),
    tenDay(10),
    fourteenDay(14),
    twentyDay(20);
    private static final Map<Integer, Period> lookup = new HashMap<Integer, Period>();

    static {
        for (Period w : EnumSet.allOf(Period.class))
            lookup.put(w.getCode(), w);
    }

    private int code;
    Period (int code) {
        this.code = code;
    }
    public int getCode ( ) {
        return code;
    }

    public static Period get (int code) {
        return lookup.get(code);
    }
}

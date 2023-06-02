package com.betelgeuse.blockchain.core.indicator;

public class Signal {
    public  Signal(Data data) {
    }
    public  String rsiResult;
    public  String bollingerBandResult;
    public String rsiALSat(double rsi) {
        String message = "";
        if (rsi > 85) message = "G.SAT";
        else if (rsi >= 60) message = "SAT";
        else if (rsi >= 45) message = "Al";
        else if (rsi >= 0) message = "G.Al";
        return message;
    }
    public String bollingAlSat(double p, double lB, double uB, double mB) {
        String msg = "";
        String dot = "";

        if (p > (uB + mB) / 2) msg = "G.SAT";
        if (p < (lB + mB) / 2 && p > mB) msg = "SAT";
        if (p > (lB + mB) / 2 && p < mB) msg = "Al";
        if (p < (uB + mB) / 2) msg = "G.Al";


        return msg;
    }
}

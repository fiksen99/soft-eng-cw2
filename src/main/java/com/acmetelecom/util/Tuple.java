package com.acmetelecom.util;

public class Tuple<T,U> {
    private T fst;
    private U snd;

    public Tuple() {

    }

    public Tuple(T fst, U snd) {
        setFst(fst);
        setSnd(snd);
    }

    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public U getSnd() {
        return snd;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }


}

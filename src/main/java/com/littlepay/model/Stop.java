package com.littlepay.model;

public enum Stop {
    Stop1(0),
    Stop2(1),
    Stop3(2);

    private int index;

    Stop(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

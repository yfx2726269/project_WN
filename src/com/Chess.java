package com;

/**
 * @author bitYang
 * @create 2022-04 -07 10:30
 */
public class Chess {
    private int rx;
    private int ry;
    private boolean isBlack;

    public Chess() {
    }

    public Chess(int rx, int ry, boolean isBlack) {
        this.rx = rx;
        this.ry = ry;
        this.isBlack = isBlack;
    }

    public int getRx() {
        return rx;
    }

    public int getRy() {
        return ry;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

    @Override
    public String toString() {
        return "Chess{" +
                "rx=" + rx +
                ", ry=" + ry +
                ", isBlack=" + isBlack +
                '}';
    }

    //用于判断两个棋子是否是同一颜色
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chess chess = (Chess) o;
        return isBlack == chess.isBlack;
    }
}

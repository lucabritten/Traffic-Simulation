package com.britten.domain;

public class Road {

    private Intersection from;
    private Intersection to;
    private int length;

    public Road(Intersection from, Intersection to, int length) {
        this.from = from;
        this.to = to;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public Intersection getTo() {
        return to;
    }

    public Intersection getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "Road{" +
                "from=" + from.getId() +
                ", to=" + to.getId() +
                ", length=" + length +
                '}';
    }
}

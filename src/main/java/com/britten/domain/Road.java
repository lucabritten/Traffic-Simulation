package com.britten.domain;

public class Road {

    private Intersection from;
    private Intersection to;
    private int length;

    public Road(Intersection from, Intersection to, int length) {
        if(length <= 0)
            throw new IllegalArgumentException("length cannot be negative or zero");
        if(from == null || to == null)
            throw new IllegalArgumentException("Intersection from and to are not allowed to be null!");

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

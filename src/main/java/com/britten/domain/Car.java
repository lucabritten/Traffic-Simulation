package com.britten.domain;

public class Car extends Vehicle{

    private static final int CAR_SPEED = 5;
    public Car(int id, Road currentRoad){
        super(id, currentRoad, CAR_SPEED);
    }
}

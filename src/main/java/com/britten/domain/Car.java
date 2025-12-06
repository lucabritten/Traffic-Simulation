package com.britten.domain;

public class Car extends Vehicle{

    private static final int CAR_SPEED = 5;


    //Test cases
    public Car(int id, Road currentRoad){
        super(id, currentRoad, CAR_SPEED, CAR_SPEED);
    }

    public Car(int id, Road currentRoad, int speed){
        super(id, currentRoad, speed, speed);
    }
}

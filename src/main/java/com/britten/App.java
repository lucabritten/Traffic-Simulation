package com.britten;

import com.britten.domain.Car;
import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;


public class App {
    public static void main(String[] args) {
        Intersection i1 = new Intersection(1, new TrafficLight(5, 2, 5));
        Road r = new Road(null, i1, 100);
        Car c = new Car(1, r);

        for(int t = 0; t < 20; t++) {
            c.move(5);
            i1.getTrafficLight().update();
            System.out.println("Tick " + t + ": " + c + " | TL=" + i1.getTrafficLight().getState());
        }
    }
}

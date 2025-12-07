package com.britten.logging;

public class ConsoleLogger implements SimulationEventListener{

    @Override
    public void onEvent(SimulationEvent event){
        System.out.println("[EVENT]: " + event);
    }
}

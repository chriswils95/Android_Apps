package edu.byu.cs.tweeter.server.StaticClass;

import java.time.Duration;
import java.time.LocalTime;

public final class Timeout {

    public static String initialTime;
    public static LocalTime currTime;

    public static String getInitialTime() {
        System.out.println(initialTime);
        return Timeout.initialTime;
    }

    public static void setInitialTime(String initialTime) {
        System.out.println("initialize time");
        Timeout.initialTime = initialTime;
        System.out.println(initialTime);
    }

    public static LocalTime getCurrTime() {
        return currTime;
    }

    public static void setCurrTime(LocalTime currTime) {
        System.out.println("in time out");
        Timeout.currTime = currTime;
    }

    public static boolean sessionTimeout(){

//        long elapsedMinutes = Duration.between(Timeout.currTime, Timeout.initialTime).toMinutes();
//        if(elapsedMinutes > 1){
//            return true;
//        }
        return false;

    }
}

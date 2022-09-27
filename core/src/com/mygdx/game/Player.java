package com.mygdx.game;

public class Player {
    
    /**
     * Just the general stats of the park
     */
    
    public static int money = 10000;
    
    // money offset allows the money counter to shrink smoothly
    // it is the value the money needs to be de- or increased.
    public static int moneyoffset = 0;
    
    public static float mood = 0;
    
    private static int counterSpeed = 25;
    
    public static int parkEntranceFee = 25;
    
    public static int visitors = 0;
    
    public static float costpermin;
    
    // de-/increases the money over time according to the moneyOffset
    // 25 per tick
    public static void updateMoney(){
        double speed = counterSpeed;
        
        if(moneyoffset < 0){
            if(moneyoffset > speed){
                money -= moneyoffset;
                moneyoffset = 0;
            }
            else{
                money-=speed;
                moneyoffset+=speed;
            }
        }
        else if(moneyoffset > 0){
            if(moneyoffset < speed){
                money += moneyoffset;
                moneyoffset = 0;
            }
            else{
                money+=speed;
                moneyoffset-=speed;
            }
        }
    }
    
    
}

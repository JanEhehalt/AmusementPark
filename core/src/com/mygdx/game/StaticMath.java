package com.mygdx.game;

public class StaticMath {
    
    /**
     *  BASIC MATH FUNCTIONS
     */
    
    public static double calculateAngle(int xPos1, int yPos1, int xPos2, int yPos2){
        float deltaX = xPos2 - xPos1;
        float deltaY = yPos2 - yPos1;

        double angle;
        if(deltaY == 0){
            if(deltaX < 0){
                angle = Math.PI;
            }
            else{
                angle = 0;
            }
        }
        else if(deltaX == 0 && deltaY >= 0){
            angle = Math.PI / 2;
        }
        else if(deltaX == 0 && deltaY < 0){
            angle = Math.PI / -2;
        }
        else{
            angle = Math.abs(Math.atan(deltaY / deltaX));

            if(deltaX < 0 && deltaY < 0){
                angle = Math.PI + angle;
            }
            else if(deltaX < 0 && deltaY > 0){
                angle = Math.PI - angle;
            }
            else if(deltaX > 0 && deltaY < 0){
                angle = 2*Math.PI - angle;
            }
        }

        return angle;
    }
    
    public static double calculateDistance(int xPos1, int yPos1, int xPos2, int yPos2, double angle){
        float deltaX = xPos2 - xPos1;
        float deltaY = yPos2 - yPos1;
        
        double distance = Math.abs((deltaY / Math.sin(angle)));
        return distance;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.mapElements.Game;
import com.mygdx.game.mapElements.Garden;
import com.mygdx.game.mapElements.NullElement;
import com.mygdx.game.mapElements.Path;
import com.mygdx.game.mapElements.Restaurant;

/**
 *  Elements:
 *      NullElement:
 *      -1: NullElement
 *
 *      Gardens:
 *      00: Tree
 *      01: Shrub
 *      02: Grass
 *      
 *      Restaurants:
 *      10: French fries
 *      11: ice cream shop
 *      12: Soda shop
 *      
 *      Games:
 *      20: Ferris Wheel
 *      21: Rollercoaster
 *      22: miniZoo
 *      23: Freefalltower
 * 
 *      Paths:
 *      30: normal Path
 * 
 */
public abstract class MapElement {
    
    int id;
    public int buildingCost;
    int buildingTime;
    
    public String name;
    
    
    public Texture t;
    
    public static int[] buildingCosts = {
        // GARDENS
        15, 10, 5, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        500, 200, 300, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        1000, 5000, 2500, 3000, 0, 0, 0, 0, 0, 0,
        // PATH
        10
        
    };
            
            
    public static int[] buildingTimes = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // PATH
        0
    };
    
    public static String[] names = {
        // GARDENS
        "Tree", "Shrub", "Grass", "", "", "", "", "", "", "",
        // RESTAURANTS
        "French Fries", "Ice Cream", "Soda Shop", "", "", "", "", "", "", "",
        // GAMES
        "Ferris Wheel", "Roller Coaster", "Mini Zoo", "Freefall Tower", "", "", "", "", "", "",
        // PATH
        "Path"
    };
    
    public MapElement(int id, int buildingCost, int buildingTime, String name){
        this.id = id;
        if(buildingCost == 0){
            this.buildingCost = buildingCosts[id];
        }
        else{
            this.buildingCost = buildingCost;
        }
        
        if(name == null){
            this.name = names[id];
        }
        else{
            this.name = name;
        }
        this.buildingTime = buildingTime;
        t = new Texture(id+".png");
    }
    
    public void render(float delta, Batch batch, int x, int y){
        batch.draw(t, x, y);
    }
    
    public void renderScaled(float delta, Batch batch, int x, int y, float scale){
        batch.draw(t, x, y, t.getWidth()*scale, t.getWidth()*scale);
    }
    
    public static MapElement getNewMapElementById(int id){
        if(id == -1) return new NullElement();
        else if(id <= 0 && id < 10) return new Garden(0, 0, id);
        else if(id >= 10 && id < 20) return new Restaurant(0, 0, id);
        else if(id == 30) return new Path(0, 0, id);
        else return new Game(0,0,id);
    }
}

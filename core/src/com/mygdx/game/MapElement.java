package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.mapElements.Game;
import com.mygdx.game.mapElements.Garden;
import com.mygdx.game.mapElements.NullElement;
import com.mygdx.game.mapElements.Path;
import com.mygdx.game.mapElements.Restaurant;
import com.mygdx.game.mapElements.StaticObject;

/**
 *  Elements:
 *      
 *      id: name
 * 
 *      BuildingOversize:
 *      -2: BuildingOversize
 * 
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
 *      StaticObjects:
 *      40: Entrance
 * 
 */
public abstract class MapElement {
    
    // attributes every MapElement has
    private int id;
    private int buildingCost;
    private int buildingTime;
    private String name;
    private Texture t;
    private int width;
    private int height;
    
    // Timer which is counting up the time in order to know, when the player has
    // to pay the operationCost
    private float operationCostTimer;
    
    
    
    public MapElement(int id){
        this.id = id;
        
        // values for id -2 and -1 can't be stores in array for obvious reasons...
        // so we have to catch those ids here
        if(id == -1){
            this.buildingCost = 5;
            this.name = "Delete";
            this.buildingTime = 0;
            this.width = 1;
            this.height = 1;
        }
        else if(id == -2){
            this.buildingCost = 0;
            this.name = "Oversize";
            this.buildingTime = 0;
            this.width = 1;
            this.height = 1;
        }
        // other ids values are recieved from the MapElementData classes Arrays
        else{
            this.buildingCost = MapElementData.buildingCosts[id];
            this.name = MapElementData.names[id];
            this.buildingTime = MapElementData.buildingTimes[id];
            this.width = MapElementData.buildingWidth[id];
            this.height = MapElementData.buildingHeight[id];
        }
        
        // Every ID has a dedicated .png which id called 'id+".png"'
        t = new Texture(id+".png");
    }
    
    // act() just checks, whether the player has to pay operationCost or not
    // is called every render loop
    public void act(float delta){
        if(id != -1 && id != -2){
            operationCostTimer += delta;
            if(operationCostTimer > MapElementData.operationCostFrequency[id]){
                Player.moneyoffset -= MapElementData.operationCost[id];
                operationCostTimer = 0;
            } 
        }
            
    }
    
    // Drawing its texture (stretching it to Tile size)
    public void render(float delta, Batch batch, int x, int y){
        batch.draw(getT(), x, y, World.TILE_WIDTH * (width), World.TILE_HEIGHT * (height));
    }
    
    // Draws the texture scaled by amount 'scale'
        // mostly used for UI
    public void renderScaled(float delta, Batch batch, int x, int y, float scale){
        batch.draw(getT(), x, y, getT().getWidth()*scale, getT().getWidth()*scale);
    }
    
    public void renderTo(float delta, Batch batch, int x, int y, int width, int height){
        batch.draw(getT(), x, y, width, height);
    }
    
    
    public static MapElement getNewMapElementById(int id){
        if(id == -1) return new NullElement(id);
        else if(id >= 0 && id < 10) return new Garden(id);
        else if(id >= 10 && id < 20) return new Restaurant(id);
        else if(id >= 20 && id < 30) return new Game(id);
        else if(id >= 30 && id < 40) return new Path(id);
        else if(id >= 40 && id < 50) return new StaticObject(id);
        
        return null;
    }
    
    /**
     * GETTERS + SETTERS
     */

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the buildingCost
     */
    public int getBuildingCost() {
        return buildingCost;
    }

    /**
     * @param buildingCost the buildingCost to set
     */
    public void setBuildingCost(int buildingCost) {
        this.buildingCost = buildingCost;
    }

    /**
     * @return the buildingTime
     */
    public int getBuildingTime() {
        return buildingTime;
    }

    /**
     * @param buildingTime the buildingTime to set
     */
    public void setBuildingTime(int buildingTime) {
        this.buildingTime = buildingTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the t
     */
    public Texture getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(Texture t) {
        this.t = t;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    

}

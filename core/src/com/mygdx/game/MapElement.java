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
 *      
 *      id: name
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
 */
public abstract class MapElement {
    
    // attributes every MapElement has
    private int id;
    private int buildingCost;
    private int buildingTime;
    private String name;
    private Texture t;
    
    
    // The cost of every building by ID
    private static int[] buildingCosts = {
        // GARDENS
        15, 10, 5, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        500, 200, 300, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        1000, 5000, 2500, 3000, 0, 0, 0, 0, 0, 0,
        // PATH
        10
        
    };
            
    // The building time of every building by ID
    private static int[] buildingTimes = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // PATH
        0
    };
    
    // The name of every building by ID
    private static String[] names = {
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
        // if Building cost wasn't initiated, take it from the Array
        if(buildingCost == 0){
            this.buildingCost = buildingCosts[id];
        }
        else{
            this.buildingCost = buildingCost;
        }
        
        // if Name wasn't initiated, so take it from the Array
        if(name == null){
            this.name = names[id];
        }
        else{
            this.name = name;
        }
        
        // TODO: if buildingTime wasn't initiated, so take it from the Array
        this.buildingTime = buildingTime;
        
        // Every ID has a dedicated .png which id called 'id+".png"'
        t = new Texture(id+".png");
    }
    
    // Drawing its texture (stretching it to Tile size)
    public void render(float delta, Batch batch, int x, int y){
        batch.draw(getT(), x, y, World.TILE_WIDTH, World.TILE_HEIGHT);
    }
    
    // Draws the texture certainly scaled
        // Used in UI
    public void renderScaled(float delta, Batch batch, int x, int y, float scale){
        batch.draw(getT(), x, y, getT().getWidth()*scale, getT().getWidth()*scale);
    }
    
    public static MapElement getNewMapElementById(int id){
        if(id == -1) return new NullElement();
        else if(id <= 0 && id < 10) return new Garden(0, 0, id);
        else if(id >= 10 && id < 20) return new Restaurant(0, 0, id);
        else if(id == 30) return new Path(0, 0, id);
        else return new Game(0,0,id);
    }

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
     * @return the buildingCosts
     */
    public static int[] getBuildingCosts() {
        return buildingCosts;
    }

    /**
     * @param aBuildingCosts the buildingCosts to set
     */
    public static void setBuildingCosts(int[] aBuildingCosts) {
        buildingCosts = aBuildingCosts;
    }

    /**
     * @return the buildingTimes
     */
    public static int[] getBuildingTimes() {
        return buildingTimes;
    }

    /**
     * @param aBuildingTimes the buildingTimes to set
     */
    public static void setBuildingTimes(int[] aBuildingTimes) {
        buildingTimes = aBuildingTimes;
    }

    /**
     * @return the names
     */
    public static String[] getNames() {
        return names;
    }

    /**
     * @param aNames the names to set
     */
    public static void setNames(String[] aNames) {
        names = aNames;
    }
    
    
}

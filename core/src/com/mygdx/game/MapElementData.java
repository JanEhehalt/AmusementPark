package com.mygdx.game;

public class MapElementData {
/**
 *      
 *  Collection of many static Arrays
 *      
 *      - Stores all the different values for every building
 *      - handling it with files would be too complicated for this
 *        kind of a project and would probably cause too many problems
 * 
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
    // The cost of every building by ID
    public static int[] buildingCosts = {
        // GARDENS
        15, 10, 5, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        500, 200, 300, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        1000, 4000, 2000, 2500, 0, 0, 0, 0, 0, 0,
        // PATH
        10, 0, 40, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        
    };
            
    // The building time of every building by ID
    public static int[] buildingTimes = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // PATH
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    // The name of every building by ID
    public static String[] names = {
        // GARDENS
        "Tree", "Shrub", "Grass", "", "", "", "", "", "", "",
        // RESTAURANTS
        "French Fries", "Ice Cream", "Soda Shop", "", "", "", "", "", "", "",
        // GAMES
        "Ferris Wheel", "Roller Coaster", "Mini Zoo", "Freefall Tower", "", "", "", "", "", "",
        // PATH
        "Path", "Dirty Path", "Path with Trashcan", "", "", "", "", "", "", "",
        // STATIC OBJECTS
        "ENTRANCE", "", "", "", "", "", "", "", "", ""
    };
    
    // The width of every building by ID
    public static int[] buildingWidth = {
        // GARDENS
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // RESTAURANTS
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // GAMES
        2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
        // PATH
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // STATIC OBJECTS
        5, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };
    
    // The height of every building by ID
    public static int[] buildingHeight = {
        // GARDENS
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // RESTAURANTS
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // GAMES
        2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
        // PATH
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // STATIC OBJECTS
        2, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };
    
    public static int[] entrance = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        15, 5, 10, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        30, 100, 40, 50, 0, 0, 0, 0, 0, 0,
        // PATH
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    public static int[] mood = {
        // GARDENS
        3, 2, 1, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        50, 40, 60, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        150, 400, 180, 250, 0, 0, 0, 0, 0, 0,
        // PATH
        0, -100, 0, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    public static int[] operationCost = {
        // GARDENS
        2, 1, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        15, 10, 12, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        50, 200, 60, 80, 0, 0, 0, 0, 0, 0,
        // PATH
        1, 0, 20, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    public static int[] operationCostFrequency = {
        // GARDENS
        60, 60, 60, 1, 1, 1, 1, 1, 1, 1,
        // RESTAURANTS
        30, 30, 30, 1, 1, 1, 1, 1, 1, 1,
        // GAMES
        60, 60, 60, 60, 1, 1, 1, 1, 1, 1,
        // PATH
        40, 1, 40, 1, 1, 1, 1, 1, 1, 1,
        // STATIC OBJECTS
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };
    
    public static float[] costpermin = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // PATH
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    public static float[] repaircost = {
        // GARDENS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // RESTAURANTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // GAMES
        200, 1000, 400, 250, 0, 0, 0, 0, 0, 0,
        // PATH
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // STATIC OBJECTS
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    public static void initCostpermin(){
        for(int i = 0; i < costpermin.length; i++){
            costpermin[i] = (float)operationCost[i]*60f/ (float)operationCostFrequency[i];
        }
    }
    
}

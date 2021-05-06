package com.mygdx.game.mapElements;

import com.mygdx.game.MapElement;

public class BuildingOversize extends MapElement{
    
    /**
     * This class is for buildings (games) which occupy more than one grid space
     * Example for grid with id:
     *   __ __
     *  |-2|-2|
     *  |--+--|
     *  |21|-2|
     *  |-----|
     * 
     * The building is in the bottom left, BuildingOversize (-2) is filling the
     *  rest of the space (BuildingOversize texture is empty)
     * 
     */
    
    
    // saves the coordinates of its root building so it knows what its origin is
    private int rootX;
    private int rootY;

    public BuildingOversize(int rootX, int rootY) {
        super(-2);
        this.rootX = rootX;
        this.rootY = rootY;
    }
    
    /**
     * GETTERS + SETTERS
     */

    /**
     * @return the rootX
     */
    public int getRootX() {
        return rootX;
    }

    /**
     * @param rootX the rootX to set
     */
    public void setRootX(int rootX) {
        this.rootX = rootX;
    }

    /**
     * @return the rootY
     */
    public int getRootY() {
        return rootY;
    }

    /**
     * @param rootY the rootY to set
     */
    public void setRootY(int rootY) {
        this.rootY = rootY;
    }
    
    
    
    
}

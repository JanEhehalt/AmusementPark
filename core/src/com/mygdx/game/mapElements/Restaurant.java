package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class Restaurant extends MapElement{
    
    /**
     * gets the attributes from MapElementData class
     * foodCost is the price every Npcs pays when getting food at a restaurant
     *  it is being added to Money variable in Player
     */
    
    private int foodCost;

    public Restaurant(int id) {
        super(id);
    }
    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * GETTERS + SETTERS
     */
    
    
    /**
     * @return the foodCost
     */
    public int getFoodCost() {
        return foodCost;
    }

    /**
     * @param foodCost the foodCost to set
     */
    public void setFoodCost(int foodCost) {
        this.foodCost = foodCost;
    }
}

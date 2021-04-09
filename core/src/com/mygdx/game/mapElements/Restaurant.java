/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

/**
 *
 * @author janeh
 */
public class Restaurant extends MapElement{
    
    int foodCost;

    public Restaurant(int buildingCost, int buildingTime, int id) {
        super(id, buildingCost, buildingTime, null);
    }
    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }
}

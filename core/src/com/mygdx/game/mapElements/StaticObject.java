package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class StaticObject extends MapElement{
    
    /**
     * The StaticObjects are Objects of the map
     *  The Player can't build on StaticObjects
     *  The entrance for example is a StaticObject
     */

    public StaticObject(int id) {
        super(id);
    }

    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

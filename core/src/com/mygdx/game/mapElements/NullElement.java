package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class NullElement extends MapElement{

    /**
     *  NullElement is just an empty grid element
     *      The player can only build on NullElements
     *      The texture is the brown dirt texture
     */
    
    public NullElement(int id) {
        super(id);
    }

    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class Path extends MapElement{
    
    /**
     * Paths are for the Npcs (Guests)
     *  They can only walk on paths
     *  Pathfinding in Graph class
     */
    
    public Path(int id) {
        super(id);
    }

    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }
    
}

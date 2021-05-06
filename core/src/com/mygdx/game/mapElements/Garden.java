package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class Garden extends MapElement{
    
    // gets the attributes from MapElementData class
    private int moodImprovement;
    // moodImprovement permanently improves the general mood of the AmusementPark
    //                  (as long as the Garden exists)

    public Garden(int id) {
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
     * @return the moodImprovement
     */
    public int getMoodImprovement() {
        return moodImprovement;
    }

    /**
     * @param moodImprovement the moodImprovement to set
     */
    public void setMoodImprovement(int moodImprovement) {
        this.moodImprovement = moodImprovement;
    }
    
}

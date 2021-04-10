package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class Garden extends MapElement{
    
    private int moodImprovement;

    public Garden(int buildingCost, int buildingTime, int id) {
        super(id, buildingCost, buildingTime, null);
    }

    
    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }

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

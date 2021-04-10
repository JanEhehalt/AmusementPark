package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MapElement;

public class Game extends MapElement {
     
    // special attributes only Games have
    private int operationCost;
    private boolean broke;
    private int capacity;
    private int turnTime;
    private int waitingRowSize;

    public Game(int buildingCost, int buildingTime, int id) {
        
        super(id, buildingCost, buildingTime, null);
    }

    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the operationCost
     */
    public int getOperationCost() {
        return operationCost;
    }

    /**
     * @param operationCost the operationCost to set
     */
    public void setOperationCost(int operationCost) {
        this.operationCost = operationCost;
    }

    /**
     * @return the broke
     */
    public boolean isBroke() {
        return broke;
    }

    /**
     * @param broke the broke to set
     */
    public void setBroke(boolean broke) {
        this.broke = broke;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the turnTime
     */
    public int getTurnTime() {
        return turnTime;
    }

    /**
     * @param turnTime the turnTime to set
     */
    public void setTurnTime(int turnTime) {
        this.turnTime = turnTime;
    }

    /**
     * @return the waitingRowSize
     */
    public int getWaitingRowSize() {
        return waitingRowSize;
    }

    /**
     * @param waitingRowSize the waitingRowSize to set
     */
    public void setWaitingRowSize(int waitingRowSize) {
        this.waitingRowSize = waitingRowSize;
    }
    
    

    
}

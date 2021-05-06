package com.mygdx.game.mapElements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Main;
import com.mygdx.game.MapElement;
import com.mygdx.game.UI;
import com.mygdx.game.World;
import com.mygdx.game.npcs.Npc;
import java.util.ArrayList;

public class Game extends MapElement {
     
    // special attributes only Games have
    // gets the attributes from MapElementData class
    private int turnTime;
    private int waitingTime;
    private int capacity;
    private int waitingRowSize;
    
    private float time;
    
    private int state = 0;
    /**
     * 0: waiting for guests
     * 1: running
     * 2: broke
     */
    
    private ArrayList<Actor> waiting;
    private ArrayList<Actor> riding;
    
    private ShapeRenderer renderer = new ShapeRenderer();
    
    public static Texture[] t = {
        new Texture("mood/waiting.png"),
        new Texture("mood/running.png"),
        new Texture("broke.png")
    };
    
    // GAMES
    // "Ferris Wheel", "Roller Coaster", "Mini Zoo", "Freefall Tower", "", "", "", "", "", ""
    
    
    // The Time one turn needs
    public static int[] turnTimes = {
        20, 15, 45, 10, 0, 0, 0, 0, 0, 0
    };
    
    // the time the Game is waiting before starting new turn
    public static int[] waitingTimes = {
        30, 20, 5, 20, 0, 0, 0, 0, 0, 0
    };
    
    // The amount, how many people can ride the game simulaneously
    public static int[] capacities = {
        8, 4, 16, 8, 0, 0, 0, 0, 0, 0
    };
    
    // The amount, how many people can wait in the row of the Game
    public static int[] waitingRowSizes = {
        4, 16, 8, 12, 0, 0, 0, 0, 0, 0
    };
    

    public Game(int id) {
        super(id);
        
        this.turnTime = turnTimes[id-20];
        this.waitingTime = waitingTimes[id-20];
        this.capacity = capacities[id-20];
        this.waitingRowSize = waitingRowSizes[id-20];
        
        waiting = new ArrayList<>();
        riding = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        
        if(getState() != 2){
            setTime(getTime() + delta);
        }
        if(getState() == 0){
            if(getTime() > getWaitingTime()){
                setState(1);
                setTime(0);
            }
        }
        else if(getState() == 1){
            if(getTime() > getTurnTime()){
                setState(0);
                setTime(0);
                for(Actor a : getRiding()){
                    ((Npc)a).setState(0);
                }
                riding = new ArrayList<>();
            }
        }
        
        if(getState() == 0){
            // Moving guests from waiting into riding
            for(int i = 0; i < getWaiting().size(); i++){
                if(getRiding().size() < getCapacity()){
                    getRiding().add(getWaiting().get(i));
                    getWaiting().remove(i);
                    i--;
                }
            }
        }
        else if(getState() == 1){
        }
        
        if(Math.random() < 0.0002){
            System.out.println("BREAK");
            setState(2);
        }
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public void render(float delta, Batch batch, int x, int y) {
        super.render(delta, batch, x, y); //To change body of generated methods, choose Tools | Templates.
        
        if(World.renderGrid){
            batch.end();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(x, y+getHeight()*World.TILE_HEIGHT-11, 32, 5);
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(x, y+getHeight()*World.TILE_HEIGHT-5, 32, 5);
            renderer.setColor(Color.GREEN);
            renderer.rect(x, y+getHeight()*World.TILE_HEIGHT-11, (float)waiting.size()/(float)waitingRowSize * 32f, 5);
            renderer.setColor(Color.ORANGE);
            renderer.rect(x, y+getHeight()*World.TILE_HEIGHT-5, (float)getRiding().size()/(float)capacity * 32f, 5);
            renderer.end();
            batch.begin();
        }
    }
    
    /**
     * GETTERS + SETTERS
     */


    public boolean isBroke() {
        if(getState() == 2){
            return true;
        }
        return false;
    }

    public void setState(int state) {
        this.state = state;
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

    /**
     * @return the waitingTime
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * @param waitingTime the waitingTime to set
     */
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    /**
     * @return the time
     */
    public float getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @return the waiting
     */
    public ArrayList<Actor> getWaiting() {
        return waiting;
    }

    /**
     * @param waiting the waiting to set
     */
    public void setWaiting(ArrayList<Actor> waiting) {
        this.waiting = waiting;
    }

    /**
     * @return the riding
     */
    public ArrayList<Actor> getRiding() {
        return riding;
    }

    /**
     * @param riding the riding to set
     */
    public void setRiding(ArrayList<Actor> riding) {
        this.riding = riding;
    }

    
    

    
}

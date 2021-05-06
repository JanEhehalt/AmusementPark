package com.mygdx.game.npcs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MapElementData;
import com.mygdx.game.Player;
import com.mygdx.game.StaticMath;
import com.mygdx.game.World;
import com.mygdx.game.mapElements.Game;
import com.mygdx.game.pathfinding.Graph;
import com.mygdx.game.pathfinding.Node;
import java.util.ArrayList;

public class Repairman extends Actor{
    
    // coordinates translated to the grid the guest is standing in
    private int xGrid;
    private int yGrid;
    
    // simple timer for counting the time
    private double elapsedTime;
    
    public static Texture t = new Texture("repairman.png");
    
    // textures for the different directions the player can face
    private final Texture rightt = new Texture("repairman+.png");
    private final Texture leftt = new Texture("repairman-.png");
    private final Texture centert = new Texture("repairman.png");
    private final Texture backt = new Texture("repairmanback.png");
    
    // Every Npc stores an graph of the map which get updated every render loop
    private Graph graph;
    // The current path the npc is following
    private ArrayList<Node> path;
    // the current goal the npc trys to reach
    private Node goal;
    // The next Point (PointOfInterest) the npc is walking to
    private Vector2 POI;
    // movementSpeed
    private float speed = 1f;
    // the point at which the player is in his path
    // path are just the list of nodes the player has to follow
    private int pathPart = 0;
    
    // is Player showing his mood bubble?
    private boolean moodBubble = false;
    
    // current movement speed in the x and y direction
    private float movementX;
    private float movementY;
    
    public static int loan = 250;
    public static int loanFrequency = 120;
    
    public static double costpermin = ((float)loan*60f/(float)loanFrequency);
   
    World world;
    
    public Repairman(Graph graph, World world){
        this.graph = graph;
        
        // sets x coordinate, y coordinate and width and height
        // methods and variables come from abstract actor 
        // x = entrance of the map, y = entrance of the map, width = texture width, height = texture height 
        setBounds((World.ENTRANCE_X+2) * World.TILE_WIDTH + World.TILE_WIDTH/2, World.ENTRANCE_Y * World.TILE_HEIGHT + World.TILE_HEIGHT/2, 16, 16);
        
        this.xGrid = (int)(getX() / World.TILE_WIDTH);
        this.yGrid = (int)(getX() / World.TILE_HEIGHT);
        
        this.world = world;
    }

    // called every render loop
    @Override
    public void act(float delta) {
        //translating current position to grid position
        setxGrid((int)(getX()/World.TILE_WIDTH));
        setyGrid((int)(getY()/World.TILE_HEIGHT));
        
        
        // if has no goal, generate new one and the path to it
        if(getGoal() == null){
            setGoal(getGraph().randomGoal(getxGrid(), getyGrid())); 
            if(getGoal() != null){
                setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                setPathPart(0);
            }
        }
        
        
        if(getGoal() != null && !(getGoal().getBuildingId() >= 20 && getGoal().getBuildingId() <= 29) ){
            Node trash = graph.findBrokeRide();
            if(trash != null){    
                ArrayList<Node> trashPath = getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(trash.getX(), trash.getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]);
                if(trashPath != null){
                    setGoal(trash);
                    setPath(trashPath);
                    setPathPart(0);
                }
            }
        }
        
        
        // if we have a goal but our path is null we try and create a new path in order to reach it
        if(getGoal() != null && getPath() == null){
            setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
            setPathPart(0);       
        }
        
        // no movement if goal and path are null
        if(getPath() == null || getGoal() == null){
            
        }
        else if(!graph.pathValid(new ArrayList<>(path.subList(pathPart, path.size())))){
            // if our path isn't valid any more we delete it
            // in the next loop of this method a new path will be searched
            setPath(null);
            setGoal(null);
            setPathPart(0);
        }
        else{
            if(getPath().size() > 1){
                // if we are close to our POI we change our pathPart to the next one to reach the next node
                int x1 = (int)getX() + (int)(getWidth()/2);
                int y1 = (int)getY() + (int)(getHeight()/2);
                int x2 = getPath().get(getPathPart()).getX() * World.TILE_WIDTH + World.TILE_WIDTH/2;
                int y2 = getPath().get(getPathPart()).getY() * World.TILE_HEIGHT + World.TILE_HEIGHT/2;
                if(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)) < 5){
                    if(getPathPart()+1 < getPath().size()){
                        setPathPart(getPathPart() + 1);
                    }
                }
            }
            else if(getPath().size() == 1){
                // if the path only has one node we go there
                setPathPart(0);
            }
            
            if(getGoal() != null && getPath() != null){
                setPOI(new Vector2(getPath().get(getPathPart()).getX() * World.TILE_WIDTH + World.TILE_WIDTH/2, getPath().get(getPathPart()).getY() * World.TILE_HEIGHT + World.TILE_HEIGHT/2));

                double angle = StaticMath.calculateAngle((int)getX() + (int)(getWidth()/2), (int)getY() + (int)(getHeight()/2), (int)getPOI().x, (int)getPOI().y);
                movementX = (float)Math.cos(angle)*getSpeed();
                movementY = (float)Math.sin(angle)*getSpeed();
                setX(getX()+ movementX);
                setY(getY()+ movementY);
            
                int x1 = (int)getX() + (int)(getWidth()/2);
                int y1 = (int)getY() + (int)(getHeight()/2);
                int x2 = getGoal().getX() * World.TILE_WIDTH + World.TILE_WIDTH/2;
                int y2 = getGoal().getY() * World.TILE_HEIGHT + World.TILE_HEIGHT/2;
                if(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)) < 5){
                    if(getGoal().getBuildingId() >= 20 && getGoal().getBuildingId() <= 29){
                        ((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).setState(0);
                        Player.moneyoffset -= MapElementData.repaircost[world.getGrid()[getGoal().getX()][getGoal().getY()].getId()];
                    }
                    setGoal(null);
                    setPath(null);
                    setPathPart(0);
                }
            
            }
            
            
        }
        
        elapsedTime += delta;
        
        if(elapsedTime % loanFrequency == 0){
            Player.moneyoffset -= loan;
        }
        
        // just resetting elapsed time in order not to overflow the float
        //  (although it would last forever :D (16bit))
        if(elapsedTime > 600) elapsedTime = 0;
            
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        // drawing the texture which fits best to the players movement
        if(movementX > 0.2*speed){
            batch.draw(rightt, getX(), getY());
        }
        else if(movementX < -0.2*speed){
            batch.draw(leftt, getX(), getY());
        }
        else{
            if(movementY > 0.3*speed){
                batch.draw(backt, getX(), getY());
            }
            else{
                batch.draw(centert, getX(), getY());
            }
        }
        
        batch.end();
        if(getGraph() != null){
            if(World.renderGrid){
                getGraph().getRenderer().setProjectionMatrix(batch.getProjectionMatrix());
                getGraph().drawPath(getPath());
                if(getPOI() != null && getGoal() != null){
                    // drawing POI and next path node which the player will go to next
                    getGraph().getRenderer().begin(ShapeRenderer.ShapeType.Line);
                    getGraph().getRenderer().setColor(Color.GREEN);
                    getGraph().getRenderer().line(getPOI().x, getPOI().y, getX()+getWidth()/2, getY()+getHeight()/2);
                    getGraph().getRenderer().line(getGoal().getX()*World.TILE_WIDTH+World.TILE_WIDTH/2, getGoal().getY()*World.TILE_HEIGHT+World.TILE_HEIGHT/2, getX()+getWidth()/2, getY()+getHeight()/2);
                    getGraph().getRenderer().end();
                    getGraph().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
                    getGraph().getRenderer().circle(getPOI().x, getPOI().y, 5);
                    getGraph().getRenderer().setColor(Color.ORANGE);
                    getGraph().getRenderer().circle(getGoal().getX()*World.TILE_WIDTH + World.TILE_WIDTH/2, getGoal().getY()*World.TILE_HEIGHT + World.TILE_HEIGHT/2, 3);
                    getGraph().getRenderer().end();
                }
            }
        }
        batch.begin();
        
        super.draw(batch, parentAlpha); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }

    /**
     * @return the xGrid
     */
    public int getxGrid() {
        return xGrid;
    }

    /**
     * @param xGrid the xGrid to set
     */
    public void setxGrid(int xGrid) {
        this.xGrid = xGrid;
    }

    /**
     * @return the yGrid
     */
    public int getyGrid() {
        return yGrid;
    }

    /**
     * @param yGrid the yGrid to set
     */
    public void setyGrid(int yGrid) {
        this.yGrid = yGrid;
    }


    /**
     * @return the elapsedTime
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * @param elapsedTime the elapsedTime to set
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }


    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return the path
     */
    public ArrayList<Node> getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(ArrayList<Node> path) {
        this.path = path;
    }

    /**
     * @return the goal
     */
    public Node getGoal() {
        return goal;
    }

    /**
     * @param goal the goal to set
     */
    public void setGoal(Node goal) {
        this.goal = goal;
    }

    /**
     * @return the POI
     */
    public Vector2 getPOI() {
        return POI;
    }

    /**
     * @param POI the POI to set
     */
    public void setPOI(Vector2 POI) {
        this.POI = POI;
    }

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * @return the pathPart
     */
    public int getPathPart() {
        return pathPart;
    }

    /**
     * @param pathPart the pathPart to set
     */
    public void setPathPart(int pathPart) {
        this.pathPart = pathPart;
    }

    /**
     * @return the moodBubble
     */
    public boolean isMoodBubble() {
        return moodBubble;
    }

    /**
     * @param moodBubble the moodBubble to set
     */
    public void setMoodBubble(boolean moodBubble) {
        this.moodBubble = moodBubble;
    }

    
    
}

package com.mygdx.game.npcs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MapElement;
import com.mygdx.game.MapElementData;
import com.mygdx.game.Player;
import com.mygdx.game.StaticMath;
import com.mygdx.game.World;
import com.mygdx.game.mapElements.Game;
import com.mygdx.game.pathfinding.Graph;
import com.mygdx.game.pathfinding.Node;
import java.util.ArrayList;

public class Npc extends Actor{
    
    /**
     * states:
     *  0   walking
     *  1   hungry
     *  2   thirsty
     *  3   fun
     *  4   leaving
     *  5   riding
     */
    private int state = 0;
    
    // coordinates translated to the grid the guest is standing in
    private int xGrid;
    private int yGrid;
    
    // coordinates of the current goal the guest is trying to reach
    private int goalX;
    private int goalY;
    
    // simple timer for counting the time
    private double elapsedTime;
    
    // textures for the different directions the player can face
    private final Texture rightt = new Texture("+npc.png");
    private final Texture leftt = new Texture("-npc.png");
    private final Texture centert = new Texture("npc.png");
    private final Texture backt = new Texture("npcback.png");
    
    // Every Npc stores an graph of the map which get updated every render loop
    private Graph graph;
    // The current path the npc is following
    private ArrayList<Node> path;
    // the current goal the npc trys to reach
    private Node goal;
    // The next Point (PointOfInterest) the npc is walking to
    private Vector2 POI;
    // movementSpeed
    private float speed = 0.7f;
    // the point at which the player is in his path
    // path are just the list of nodes the player has to follow
    private int pathPart = 0;
    
    // is Player showing his mood bubble?
    private boolean moodBubble = false;
    
    // mood value of the npc which influences the mood of the park
    private float mood;
    
    // current movement speed in the x and y direction
    private float movementX;
    private float movementY;
    
    World world;
    
    // mood bubbles
    private static Texture[] moodBubbleTexture = {
        new Texture("mood/fun.png"),
        new Texture("mood/hungry.png"),
        new Texture("mood/thirsty.png"),
        new Texture("mood/angry.png"),
        new Texture("mood/leaving.png"),
        new Texture("mood/trash.png")
    };
    
    public Npc(Graph graph, World world){
        this.graph = graph;
        this.world = world;
        // sets x coordinate, y coordinate and width and height
        // methods and variables come from abstract actor 
        // x = entrance of the map, y = entrance of the map, width = texture width, height = texture height 
        setBounds((World.ENTRANCE_X+2) * World.TILE_WIDTH + World.TILE_WIDTH/2, World.ENTRANCE_Y * World.TILE_HEIGHT + World.TILE_HEIGHT/2, 16, 16);
        
        this.xGrid = (int)(getX() / World.TILE_WIDTH);
        this.yGrid = (int)(getX() / World.TILE_HEIGHT);
        // generating random goal in the beginning
        this.goal = graph.randomGoal(xGrid, yGrid);
    }

    // called every render loop
    @Override
    public void act(float delta) {
        //translating current position to grid position
        setxGrid((int)(getX()/World.TILE_WIDTH));
        setyGrid((int)(getY()/World.TILE_HEIGHT));
        if(getState() != 5){
        switch(getState()){
            /**
             * state:
             *  0   walking
             */
            case 0:
                setElapsedTime(0);
                // if has no goal, generate new one and the path to it
                if(getGoal() == null){
                    setGoal(getGraph().randomGoal(getxGrid(), getyGrid())); 
                    if(getGoal() != null){
                        setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                    }
                }
                // if no path found we generate a new goal
                // COULD JUST SET GOAL TO NULL
                if(getPath() == null){
                    setGoal(getGraph().randomGoal(getxGrid(), getyGrid())); 
                }
                
                // small chance for npcs to change state
                if(getGoal() == null || getGoal().getBuildingId() != 32){
                    if(Math.random() < 0.0005){
                        setState(1);
                        setGoal(null);
                        setPath(null);
                        setPOI(null);
                        setPathPart(0);
                    }
                    if(Math.random() < 0.0005){
                        setState(2);
                        setGoal(null);
                        setPath(null);
                        setPOI(null);
                        setPathPart(0);
                    }
                    if(Math.random() < 0.0005){
                        setState(3);
                        setGoal(null);
                        setPath(null);
                        setPOI(null);
                        setPathPart(0);
                    }
                    if(Math.random() < 0.0003){
                        setState(4);
                        setGoal(null);
                        setPath(null);
                        setPOI(null);
                        setPathPart(0);
                    }
                }
                if(state == 0){
                    if(getGoal() != null){
                        if(getxGrid() == getGoal().getX() && getyGrid() == getGoal().getY()){
                            setGoal(getGraph().randomGoal(getxGrid(), getyGrid()));
                            setPathPart(0);
                            if(getGoal() != null){
                                setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                            }
                        }
                    }
                }
                if(Math.random() < 0.002 && state == 0){
                    if(world.getGrid()[getxGrid()][getyGrid()].getId() == 30){
                        Node trashcan = graph.findViableTrashcan(getxGrid(), getyGrid());
                        if(trashcan == null){
                            world.getGrid()[getxGrid()][getyGrid()] = MapElement.getNewMapElementById(31);
                        }
                        else{
                            setGoal(trashcan);    
                            setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), trashcan, new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                            setPathPart(0);
                            setPOI(null);
                        }
                    }
                }
                
                
            break;
            case 1:
                
                /**
                 * Now there will be happening the same in states 1(hungry), 2(thirsty) and 3(looking for fun)
                 * 
                 *  We find a goal according to the state
                 *      in 1 e.g. we either look for frenchfries shop or ice shop
                 *      if we found one we try to find a path to reach it
                 *  When we reached the restaurant we add the money to the players budget
                 *  Then we reset state to 0
                 */
                
                
                if(getGoal() == null){
                    setGoal(getGraph().findId(10, getxGrid(), getyGrid()));
                    if(getGoal() == null){
                        setGoal(getGraph().findId(11, getxGrid(), getyGrid()));
                    }
                    setPathPart(0);
                    if(getGoal() != null){
                        setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                    }
                }
                else if(getxGrid() == getGoal().getX() && getyGrid() == getGoal().getY()){
                    Player.moneyoffset += MapElementData.entrance[getGoal().getBuildingId()];
                    setState(0);
                    setGoal(null);
                    setPath(null);
                    setPOI(null);
                    setPathPart(0);
                }
                else{
                    
                }
            break;
            case 2:
                if(getGoal() == null){
                    setGoal(getGraph().findId(12, getxGrid(), getyGrid()));
                    if(getGoal() == null){
                        setGoal(getGraph().findId(11, getxGrid(), getyGrid()));
                    }
                    setPathPart(0);
                    if(getGoal() != null){
                        setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                    }
                }
                else if(getxGrid() == getGoal().getX() && getyGrid() == getGoal().getY()){
                    
                    Player.moneyoffset += MapElementData.entrance[getGoal().getBuildingId()];
                    setState(0);
                    setGoal(null);
                    setPath(null);
                    setPOI(null);
                    setPathPart(0);
                }
                else{
                    
                }
            break;
            case 3:
                if(getGoal() == null){
                    setGoal(getGraph().findId(20+(int)(Math.random()*4), getxGrid(), getyGrid()));
                    setPathPart(0);
                    if(getGoal() != null){
                        setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                    }
                }
                else if(getxGrid() == getGoal().getX() && getyGrid() == getGoal().getY()){
                    if(world.getGrid()[getGoal().getX()][getGoal().getY()] instanceof Game){
                        if(!((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).isBroke() ){
                            if(((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).getState() != 2){
                                if(((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).getWaiting().size() < ((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).getWaitingRowSize()){
                                    ((Game)world.getGrid()[getGoal().getX()][getGoal().getY()]).getWaiting().add(this);
                                    setState(5);
                                    Player.moneyoffset += MapElementData.entrance[getGoal().getBuildingId()];
                                }
                                else{
                                    // WHEN WANTING TO JOIN FULL GAME
                                    // MOOD SHRINKS AND MAYBE THE GUEST WILL LEAVE (30%)
                                    setState(0);
                                    if(Math.random() < 0.3){
                                    setState(4);
                                    }
                                    mood = -10;
                                }
                            }
                        }
                        else{
                            // WHEN WANTING TO JOIN BROKEN GAME
                            // MOOD SHRINKS AND MAYBE THE GUEST WILL LEAVE (30%)
                            setState(0);
                            if(Math.random() < 0.3){
                            setState(4);
                            }
                            mood -= 10;
                        }
                    }
                    setGoal(null);
                    setPath(null);
                    setPOI(null);
                    setPathPart(0);
                }
                else{
                    
                }
            break;
            case 4:
                /**
                 * looking for entrance in order to leave
                 * try and find a path to reach it
                 */
                if(getGoal() == null){
                    setGoal(getGraph().getNodeWith(World.ENTRANCE_X+2, World.ENTRANCE_Y));
                    setPathPart(0);
                    setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                }
            break;
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
            setPathPart(0);
        }
        else{
            if(getPath().size() > 1){
                // if we are close to our POI we change our pathPart to the next one to reach the next node
                int x1 = (int)getX() + (int)(getWidth()/2);
                int y1 = (int)getY() + (int)(getHeight()/2);
                int x2 = getPath().get(getPathPart()).getX() * World.TILE_WIDTH + World.TILE_WIDTH/2;
                int y2 = getPath().get(getPathPart()).getY() * World.TILE_HEIGHT + World.TILE_HEIGHT/2;
                double angle = StaticMath.calculateAngle(x1,y1,x2,y2);
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
            
            /**
             * BASIC MOVEMENT
             * look at it, you'll understand :D
             */
            
            setPOI(new Vector2(getPath().get(getPathPart()).getX() * World.TILE_WIDTH + World.TILE_WIDTH/2, getPath().get(getPathPart()).getY() * World.TILE_HEIGHT + World.TILE_HEIGHT/2));
            
            double angle = StaticMath.calculateAngle((int)getX() + (int)(getWidth()/2), (int)getY() + (int)(getHeight()/2), (int)getPOI().x, (int)getPOI().y);
            movementX = (float)Math.cos(angle)*getSpeed();
            movementY = (float)Math.sin(angle)*getSpeed();
            setX(getX()+ movementX);
            setY(getY()+ movementY);
            
        }
        
        setElapsedTime(getElapsedTime() + delta);
        if(getState() != 0 && getState() != 4 && path == null){
            /**
             * If the npc is looking for a building but won't find it
             * the timer is counding up.
             * If it reaches 8 seconds and the npc didn't find the building he wants
             * in that time he will change to state 4 and leave the park
             *
             * also his mood will shrink and influence the park's mood in a bad way
             */
            if(mood > -5){
                mood-=0.5;
            }
            if(getElapsedTime() > 8){
                setState(4);
                setElapsedTime(0);
            }
        }
        else{
            /**
             * if he's happy and has a path etc. his mood will get positive
             * he will influence the parks general mood in a good way
             */
            if(mood < 0) mood = 0;
            if(mood < 5){
                mood+=0.5;
            }
        }
         // state 4: leaving
        if(getState() == 4){
            if(goal == null){
                // finding the exit
                goal = graph.findId(40, xGrid, yGrid);
                if(goal != null){
                    setGoal(getGraph().getNodeWith(getGoal().getX()+2, getGoal().getY()));
                }
            }
            if(path == null && goal != null){
                setPath(getGraph().findPath(getGraph().getNodeWith(getxGrid(), getyGrid()), getGraph().getNodeWith(getGoal().getX(), getGoal().getY()), new ArrayList<Node>(), new boolean[getGraph().getNodes().length]));
                //very negative mood if npc can't reach the exit
                mood = -40;
            }
            if(goal != null && path != null){
                if(elapsedTime > 15){
                    setX((World.ENTRANCE_X+2)*World.TILE_WIDTH+2);
                    setY(World.ENTRANCE_Y*World.TILE_HEIGHT+2);
                }
            }
        }
        
        // just resetting elapsed time in order not to overflow the float
        //  (although it would last forever :D (16bit))
        if(elapsedTime > 600) elapsedTime = 0;
        
        }
        
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(state != 5){
        // drawing the texture which fits best to the players movement
        if(movementX > 0.2){
            batch.draw(rightt, getX(), getY());
        }
        else if(movementX < -0.2){
            batch.draw(leftt, getX(), getY());
        }
        else{
            if(movementY > 0.3){
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
        
        // if state == 0 there is a certain change for showing the bubble
        if(state == 0){
            if(Math.random() < 0.01 && Math.random() < 0.05) {
                setMoodBubble(!isMoodBubble());
            }
            if(moodBubble){
                batch.draw(getMoodBubbleTexture()[getState()], getX()+getWidth()-2, getY()+getHeight()-2);
            }
            if(getGoal() != null && getGoal().getBuildingId() == 32){
                batch.draw(getMoodBubbleTexture()[5], getX()+getWidth()-2, getY()+getHeight()-2);
            }
        }
        // if state not 0 the bubble will always show
        else{
            batch.draw(getMoodBubbleTexture()[getState()], getX()+getWidth()-2, getY()+getHeight()-2);
        }
        }
        super.draw(batch, parentAlpha); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
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
     * @return the goalX
     */
    public int getGoalX() {
        return goalX;
    }

    /**
     * @param goalX the goalX to set
     */
    public void setGoalX(int goalX) {
        this.goalX = goalX;
    }

    /**
     * @return the goalY
     */
    public int getGoalY() {
        return goalY;
    }

    /**
     * @param goalY the goalY to set
     */
    public void setGoalY(int goalY) {
        this.goalY = goalY;
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

    /**
     * @return the mood
     */
    public static Texture[] getMoodBubbleTexture() {
        return moodBubbleTexture;
    }

    /**
     * @param aMood the mood to set
     */
    public static void setMoodBubbleTexture(Texture[] aMood) {
        moodBubbleTexture = aMood;
    }
    
    public float getMood(){
        return mood; 
   }
 
    
}

package com.mygdx.game;

import com.mygdx.game.npcs.Npc;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import static com.mygdx.game.Main.RESOLUTIONS;
import com.mygdx.game.mapElements.BuildingOversize;
import com.mygdx.game.mapElements.Game;
import com.mygdx.game.mapElements.NullElement;
import com.mygdx.game.mapElements.Path;
import com.mygdx.game.mapElements.StaticObject;
import com.mygdx.game.npcs.Cleaner;
import com.mygdx.game.npcs.Repairman;
import com.mygdx.game.pathfinding.Graph;

public class World {
    // Stage is handling the viewport and camera stuff
    private Stage stage;
    // saving camera reference // not necessary
    private Camera camera;
    private ShapeRenderer renderer;
    
    // The Map Grid
    private MapElement[][] grid;
    
    // Camera Movement Speed
    public static int cameraSpeed = 10;
    
    // active resolution from Array in Main
    public static int currentResolution = 5;
    
    // Activate the rendering of the Grid borders | TAB
    public static boolean renderGrid = false;
    
    // Making TILE_WIDTH and HEIGHT Variable, could just be changed any time
    public static int TILE_WIDTH = 32;
    public static int TILE_HEIGHT = 32;
    
    // Position of the Park Entrance
    public static int ENTRANCE_X;
    public static int ENTRANCE_Y;
    
    // Graph for pathfinding
    public Graph graph;
    
    //

    public World(int tileAmountX, int tileAmountY) {
        // creating camera / viewport stuff for View
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(RESOLUTIONS[currentResolution*2],RESOLUTIONS[currentResolution*2+1], getCamera()));
        renderer = new ShapeRenderer();
        camera.position.set(tileAmountX*TILE_WIDTH/2, tileAmountY*TILE_HEIGHT/2, 0);
        
        ENTRANCE_X = tileAmountX/2-2;
        ENTRANCE_Y = tileAmountY-2;

     
        // Generating empty (/NullElements) Grid
        grid = new MapElement[tileAmountX][tileAmountY];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = new NullElement(-1);
            }
        }
        placeInGrid(MapElement.getNewMapElementById(40), ENTRANCE_X, ENTRANCE_Y);
        
        graph = Graph.generateGraphFromMap(grid);

    }
    
    // Handing the resizing of the window
    public void resize(int width, int height){
        getStage().getViewport().update(width, height);
    }
    
    public void render(){
        getCamera().update();
        getRenderer().setProjectionMatrix(getCamera().combined);
        
        updateCostpermin();
        updateMood();
        Player.visitors = getStage().getActors().size;
        for(Actor a : getStage().getActors()){
            if(a instanceof Repairman || a instanceof Cleaner){
                Player.visitors--;
            }
        }
        
        // DESPAWNING THE LEAVING NPCS /////////////////////////////////////
        for(int i = 0; i < getStage().getActors().size; i++){
            if(getStage().getActors().get(i) instanceof Npc){
                if(((Npc)getStage().getActors().get(i)).getState() == 4){
                    if(graph.getNodeWith(((Npc)getStage().getActors().get(i)).getxGrid(), ((Npc)getStage().getActors().get(i)).getyGrid()) != null){
                        if((((Npc)getStage().getActors().get(i)).getxGrid() == ENTRANCE_X+2 && ((Npc)getStage().getActors().get(i)).getyGrid() == ENTRANCE_Y) ||
                            graph.getNodeWith(((Npc)getStage().getActors().get(i)).getxGrid(), ((Npc)getStage().getActors().get(i)).getyGrid()).getBuildingId() == -1 ||
                            (((Npc)getStage().getActors().get(i)).getElapsedTime() > 10 && (((Npc)getStage().getActors().get(i)).getPath() == null || ((Npc)getStage().getActors().get(i)).getGoal() == null))
                             ){
                            getStage().getActors().removeIndex(i);
                            i--;
                        }
                    }
                }
            }
        }
        // randomly spawning Npcs
        if(Math.random() < Player.mood/70000){
            getStage().addActor(new Npc(graph, this));
            Player.moneyoffset += Player.parkEntranceFee;
        }
        ////////////////////////////////////////////////////////////////////
        
        
        // UPDATING THE PATH GRAPH /////////////////////////////////////////
        graph = Graph.generateGraphFromMap(grid);
        graph.printGraph();
        for(Actor a : stage.getActors()){
            if(a instanceof Npc){
                ((Npc) a).setGraph(graph);
            }
            if(a instanceof Cleaner){
                ((Cleaner) a).setGraph(graph);
            }
            if(a instanceof Repairman){
                ((Repairman) a).setGraph(graph);
            }
        }
        ////////////////////////////////////////////////////////////////////

        // RENDERING THE MapElements ///////////////////////////////////////
        getStage().getBatch().setProjectionMatrix(getCamera().combined);
        getStage().getBatch().begin();
        for(int i = 0; i < getGrid().length; i++){
            for(int j = 0; j < getGrid()[0].length; j++){
                getGrid()[i][j].act(Gdx.graphics.getDeltaTime());
                getGrid()[i][j].render(0, getStage().getBatch(), i*TILE_WIDTH, j*TILE_HEIGHT);
            }
        }
        for(int i = 0; i < getGrid().length; i++){
            for(int j = 0; j < getGrid()[0].length; j++){
                if(getGrid()[i][j] instanceof Game){
                    getStage().getBatch().draw(Game.t[((Game)getGrid()[i][j]).getState()], i*World.TILE_WIDTH+((Game)getGrid()[i][j]).getWidth()*World.TILE_WIDTH - Game.t[((Game)getGrid()[i][j]).getState()].getWidth()/2, j*World.TILE_HEIGHT+ ((Game)getGrid()[i][j]).getHeight()*World.TILE_HEIGHT - World.TILE_HEIGHT/2);
                }
            }
        }
        
        
        getStage().getBatch().end();
        ////////////////////////////////////////////////////////////////////
        
        // DRAWING THE PATH GRAPH //////////////////////////////////////////
        if(renderGrid){
            graph.draw(stage.getBatch());
        }
        ////////////////////////////////////////////////////////////////////
        
        // MAKING NPCS ACT AND DRAWING THEM ////////////////////////////////
        // THE STAGE IS HANDLING MOST OF IT ////////////////////////////////
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        ////////////////////////////////////////////////////////////////////

        // Getting, which grid tile the player has selected atm
        Vector2 selectedGrid = getGridElement(Gdx.input.getX(), Gdx.input.getY());
        //System.out.println(selectedGrid);
        
        // RENDERING THE GRID BORDERS //////////////////////////////////////
        /*
        if(renderGrid){
            getRenderer().begin(ShapeRenderer.ShapeType.Line);
            getRenderer().setColor(Color.LIGHT_GRAY);
            for(int i = 0; i < getGrid().length; i++){
                for(int j = 0; j < getGrid()[0].length; j++){
                    getRenderer().rect(i*TILE_WIDTH,j*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
                }
            }
            getRenderer().end();
        }
        */
        ////////////////////////////////////////////////////////////////////

        // SELECTED GRID FIELD /////////////////////////////////////////////
        if(selectedGrid.x >= 0 && selectedGrid.x < getGrid().length && selectedGrid.y >= 0 && selectedGrid.y < getGrid()[0].length){
            
            // Drawing the selected Building on selected Grid field if there would
            // be space to build the selected Building there
            if(UI.selectedBuilding != -1){
                boolean space = true;
                    for(int i = 0; i <  UI.mapElements[UI.selectedBuilding].getWidth(); i++){
                        for(int j = 0; j <  UI.mapElements[UI.selectedBuilding].getHeight(); j++){
                            if(selectedGrid.x+i >= getGrid().length || selectedGrid.y+j >= getGrid()[0].length){
                                space = false;
                                break;
                            }
                            if(!(getGrid()[(int)selectedGrid.x+i][(int)selectedGrid.y+j] instanceof NullElement)){
                                space = false;
                                break;
                            }
                        }
                        if(!space) break;
                    }
                // if there is space -> place
                if(space){ 
                    getStage().getBatch().begin();
                    getStage().getBatch().setColor(0.5f, 0.5f, 0.5f, 0.8f);
                    UI.mapElements[UI.selectedBuilding].render(TILE_WIDTH, getStage().getBatch(), (int)selectedGrid.x*TILE_WIDTH, (int)selectedGrid.y*TILE_HEIGHT);
                    getStage().getBatch().setColor(1, 1, 1, 1f);
                    getStage().getBatch().end();
                }
                // If no space just render red square
                else{
                    if(renderGrid){
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        getRenderer().begin(ShapeRenderer.ShapeType.Filled);
                        getRenderer().setColor(1f,0,0,0.5f);
                        getRenderer().rect(selectedGrid.x*TILE_WIDTH, selectedGrid.y*TILE_HEIGHT, TILE_WIDTH,TILE_HEIGHT);
                        getRenderer().end();
                        Gdx.gl.glDisable(GL20.GL_BLEND);
                        getRenderer().begin(ShapeRenderer.ShapeType.Line);
                        getRenderer().setColor(Color.RED);
                        getRenderer().rect(selectedGrid.x*TILE_WIDTH, selectedGrid.y*TILE_HEIGHT, TILE_WIDTH,TILE_HEIGHT);
                        getRenderer().end();
                    }
                    else{
                        getRenderer().begin(ShapeRenderer.ShapeType.Line);
                        getRenderer().setColor(Color.RED);
                        getRenderer().rect(selectedGrid.x*TILE_WIDTH, selectedGrid.y*TILE_HEIGHT, TILE_WIDTH,TILE_HEIGHT);
                        getRenderer().end();
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////
        
        
        // CAMERA MOVEMENT /////////////////////////////////////////////////
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                if(getCamera().position.x + RESOLUTIONS[currentResolution*2]/2 < getGrid().length*TILE_WIDTH)
                getCamera().translate(cameraSpeed, 0, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                if(getCamera().position.x - RESOLUTIONS[currentResolution*2]/2 > 0)
                getCamera().translate(-cameraSpeed, 0, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                if(getCamera().position.y + RESOLUTIONS[currentResolution*2+1]/2 < getGrid()[0].length*TILE_HEIGHT)
                getCamera().translate(0, cameraSpeed, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                if(getCamera().position.y - RESOLUTIONS[currentResolution*2+1]/2 > 0)
                getCamera().translate(0, -cameraSpeed, 0);
            }
            // ZOOMING with KEYS
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                    scroll(0, -1);
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                    scroll(0, 1);
                }
            }
            // Turn Grid on/off
            if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)){
                renderGrid = !renderGrid;
            }
        ////////////////////////////////////////////////////////////////////
        
        // BUILDING A SELECTED MAPELEMENT //////////////////////////////////
        if(UI.selectedBuilding != -1){
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                placeInGrid(MapElement.getNewMapElementById(UI.mapElements[UI.selectedBuilding].getId()), (int)selectedGrid.x, (int)selectedGrid.y);
            }
            
            if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                UI.selectedBuilding = -1;
            }
        }
        ////////////////////////////////////////////////////////////////////
        
        
        // INPUT HANDLING FOR BUYING WORKMEN AND CLEANER //////////////////////
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            spawnRepairman();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            despawnRepairman();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            spawnCleaner();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            despawnCleaner();
        }
        ////////////////////////////////////////////////////////////////////
        
    }
    private void spawnRepairman(){
        stage.addActor(new Repairman(graph, this));
    }
    
    private void despawnRepairman(){
        for(int i = 0; i < stage.getActors().size; i++){
            if(stage.getActors().get(i) instanceof Repairman){
                stage.getActors().removeIndex(i);
                return;
            }
        }
    }
    
    private void spawnCleaner(){
        stage.addActor(new Cleaner(graph, this));
    }
    
    private void despawnCleaner(){
        for(int i = 0; i < stage.getActors().size; i++){
            if(stage.getActors().get(i) instanceof Cleaner){
                stage.getActors().removeIndex(i);
                return;
            }
        }
    }
    
    // Tries to place the selected building on the grid
        // if there is space and the player has enough money
    public void placeInGrid(MapElement me, int x, int y){
        if(x >= 0 && x < getGrid().length && y >= 0 && y < getGrid()[0].length){
            // Only build if Player has enough money
            if(Player.money + Player.moneyoffset >= me.getBuildingCost()){
                // me is NullElement -> Deleting a building
                if(me instanceof NullElement){
                    // Catch, so that the player can't remove the Entrance of the Park
                    if(grid[x][y] instanceof BuildingOversize){
                        if(((BuildingOversize)grid[x][y]).getRootX() == ENTRANCE_X && ((BuildingOversize)grid[x][y]).getRootY() == ENTRANCE_Y){
                            return;
                        }
                    }
                    // Player can't remove NullElement or StaticObject(Entrance)
                    if(!(grid[x][y] instanceof NullElement) && !(grid[x][y] instanceof StaticObject)){
                        // find root of the building to be deleted
                        int rootX = x;
                        int rootY = y;
                        // if player clicked on BuildingOversize we have to find its root
                        if(getGrid()[x][y] instanceof BuildingOversize){
                            rootX = ((BuildingOversize)getGrid()[x][y]).getRootX();
                            rootY = ((BuildingOversize)getGrid()[x][y]).getRootY();
                        }
                        // Then remove all the MapElements which are part of the building
                        int width = getGrid()[rootX][rootY].getWidth();
                        int height = getGrid()[rootX][rootY].getHeight();
                        for(int i = 0; i < width;i++){
                            for(int j = 0; j < height;j++){
                                getGrid()[rootX+i][rootY+j] = MapElement.getNewMapElementById(-1);
                            }
                        }
                        getGrid()[x][y] = MapElement.getNewMapElementById(-1);
                        // Player has to pay for Removing the Building
                        Player.moneyoffset -= me.getBuildingCost();
                    }
                }
                // Else Player is building a new building
                else{
                    // first check for space
                    boolean space = true;
                    for(int i = 0; i < me.getWidth(); i++){
                        for(int j = 0; j < me.getHeight(); j++){
                            if(x+i > getGrid().length || y+j > getGrid()[0].length){
                                space = false;
                                break;
                            }
                            if(!(getGrid()[x+i][y+j] instanceof NullElement)){
                                space = false;
                                break;
                            }
                        }
                        if(!space) break;
                    }
                    // if there is space build the building and fill the rest
                    // with BuildingOversize
                    if(space){
                        for(int i = 0; i < me.getWidth(); i++){
                            for(int j = 0; j < me.getHeight(); j++){
                                getGrid()[x+i][y+j] = new BuildingOversize(x, y);
                            }
                        }
                        getGrid()[x][y] = MapElement.getNewMapElementById(me.getId());
                        Player.moneyoffset -= me.getBuildingCost();
                    }
                }
            }
        }
    }
    
    // Calculates the Cost the Player has to pay per minute by iterating
    // over all the Buildings the player has built in his park
    // Cost/min is stored in Array in MapElementData
    public void updateCostpermin(){
        Player.costpermin = 0;
        for(int i = 0; i < getGrid().length; i++){
            for(int j = 0; j < getGrid()[0].length; j++){
                if(getGrid()[i][j].getId() != -1 && getGrid()[i][j].getId() != -2){
                    Player.costpermin += MapElementData.costpermin[getGrid()[i][j].getId()];
                }
            }
        }
        for(Actor a : stage.getActors()){
            if(a instanceof Cleaner){
                Player.costpermin += Cleaner.costpermin;
            }
            else if(a instanceof Repairman){
                Player.costpermin += Repairman.costpermin;
            }
        }
    }
    
    // Calculates the Mood of the park by iterating
    // over all the Buildings the player has built in his park
    // Mood is stored in Array in MapElementData
    public void updateMood(){
        float mood = 0;
        for(int i = 0; i < getGrid().length; i++){
            for(int j = 0; j < getGrid()[0].length; j++){
                if(getGrid()[i][j].getId() != -1 && getGrid()[i][j].getId() != -2){
                    mood += MapElementData.mood[getGrid()[i][j].getId()];
                }
            }
        }
        for(Actor a : getStage().getActors()){
            if(a instanceof Npc){
                mood += ((Npc) a).getMood();
            }
        }
        Player.mood = mood;
    }
    
    // managing Scrolling for camera, only gets called when not pressing CTRL_LEFT
    public void scroll(float amountX, float amountY){
        // CAMERA ZOOM /////////////////////////////////////////////////////////
        if(currentResolution+amountY >= 0 && currentResolution+amountY < RESOLUTIONS.length/2-1){
            currentResolution+=amountY;
            camera.viewportHeight = RESOLUTIONS[currentResolution*2+1];
            camera.viewportWidth = RESOLUTIONS[currentResolution*2];
        }
        ////////////////////////////////////////////////////////////////////////
    }
    
    // calculates from absolute mouse input coordinates what relative grid tile 
    // the player has selected atm
    public Vector2 getGridElement(int inputX, int inputY){
        Vector2 mousePos = getStage().getViewport().unproject(new Vector2(inputX, inputY));
        return new Vector2((float)Math.floor(mousePos.x/TILE_WIDTH), (float)Math.floor(mousePos.y/TILE_HEIGHT));
    }
    
    /**
     * GETTER + SETTER
     */

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * @param camera the camera to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @return the renderer
     */
    public ShapeRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(ShapeRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * @return the grid
     */
    public MapElement[][] getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(MapElement[][] grid) {
        this.grid = grid;
    }
    

}

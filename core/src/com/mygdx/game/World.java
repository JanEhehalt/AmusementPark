package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import static com.mygdx.game.Main.RESOLUTIONS;
import com.mygdx.game.mapElements.NullElement;

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
    
    // Activate, deactive rendering the Grid borders | TAB
    public static boolean renderGrid = true;
    
    public static int TILE_WIDTH = 48;
    public static int TILE_HEIGHT = 48;
    

    public World(int tileAmountX, int tileAmountY) {
        // creating camera / viewport stuff for View
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(RESOLUTIONS[currentResolution*2],RESOLUTIONS[currentResolution*2+1], getCamera()));
        renderer = new ShapeRenderer();
        camera.position.set(tileAmountX*TILE_WIDTH/2, tileAmountY*TILE_HEIGHT/2, 0);
        
        
        // Generating empty (/NullElements) Grid
        grid = new MapElement[tileAmountX][tileAmountY];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = new NullElement();
            }
        }
        
    }
    
    // Handing the resizing of the window
    public void resize(int width, int height){
        getStage().getViewport().update(width, height);
    }
    
    public void render(){
        getCamera().update();
        getRenderer().setProjectionMatrix(getCamera().combined);

        // RENDERING THE MapElement UNDERGROUND ////////////////////////////
            getStage().getBatch().setProjectionMatrix(getCamera().combined);
            getStage().getBatch().begin();
            for(int i = 0; i < getGrid().length; i++){
                for(int j = 0; j < getGrid()[0].length; j++){
                    getGrid()[i][j].render(0, getStage().getBatch(), i*TILE_WIDTH, j*TILE_HEIGHT);
                }
            }
            getStage().getBatch().end();
        ////////////////////////////////////////////////////////////////////

        // Getting, which grid tile the player has selected atm
        Vector2 selectedGrid = getGridElement(Gdx.input.getX(), Gdx.input.getY());
        
        // RENDERING THE GRID BORDERS //////////////////////////////////////
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
        ////////////////////////////////////////////////////////////////////

        // SELECTED GRID FIELD /////////////////////////////////////////////
        if(selectedGrid.x >= 0 && selectedGrid.x < getGrid().length && selectedGrid.y >= 0 && selectedGrid.y < getGrid()[0].length){
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
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                scroll(0, -1);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                scroll(0, 1);
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
    }
    
    // Tries to place the selected building on the grid
        // if there is space and the player has enough money
    public void placeInGrid(MapElement me, int x, int y){
        if(Player.money >= me.getBuildingCost()){
            if(me instanceof NullElement){
                if(!(grid[x][y] instanceof NullElement)){
                    getGrid()[x][y] = me;
                    Player.money -= me.getBuildingCost();
                }
            }
            else{
                if(getGrid()[x][y] instanceof NullElement){
                    getGrid()[x][y] = me;
                    Player.money -= me.getBuildingCost();
                }
            }
        }
    }
    
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *  The World class will manage following features:
 *      - camera transformations
 *      - rendering the game world according to the grid array
 *      - grid array which mapElement is in which place
 */
public class World {
    
    Stage stage;
    Camera camera;
    ShapeRenderer renderer;
    
    MapElement[][] grid;
    
    int cameraSpeed = 10;
    
    public static int currentResolution = 5;
    
    public static boolean renderGrid = true;

    public World(int tileAmountX, int tileAmountY) {
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(RESOLUTIONS[currentResolution*2],RESOLUTIONS[currentResolution*2+1],camera));
        renderer = new ShapeRenderer();
        
        grid = new MapElement[tileAmountX][tileAmountY];
        camera.position.set(tileAmountX*32/2, tileAmountY*32/2, 0);
        
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = new NullElement();
            }
        }
    }
    
    public void resize(int width, int height){
        stage.getViewport().update(width, height);
    }
    
    public void render(){
        camera.update();
        renderer.setProjectionMatrix(camera.combined);

        // RENDERING THE GREEN UNDERGROUND /////////////////////////////////
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(139f/255f, 90f/255f, 43f/255f, 1);
            renderer.rect(0,0, grid.length*32, grid[0].length*32);
            renderer.end();
        ////////////////////////////////////////////////////////////////////
        
        // RENDERING THE MapElement UNDERGROUND ////////////////////////////
            stage.getBatch().setProjectionMatrix(camera.combined);
            stage.getBatch().begin();
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    grid[i][j].render(0, stage.getBatch(), i*32, j*32);
                }
            }
            stage.getBatch().end();
        ////////////////////////////////////////////////////////////////////

        Vector2 selectedGrid = getGridElement(Gdx.input.getX(), Gdx.input.getY());
        // RENDERING THE GRID IN GENERAL ///////////////////////////////////
        if(renderGrid){
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.LIGHT_GRAY);
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    renderer.rect(i*32,j*32,32,32);
                }
            }
            renderer.end();
        }
        ////////////////////////////////////////////////////////////////////

        // SELECTED GRID FIELD /////////////////////////////////////////////
        if(selectedGrid.x >= 0 && selectedGrid.x < grid.length && selectedGrid.y >= 0 && selectedGrid.y < grid[0].length){
            if(renderGrid){
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(1f,0,0,0.5f);
                renderer.rect(selectedGrid.x*32, selectedGrid.y*32, 32,32);
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.RED);
                renderer.rect(selectedGrid.x*32, selectedGrid.y*32, 32,32);
                renderer.end();
            }
            else{
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.RED);
                renderer.rect(selectedGrid.x*32, selectedGrid.y*32, 32,32);
                renderer.end();
            }
        }
        ////////////////////////////////////////////////////////////////////
        
        // CAMERA MOVEMENT /////////////////////////////////////////////////
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                if(camera.position.x + RESOLUTIONS[currentResolution*2]/2 < grid.length*32)
                camera.translate(cameraSpeed, 0, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                if(camera.position.x - RESOLUTIONS[currentResolution*2]/2 > 0)
                camera.translate(-cameraSpeed, 0, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                if(camera.position.y + RESOLUTIONS[currentResolution*2+1]/2 < grid[0].length*32)
                camera.translate(0, cameraSpeed, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                if(camera.position.y - RESOLUTIONS[currentResolution*2+1]/2 > 0)
                camera.translate(0, -cameraSpeed, 0);
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
                placeInGrid(MapElement.getNewMapElementById(UI.mapElements[UI.selectedBuilding].id), (int)selectedGrid.x, (int)selectedGrid.y);
            }
            
            if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                UI.selectedBuilding = -1;
            }
        }
        ////////////////////////////////////////////////////////////////////
    }
    
    public void placeInGrid(MapElement me, int x, int y){
        if(Player.money >= me.buildingCost){
            if(me instanceof NullElement){
                if(!(grid[x][y] instanceof NullElement)){
                    grid[x][y] = me;
                    Player.money -= me.buildingCost;
                }
            }
            else{
                if(grid[x][y] instanceof NullElement){
                    grid[x][y] = me;
                    Player.money -= me.buildingCost;
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
    
    public Vector2 getGridElement(int inputX, int inputY){
        Vector2 mousePos = stage.getViewport().unproject(new Vector2(inputX, inputY));
        return new Vector2((float)Math.floor(mousePos.x/32), (float)Math.floor(mousePos.y/32));
    }
    

}

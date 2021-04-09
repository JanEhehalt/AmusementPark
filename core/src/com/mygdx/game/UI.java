/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 *
 * @author janeh
 */
public class UI {
    
    public static int selectedBuilding = 1;
    
    ShapeRenderer UIrenderer;
    Batch UIbatch;
    
    BitmapFont font;
    
    Viewport worldViewport;
    
    Texture buildingsUi;
    
    public static MapElement[] mapElements = new MapElement[12];
    

    public UI(Viewport WorldViewport) {
        UIrenderer = new ShapeRenderer();
        worldViewport = WorldViewport;
        UIbatch = new SpriteBatch();
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 21;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        
        mapElements[0] = MapElement.getNewMapElementById(0);
        mapElements[1] = MapElement.getNewMapElementById(1);
        mapElements[2] = MapElement.getNewMapElementById(2);
        mapElements[3] = MapElement.getNewMapElementById(10);
        mapElements[4] = MapElement.getNewMapElementById(11);
        mapElements[5] = MapElement.getNewMapElementById(12);
        mapElements[6] = MapElement.getNewMapElementById(20);
        mapElements[7] = MapElement.getNewMapElementById(21);
        mapElements[8] = MapElement.getNewMapElementById(22);
        mapElements[9] = MapElement.getNewMapElementById(23);
        mapElements[10] = MapElement.getNewMapElementById(30);
        mapElements[11] = MapElement.getNewMapElementById(-1);;
    }
    
    public void render(){
        
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        UIrenderer.begin(ShapeRenderer.ShapeType.Filled);
        UIrenderer.setColor(0,0,0,0.8f);
        UIrenderer.rect(Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-100, 250, 70);
        UIrenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        UIbatch.begin();
        font.setColor(Color.WHITE);
        font.draw(UIbatch, Player.money+" $", Gdx.graphics.getWidth()-280, Gdx.graphics.getHeight()-65, 210, 1, false);
        UIbatch.end();
        UIrenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        
        UIrenderer.setColor(Color.WHITE);
        for(int i = 0; i < mapElements.length; i++){
            UIrenderer.rect(20, Gdx.graphics.getHeight() - 80 - i*72-8, 72, 72);
        }
        if(selectedBuilding != -1){
            UIrenderer.setColor(Color.RED);
            UIrenderer.rect(20, Gdx.graphics.getHeight() - 80 - selectedBuilding*72-8, 72, 72);
            
            UIrenderer.end();
            UIbatch.begin();
            font.setColor(Color.CLEAR);
            float width = font.draw(UIbatch, mapElements[selectedBuilding].name, 0, 0).width;
            float priceWidth = font.draw(UIbatch, mapElements[selectedBuilding].buildingCost+" $", 0, 0).width;
            UIbatch.end();
            
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            UIrenderer.begin(ShapeRenderer.ShapeType.Filled);
            UIrenderer.setColor(0,0,0, 0.5f);
            UIrenderer.rect(92, Gdx.graphics.getHeight() - 80 - selectedBuilding*72-8, width+7, 72);
            UIrenderer.rect(90+width+5-priceWidth, Gdx.graphics.getHeight() - 80 - (selectedBuilding+1)*72-8, priceWidth+4, 72);
            UIrenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            UIrenderer.setColor(Color.WHITE);
            
            
            UIbatch.begin();
            font.setColor(Color.WHITE);
            font.draw(UIbatch, mapElements[selectedBuilding].name, 96, Gdx.graphics.getHeight() - 80 - selectedBuilding*72+40, width, 1, false);
            font.draw(UIbatch, mapElements[selectedBuilding].buildingCost+" $", 92+width+5-priceWidth, Gdx.graphics.getHeight() - 80 - (selectedBuilding+1)*72+40, priceWidth, 1, false);
            
            UIbatch.end();
            UIrenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
        UIrenderer.end();
        UIbatch.begin();
        
        for(int i = 0; i < mapElements.length; i++){
            mapElements[i].renderScaled(0, UIbatch, 24, Gdx.graphics.getHeight() - 80 - i*72-4, 2f);
        }
        
        UIbatch.end(); 
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            selectedBuilding = 0;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            selectedBuilding = 1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            selectedBuilding = 2;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            selectedBuilding = 3;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
            selectedBuilding = 4;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
            selectedBuilding = 5;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)){
            selectedBuilding = 6;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)){
            selectedBuilding = 7;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)){
            selectedBuilding = 8;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            selectedBuilding = 9;
        }
        if(Gdx.input.isKeyJustPressed(71)){
            selectedBuilding = 10;
        }
        
    }
    
    public void scroll(float amountX, float amountY){
        if(amountY < 0){
            if(selectedBuilding > 0){
                selectedBuilding--;
            }
        }
        else{
            if(selectedBuilding < mapElements.length-1){
                selectedBuilding++;
            }
        }
    }
    
}

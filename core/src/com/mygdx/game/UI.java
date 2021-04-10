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

public class UI {
    
    public static int selectedBuilding = -1;
    public static MapElement[] mapElements;
    
    private ShapeRenderer UIrenderer;
    private Batch UIbatch;
    private BitmapFont font;
    
    

    public UI() {
        UIrenderer = new ShapeRenderer();
        UIbatch = new SpriteBatch();
        
        // generating the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 21;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);

        // Creating all the MapElements which can be built and will be part of the UI
        mapElements = new MapElement[12];
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
        
        // RENDERING MONEY BOX /////////////////////////////////////////////////
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        getUIrenderer().setColor(0,0,0,0.8f);
        getUIrenderer().rect(Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-100, 250, 70);
        getUIrenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        getUIbatch().begin();
        getFont().setColor(Color.WHITE);
        getFont().draw(getUIbatch(), Player.money+" $", Gdx.graphics.getWidth()-280, Gdx.graphics.getHeight()-65, 210, 1, false);
        getUIbatch().end();
        getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        ////////////////////////////////////////////////////////////////////////
        
        // RENDERING BUILDINGS UI //////////////////////////////////////////////
        getUIrenderer().setColor(Color.WHITE);
        for(int i = 0; i < mapElements.length; i++){
            getUIrenderer().rect(20, Gdx.graphics.getHeight() - 80 - i*72-8, 72, 72);
        }
        if(selectedBuilding != -1){
            getUIrenderer().setColor(Color.RED);
            getUIrenderer().rect(20, Gdx.graphics.getHeight() - 80 - selectedBuilding*72-8, 72, 72);
            
            getUIrenderer().end();
            getUIbatch().begin();
            getFont().setColor(Color.CLEAR);
            float width = getFont().draw(getUIbatch(), mapElements[selectedBuilding].getName(), 0, 0).width;
            float priceWidth = getFont().draw(getUIbatch(), mapElements[selectedBuilding].getBuildingCost()+" $", 0, 0).width;
            getUIbatch().end();
            
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
            getUIrenderer().setColor(0,0,0, 0.5f);
            getUIrenderer().rect(92, Gdx.graphics.getHeight() - 80 - selectedBuilding*72-8, width+7, 72);
            getUIrenderer().rect(90+width+5-priceWidth, Gdx.graphics.getHeight() - 80 - (selectedBuilding+1)*72-8, priceWidth+4, 72);
            getUIrenderer().end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            getUIrenderer().setColor(Color.WHITE);
            
            
            getUIbatch().begin();
            getFont().setColor(Color.WHITE);
            getFont().draw(getUIbatch(), mapElements[selectedBuilding].getName(), 96, Gdx.graphics.getHeight() - 80 - selectedBuilding*72+40, width, 1, false);
            getFont().draw(getUIbatch(), mapElements[selectedBuilding].getBuildingCost()+" $", 92+width+5-priceWidth, Gdx.graphics.getHeight() - 80 - (selectedBuilding+1)*72+40, priceWidth, 1, false);
            
            getUIbatch().end();
            getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        }
        getUIrenderer().end();
        getUIbatch().begin();
        
        for(int i = 0; i < mapElements.length; i++){
            mapElements[i].renderScaled(0, getUIbatch(), 24, Gdx.graphics.getHeight() - 80 - i*72-4, 2f);
        }
        
        getUIbatch().end(); 
        ////////////////////////////////////////////////////////////////////////
        
        // SELECTING BUILDING IS ALSO POSSIBLE BY NUM_KEYS /////////////////////
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
        if(Gdx.input.isKeyJustPressed(71)){ // KEYCODE ß\?
            selectedBuilding = 10;
        }
        ////////////////////////////////////////////////////////////////////////
        
    }
    
    // Handling the scrolling through the buildings
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

    /**
     * @return the UIrenderer
     */
    public ShapeRenderer getUIrenderer() {
        return UIrenderer;
    }

    /**
     * @param UIrenderer the UIrenderer to set
     */
    public void setUIrenderer(ShapeRenderer UIrenderer) {
        this.UIrenderer = UIrenderer;
    }

    /**
     * @return the UIbatch
     */
    public Batch getUIbatch() {
        return UIbatch;
    }

    /**
     * @param UIbatch the UIbatch to set
     */
    public void setUIbatch(Batch UIbatch) {
        this.UIbatch = UIbatch;
    }

    /**
     * @return the font
     */
    public BitmapFont getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(BitmapFont font) {
        this.font = font;
    }
    
}

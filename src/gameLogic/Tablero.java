package gameLogic;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import gameActors.BaseActor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Tablero extends BaseScreen{
	BaseActor fondo;
	
	@Override
	public void initialize() {
		fondo = new BaseActor(0,0,mainStage);	
		Camera mainCamera = fondo.getStage().getCamera();
		fondo.loadTexture("assets/Tablero.png");
		fondo.setSize(mainCamera.viewportWidth, mainCamera.viewportHeight);
		
		
		System.out.println(mainCamera.viewportHeight);
		System.out.println(mainCamera.viewportWidth);
		
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public boolean keyDown(int keycode)
    {        
           
        
        if ( keycode == Keys.B ) {
        	MathMaze.setActiveScreen(new LevelScreen());
        }
        
        
        return false;  
    }
	
}

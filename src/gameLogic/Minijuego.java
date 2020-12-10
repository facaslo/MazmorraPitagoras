package gameLogic;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import java.util.ArrayList;
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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import gameActors.BaseActor;
import gameActors.TiledActor;
import gameActors.Carta;
import com.badlogic.gdx.math.MathUtils;

public class Minijuego extends BaseScreen{
	
	String nombreTablero;
	private TiledActor tma;
	ArrayList<MapObject> listaCajasPositivas;
	ArrayList<MapObject> listaCajasNegativas;
	ArrayList<MapObject> incognitas;	
	ArrayList<Carta> CartasMano;
	
	public Minijuego(String nombreMapa) {
		super(nombreMapa);
	}
	
	
	@Override
	public void initialize() {		
		tma = new TiledActor(nombreTma, mainStage , false);		
		
		
		// Hitboxes de cartas positivas y negativas
		listaCajasPositivas = tma.getRectangleListContain("numeropositivo");
		listaCajasNegativas = tma.getRectangleListContain("numeronegativo");		
		incognitas = tma.getRectangleList("incognita");
		CartasMano = new ArrayList<Carta>();
		
		Camera mainCamera = tma.getStage().getCamera();
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;
		mainCamera.position.x= 104;
		mainCamera.position.y= 80;	
		
				
		// Se colocan las incognitas
		for (int i=0; i<incognitas.size(); i++) {
			Carta nuevaCarta = new Carta((float)incognitas.get(i).getProperties().get("x"),
					(float)incognitas.get(i).getProperties().get("y"),					
					mainStage, false, 
					"assets/Boxes/cofreTesoro/cofreTesoro1.png");	
		}
		
		
		//Cargar sprites de las cartas positivas
		for (int i=0; i<listaCajasPositivas.size(); i++) {	
			int random = MathUtils.random(1,44);			
			Carta nuevaCarta = new Carta((float)listaCajasPositivas.get(i).getProperties().get("x"),
					(float)listaCajasPositivas.get(i).getProperties().get("y"),					
					mainStage, false, 
					"assets/Boxes/monstersPositive/MonsterPositive"+ Integer.toString(random) + ".png");
			CartasMano.add(nuevaCarta);
		}		
		
		//Cargar sprites de las cartas negativas
		for (int i=0; i<listaCajasNegativas.size(); i++) {
			int random = MathUtils.random(1,44);
			Carta nuevaCarta = new Carta((float)listaCajasNegativas.get(i).getProperties().get("x"),
					(float)listaCajasNegativas.get(i).getProperties().get("y"),					
					mainStage, false, 
					"assets/Boxes/monstersNegative/MonsterNegative"+ Integer.toString(random) + ".png");
			CartasMano.add(nuevaCarta);
		}	
		
		
		
	}
	
	
	public void resetTablero() {
		initialize();
	}
	

	@Override
	public void update(float dt) {
		Camera mainCamera = tma.getStage().getCamera();		
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;
		mainCamera.position.x= 104;
		mainCamera.position.y= 80;	
		
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public boolean keyDown(int keycode)
    {               
        
        if ( keycode == Keys.B) {        	
        	MathMaze.setActiveScreen(MathMaze.getLevelScreen());
        }
        
        if ( keycode == Keys.C) {
        	// MathMaze.setActiveScreen(new Minijuego("assets/tableroA2.tmx"));
        	resetTablero();
        }        
        
        
        return false;  
    }
	
	
	@Override
	public void resize(int width, int height) {		
		mainStage.getViewport().update(width, height, true);
	}
	
	
	
	
}

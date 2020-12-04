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
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import gameActors.*;

public class LevelScreen extends BaseScreen{
	Jugador jugador;   	
	ArrayList<BaseActor> listaHitbox;
	ArrayList<Puerta> listaHitboxPuertas;	
	

	@Override
	public void initialize() {
				
		TiledActor tma = new TiledActor("assets/Map.tmx", mainStage);		
				
		ArrayList<MapObject> listaPared = tma.getRectangleList("pared");
		ArrayList<MapObject> listaPuertas = tma.getRectangleListContain("puerta");
		listaHitboxPuertas = new ArrayList<Puerta>();		
		listaHitbox = new ArrayList<BaseActor>();		
		
		addHitboxestoTiles(listaPared);
		addHitboxestoTiles(listaPuertas);		
		
		//System.out.println(listaHitbox);
		//System.out.println(listaHitboxPuertas);				
		
		jugador = new Jugador(400,100, mainStage);
		TiledActor tma2 = new TiledActor("assets/Map2.tmx", mainStage);
	}

	@Override
	public void update(float dt) {
		// Se añaden las colisiones entre la tortuga y las rocas
			 for (BaseActor wall : listaHitbox)
				jugador.preventOverlap(wall);
			 
			 for (BaseActor wall : listaHitboxPuertas)
					jugador.preventOverlap(wall);	
			 
		
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Añadir hitboxes a los tiles y separar en puertas y muros
	public void addHitboxestoTiles(ArrayList<MapObject> lista) {
		for(MapObject rectangulo: lista) {		   
			if(((String)rectangulo.getProperties().get("name")).contains("puerta"))	{			
				Puerta puerta = new Puerta((float)rectangulo.getProperties().get("x"),
						(float)rectangulo.getProperties().get("y"), mainStage);
				puerta.setNombrePuerta((String)rectangulo.getProperties().get("name"));
				System.out.println(puerta.getNombrePuerta());
				puerta.setBoundaryRectangle();
				listaHitboxPuertas.add(puerta);				
			}
			else {
				BaseActor hitbox = new BaseActor((float)rectangulo.getProperties().get("x"),
						(float)rectangulo.getProperties().get("y"), mainStage);			
				float anchoHitbox = (float)rectangulo.getProperties().get("width");
				hitbox.setWidth(anchoHitbox);
				float altoHitbox = (float)rectangulo.getProperties().get("height");
				hitbox.setHeight(altoHitbox);
			   	hitbox.setBoundaryRectangle(); 	
				listaHitbox.add(hitbox);
			}
		}
	}
	
	public boolean keyDown(int keycode)
    {
        if ( keycode == Keys.SPACE ) {
        	if(listaHitboxPuertas.size() != 0) {
	        	listaHitboxPuertas.get(0).abrirPuerta();
	        	listaHitboxPuertas.get(0).remove();
	        	mainStage.addActor(listaHitboxPuertas.get(0));
	        	listaHitboxPuertas.remove(0);
        	} 	
        }
        
        if ( keycode == Keys.A ) {
        	MathMaze.setActiveScreen(new Tablero());
        }
        
        
        if ( keycode == Keys.B ) {
        	MathMaze.setActiveScreen(new LevelScreen());
        }
        
        
        return false;  
    }
	
	

}

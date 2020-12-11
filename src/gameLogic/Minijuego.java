package gameLogic;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import gameActors.Cofre;
import gameActors.Remolino;

import com.badlogic.gdx.math.MathUtils;

public class Minijuego extends BaseScreen{
	
	String nombreTablero;
	private TiledActor tma;
	ArrayList<MapObject> listaCajasPositivas;
	ArrayList<MapObject> listaCajasNegativas;
	ArrayList<MapObject> incognitas;
	ArrayList<MapObject> listaCeros;
	MapObject zonaIzquierda;
	MapObject zonaDerecha;
	ArrayList<Carta> cartaManoSuma;
	ArrayList<MapObject> hitboxesCartasManoSuma;
	
	boolean incognitaZonaIzquierda;
	BaseActor hitboxZonaIzquierda;
	BaseActor hitboxZonaDerecha;
	public static ArrayList<BaseActor> BaseActors;	
	int elementosZona;
	
	boolean victoria;
	Cofre cofre;
	
	
	
	public Minijuego(String nombreMapa , boolean izquierda) {
		super(nombreMapa);
		incognitaZonaIzquierda = izquierda;
	}
	
	
	@Override
	public void initialize() {		
		tma = new TiledActor(nombreTma, mainStage , false);			
		
		// Hitboxes de cartas positivas y negativas
		listaCajasPositivas = tma.getRectangleListContain("numeropositivo");
		listaCajasNegativas = tma.getRectangleListContain("numeronegativo");	
		incognitas = tma.getRectangleList("incognita");
		listaCeros = tma.getRectangleListContain("cero");
		zonaIzquierda = tma.getRectangleList("zonaIzquierda").get(0);
		zonaDerecha = tma.getRectangleList("zonaDerecha").get(0);
		
		hitboxesCartasManoSuma = tma.getRectangleListContain("cartaManoSuma");
		cartaManoSuma = new ArrayList<Carta>();
		
		BaseActors = new ArrayList<BaseActor>();
		
		
		Camera mainCamera = tma.getStage().getCamera();
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;
		mainCamera.position.x= 104;
		mainCamera.position.y= 80;	
		
		// Crear las zonas del tablero
				hitboxZonaIzquierda = new BaseActor((float)zonaIzquierda.getProperties().get("x"), 
						(float)zonaIzquierda.getProperties().get("y"), mainStage);
				hitboxZonaIzquierda.setWidth((float)zonaIzquierda.getProperties().get("width"));
				hitboxZonaIzquierda.setHeight((float)zonaIzquierda.getProperties().get("height"));
				hitboxZonaIzquierda.setBoundaryRectangle();
				
				hitboxZonaDerecha = new BaseActor((float)zonaDerecha.getProperties().get("x"), 
						(float)zonaDerecha.getProperties().get("y"), mainStage);
				hitboxZonaDerecha.setWidth((float)zonaDerecha.getProperties().get("width"));
				hitboxZonaDerecha.setHeight((float)zonaDerecha.getProperties().get("height"));
				hitboxZonaDerecha.setBoundaryRectangle();
		
				
		// Se colocan las incognitas
		for (int i=0; i<incognitas.size(); i++) {
			cofre = new Cofre((float)incognitas.get(i).getProperties().get("x"),
					(float)incognitas.get(i).getProperties().get("y"),					
					mainStage ) ;		
			BaseActors.add(cofre);
		}
		
		
		//Cargar sprites de las cartas positivas
		for (int i=0; i<listaCajasPositivas.size(); i++) {	
			int random = MathUtils.random(1,44);			
			Carta nuevaCarta = new Carta((float)listaCajasPositivas.get(i).getProperties().get("x"),
					(float)listaCajasPositivas.get(i).getProperties().get("y"),					
					mainStage, false, 
					"assets/Boxes/monstersPositive/MonsterPositive"+ Integer.toString(random) + ".png");
			cartaManoSuma.add(nuevaCarta);
			BaseActors.add(nuevaCarta);
		}		
		
		//Cargar sprites de las cartas negativas
		for (int i=0; i<listaCajasNegativas.size(); i++) {
			int random = MathUtils.random(1,44);
			Carta nuevaCarta = new Carta((float)listaCajasNegativas.get(i).getProperties().get("x"),
					(float)listaCajasNegativas.get(i).getProperties().get("y"),					
					mainStage, false, 
					"assets/Boxes/monstersNegative/MonsterNegative"+ Integer.toString(random) + ".png");
			cartaManoSuma.add(nuevaCarta);
			BaseActors.add(nuevaCarta);
		}	
		
		//Cargar los sprites del cero
		for (int i=0; i<listaCeros.size(); i++) {			
			Remolino remolino = new Remolino((float)listaCeros.get(i).getProperties().get("x"),
					(float)listaCeros.get(i).getProperties().get("y"),					
					mainStage);
			BaseActors.add(remolino);
					
		}	
		
		// Cargar las cartas en mano de suma
		for (int i=0; i<hitboxesCartasManoSuma.size(); i++) {
			if (cartaManoSuma.get(i).getNombreCarta().contains("monstersNegative/MonsterNegative")) {
				String nuevoNombre = cartaManoSuma.get(i).getNombreCarta().replaceAll("monstersNegative/MonsterNegative", 
						"monstersPositive/MonsterPositive");
				Carta nuevaCarta = new Carta((float)hitboxesCartasManoSuma.get(i).getProperties().get("x"), 
						(float)hitboxesCartasManoSuma.get(i).getProperties().get("y"),
						mainStage, true, nuevoNombre);
				BaseActors.add(nuevaCarta);	
			}
			else if (cartaManoSuma.get(i).getNombreCarta().contains("monstersPositive/MonsterPositive")) {
				String nuevoNombre = cartaManoSuma.get(i).getNombreCarta().replaceAll("monstersPositive/MonsterPositive", 
						"monstersNegative/MonsterNegative");
				Carta nuevaCarta = new Carta((float)hitboxesCartasManoSuma.get(i).getProperties().get("x"), 
						(float)hitboxesCartasManoSuma.get(i).getProperties().get("y"),
						mainStage, true, nuevoNombre);
				BaseActors.add(nuevaCarta);	
			}
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
		
		// Verificamos si las cartas coinciden
		try {
			String nombreCartaA = Carta.getCartasClicked().get(0).getNombreCarta();
			String nombreCartaB = Carta.getCartasClicked().get(1).getNombreCarta();
			String numeroCartaA = nombreCartaA.replaceAll("[^0-9]", "");
			String numeroCartaB = nombreCartaB.replaceAll("[^0-9]", "");			
			
			if(numeroCartaA.equals(numeroCartaB)) {
				if(nombreCartaA.contains("monstersPositive") && nombreCartaB.contains("monstersNegative") ||
				nombreCartaB.contains("monstersPositive") && nombreCartaA.contains("monstersNegative")	) {
				Carta.getCartasClicked().get(0).remove();
				Carta.getCartasClicked().get(1).remove();
				BaseActors.remove(Carta.getCartasClicked().get(0));
				BaseActors.remove(Carta.getCartasClicked().get(1));
				Carta.cleanCartasClicked();				
				}
			}
		}
		catch(Exception e) {
			// System.out.println(Carta.getCartasClicked());
		}
		
		
		elementosZona = 0;
		
		for (BaseActor actor: BaseActors) {		
			if (incognitaZonaIzquierda) {
				if (hitboxZonaIzquierda.overlaps(actor)) {				
					elementosZona++;			
				}
			}else {
				if (hitboxZonaDerecha.overlaps(actor)) {				
					elementosZona++;										
				}
			}			
		}
		
			
		// Verificar si se puede avanzar de nivel
		if(elementosZona == 1) {
			cofre.setCofreAbierto(true);		
		}
		
		
		
		
		
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode)
    {               
        
        if ( keycode == Keys.B) {        	
        	MathMaze.setActiveScreen(MathMaze.getLevelScreen());
        }
        
        if ( keycode == Keys.C) {
        	MathMaze.setActiveScreen(new Minijuego("assets/tableroA2.tmx", true));        	
        }        
        
        
        return false;  
    }
	
	
	@Override
	public void resize(int width, int height) {		
		mainStage.getViewport().update(width, height, true);
	}
	
	
	
	
}

package gameLogic;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import gameActors.*;

public class LevelScreen extends BaseScreen{
	private Jugador jugador;   	
	private ArrayList<BaseActor> listaHitbox;
	private ArrayList<Puerta> listaHitboxPuertas;
	private ArrayList<Cofre> listaHitboxCofre;
	private ArrayList<Puerta> Puertitas;
	private ArrayList<Cofre> Cofresitos;
	
	private TiledActor tma;	
	
	// Hud
	private Camera mainCamera;
	private String noLlaves;
	private boolean menuClickedFrameAnterior;
	private boolean menuClicked;
	private Button menu;
	
	
	private TextButton botonContinuar;
	private TextButton botonSilenciar;	
	private TextButton botonSalir;	
	
	private Music temaMazmorra;
	

	public LevelScreen(){
		super(null, "" , 0 , 0 );
	}	
	
	@Override	
	public void initialize() {		
		menuClicked = false;
		menuClickedFrameAnterior = menuClicked;
		
		tma = new TiledActor("assets/Map.tmx", mainStage , true);		
				
		ArrayList<MapObject> listaPared = tma.getRectangleList("pared");
		ArrayList<MapObject> listaPuertas = tma.getRectangleListContain("puerta");	
		ArrayList<MapObject> listaAntorchas = tma.getRectangleListContain("antorcha");
		ArrayList<MapObject> listaCofres = tma.getRectangleListContain("cofre");
		listaHitboxPuertas = new ArrayList<Puerta>();		
		listaHitbox = new ArrayList<BaseActor>();	
		listaHitboxCofre = new ArrayList<Cofre>();	
		
		addHitboxestoTiles(listaPared);
		addHitboxestoTiles(listaPuertas);		
		addHitboxestoTiles(listaCofres);
		
		
		
		jugador = new Jugador(624,64, mainStage);
		jugador.centerAtPosition(624, 64);
		addSpriteToMap(listaAntorchas, "antorcha" , "assets/Antorcha.png" , 1, 8);
		TiledActor tma2 = new TiledActor("assets/Map2.tmx", mainStage, true);	
		
		mainCamera = tma.getStage().getCamera();
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;	
		
		
		/*
		* HUD
		*/			
		uiStageNivel.addActor(uiTable);
		
			
		
		ButtonStyle estiloMenu = new ButtonStyle();
		Texture texturaMenu = new Texture(Gdx.files.internal("assets/Botones/botonMenu.png"));
		TextureRegion regionMenu = new TextureRegion(texturaMenu);
		estiloMenu.up = new TextureRegionDrawable(regionMenu);	
		menu = new Button(estiloMenu);	
		
		
		menu.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {												
                super.enter(event, x , y, pointer, fromActor);
                menu.setColor(Color.GRAY);        
            }	
			
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	menu.setColor(1,1,1,1);
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				menuClicked = true;		
			}			
		});
		
		
		crearHudNivel();
		
		
		botonContinuar = new TextButton( "Continuar", BaseGame.textButtonStyle );
		botonContinuar.getLabel().setFontScale(0.40f);
		
		botonContinuar.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {												
                super.enter(event, x , y, pointer, fromActor);
                botonContinuar.setColor(Color.GRAY);        
            }	
			
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	botonContinuar.setColor(1,1,1,1);
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				menuClicked = false;				
			}			
		});
		
		botonSilenciar = new TextButton( "Silenciar", BaseGame.textButtonStyle );
		botonSilenciar.getLabel().setFontScale(0.40f);
		
		botonSilenciar.addListener(new ClickListener() {	
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {												
                super.enter(event, x , y, pointer, fromActor);
                botonSilenciar.setColor(Color.GRAY);        
            }	
			
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	botonSilenciar.setColor(1,1,1,1);
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				temaMazmorra.setVolume(0f);	
			}		
			
		});
		
				
		
		botonSalir = new TextButton( "Salir", BaseGame.textButtonStyle );
		botonSalir.getLabel().setFontScale(0.40f);
		
		botonSalir.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {												
                super.enter(event, x , y, pointer, fromActor);
                botonSalir.setColor(Color.GRAY);        
            }	
			
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	botonSalir.setColor(1,1,1,1);
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();	
			}		
		});
		
		
		/*
		 * Fin HUD
		 */
		
		temaMazmorra = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/cave_theme_1.wav"));
		temaMazmorra.setLooping(true);
		temaMazmorra.setVolume(0.5f);
		temaMazmorra.play();
	}
	
	
	

	@Override
	public void update(float dt) {
		// Se añaden las colisiones entre la tortuga y las rocas
		for (BaseActor wall : listaHitbox)
			jugador.preventOverlap(wall);
		 
		for (BaseActor wall : listaHitboxPuertas)
			jugador.preventOverlap(wall);
		
		for (BaseActor wall : listaHitboxCofre)
			jugador.preventOverlap(wall);	
				
		if(Jugador.getNumeroLlavesCambio() == true) {
			if(!menuClicked) {	
				crearHudNivel();						
			} 
		}
			 
		if (menuClickedFrameAnterior != menuClicked) {
			if(menuClicked) {
				crearMenuNivel();
			} else {
				crearHudNivel();
			}
		} 			
			 
		 // To keep camera zoom
		 mainCamera = tma.getStage().getCamera();
		 mainCamera.viewportWidth = 208;
		 mainCamera.viewportHeight = 160;
		 mainCamera.update();
		 
		 
		 			 
		 menuClickedFrameAnterior = menuClicked;			
	}	
	
	//Añadir hitboxes a los tiles y separar en puertas y muros
	public void addHitboxestoTiles(ArrayList<MapObject> lista) {
		for(MapObject rectangulo: lista) {		   
			if(((String)rectangulo.getProperties().get("name")).contains("puerta"))	{			
				Puerta puerta = new Puerta((float)rectangulo.getProperties().get("x"),
						(float)rectangulo.getProperties().get("y"), mainStage);
				puerta.setNombrePuerta((String)rectangulo.getProperties().get("name"));				
				puerta.setBoundaryRectangle();
				listaHitboxPuertas.add(puerta);				
			}
			else if(((String)rectangulo.getProperties().get("name")).contains("cofre")){
				Cofre cofre = new Cofre((float)rectangulo.getProperties().get("x"),
						(float)rectangulo.getProperties().get("y"), mainStage, false);				
				cofre.setNombre((String)rectangulo.getProperties().get("name"));
				cofre.setBoundaryRectangle();
				listaHitboxCofre.add(cofre);	
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
	
	public void addSpriteToMap(ArrayList<MapObject> lista , String nombre, String direccionSpritesheet , int rows, int cols) {
		for(MapObject rectangulo: lista) {
			if(((String)rectangulo.getProperties().get("name")).contains(nombre)) {
				BaseActor sprite = new BaseActor((float)rectangulo.getProperties().get("x"),
						(float)rectangulo.getProperties().get("y"), mainStage);		
				sprite.loadAnimationFromSheet(direccionSpritesheet, rows, cols, 0.1f, true);
			}			
		}
	}
	
	public boolean keyDown(int keycode)
    {
        if ( keycode == Keys.SPACE ) {
        	Puertitas =  new ArrayList<>(listaHitboxPuertas);
        	for (Puerta puerta: Puertitas) {
        		if (puerta.isWithinDistance(10, jugador) && Jugador.getNumeroLlaves() != 0) {
        			puerta.abrirPuerta();
        			listaHitboxPuertas.remove(puerta);
        			Jugador.setNumeroLlaves(Jugador.getNumeroLlaves() -1);
        		}
        	}        	
        	
        	Cofresitos = new ArrayList<>(listaHitboxCofre);
        	for (Cofre cofre: Cofresitos) {
        		if (cofre.isWithinDistance(10, jugador) && !cofre.getCofreAbierto()) {
        			if(cofre.getNombre().equals("cofreA"))
        				MazmorraPitagoras.setActiveScreen(new Minijuego(cofre, "A", 1, 2));        				
        			else if(cofre.getNombre().equals("cofreB"))
        				MazmorraPitagoras.setActiveScreen(new Minijuego(cofre, "B", 1, 3));
        			else {
        				MazmorraPitagoras.setActiveScreen(new Minijuego(cofre, "C", 1, 5));
        			}
        			
        		}
        	}        	
        	
        	
        	
        }
        
        if ( keycode == Keys.A ) {        	
        	
        }      
        
        if ( keycode == Keys.T ) {        	
        	Jugador.setNumeroLlaves(Jugador.getNumeroLlaves()+1);
        	System.out.println(Jugador.getNumeroLlaves());
        	System.out.println(Jugador.getNumeroLlavesCambio());
        	
        }       
                      
        return false;  
    }
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void alignCameraUiStage(){		
		// La zona que está enfocando la cámara
		ArrayList<BaseActor> baseActors = new ArrayList<BaseActor>();
		for (Actor actor : uiStageNivel.getActors()) {
			if (actor instanceof BaseActor)
				baseActors.add((BaseActor)actor);
		}
		
		for (BaseActor baseActor : baseActors) {
			baseActor.setPosition(jugador.getX()+ mainCamera.viewportWidth/2,
					jugador.getY()+ mainCamera.viewportHeight/2);		
		}
		
		
		
		// Para centrar la cámara en el actor. Coordenada 0 porque se está filmando un plano
	
		
	}
	
	public void crearHudNivel() {
		isPaused = false;
		noLlaves = String.format( "x%d", Jugador.getNumeroLlaves());
		Label numeroLlaves = new Label(noLlaves, BaseGame.labelStyleLlaves );	
			
		BaseActor llaveSprite = new BaseActor(400,100);
		llaveSprite.loadTexture("assets/llave.png");
		llaveSprite.setScale(0.5f);		
		uiTable.clear();
		uiTable.padRight(5f);
		uiTable.padLeft(5f);		
		uiTable.add(menu).top().pad(6.2f,0f,0f,0f);
		uiTable.add().expandY().expandX().top();		
		uiTable.add(llaveSprite).top().padRight(-5f);
		uiTable.add(numeroLlaves).top().pad(6.2f,0f,0f,0f);
	}
	
	public void crearMenuNivel() {
		uiTable.clear();
	 	uiTable.add(botonContinuar).size(48, 20).padBottom(2f);
	 	uiTable.row();
	 	uiTable.add(botonSilenciar).size(48, 20).padBottom(2f);
	 	uiTable.row();
	 	uiTable.add(botonSalir).size(48, 20); 
	 	isPaused = true;
	}
	
	
}

	




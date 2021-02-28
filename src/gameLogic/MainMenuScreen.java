package gameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gameActors.BaseActor;
import gameActors.Jugador;

public class MainMenuScreen extends BaseScreen {
	private BaseActor fondo;
	private Music musicaMenu;
	boolean instruccionesActual;
	String nombreInstruccion;
	
	public MainMenuScreen() {
		super(null,"", 0, 0);		
	}

	@Override
	public void initialize() {
		instruccionesActual = false;		
		nombreInstruccion = "assets/instrucciones/instruccion0.png";
		
		fondo = new BaseActor(0,0,mainStage);
		fondo.loadTexture("assets/mainBackground.png");
		musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/Track_#1.wav"));
		
		Action parpadeo = Actions.forever(Actions.sequence(Actions.fadeOut(1f), Actions.fadeIn(1f)));
		
		Label titulo = new Label("La mazmorra de Pitagoras" , BaseGame.labelStyleLlaves);
		titulo.setFontScale(0.8f);	
		
		Label subtitulo = new Label("Hecho por:\nFabian Castro\nOscar Alvarado\nDaniel Murayari" , BaseGame.labelStyleLlaves);
		subtitulo.setFontScale(0.35f);
		
		TextButton iniciar = new TextButton( "Iniciar", BaseGame.textButtonStyleMain );
		iniciar.getLabel().setFontScale(0.30f);
		iniciar.addListener(new ClickListener(){
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
												
                super.enter(event, x , y, pointer, fromActor);
                iniciar.setColor(new Color(225/255f, 164/255f, 22/255f, 1));
                          
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	iniciar.setColor(new Color(1, 1, 1, 1));
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				musicaMenu.stop();
				MazmorraPitagoras.setActiveScreen(MazmorraPitagoras.getLevelScreen());
			}
		});
		
		TextButton instrucciones = new TextButton( "Instrucciones", BaseGame.textButtonStyleMain );
		instrucciones.getLabel().setFontScale(0.3f);
		instrucciones.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
												
                super.enter(event, x , y, pointer, fromActor);
                instrucciones.setColor(new Color(225/255f, 164/255f, 22/255f, 1));
                          
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	instrucciones.setColor(new Color(1, 1, 1, 1));
                }
            }
			
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				mainStage.clear();
				uiTable.clear();
				instruccionesActual = true;
				fondo = new BaseActor(0,0,mainStage);
				fondo.loadTexture("assets/instrucciones/instruccion0.png");		
				
			}
			
			});		
		
		
		uiTable.clear();
		uiTable.padTop(80);
		uiTable.add().expandX();
		uiTable.add(titulo).colspan(2).top().padBottom(5);
		uiTable.add().expandX();
		uiTable.row();		
		uiTable.add().expandX();
		uiTable.add(iniciar).size(48, 20).padBottom(5);		
		uiTable.add(instrucciones).size(48, 20).padBottom(5);
		uiTable.add().expandX();		
		uiTable.row(); 		
		uiTable.add().fillX();
		uiTable.add(subtitulo).colspan(2);
		uiTable.add().fillX();
		
		
		uiStage.addActor(uiTable);
		
		musicaMenu.setLooping(true);
		musicaMenu.setVolume(0.5f);
		musicaMenu.play();
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		
		return false;
	}

	

	@Override
	public void update(float dt) {
		
		
	}
	
	@Override
	public boolean keyDown(int keycode)
    {               
               
        
        if ( keycode == Keys.RIGHT) {        		       
        	int numeroInstruccion = Integer.parseInt(nombreInstruccion.replaceAll("[^0-9]", ""));
        	if(instruccionesActual && numeroInstruccion != 11 ) {
        		mainStage.clear();
        		fondo = new BaseActor(0,0,mainStage);
        		nombreInstruccion = String.format("assets/instrucciones/instruccion%d.png", numeroInstruccion+1);
        		fondo.loadTexture(nombreInstruccion);
        	}       	
        }
        
        if ( keycode == Keys.LEFT) {        		       
        	int numeroInstruccion = Integer.parseInt(nombreInstruccion.replaceAll("[^0-9]", ""));
        	if(instruccionesActual && numeroInstruccion != 0 ) {
        		mainStage.clear();
        		fondo = new BaseActor(0,0,mainStage);
        		nombreInstruccion = String.format("assets/instrucciones/instruccion%d.png", numeroInstruccion-1);
        		fondo.loadTexture(nombreInstruccion);
        	}       	
        }  
        
        if ( keycode == Keys.ESCAPE) {        	
            initialize();
        } 
        
        
        return false;  
    }
	
	
	
}

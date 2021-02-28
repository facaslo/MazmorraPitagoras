package gameLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;

import gameActors.Cofre;


public abstract class BaseScreen implements Screen, InputProcessor{
	protected Stage mainStage;
	protected Stage uiStage;
	protected Stage uiStageNivel;	
	protected OrthographicCamera gameCamera;
	protected OrthographicCamera uiCamera;
	protected Viewport gameViewport;
	protected Viewport uiPort;
	 
	protected float width;
	protected float height;
	protected String letraTablero;
	protected int numeroTablero;
	protected int tablerosTotales;
	protected Cofre cofreUsado;
	
	protected Table uiTable;
	protected boolean isPaused;
	
	
	
	public BaseScreen(Cofre cof, String letraTab, int noTablero, int tabTotales)	{
		
		letraTablero = letraTab;
		numeroTablero = noTablero; 
		tablerosTotales = tabTotales;	
		cofreUsado = cof;
		
		width = 208;
		height = 160;
		
		gameCamera = new OrthographicCamera();
		gameViewport = new FitViewport(width, height , gameCamera);	
		
		uiCamera = new OrthographicCamera();
		uiPort = new FitViewport(width, height , uiCamera);	
		
		
		mainStage = new Stage(gameViewport);
		uiStage = new Stage(gameViewport);	
		uiStageNivel = new Stage(uiPort);		
		
		uiTable = new Table();
		uiTable.setFillParent(true);
		
		
		initialize();		
	}	
	
	
	public abstract void initialize();
	public abstract void update(float dt);
	
	@Override
	public void render(float dt){
		if (isPaused) 
	        dt = 0;
		uiStage.act(dt);
		mainStage.act(dt);
		uiStageNivel.act(dt);
		update(dt);		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		mainStage.draw();
		uiStage.draw();	
		uiStageNivel.draw();
	}
	
		
	public void resize(int width, int height) { 
		gameViewport.update(width, height, false);
		uiPort.update(width, height, true);
	}
	
	public void pause() { }
	public void resume() { }
	public void dispose() { }
	// show corre cuando se carga una pantalla
	public void show() {
		InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
		im.addProcessor(this);
		im.addProcessor(uiStage);
		im.addProcessor(mainStage);
		im.addProcessor(uiStageNivel);
	}
	// show corre cuando se cambia la pantalla
	public void hide() { 
		InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
		im.removeProcessor(this);
		im.removeProcessor(uiStage);
		im.removeProcessor(mainStage);
		im.removeProcessor(uiStageNivel);
	}
	
	// Verificar si se ha dado un evento de click
	public boolean isTouchDownEvent(Event e){
	    	return (e instanceof InputEvent) && ((InputEvent)e).getType().equals(Type.touchDown);
	}
	
	// Estos métodos son requeridos por la interfaz InputProcessor
	public boolean keyDown(int keycode)
	{ return false; }
	public boolean keyUp(int keycode)
	{ return false; }
	public boolean keyTyped(char c)
	{ return false; }
	public boolean mouseMoved(int screenX, int screenY)
	{ return false; }
	public boolean scrolled(int amount)
	{ return false; }
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{ return false; }
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{ return false; }
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{ return false; }


	

}


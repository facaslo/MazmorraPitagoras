package gameLogic;
import com.badlogic.gdx.Gdx;
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


public abstract class BaseScreen implements Screen, InputProcessor{
	protected Stage mainStage;
	protected Stage uiStage;		
	public OrthographicCamera gameCamera;
	private Viewport gameViewport;
	private float width;
	private float height;
	public String nombreTma;
	
	
	public BaseScreen(String nombreMini)	{
		
		nombreTma = nombreMini;
		width = 200;
		height = 150;
		gameCamera = new OrthographicCamera();
		gameViewport = new FitViewport(width, height , gameCamera);	
		mainStage = new Stage(gameViewport);
		uiStage = new Stage(gameViewport);	
		initialize();		
	}	
	
	
	public abstract void initialize();
	public abstract void update(float dt);
	
	@Override
	public void render(float dt){
		uiStage.act(dt);
		mainStage.act(dt);
		update(dt);		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		mainStage.draw();
		uiStage.draw();		
	}
	
		
	public void resize(int width, int height) { 
		gameViewport.update(width, height);
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
	}
	// show corre cuando se cambia la pantalla
	public void hide() { 
		InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
		im.removeProcessor(this);
		im.removeProcessor(uiStage);
		im.removeProcessor(mainStage);
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


package gameActors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Input.Keys;

public class Carta extends BaseActor{
	boolean enMano;
	boolean hovered;	
	boolean clicked;
	float ancho;
	Camera gameCamera;
	
	public Carta(float x, float y, Stage s , boolean mano , String name) {
		super(x, y, s);
		enMano = mano;
		hovered = false;
		clicked = false;
		ancho = 16f;
		
		
		loadTexture(name);
		gameCamera = this.getStage().getCamera();		
		
		
		// Evento entrada y salida del mouse
		this.addListener(new ClickListener() {  			
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
								
                super.enter(event, x , y, pointer, fromActor);
                
                seleccionar(0.15f);             
            }	
			
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
				
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1 && !clicked) {
                	removerAnimacion();                   
                }
            }
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				click();
			}
			
        });
		
		
	}	
	
	
	public void act(float dt){
		super.act(dt);	
		
	}
	
	public void setEnMano(boolean valor) {
		enMano = valor;
	}	
	
	
	// Animación para cuando el mouse entre en la carta
	public void seleccionar(float numero) {		
		Action crecer = Actions.scaleBy(numero, numero, 0.3f);
		Action decrecer = Actions.scaleBy(-numero, -numero, 0.3f);
		Action parpadeo = Actions.forever(Actions.sequence(crecer, decrecer));
		this.addAction(parpadeo);			
			
	}
	
	// Animación para cuando el mouse salga de la carta
	public void removerAnimacion() {
		this.clearActions();
		this.setScale(1);
	}
	
	public void click() {		
		if (!clicked) {
			removerAnimacion();
			seleccionar(0.3f);
			clicked = true;		
		}
		else {
			removerAnimacion();
			this.setScale(1);
			clicked = false;
		}
		
		/* Action crecer = Actions.scaleBy(0.2f, 0.2f, 0.2f);
		this.addAction(crecer); */
	}

	
	

}

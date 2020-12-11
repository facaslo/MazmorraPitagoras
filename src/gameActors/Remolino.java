package gameActors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gameLogic.Minijuego;

public class Remolino extends BaseActor{

	public Remolino(float x, float y, Stage s) {
		super(x, y, s);
		
		loadTexture("assets/Boxes/Incognita/CeroRemolino.png");
		Action spin = Actions.rotateBy(15f, 1f);
		this.addAction(Actions.forever(spin));	
		this.setBoundaryRectangle();
		
		// Eventos del mouse con las cartas
		this.addListener(new ClickListener(Buttons.LEFT) {  			
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
								
                super.enter(event, x , y, pointer, fromActor);
                
                seleccionar(0.15f);             
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
				
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1 ) {
                	removerAnimacion();   
                	
                }
            }
			// Evento click
			@Override
			public void clicked(InputEvent event, float x, float y) {
				click();
							
			}
			
        });
		
	}
	
	public void click() {
		this.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.removeActor()));
		Minijuego.BaseActors.remove(this);
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
		try {
			this.removeAction(this.getActions().get(1));
			this.setScale(1);
		} catch(Exception e) {
			System.out.println("La unica acción es girar");
		}
	}

}

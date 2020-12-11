package gameActors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Cofre extends BaseActor {
	boolean cofreAbierto;
		
	public Cofre(float x, float y, Stage s) {
		super(x, y, s);		
		
		cofreAbierto = false;		
		this.loadTexture("assets/Boxes/cofreTesoro/cofreTesoro1.png");
		this.setBoundaryRectangle();
		
		this.addListener(new ClickListener(Buttons.LEFT){
			// Evento click
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (cofreAbierto) {
					abrirCofre();
				}
			}
				
		});
		
	}
	
	public void abrirCofre() {
		this.removeAnimation();
		String[] ficheros = {"assets/Boxes/cofreTesoro/cofreTesoro1.png",							 
							"assets/Boxes/cofreTesoro/cofreTesoro5.png"};
		this.loadAnimationFromFiles(ficheros, 2f, false);		
	}
	
	public void setCofreAbierto(boolean valor) {
		cofreAbierto = valor;
	}
	
}

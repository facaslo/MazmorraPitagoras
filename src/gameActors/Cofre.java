package gameActors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Cofre extends BaseActor {
	boolean cofreAbierto;
	String NombreCofre;
	boolean tablero;
		
	public Cofre(float x, float y, Stage s, boolean tab) {
		super(x, y, s);		
		tablero = tab;
		
		cofreAbierto = false;
		if(tab==true) {
			this.loadTexture("assets/Boxes/cofreTesoro/cofreTesoro1.png");
		}
		else {
			this.loadTexture("assets/Boxes/cofreTesoro/cofreTesoro2.png");
		}
	
			
		this.setBoundaryRectangle();
		
		/*
		this.addListener(new ClickListener(Buttons.LEFT){
			// Evento click
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (cofreAbierto) {
					abrirCofre();
				}
			}
				
		});
		*/
		
	}
	
	public void abrirCofreTablero() {
		this.removeAnimation();
		String[] ficheros = {"assets/Boxes/cofreTesoro/cofreTesoro1.png",							 
							"assets/Boxes/cofreTesoro/cofreTesoro5.png"};
		this.loadAnimationFromFiles(ficheros, 2f, false);		
	}
	
	public void abrirCofreMesa() {
		this.removeAnimation();
		String[] ficheros = {"assets/Boxes/cofreTesoro/cofreTesoro2.png",							 
							"assets/Boxes/cofreTesoro/cofreTesoro6.png"};
		this.loadAnimationFromFiles(ficheros, 2f, false);	
		cofreAbierto = true;
	}
	
	public void setCofreAbierto(boolean valor) {
		cofreAbierto = valor;
	}
	
	public boolean getCofreAbierto() {
		return cofreAbierto;
	}
	
	public void setNombre(String nom) {
		this.NombreCofre = nom;		
	}
	
	public String getNombre() {
		return NombreCofre;		
	}
	
}

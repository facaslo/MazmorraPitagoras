package gameActors;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;


public class BaseActor extends Actor{
	private TextureRegion textureRegion;
	private Polygon boundaryPolygon;
	private Animation<TextureRegion> animation;
	// Tiempo transcurrido en la animación
	private float elapsedTime;
	private boolean animationPaused;	
	private Vector2 velocityVec;
	private Vector2 accelerationVec;
	private float acceleration;
	private float maxSpeed;
	private float deceleration;
	private static Rectangle worldBounds;
	
	public BaseActor(float x, float y, Stage s)
	{
		// Constructor de la clase Actor 
		super();
		// La posicion dentro del escenario que va a ocupar el actor
		setPosition(x,y);
		// Añadir este actor al escenario
		s.addActor(this);
		
		// Vectores de velocidad y aceleracion
		velocityVec = new Vector2(0,0);		
		accelerationVec = new Vector2(0,0);
		// La aceleración del actor
		acceleration = 0;
		// La máxima velocidad que alcanza el actor
		maxSpeed = 1000;
		// La desaceleración del actor
		deceleration = 0;
	}
	
	
/* ----------------------------------------------------------------------------------------------------- 
	
	Métodos de animación
	
	----------------------------------------------------------------------------------------------------- */
	public void setAnimation(Animation<TextureRegion> anim) {
		animation = anim;
		// El frame del estado en el tiempo 0 
		TextureRegion tr = animation.getKeyFrame(0);
		float w = tr.getRegionWidth();
		float h = tr.getRegionHeight();
		setSize(w, h);
		// Se mueve el origen del sprite de la parte inferior izquierda al centro
		setOrigin (w/2, h/2);
		// Si no se ha definido un polígono de hitbox, añadir un rectángulo 
		if (boundaryPolygon == null)
			setBoundaryRectangle();
	}
	
	public void removeAnimation(){
		this.animation=null;
	}
	
	public void setAnimationPaused(boolean pause) {
		animationPaused = pause;
	}	
	
	// Método para crear animaciones de archivos diferentes
	
	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop){
		int fileCount = fileNames.length;
		// El array que va a contener las texturas de la animación
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		// For para cargar las imagenes una por una
		for (int n=0; n< fileCount; n++) {
			String fileName = fileNames[n];
			Texture texture = new Texture( Gdx.files.internal(fileName), true);
			/* El modo de filtro para la textura. Como interpola pixeles cercanos para producir 
			 * una sensación de suavidad
			 */
			texture.setFilter( TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear );
			textureArray.add(( new TextureRegion (texture)));			
		}
		
		// Se crea la animación
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		
		// Establece el modo de reproducción de la animación
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		
		if (animation == null)
			setAnimation(anim);
		
		return anim;
	}
	
	// Método para crear animaciones de sprite sheets
	
	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,	float frameDuration, 
			boolean loop){
		// Todas las imágenes están contenidas en un mismo archivo, por lo que solo hay que cargar una textura 
		Texture texture = new Texture(Gdx.files.internal(fileName), true);		
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		// El ancho y alto de cada sprite individual
		int frameWidth = texture.getWidth() / cols;
		int frameHeight = texture.getHeight() / rows;
		
		// Una matriz que va a contener los sprites cortados de la spritesheet original
		TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		// Guardamos los sprites individuales en un array
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				textureArray.add( temp[r][c] );
		
		// Se crea la animación
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration,	textureArray);
		
		// Establece el modo de reproducción de la animación
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		
		// Si el elemento aún no tiene animación, asígnele la animación
		if (animation == null)
			setAnimation(anim);
		
		return anim;
	}
	
	
	// En caso de que el actor no se mueva, la animación es estática
	public Animation<TextureRegion> loadTexture(String fileName){
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}
	
	// Para verificar si la animación ya terminó
	public boolean isAnimationFinished(){
		return animation.isAnimationFinished(elapsedTime);
	}
	
/* ----------------------------------------------------------------------------------------------------- 
	
	Métodos de velocidad
	
	----------------------------------------------------------------------------------------------------- */
	
	// Vector de velocidad
	public void setSpeed(float speed) {
		// Si el vector velocidad es nulo, asigne el vector (rapidez,0)
		if (velocityVec.len() == 0)
			velocityVec.set(speed,0);
		else 
			velocityVec.setLength(speed);
	}
	
	public float getSpeed(){
		return velocityVec.len();
	}
	
	public void setMotionAngle(float angle) {
		velocityVec.setAngleDeg(angle);
	}
	
	public float getMotionAngle() {
		return velocityVec.angleDeg();
	}
	
	public boolean isMoving() {
		return (getSpeed() > 0);
	}
	
	/* ----------------------------------------------------------------------------------------------------- 
	
	Métodos de aceleración
	
	----------------------------------------------------------------------------------------------------- */
	
	// Vector de aceleración
	public void setAcceleration(float acc){
		acceleration = acc;
	}
	// Para acelerar en un ángulo determinado
	public void accelerateAtAngle(float angle){
		accelerationVec.add( new Vector2(acceleration, 0).setAngleDeg(angle));		
	}
	
	// Para acelerar el objeto en la dirección donde está rotado
	public void accelerateForward()	{
		accelerateAtAngle( getRotation() );
	}		
	
	// Constantes de velocidad máxima y desaceleración
	public void setMaxSpeed(float ms){
		maxSpeed = ms;
	}
	
	public void setDeceleration(float dec){
		deceleration = dec;
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	Métodos de aplicación de las físicas
	
	----------------------------------------------------------------------------------------------------- */
	
	public void applyPhysics(float dt) {
		// Aplicar la aceleración al vector velocidad en sus dos componentes 
		velocityVec.add( accelerationVec.x * dt, accelerationVec.y *dt);
		
		// La rapidez (longitud del vector velocidad)
		float speed = getSpeed();
				
		// Desacelerar cuando la aceleración deja de actuar
		if (accelerationVec.len() == 0)
			speed -= deceleration * dt;
		
		// Mantener la rapidez dentro de los límites 0 y maxSpeed
		speed = MathUtils.clamp(speed, 0 , maxSpeed);
		
		// Actualizar las velocidades
		setSpeed(speed);
		
		// Actualizar la posición de los objetos
		moveBy( velocityVec.x * dt, velocityVec.y * dt);
		
		// Llevar al actor a un estado de reposo.
		accelerationVec.set(0,0);
	}
	
/* ----------------------------------------------------------------------------------------------------- 
	
	Métodos de detectar colisiones
	
	----------------------------------------------------------------------------------------------------- */
	// Para limitar los bordes del mundo
	public void setBoundaryRectangle()	{
		float w = getWidth();
		float h = getHeight();
		float[] vertices = {0,0, w,0, w,h, 0,h};
		boundaryPolygon = new Polygon(vertices);
	}
	
	
	
	// Para crear hitbox poligonales
	public void setBoundaryPolygon(int numSides)
	{
		// Obtener el ancho del actor
		float w = getWidth();
		// Obtener el alto del actor
		float h = getHeight();
		// Como hay dos coordenadas por vértice, se inicializa un array de tamaño 2*numSides
		float[] vertices = new float[2*numSides];
		for (int i = 0; i < numSides; i++){			
			float angle = i * 6.28f / numSides;
			// Coordenada en x
			vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
			// Coordenada en y 
			vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
		}
		boundaryPolygon = new Polygon(vertices);
	}
	
	public void setBoundaryPolygon(float[] vertices) {
		boundaryPolygon = new Polygon(vertices);
	}
	
	public Polygon getBoundaryPolygon()
	{
		boundaryPolygon.setPosition( getX(), getY() );
		boundaryPolygon.setOrigin( getOriginX(), getOriginY() );
		boundaryPolygon.setRotation ( getRotation() );
		boundaryPolygon.setScale( getScaleX(), getScaleY() );
					
		return boundaryPolygon;
	}
	
	// Para verificar si hay sobrelapamiento
	public boolean overlaps(BaseActor other){
		/* Se cargan los polígonos de los dos actores 
		 * */
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		/* Primero se verifica si los rectángulos exteriores se sobreponen.
		 * Si no lo hacen, no lo harán las regiones poligonales internas 
		 * */
			if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
				return false;
		// El método verifica si dos polígonos convexos se intersectan 
		return Intersector.overlapConvexPolygons( poly1, poly2 );
	}
	
		
	// Para evitar que dos objetos se sobrepongan
	public Vector2 preventOverlap(BaseActor other)	{
		/* Se cargan los polígonos de los dos actores 
		 * */
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		/* Si la hitbox rectangular del otro actor no intersecta la hitbox rectangular del otro, no 
		 * ha habido colision */
		if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
			return null;
		/* La posición mas corta a la que se tiene que mover el objeto para reposicionarlo
		 * y que no siga atravesando al otro objeto
		 */
		MinimumTranslationVector mtv = new MinimumTranslationVector();
		// Verifica si hay sobreposición de los polígonos de adentro
		boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
		if ( !polygonOverlap )
			return null;
		// Mover el vector 
		this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
		return mtv.normal;
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	Método para alinear cámara
	
	----------------------------------------------------------------------------------------------------- */
	
	public void alignCamera(){
		Camera cam = this.getStage().getCamera();
		// La zona que está enfocando la cámara
		Viewport v = this.getStage().getViewport();
		// Para centrar la cámara en el actor. Coordenada 0 porque se está filmando un plano
		cam.position.set( this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );
		// Limitar el área de movimiento de la cámara
		cam.position.x = MathUtils.clamp(cam.position.x,
		cam.viewportWidth/2, worldBounds.width - cam.viewportWidth/2);
		cam.position.y = MathUtils.clamp(cam.position.y,
		cam.viewportHeight/2, worldBounds.height - cam.viewportHeight/2);		
		cam.update();
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	Otros métodos
	
	----------------------------------------------------------------------------------------------------- */
	
	
	/* Método para fijar la posición de un actor en pantalla
	 * 
	 * */
	public void centerAtPosition(float x, float y)	{
		setPosition( x - getWidth()/2 , y - getHeight()/2 );
	}
	/* Método para fijar la posición respecto a un actor
	 * 
	 * */
	public void centerAtActor(Actor other){
		centerAtPosition( other.getX() + other.getWidth()/2 , other.getY() + other.getHeight()/2 );
	}
	
	/* Método para cambiar la opacidad del actor
	 * 
	 * */
	public void setOpacity(float opacity){
		this.getColor().a = opacity;
	}
	
	/* 
	 * Método para guardar clases del mismo tipo dentro de un arrayList
	 * */
	public static ArrayList<BaseActor> getList(Stage stage, String className){
		ArrayList<BaseActor> list = new ArrayList<BaseActor>();		
		Class actorClass = null;
		
		try {
			actorClass = Class.forName("gameActors."+className);
		} catch (Exception error) {
			error.printStackTrace();
		}
		
		for (Actor a : stage.getActors()) {
			if (actorClass.isInstance(a))
				list.add((BaseActor)a);
		}
		
		return list;		
	}
	
	/* Método para contar el número de Actores de un mismo tipo
	 * */
	public static int count(Stage stage, String className){
		return getList(stage, className).size();
	}
	
	/* Método para definir los límites del mundo
	 * */
	public static void setWorldBounds(float width, float height){
		worldBounds = new Rectangle( 0,0, width, height );
	}
	
	/* Método para definir los límites del mundo
	 * */
	public static void setWorldBounds(BaseActor ba)
	{
		setWorldBounds( ba.getWidth(), ba.getHeight() );
	}
	
	/* Para que el actor no se salga de los límites del mapa
	 */
	public void boundToWorld(){
		// Verificar borde izquierdo
		if (getX() < 0)
			setX(0);
		// Verificar borde derecho
		if (getX() + getWidth() > worldBounds.width)
			setX(worldBounds.width - getWidth());
		// Verificar borde inferior
		if (getY() < 0)
			setY(0);
		// Verificar borde superior
		if (getY() + getHeight() > worldBounds.height)
			setY(worldBounds.height - getHeight());
	}
	
		
	
/* ----------------------------------------------------------------------------------------------------- 
	
	* Métodos act y draw
	
	----------------------------------------------------------------------------------------------------- */
	
	@Override
	public void act(float dt) {
		// dt equivale a 1/60 segundos. El método act se actualiza cada dt
		super.act(dt);
		
		if(!animationPaused)
			elapsedTime += dt;
		
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch,  parentAlpha);
		
		// Se emplea para aplicar efecto de tinte
		Color c = getColor();		
		batch.setColor(c.r, c.g, c.b, c.a);
		
		if (animation != null && isVisible()) {
			batch.draw( animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}
}

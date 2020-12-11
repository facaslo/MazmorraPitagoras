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
	// Tiempo transcurrido en la animaci�n
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
		// A�adir este actor al escenario
		s.addActor(this);
		
		// Vectores de velocidad y aceleracion
		velocityVec = new Vector2(0,0);		
		accelerationVec = new Vector2(0,0);
		// La aceleraci�n del actor
		acceleration = 0;
		// La m�xima velocidad que alcanza el actor
		maxSpeed = 1000;
		// La desaceleraci�n del actor
		deceleration = 0;
	}
	
	
/* ----------------------------------------------------------------------------------------------------- 
	
	M�todos de animaci�n
	
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
		// Si no se ha definido un pol�gono de hitbox, a�adir un rect�ngulo 
		if (boundaryPolygon == null)
			setBoundaryRectangle();
	}
	
	public void removeAnimation(){
		this.animation=null;
	}
	
	public void setAnimationPaused(boolean pause) {
		animationPaused = pause;
	}	
	
	// M�todo para crear animaciones de archivos diferentes
	
	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop){
		int fileCount = fileNames.length;
		// El array que va a contener las texturas de la animaci�n
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		// For para cargar las imagenes una por una
		for (int n=0; n< fileCount; n++) {
			String fileName = fileNames[n];
			Texture texture = new Texture( Gdx.files.internal(fileName), true);
			/* El modo de filtro para la textura. Como interpola pixeles cercanos para producir 
			 * una sensaci�n de suavidad
			 */
			texture.setFilter( TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear );
			textureArray.add(( new TextureRegion (texture)));			
		}
		
		// Se crea la animaci�n
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		
		// Establece el modo de reproducci�n de la animaci�n
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		
		if (animation == null)
			setAnimation(anim);
		
		return anim;
	}
	
	// M�todo para crear animaciones de sprite sheets
	
	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,	float frameDuration, 
			boolean loop){
		// Todas las im�genes est�n contenidas en un mismo archivo, por lo que solo hay que cargar una textura 
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
		
		// Se crea la animaci�n
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration,	textureArray);
		
		// Establece el modo de reproducci�n de la animaci�n
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		
		// Si el elemento a�n no tiene animaci�n, as�gnele la animaci�n
		if (animation == null)
			setAnimation(anim);
		
		return anim;
	}
	
	
	// En caso de que el actor no se mueva, la animaci�n es est�tica
	public Animation<TextureRegion> loadTexture(String fileName){
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}
	
	// Para verificar si la animaci�n ya termin�
	public boolean isAnimationFinished(){
		return animation.isAnimationFinished(elapsedTime);
	}
	
/* ----------------------------------------------------------------------------------------------------- 
	
	M�todos de velocidad
	
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
	
	M�todos de aceleraci�n
	
	----------------------------------------------------------------------------------------------------- */
	
	// Vector de aceleraci�n
	public void setAcceleration(float acc){
		acceleration = acc;
	}
	// Para acelerar en un �ngulo determinado
	public void accelerateAtAngle(float angle){
		accelerationVec.add( new Vector2(acceleration, 0).setAngleDeg(angle));		
	}
	
	// Para acelerar el objeto en la direcci�n donde est� rotado
	public void accelerateForward()	{
		accelerateAtAngle( getRotation() );
	}		
	
	// Constantes de velocidad m�xima y desaceleraci�n
	public void setMaxSpeed(float ms){
		maxSpeed = ms;
	}
	
	public void setDeceleration(float dec){
		deceleration = dec;
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	M�todos de aplicaci�n de las f�sicas
	
	----------------------------------------------------------------------------------------------------- */
	
	public void applyPhysics(float dt) {
		// Aplicar la aceleraci�n al vector velocidad en sus dos componentes 
		velocityVec.add( accelerationVec.x * dt, accelerationVec.y *dt);
		
		// La rapidez (longitud del vector velocidad)
		float speed = getSpeed();
				
		// Desacelerar cuando la aceleraci�n deja de actuar
		if (accelerationVec.len() == 0)
			speed -= deceleration * dt;
		
		// Mantener la rapidez dentro de los l�mites 0 y maxSpeed
		speed = MathUtils.clamp(speed, 0 , maxSpeed);
		
		// Actualizar las velocidades
		setSpeed(speed);
		
		// Actualizar la posici�n de los objetos
		moveBy( velocityVec.x * dt, velocityVec.y * dt);
		
		// Llevar al actor a un estado de reposo.
		accelerationVec.set(0,0);
	}
	
/* ----------------------------------------------------------------------------------------------------- 
	
	M�todos de detectar colisiones
	
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
		// Como hay dos coordenadas por v�rtice, se inicializa un array de tama�o 2*numSides
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
		/* Se cargan los pol�gonos de los dos actores 
		 * */
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		/* Primero se verifica si los rect�ngulos exteriores se sobreponen.
		 * Si no lo hacen, no lo har�n las regiones poligonales internas 
		 * */
			if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
				return false;
		// El m�todo verifica si dos pol�gonos convexos se intersectan 
		return Intersector.overlapConvexPolygons( poly1, poly2 );
	}
	
		
	// Para evitar que dos objetos se sobrepongan
	public Vector2 preventOverlap(BaseActor other)	{
		/* Se cargan los pol�gonos de los dos actores 
		 * */
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		/* Si la hitbox rectangular del otro actor no intersecta la hitbox rectangular del otro, no 
		 * ha habido colision */
		if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
			return null;
		/* La posici�n mas corta a la que se tiene que mover el objeto para reposicionarlo
		 * y que no siga atravesando al otro objeto
		 */
		MinimumTranslationVector mtv = new MinimumTranslationVector();
		// Verifica si hay sobreposici�n de los pol�gonos de adentro
		boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
		if ( !polygonOverlap )
			return null;
		// Mover el vector 
		this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
		return mtv.normal;
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	M�todo para alinear c�mara
	
	----------------------------------------------------------------------------------------------------- */
	
	public void alignCamera(){
		Camera cam = this.getStage().getCamera();
		// La zona que est� enfocando la c�mara
		Viewport v = this.getStage().getViewport();
		// Para centrar la c�mara en el actor. Coordenada 0 porque se est� filmando un plano
		cam.position.set( this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );
		// Limitar el �rea de movimiento de la c�mara
		cam.position.x = MathUtils.clamp(cam.position.x,
		cam.viewportWidth/2, worldBounds.width - cam.viewportWidth/2);
		cam.position.y = MathUtils.clamp(cam.position.y,
		cam.viewportHeight/2, worldBounds.height - cam.viewportHeight/2);		
		cam.update();
	}
/* ----------------------------------------------------------------------------------------------------- 
	
	Otros m�todos
	
	----------------------------------------------------------------------------------------------------- */
	
	
	/* M�todo para fijar la posici�n de un actor en pantalla
	 * 
	 * */
	public void centerAtPosition(float x, float y)	{
		setPosition( x - getWidth()/2 , y - getHeight()/2 );
	}
	/* M�todo para fijar la posici�n respecto a un actor
	 * 
	 * */
	public void centerAtActor(Actor other){
		centerAtPosition( other.getX() + other.getWidth()/2 , other.getY() + other.getHeight()/2 );
	}
	
	/* M�todo para cambiar la opacidad del actor
	 * 
	 * */
	public void setOpacity(float opacity){
		this.getColor().a = opacity;
	}
	
	/* 
	 * M�todo para guardar clases del mismo tipo dentro de un arrayList
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
	
	/* M�todo para contar el n�mero de Actores de un mismo tipo
	 * */
	public static int count(Stage stage, String className){
		return getList(stage, className).size();
	}
	
	/* M�todo para definir los l�mites del mundo
	 * */
	public static void setWorldBounds(float width, float height){
		worldBounds = new Rectangle( 0,0, width, height );
	}
	
	/* M�todo para definir los l�mites del mundo
	 * */
	public static void setWorldBounds(BaseActor ba)
	{
		setWorldBounds( ba.getWidth(), ba.getHeight() );
	}
	
	/* Para que el actor no se salga de los l�mites del mapa
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
	
	* M�todos act y draw
	
	----------------------------------------------------------------------------------------------------- */
	
	@Override
	public void act(float dt) {
		// dt equivale a 1/60 segundos. El m�todo act se actualiza cada dt
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

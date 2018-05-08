/*
 * GameCanvas.java
 *
 * To properly follow the model-view-controller separation, we should not have
 * any specific drawing code in GameMode. All of that code goes here.  As
 * with GameEngine, this is a class that you are going to want to copy for
 * your own projects.
 *
 * An important part of this canvas design is that it is loosely coupled with
 * the model classes. All of the drawing methods are abstracted enough that
 * it does not require knowledge of the interfaces of the model classes.  This
 * important, as the model classes are likely to change often.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Primary view class for the game, abstracting the basic graphics calls.
 * 
 * This version of GameCanvas only supports (rectangular) Sprite drawing.
 * support for polygonal textures and drawing primitives will be present
 * in future labs.
 *
 * TODO: Re-organize GameCanvas to better support:
 * 	- First, world drawing with "3d stuff", done with PerspectiveCamera, Decals, etc
 * 	- Then HUD drawing with OrthographicCamera, SpriteBatch
 */
public class GameCanvas {
	/** While we are not drawing polygons (yet), this spritebatch is more reliable */
	private PolygonSpriteBatch spriteBatch;
	
	/** Track whether or not we are active (for error checking) */
	private boolean active;
	
	/** The current color blending mode */
	private BlendState blend;
	
	/** Value to cache window width (if we are currently full screen) */
	int width;
	/** Value to cache window height (if we are currently full screen) */
	int height;

	// CACHE OBJECTS
	/** Affine cache for current sprite to draw */
	private Affine2 local;
	/** Cache object to unify everything under a master draw method */
	private TextureRegion holder;


	// World drawing
	PerspectiveCamera camera;
	DecalBatch batch;
	Array<Decal> roadDecals;

	Viewport viewport;

	private float CAM_HEIGHT = 4.32f;
	private Vector3 CAM_START_POS = new Vector3(0f, -10f, 4.32f);
	private float NORMAL_CAM_FOV = 67;

	// Damage-indicator stuff
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private float damageIndicatorAlpha = 1.0f;


	/**
	 * Creates a new GameCanvas determined by the application configuration.
	 * 
	 * Width, height, and fullscreen are taken from the LWGJApplicationConfig
	 * object used to start the application.  This constructor initializes all
	 * of the necessary graphics objects.
	 */
	public GameCanvas() {
		active = false;
		spriteBatch = new PolygonSpriteBatch();
		
		// Set the projection matrix (for proper scaling)
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, getWidth(), getHeight());
		
		// Initialize the cache objects
		holder = new TextureRegion();
		local  = new Affine2();

		// Initialize game world drawing stuff
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport = new FitViewport(1600, 900, camera);
		viewport.apply();
		camera.position.z = CAM_HEIGHT;
		camera.near = 0.0001f;
		camera.lookAt(0, 0, 0);
		//camera.rotate(150, 0, 1, 0);
		camera.update();

		batch = new DecalBatch(new CameraGroupStrategy(camera));

		Gdx.gl20.glDepthMask(false);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	}

    /**
     * Eliminate any resources that should be garbage collected manually.
     */
    public void dispose() {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot dispose while drawing active", new IllegalStateException());
			return;
		}
		spriteBatch.dispose();
    	spriteBatch = null;
    	local  = null;
    	holder = null;
    }

	/**
	 * Returns the width of this canvas
	 *
	 * This currently gets its value from Gdx.graphics.getWidth()
	 *
	 * @return the width of this canvas
	 */
	public int getWidth() {
		return Gdx.graphics.getWidth();
	}
	
	/**
	 * Changes the width of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param width the canvas width
	 */
	public void setWidth(int width) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		this.width = width;
		if (!isFullscreen()) {
			Gdx.graphics.setWindowedMode(width, getHeight());
		}
		resize();
	}
	
	/**
	 * Returns the height of this canvas
	 *
	 * This currently gets its value from Gdx.graphics.getHeight()
	 *
	 * @return the height of this canvas
	 */
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}
	
	/**
	 * Changes the height of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param height the canvas height
	 */
	public void setHeight(int height) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		this.height = height;
		if (!isFullscreen()) {
			Gdx.graphics.setWindowedMode(getWidth(), height);	
		}
		resize();
	}
	
	/**
	 * Returns the dimensions of this canvas
	 *
	 * @return the dimensions of this canvas
	 */
	public Vector2 getSize() {
		return new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}
	
	/**
	 * Changes the width and height of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param width the canvas width
	 * @param height the canvas height
	 */
	public void setSize(int width, int height) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		this.width = width;
		this.height = height;
		if (!isFullscreen()) {
			Gdx.graphics.setWindowedMode(width, height);
		}
		resize();
	}
	
	/**
	 * Returns whether this canvas is currently fullscreen.
	 *
	 * @return whether this canvas is currently fullscreen.
	 */	 
	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen(); 
	}
	
	/**
	 * Sets whether or not this canvas should change to fullscreen.
	 *
	 * If desktop is true, it will use the current desktop resolution for
	 * fullscreen, and not the width and height set in the configuration
	 * object at the start of the application. This parameter has no effect
	 * if fullscreen is false.
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param value True if want full screen mode.
	 */	 
	public void setFullscreen(boolean value) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		if (value) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(width, height);
		}
	}

	/**
	 * Resets the SpriteBatch camera when this canvas is resized.
	 *
	 * If you do not call this when the window is resized, you will get
	 * weird scaling issues.
	 */
	 public void resize() {
		// Resizing screws up the spriteBatch projection matrix
		 viewport.update(getWidth(), getHeight());
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Returns the current color blending state for this canvas.
	 *
	 * Textures draw to this canvas will be composited according
	 * to the rules of this blend state.
	 *
	 * @return the current color blending state for this canvas
	 */
	public BlendState getBlendState() {
		return blend;
	}
	
	/**
	 * Sets the color blending state for this canvas.
	 *
	 * Any texture draw subsequent to this call will use the rules of this blend 
	 * state to composite with other textures.  Unlike the other setters, if it is 
	 * perfectly safe to use this setter while  drawing is active (e.g. in-between 
	 * a begin-end pair).  
	 *
	 * @param state the color blending rule
	 */
	public void setBlendState(BlendState state) {
		if (state == blend) {
			return;
		}
		switch (state) {
		case NO_PREMULT:
			spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case ALPHA_BLEND:
			spriteBatch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case ADDITIVE:
			spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
			break;
		case OPAQUE:
			spriteBatch.setBlendFunction(GL20.GL_ONE,GL20.GL_ZERO);
			break;
		}
		blend = state;
	}

	/**
	 * Start and active drawing sequence with the identity transform.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 */
    public void beginHUDDrawing() {
    	spriteBatch.begin();
    	active = true;
    	
    }

	/**
	 * Ends a drawing sequence, flushing textures to the graphics card.
	 */
    public void endHUDDrawing() {
    	spriteBatch.end();
    	active = false;
    }
    
	/**
     * Draw the seamless background image.
     *
     * The background image is drawn (with NO SCALING) at position x, y.  Width-wise, 
     * the image is seamlessly scrolled; when we reach the image we draw a second copy.  
     *
     * To work properly, the image should be wide and high enough to fill the screen.
     * 
     * @param image  Texture to draw as an overlay
	 */
    public void drawBackground(Texture image) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		draw(image, TextureOrigin.MIDDLE, 0.5f, 0.5f, 1f, true, 0, Color.WHITE);
    }



	/**
	 * Reset camera to original coordinates.
	 */
	public void resetCam() {
        camera.position.set(CAM_START_POS);
	}

	/**
	 * Clear the screen. Call this before starting any drawing.
	 */
	public void clearScreen() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);  // Homage to the XNA years
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param image The texture to draw
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 */
	public void draw(Texture image, float x, float y) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(image, x,  y);
	}

	public void draw(Texture image, float x, float y, float width, float height) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(image, x,  y, width, height);
	}

	public void draw(Texture image, float x, float y, float width, float height, Color tint) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		// Unlike Lab 1, we can shortcut without a master drawing method
		spriteBatch.setColor(tint);
		spriteBatch.draw(image, x,  y, width, height);
	}

	/**
	 * Draws the tinted texture with the given transformations
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin
	 * @param y 	The y-coordinate of the texture origin
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */	
	public void draw(Texture image, Color tint, float ox, float oy, 
					float x, float y, float angle, float sx, float sy) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Call the master drawing method (we have to for transforms)
		holder.setRegion(image);
		draw(holder,tint,ox,oy,x,y,angle,sx,sy);
	}

	public enum TextureOrigin {
		MIDDLE,
		MIDDLE_LEFT,
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
	}

	/** Same as function below this, but for TextureRegion
	 *
	 * @param image Texture of the image
	 * @param o Where the origin is located for the Texture, see enum.
	 * @param x Scale where x-coord is on screen, relative to canvas width (0.5 = middle, 1 = right)
	 * @param y scale where y-coord is on screen, relative to canvas height (0.5 = middle, 1 = top)
	 * @param scale Scale of texture based on either width/height
	 * @param widthScale True if scale is based on canvas width, false if based on canvas height
	 * @param angle Angle this texture is drawn in
	 * @param c Color tint of the image
	 */
	public void draw(TextureRegion image, TextureOrigin o, float x, float y, float scale, boolean widthScale, float angle, Color c) {
		Vector2 oxy = new Vector2();
		switch(o) {
			case MIDDLE:
				oxy.x = image.getRegionWidth()*0.5f;
				oxy.y = image.getRegionHeight()*0.5f;
				break;
			case TOP_LEFT:
				oxy.y = image.getRegionHeight();
				break;
			case TOP_RIGHT:
				oxy.x = image.getRegionWidth();
				oxy.y = image.getRegionHeight();
				break;
			case BOTTOM_LEFT:
				break;
			case BOTTOM_RIGHT:
				oxy.x = image.getRegionWidth();
				break;
			default:
				break;
		}
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getRegionWidth();
		else s = scale*getHeight()/image.getRegionHeight();
//		System.out.println("x: " + oxy.x + " y: " + oxy.y + " xpos: " + x*getWidth() + " ypos: " + y*getHeight() + " angle: " + angle + " scale: " + s);
		draw(image, c, oxy.x, oxy.y, x*getWidth(), y*getHeight(), angle, s, s);
	}
	/** Default color is white. */

	/**	Nice new draw method that is resize-friendly yay. :D
	 *
	 * @param image Texture of the image
	 * @param o Where the origin is located for the Texture, see enum.
	 * @param x Scale where x-coord is on screen, relative to canvas width (0.5 = middle, 1 = right)
	 * @param y scale where y-coord is on screen, relative to canvas height (0.5 = middle, 1 = top)
	 * @param scale Scale of texture based on either width/height
	 * @param widthScale True if scale is based on canvas width, false if based on canvas height
	 * @param angle Angle this texture is drawn in
	 * @param c Color tint of the image
	 */
	public void draw(Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale, float angle, Color c) {
		Vector2 oxy = new Vector2();
		switch(o) {
			case MIDDLE:
				oxy.x = image.getWidth()*0.5f;
				oxy.y = image.getHeight()*0.5f;
				break;
			case MIDDLE_LEFT:
				oxy.x = 0;
				oxy.y = image.getHeight()*0.5f;
				break;
			case TOP_LEFT:
				oxy.y = image.getHeight();
				break;
			case TOP_RIGHT:
				oxy.x = image.getWidth();
				oxy.y = image.getHeight();
				break;
			case BOTTOM_LEFT:
				break;
			case BOTTOM_RIGHT:
				oxy.x = image.getWidth();
				break;
			default:
				break;
		}
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getWidth();
		else s = scale*getHeight()/image.getHeight();
//		System.out.println("x: " + oxy.x + " y: " + oxy.y + " xpos: " + x*getWidth() + " ypos: " + y*getHeight() + " angle: " + angle + " scale: " + s);
		draw(image, c, oxy.x, oxy.y, x*getWidth(), y*getHeight(), angle, s, s);
	}
	/** Default color is white. */
	public void draw(Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale, float angle) {
		draw(image, o, x, y, scale, widthScale, angle, Color.WHITE);
	}
	/** Default angle is 0 and color is white. */
	public void draw(Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale) {
		draw(image, o, x, y, scale, widthScale, 0, Color.WHITE);
	}
	/** Same as above but for TextureRegion. */
	public void draw(TextureRegion image, TextureOrigin o, float x, float y, float scale, boolean widthScale) {
		draw(image, o, x, y, scale, widthScale, 0, Color.WHITE);
	}
	/** Default angle is 0. */
	public void draw(Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale, Color c) {
		draw(image, o, x, y, scale, widthScale, 0, c);
	}
	/** With shake amount */
	public void drawShake(Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale, float angle, Color c, float shakeAmnt) {
		Vector2 oxy = new Vector2();
		switch(o) {
			case MIDDLE:
				oxy.x = image.getWidth()*0.5f;
				oxy.y = image.getHeight()*0.5f;
				break;
			case MIDDLE_LEFT:
				oxy.x = 0;
				oxy.y = image.getHeight()*0.5f;
				break;
			case TOP_LEFT:
				oxy.y = image.getHeight();
				break;
			case TOP_RIGHT:
				oxy.x = image.getWidth();
				oxy.y = image.getHeight();
				break;
			case BOTTOM_LEFT:
				break;
			case BOTTOM_RIGHT:
				oxy.x = image.getWidth();
				break;
			default:
				break;
		}
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getWidth();
		else s = scale*getHeight()/image.getHeight();
		draw(image, c, oxy.x, oxy.y, x*getWidth(), y*getHeight()+shakeAmnt, angle, s, s);
	}

	public boolean inArea(Vector2 p, TextureRegion image, TextureOrigin o, float x, float y, float scale, boolean widthScale) {
		int xb = 0; //x bottom bound
		int xt = 0; //x top bound
		int yb = 0;
		int yt = 0;
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getRegionWidth();
		else s = scale*getHeight()/image.getRegionHeight();
		switch(o) {
			case MIDDLE:
				xb=(int)(x*getWidth()-s*0.5f*image.getRegionWidth());
				xt=(int)(x*getWidth()+s*0.5f*image.getRegionWidth());
				yb=(int)(y*getHeight()-s*0.5f*image.getRegionHeight());
				yt=(int)(y*getHeight()+s*0.5f*image.getRegionHeight());
				break;
			case TOP_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getRegionWidth());
				yb=(int)(y*getHeight()-s*image.getRegionHeight());
				yt=getHeight();
				break;
			case TOP_RIGHT:
				xb=(int)(x*getWidth()-s*image.getRegionWidth());
				xt=getWidth();
				yb=(int)(y*getHeight()-s*image.getRegionHeight());
				yt=getHeight();
				break;
			case BOTTOM_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getRegionWidth());
				yb=0;
				yt=(int)(y*getHeight()+s*image.getRegionHeight());
				break;
			case BOTTOM_RIGHT:
				xb=(int)(x*getWidth()-s*image.getRegionWidth());
				xt=getWidth();
				yb=0;
				yt=(int)(y*getHeight()+s*image.getRegionHeight());
				break;
			default:
				break;
		}
		return p.x > xb && p.x < xt && (getHeight()-p.y) > yb && (getHeight()-p.y) < yt;
	}

	public boolean inArea(Vector2 p, Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale) {
		int xb = 0; //x bottom bound
		int xt = 0; //x top bound
		int yb = 0;
		int yt = 0;
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getWidth();
		else s = scale*getHeight()/image.getHeight();
		switch(o) {
			case MIDDLE:
				xb=(int)(x*getWidth()-s*0.5f*image.getWidth());
				xt=(int)(x*getWidth()+s*0.5f*image.getWidth());
				yb=(int)(y*getHeight()-s*0.5f*image.getHeight());
				yt=(int)(y*getHeight()+s*0.5f*image.getHeight());
				break;
			case MIDDLE_LEFT:
				xb=0;
				xt=(int)(x*getWidth() + s*image.getWidth());
				yb=(int)(y*getHeight()-s*0.5f*image.getHeight());
				yt=(int)(y*getHeight()+s*0.5f*image.getHeight());
				break;
			case TOP_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getWidth());
				yb=(int)(y*getHeight()-s*image.getHeight());
				yt=getHeight();
				break;
			case TOP_RIGHT:
				xb=(int)(x*getWidth()-s*image.getWidth());
				xt=getWidth();
				yb=(int)(y*getHeight()-s*image.getHeight());
				yt=getHeight();
				break;
			case BOTTOM_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getWidth());
				yb=0;
				yt=(int)(y*getHeight()+s*image.getHeight());
				break;
			case BOTTOM_RIGHT:
				xb=(int)(x*getWidth()-s*image.getWidth());
				xt=getWidth();
				yb=0;
				yt=(int)(y*getHeight()+s*image.getHeight());
				break;
			default:
				break;
		}
		return p.x > xb && p.x < xt && (getHeight()-p.y) > yb && (getHeight()-p.y) < yt;
	}

	public boolean inArea(Vector2 p, Texture image, TextureOrigin o, float x, float y, float scale, boolean widthScale, float cb) {
		// BASICALLY COPIED FROM ABOVE METHOD LOL I LOVE CODE DUPLICATION
		int xb = 0; //x bottom bound
		int xt = 0; //x top bound
		int yb = 0;
		int yt = 0;
		float s = 0;
		if(widthScale) s = scale*getWidth()/image.getWidth();
		else s = scale*getHeight()/image.getHeight();
		switch(o) {
			case MIDDLE:
				xb=(int)(x*getWidth()-s*0.5f*image.getWidth());
				xt=(int)(x*getWidth()+s*0.5f*image.getWidth());
				yb=(int)(y*getHeight()-s*0.5f*image.getHeight());
				yt=(int)(y*getHeight()+s*0.5f*image.getHeight());
				break;
			case MIDDLE_LEFT:
				xb=0;
				xt=(int)(x*getWidth() + s*image.getWidth());
				yb=(int)(y*getHeight()-s*0.5f*image.getHeight());
				yt=(int)(y*getHeight()+s*0.5f*image.getHeight());
				break;
			case TOP_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getWidth());
				yb=(int)(y*getHeight()-s*image.getHeight());
				yt=getHeight();
				break;
			case TOP_RIGHT:
				xb=(int)(x*getWidth()-s*image.getWidth());
				xt=getWidth();
				yb=(int)(y*getHeight()-s*image.getHeight());
				yt=getHeight();
				break;
			case BOTTOM_LEFT:
				xb=0;
				xt=(int)(x*getWidth()+s*image.getWidth());
				yb=0;
				yt=(int)(y*getHeight()+s*image.getHeight());
				break;
			case BOTTOM_RIGHT:
				xb=(int)(x*getWidth()-s*image.getWidth());
				xt=getWidth();
				yb=0;
				yt=(int)(y*getHeight()+s*image.getHeight());
				break;
			default:
				break;
		}
		return p.x > xb-(cb*width) && p.x < xt+(cb*width) && (getHeight()-p.y) > yb-(cb*height) && (getHeight()-p.y) < yt+(cb*height);
	}
	
	/**
	 * Draws the tinted texture region (filmstrip) at the given position.
	 *
	 * A texture region is a single texture file that can hold one or more textures.
	 * It is used for filmstrip animation.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 */
	public void draw(TextureRegion region, float x, float y) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(region, x,  y);
	}
	
	public void draw(TextureRegion region, float x, float y, float width, float height) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(region, x,  y, width, height);
	}
	
	/**
	 * Draws the tinted texture region (filmstrip) with the given transformations
	 *
	 * THIS IS THE MASTER DRAW METHOD (Modify for exercise 4)
	 *
	 * A texture region is a single texture file that can hold one or more textures.
	 * It is used for filmstrip animation.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin
	 * @param y 	The y-coordinate of the texture origin
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */	
	public void draw(TextureRegion region, Color tint, float ox, float oy, 
					 float x, float y, float angle, float sx, float sy) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		computeTransform(ox,oy,x,y,angle,sx,sy);
		spriteBatch.setColor(tint);
		spriteBatch.draw(region,region.getRegionWidth(),region.getRegionHeight(),local);
	}
	
	/**
	 * Compute the affine transform (and store it in local) for this image.
	 * 
	 * This helper is meant to simplify all of the math in the above draw method
	 * so that you do not need to worry about it when working on Exercise 4.
	 *
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin
	 * @param y 	The y-coordinate of the texture origin
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */
	private void computeTransform(float ox, float oy, float x, float y, float angle, float sx, float sy) {
		local.setToTranslation(x,y);
		local.rotate(angle);
		local.scale(sx,sy);
		local.translate(-ox,-oy);
	}

    /**
     * Draws text on the screen.
     *
     * @param text The string to draw
     * @param font The font to use
     * @param x The x-coordinate of the lower-left corner
     * @param y The y-coordinate of the lower-left corner
     */
    public void drawText(String text, BitmapFont font, float x, float y) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		GlyphLayout layout = new GlyphLayout(font,text);
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, layout, x, y);
    }

	/**
	 * Draws text on the screen with a specific color.
	 */
	public void drawText(String text, BitmapFont font, float x, float y, Color color) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		GlyphLayout layout = new GlyphLayout(font,text);
		font.setColor(color);
		font.draw(spriteBatch, layout, x, y);
	}

	/**
	 * Draws text centered on the screen.
     *
     * @param text The string to draw
     * @param font The font to use
     * @param offset The y-value offset from the center of the screen.
     */
    public void drawTextCentered(String text, BitmapFont font, float offset) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		GlyphLayout layout = new GlyphLayout(font,text);
		float x = (getWidth()  - layout.width) / 2.0f;
		float y = (getHeight() + layout.height) / 2.0f;
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, layout, x, y+offset);
    }

    public void drawTextCenterOrigin(String text, BitmapFont font, float x, float y) {
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		GlyphLayout layout = new GlyphLayout(font,text);
		float xpos = x*getWidth() - (layout.width) / 2.0f;
		float ypos = y*getHeight() + (layout.height) / 2.0f;
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, layout, xpos, ypos);
	}

	/**
	 * Enumeration of supported BlendStates.
	 *
	 * For reasons of convenience, we do not allow user-defined blend functions.
	 * 99% of the time, we find that the following blend modes are sufficient
	 * (particularly with 2D games).
	 */
	public enum BlendState {
		/** Alpha blending on, assuming the colors have pre-multipled alpha (DEFAULT) */
		ALPHA_BLEND,
		/** Alpha blending on, assuming the colors have no pre-multipled alpha */
		NO_PREMULT,
		/** Color values are added together, causing a white-out effect */
		ADDITIVE,
		/** Color values are draw on top of one another with no transparency support */
		OPAQUE
	}

	/**
	 * Set camera x, y values.
	 * @param newXY
	 */
	public void setCameraXY(Vector2 newXY) {
		camera.position.set(newXY.x, newXY.y, camera.position.z);
		camera.lookAt(0, 20, 0);

	}

	/**
	 * Set the camera FOV to the normal FOV amount multiplied by
	 * @param speedRatio
	 *
	 * speedRatio should come from road and is currentSpeed / NORMAL_SPEED.
	 * This should be >= 1.0. The idea is that, when the ratio is higher,
	 * the FOV should be higher.
	 */
	public void setCameraFOV(float speedRatio) {
		camera.fieldOfView = NORMAL_CAM_FOV * speedRatio;
	}

	/**
     * Draw road with infinite scrolling effect, with PerspectiveCamera.
	 *
	 * The calls are buffered. Must call drawWorld() to draw to screen.
	 *
	 * PerspectiveCamera used for 3D perspective.
	 */
	public void drawRoad(float xOff, Decal d) {
		//System.out.println(camera.position);

		camera.update();
		batch.add(d);
	}

	/**
	 * Draw a RoadObject
	 *  z - near 4.32 (hoverDistance)
	 *
	 * @param t the texture to draw
	 * @param x x coord
	 * @param y y coord
	 * @param width scaling of the object sprite width
	 * @param height scaling of the object sprite height
	 * @param xRotationAngle x rotation angle (in degrees)
	 * @param yRotationAngle y rotation angle (in degrees)
	 */
	public void drawRoadObject(Texture t, float x, float y, float z, float width, float height, float xRotationAngle, float yRotationAngle) {
		camera.update();
		//System.out.println(t);
		Decal objectDecal = Decal.newDecal(width, height, new TextureRegion(t), true);
		objectDecal.setPosition(x, y, z);
		objectDecal.rotateX(xRotationAngle);
		objectDecal.rotateY(yRotationAngle);
		batch.add(objectDecal);
	}

	public void drawRoadObject(TextureRegion t, float x, float y, float z, float width, float height, float xRotationAngle, float yRotationAngle) {
		camera.update();
		//System.out.println(t);
		Decal objectDecal = Decal.newDecal(width, height, t, true);
		objectDecal.setPosition(x, y, z);
		objectDecal.rotateX(xRotationAngle);
		objectDecal.rotateY(yRotationAngle);
		batch.add(objectDecal);
	}

	public void drawWorld() {
		Gdx.gl20.glDepthMask(false);
		batch.flush();
	}

	public void drawDamageIndicator(float alpha) {
	    Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    shapeRenderer.setColor(new Color(1, 0, 0, alpha));
	    shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    shapeRenderer.end();
	}

	public void draw(Texture tex, float x, float y, float ox, float oy, float width, float height,
					 	float sx, float sy, float ang, int srcX, int srcY, int srcWidth, int srcHeight, boolean fx, boolean fy){
		if (!active) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(tex,x,y,ox,oy,width,height,sx,sy,ang,srcX,srcY,srcWidth,srcHeight,fx,fy);
	}

	/** Draws a fade-to-black rectangle when transitioning between modes.
	 * (Can also be used to draw fade-ins).
	 * Alpha 0 = transparent, 1 = opaque
	 *
	 * @param alpha The opacity of the rectangle
	 */
	public void drawFade(float alpha) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(new Color(0,0,0, alpha));
		shapeRenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		shapeRenderer.end();
	}

	/**
	 * Draw a line from the start to end coordinates.
	 */
	public void drawLine(Vector2 start, Vector2 end) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 1, 1); // Red line
		shapeRenderer.rectLine(start.x, start.y, end.x, end.y, 2f);
		shapeRenderer.end();
	}
}
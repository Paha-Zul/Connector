package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	public static ShapeRenderer renderer;
	public static OrthographicCamera camera;
	public static OrthographicCamera UICamera;
	public static Viewport viewport;
	public static Stage stage;
	public static BitmapFont defaultFont, defaultLargeFont, defaultHugeFont;
	public static ExecutorService executor;
	public static EasyAssetManager easyAssetManager;
	public static ActionResolver resolver;
	public static AdInterface adInterface;

	public static TextureRegion defaultButtonUp, defaultButtonDown;

	public Game(ActionResolver actionResolver, AdInterface ads){
		resolver = actionResolver;
		adInterface = ads;
	}
	
	@Override
	public void create () {

		camera = new OrthographicCamera(480, 800);
		viewport = new StretchViewport(480, 800, camera);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		stage = new Stage(viewport);
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(camera.combined);

		executor = Executors.newFixedThreadPool(3);
        easyAssetManager = new EasyAssetManager();

        this.loadAllPng(Gdx.files.internal("art/"));
        this.loadAssets();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("copperplatessibold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 18;
		defaultFont = generator.generateFont(parameter); // font size 12 pixels

		generator = new FreeTypeFontGenerator(Gdx.files.internal("copperplatessibold.ttf"));
		parameter.size = 28;
		defaultLargeFont = generator.generateFont(parameter); // font size 12 pixels

		generator = new FreeTypeFontGenerator(Gdx.files.internal("copperplatessibold.ttf"));
		parameter.size = 60;
		defaultHugeFont = generator.generateFont(parameter); // font size 12 pixels

		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		defaultButtonUp = new TextureRegion(easyAssetManager.get("defaultButton_normal", Texture.class));
		defaultButtonDown = new TextureRegion(easyAssetManager.get("defaultButton_down", Texture.class));

		Gdx.input.setInputProcessor(stage);
		this.setScreen(new LogoScreen(this));
	}

	public void loadAllPng(FileHandle handle){
		TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
		param.genMipMaps = true;
		param.magFilter = Texture.TextureFilter.Linear;
		param.minFilter = Texture.TextureFilter.Linear;

		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".png")){
				easyAssetManager.load(h.path(), Texture.class, param);
			}else if(h.isDirectory()){
				this.loadAllPng(Gdx.files.internal(h.path()+"/"));
			}
		}
	}

	public void loadAssets(){
        boolean done = false;
		while(!done){
            done = easyAssetManager.update();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

		Game.stage.act();
		Game.stage.draw();
	}
}

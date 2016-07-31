package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quickbite.connector2.screens.LogoScreen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	public static ShapeRenderer renderer;
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static Stage stage;
	public static BitmapFont defaultHugeFont;
	public static ExecutorService executor;
	public static EasyAssetManager easyAssetManager;
	public static ActionResolver resolver;
	public static AdInterface adInterface;


	public static TextureAtlas shapeAtlas, UIAtlas;

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

        this.loadAllPng(Gdx.files.internal("art/load/"));
        this.loadSheets(Gdx.files.internal("art/sheets/"));
        this.loadParticles(Gdx.files.internal("particles/"));
        this.loadSounds(Gdx.files.internal("sounds/"));
        this.loadMusic(Gdx.files.internal("music/"));
        this.loadFonts(Gdx.files.internal("fonts/"));

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				System.out.println("Shutting down threads");
				executor.shutdownNow();
				System.out.println("shut down threads");
				try {
					executor.awaitTermination(5, TimeUnit.SECONDS);
					System.out.println("Awaited");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Gdx.input.setInputProcessor(stage);
		this.setScreen(new LogoScreen(this));
	}

	public void loadAllPng(FileHandle handle){
		TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
		param.genMipMaps = true;
		param.magFilter = Texture.TextureFilter.Linear;
		param.minFilter = Texture.TextureFilter.Linear;

		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".png") || h.name().endsWith(".9.png")){
				easyAssetManager.load(h.path(), Texture.class, param);
			}else if(h.isDirectory()){
				this.loadAllPng(Gdx.files.internal(h.path()+"/"));
			}
		}
	}

	public void loadSheets(FileHandle handle){
		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".pack"))
				easyAssetManager.load(h.path(), TextureAtlas.class);
		}
	}

	public void loadParticles(FileHandle handle){
        for(FileHandle h : handle.list()){
            if(h.name().endsWith(".p"))
                easyAssetManager.load(h.path(), ParticleEffect.class);
        }
	}

	public void loadSounds(FileHandle handle){
		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".ogg"))
				easyAssetManager.load(h.path(), Sound.class);
		}
	}

	public void loadMusic(FileHandle handle){
		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".ogg") || h.name().endsWith(".wav"))
				easyAssetManager.load(h.path(), Music.class);
		}
	}

	public void loadFonts(FileHandle handle){
		for(FileHandle h : handle.list()){
			if(h.name().endsWith(".fnt"))
				easyAssetManager.load(h.path(), BitmapFont.class);
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

package com.quickbite.connector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	public static ShapeRenderer renderer;
	public static OrthographicCamera camera;
	public static OrthographicCamera UICamera;
	public static Stage stage;
	public static BitmapFont defaultFont;
	public static ExecutorService executor;
	
	@Override
	public void create () {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		stage = new Stage();
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(camera.combined);

		executor = Executors.newFixedThreadPool(3);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("copperplatessibold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 18;
		defaultFont = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		Gdx.input.setInputProcessor(stage);
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

		Game.stage.draw();
	}
}

package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Cell;

public class PlayScreen implements Screen {

    public static Vector2 positionCamera, sizeViewport;
    public static boolean oneSelected = false;

    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Stage stage;
    //carrega texturas
    private Texture texture = new Texture("cell.png");

    private Cell cell1, cell2, cell3;

    public PlayScreen(MainGame game){

        this.game = game;

        //create camera & create viewport
        camera = new OrthographicCamera(MainGame.V_Width,MainGame.V_Height);
        viewport = new FitViewport(MainGame.V_Width,MainGame.V_Height,camera);
        camera.position.set(MainGame.V_Width/2 , MainGame.V_Height/2, 0);

        positionCamera = new Vector2(camera.position.x, camera.position.y);
        sizeViewport = new Vector2(viewport.getScreenX(), viewport.getScreenY());

        //box 2d
        world = new World(new Vector2(0,0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

        //cria unidades
        cell1 = new Cell(world, 600, 700);
        cell2 = new Cell(world, 300, 600);
        cell3 = new Cell(world, 800, 1000);

        //adiciona unidades no stage
        stage = new Stage();
        stage.addActor(cell1);
        stage.addActor(cell2);
        stage.addActor(cell3);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0.1f,0.4f,0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update players
        stage.act();

        //draw textures
        Draw(cell1);
        Draw(cell2);
        Draw(cell3);

        //other updates
        update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        texture.dispose();

    }

    @Override
    public void dispose() {
        texture.dispose();
        world.dispose();
        stage.dispose();
        box2DDebugRenderer.dispose();
    }

    private void handleInput(){
        if (Gdx.input.isTouched()){

            camera.position.x -= Gdx.input.getDeltaX()*MainGame.V_Width/ sizeViewport.x;
            camera.position.y += Gdx.input.getDeltaY()*MainGame.V_Height/ sizeViewport.y;

            if(camera.position.x < 0){
                camera.position.x = 0;
            }
            if(camera.position.x > MainGame.V_Width){
                camera.position.x = MainGame.V_Width;
            }
            if(camera.position.y < 0){
                camera.position.y = 0;
            }
            if(camera.position.y > MainGame.V_Height) {
                camera.position.y = MainGame.V_Height;
            }
            positionCamera.set(camera.position.x, camera.position.y);
        }
    }

    private void update(){
        //physics time execute
        world.step(1/60f, 6,2);

        //update camera
        handleInput();
        camera.update();
        positionCamera.set(camera.position.x, camera.position.y);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    private void Draw(Cell cell){
        Matrix4 matrix4 = game.batch.getProjectionMatrix().cpy().scale(MainGame.PPM, MainGame.PPM, 0);
        box2DDebugRenderer.render(world, matrix4);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        if(cell.selected){game.batch.setColor(0.9f,0.1f,0.1f,1);}
        if(!cell.selected){game.batch.setColor(0.8f,0.8f,0.9f,1);}

        game.batch.draw(texture,
               cell.body.getPosition().x*MainGame.PPM - cell.radius,
               cell.body.getPosition().y*MainGame.PPM - cell.radius,
               cell.radius*2,cell.radius*2);
        game.batch.end();
    }
}

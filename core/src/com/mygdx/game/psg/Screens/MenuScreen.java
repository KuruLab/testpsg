package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.psg.Engine.Detector;
import com.mygdx.game.psg.Engine.Gesture;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Button;

import java.io.IOException;

import static com.badlogic.gdx.math.MathUtils.random;

public class MenuScreen implements Screen {

    public static Stage stage;
    public static World world;
    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer box2DDebugRenderer;

    private static Vector2 sizeViewport;
    public static  Vector2 positionCamera;
    private float camX, camY;
    public static float touchRadius, zoom, zoomInit;

    private static int initCount;
    public static Button button;

    public static boolean oneSelected;

    private Vector2 position;

    private Texture textureCell = new Texture("cell.png");
    private Texture textureSelect = new Texture("select.png");
    private Texture textureAttack = new Texture("attack.png");
    private Texture textureCircle = new Texture("circle.png");

    @Override
    public void show() {

        //add units on stage
        stage = new Stage();
        setMenu();

    }

    @Override
    public void render(float delta) {

/*
        if(initCount == 0) {
            if (Math.abs(zoom - zoomFinal) > 0.005f) {
                zoom = zoom + (zoomFinal - zoom) * delta*2.5f;
            } else {
                zoom = zoomFinal;
            }
        }else{
            zoom = zoom + (zoomFinal - zoom) * delta*0.5f;
            initCount--;
        }
*/

        //physics time execute
        world.step(1/60f, 6,2);

        //clear board
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw body
        Matrix4 matrix4 = game.batch.getProjectionMatrix().cpy().scale(MainGame.PPM, MainGame.PPM, 0);
        //box2DDebugRenderer.render(world, matrix4);
        game.batch.setProjectionMatrix(camera.combined);

        //update players
        stage.act();
        updateCamera(delta);
        Draw();
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

    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
        box2DDebugRenderer.dispose();

    }

    public MenuScreen(MainGame game)throws IOException {

        //set zoom
        zoom = (MainGame.V_Height/MainGame.W_Height)*2;

        this.game = game;

        //create camera & create viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(MainGame.W_Width, MainGame.W_Height,camera);
        camera.position.set(0,0,0);
        camX = camera.position.x;
        camY = camera.position.y;
        Gesture gesture = new Gesture();
        Gdx.input.setInputProcessor(gesture);

        //update references
        positionCamera = new Vector2(camera.position.x, camera.position.y);
        sizeViewport = new Vector2();

        //box 2d
        world = new World(new Vector2(0,0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new Detector());

    }

    private void setMenu(){
        stage.addActor(new Button(MainGame.M_Width/1.5f, -(MainGame.M_Height/2.5f - MainGame.M_Height/6f), Button.TypeButton.CONTINUE, 0));
        stage.addActor(new Button(MainGame.M_Width/1.5f, -MainGame.M_Height/1.3f, Button.TypeButton.NEW, 6));

        stage.addActor(new Button(-MainGame.M_Width/1.5f, -(MainGame.M_Height/2.5f - MainGame.M_Height/6f), Button.TypeButton.START, 0));
        stage.addActor(new Button(-MainGame.M_Width/1.5f, -MainGame.M_Height/1.3f, Button.TypeButton.CONTINUE, 6));

        position = new Vector2();
        position.set(0,MainGame.M_Height/2.5f);
        position.setAngle(270);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 0));
        position.setAngle(position.angle() + 360/6);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 1));
        position.setAngle(position.angle() + 360/6);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 2));
        position.setAngle(position.angle() + 360/6);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 3));
        position.setAngle(position.angle() + 360/6);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 4));
        position.setAngle(position.angle() + 360/6);
        stage.addActor(new Button(position.x, position.y + MainGame.M_Height/6, Button.TypeButton.COLOR, 5));

        stage.addActor(new Button(0, -MainGame.M_Height/1.3f, Button.TypeButton.MENU, 6));

        for(int i = 0; i < 30; i++){
            stage.addActor(
                    new Button(
                            random(-MainGame.M_Width, MainGame.M_Width),
                            random(-MainGame.M_Height, MainGame.M_Height),
                            Button.TypeButton.EFFECT, random(0,5)));
            stage.addActor(
                    new Button(
                            random(-MainGame.M_Width, MainGame.M_Width),
                            random(-MainGame.M_Height, MainGame.M_Height),
                            Button.TypeButton.EFFECT, 6));

            stage.addActor(
                    new Button(
                            random(-MainGame.M_Width, MainGame.M_Width),
                            random(-MainGame.M_Height, MainGame.M_Height),
                            Button.TypeButton.EFFECT, 6));


        }

    }

    private void updateCamera(float delta){
        //update camera

        /*
        if (Gdx.input.isTouched()) {
            camX -= Gdx.input.getDeltaX() * zoom;
            camY += Gdx.input.getDeltaY() * zoom;
        }

        if(Math.abs(camera.position.x - camX) > 0.0005f){
            camera.position.x = camera.position.x  + (camX - camera.position.x)*delta*10f;
        }else{
            camera.position.x = camX;
        }

        if(Math.abs(camera.position.y - camY) > 0.0005f){
            camera.position.y = camera.position.y  + (camY - camera.position.y)*delta*10f;

        }else{
            camera.position.y = camY;
        }


        if (camera.position.x < MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.M_Width) {
            camera.position.x = MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.M_Width;
            camX = camera.position.x;
        }

        if (camera.position.x > -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.M_Width) {
            camera.position.x = -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.M_Width;
            camX = camera.position.x;
        }

        if (camera.position.y < MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.M_Height) {
            camera.position.y = MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.M_Height;
            camY = camera.position.y;
        }

        if (camera.position.y > -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.M_Height) {
            camera.position.y = -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.M_Height;
            camY = camera.position.y;
        }
      */

        if(MainGame.controler == MainGame.Controler.MENU){
            camera.position.x = 1080 + MainGame.W_Width * zoom * 0.5f;
        }

        if(MainGame.controler == MainGame.Controler.COLOR){
            camera.position.x = 0;
        }

        if(MainGame.controler == MainGame.Controler.START || MainGame.controler == MainGame.Controler.RESTART){
            camera.position.x = -1080 - MainGame.W_Width * zoom * 0.5f;
        }


        camX = camera.position.x;
        camY = camera.position.y;

        //positionCamera.set(camera.position.x, camera.position.y);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
        positionCamera.set(camera.position.x, camera.position.y);
        camera.zoom = zoom;
        camera.update();
    }

    private void Draw(){
        game.batch.begin();
        //draw light
        for(Actor actor : stage.getActors()) {
            DrawLightButton((Button)actor);
        }

        if(oneSelected) {
        //draw select
        DrawInfo();
        }

        //button
        for(Actor actor : stage.getActors()) {
            DrawButton((Button)actor);
        }

        //draw energy
        for(Actor actor : stage.getActors()) {
            DrawEnergy((Button)actor);
        }

        game.batch.end();
    }

    private void DrawLightButton(Button button){

        game.batch.setColor(
                button.getColor().r,
                button.getColor().g,
                button.getColor().b,
                button.getColor().a*0.5f);

        //draw energy
        game.batch.draw(textureAttack,
                button.body.getPosition().x * MainGame.PPM  - (button.radiusEnergy * 3f),
                button.body.getPosition().y * MainGame.PPM -  (button.radiusEnergy * 3f),
                (button.radiusEnergy * 3f) * 2f,
                (button.radiusEnergy * 3f) * 2f);
    }

    private  void DrawInfo(){
        //draw select
        game.batch.setColor(
                button.getColor().r,
                button.getColor().g,
                button.getColor().b,
                button.getColor().a*0.5f);

        game.batch.draw(
                textureAttack,

                button.body.getPosition().x* MainGame.PPM - (button.baseRadius + touchRadius*zoom) * 2f,
                button.body.getPosition().y* MainGame.PPM - (button.baseRadius + touchRadius*zoom) * 2f,
                (button.baseRadius + touchRadius*zoom) * 4f,
                (button.baseRadius + touchRadius*zoom) * 4f);


        game.batch.setColor(
                button.getColor().r,
                button.getColor().g,
                button.getColor().b,
                button.getColor().a*0.9f);

        //draw select
        game.batch.draw(
                textureSelect,
                button.body.getPosition().x* MainGame.PPM - (button.baseRadius - touchRadius*zoom)*1.15f,
                button.body.getPosition().y* MainGame.PPM - (button.baseRadius - touchRadius*zoom)*1.15f,
                (button.baseRadius + touchRadius*zoom) * 2.3f,
                (button.baseRadius + touchRadius*zoom) * 2.3f);
    }

    private void DrawButton(Button button){

        game.batch.setColor(button.getColor());

        //draw button
        game.batch.draw(
                textureCell,
                button.body.getPosition().x * MainGame.PPM - button.baseRadius,
                button.body.getPosition().y * MainGame.PPM - button.baseRadius,
                button.baseRadius * 2,
                button.baseRadius * 2);

    }

    private void DrawEnergy(Button button){

        game.batch.setColor(button.getColor().mul(Color.WHITE));

        game.batch.draw(textureCircle,
                button.body.getPosition().x * MainGame.PPM - button.radiusEnergy,
                button.body.getPosition().y * MainGame.PPM - button.radiusEnergy,
                button.radiusEnergy * 2,
                button.radiusEnergy * 2);

    }

}

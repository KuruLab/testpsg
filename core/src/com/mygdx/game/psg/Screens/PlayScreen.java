package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.psg.Engine.CollisionDetector;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Attack;
import com.mygdx.game.psg.Sprites.Cell;

public class PlayScreen implements Screen{
    private Stage stage;

    //auxiliary variables
    public static Vector2 positionCamera, sizeViewport;
    public static boolean oneSelected, oneTarget;
    public static  int selectedeID, targedID;


    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    //load textures
    private Texture textureCell = new Texture("cell.png");
    private Texture textureAttack = new Texture("attack.png");

    private int ID = 0, numberOfCells = 10;
    private Actor cell1, cell2, cell3;

    public PlayScreen(MainGame game){

        this.game = game;

        //create camera & create viewport
        camera = new OrthographicCamera(MainGame.V_Width,MainGame.V_Height);
        viewport = new FitViewport(MainGame.V_Width,MainGame.V_Height,camera);
        camera.position.set(MainGame.V_Width/2 , MainGame.V_Height/2, 0);

        //update references
        positionCamera = new Vector2(camera.position.x, camera.position.y);
        sizeViewport = new Vector2(viewport.getScreenX(), viewport.getScreenY());

        //box 2d
        world = new World(new Vector2(0,0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        CollisionDetector collisionDetector = new CollisionDetector();
        world.setContactListener(collisionDetector);
    }

    @Override
    public void show() {

        //create units
        //cell1 = new Cell(world, 600, 700, 1);
        //cell2 = new Cell(world, 300, 600, 2);
        //cell3 = new Cell(world, 800, 1000, 3);

        //add units on stage
        stage = new Stage();

        for(int i = 0; i <= numberOfCells; i++){
            stage.addActor(new Cell(world, MathUtils.random(-MainGame.V_Width/2,MainGame.V_Width*3/2),
                    MathUtils.random(-MainGame.V_Height/2,MainGame.V_Width*3/2), setID()));
        }
        //stage.addActor(cell1);
        //stage.addActor(cell2);
        //stage.addActor(cell3);

        for(int i = 0; i <= numberOfCells; i++){
            stage.addActor(new Attack(world, 0, MathUtils.random(-MainGame.V_Width/2,MainGame.V_Width*3/2),
                    MathUtils.random(-MainGame.V_Height/2,MainGame.V_Width*3/2), setID()));
        }

        if(oneSelected && oneTarget && true){

            stage.addActor(new Attack(world, targedID ,
                    stage.getActors().get(targedID).getX(),stage.getActors().get(targedID).getX(), setID()));
            oneSelected = false;
            oneTarget = false;
        }

        for(Actor actor : stage.getActors())
        {

        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0.1f,0.4f,0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Matrix4 matrix4 = game.batch.getProjectionMatrix().cpy().scale(MainGame.PPM, MainGame.PPM, 0);
        //box2DDebugRenderer.render(world, matrix4);
        game.batch.setProjectionMatrix(camera.combined);

        //update players
        stage.act();

        //draw textures
        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Cell.class) {
                DrawCell((Cell) actor);
            }
        }

        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Attack.class) {
                DrawAttack((Attack) actor);
            }
        }

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

        textureCell.dispose();
        textureAttack.dispose();
    }

    @Override
    public void dispose() {
        textureCell.dispose();
        textureAttack.dispose();
        world.dispose();
        stage.dispose();
        box2DDebugRenderer.dispose();
    }

    private void controlCamera(){
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
        controlCamera();
        camera.update();
        positionCamera.set(camera.position.x, camera.position.y);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    private void DrawCell(Cell cell){


        game.batch.begin();
        if(cell.selected){game.batch.setColor(0.9f,0.1f,0.1f,1);}
        if(!cell.selected){game.batch.setColor(0.8f,0.8f,0.9f,1);}

        game.batch.draw(textureCell,
               cell.body.getPosition().x*MainGame.PPM - cell.baseRadius,
               cell.body.getPosition().y*MainGame.PPM - cell.baseRadius,
               cell.baseRadius *2,cell.baseRadius *2);
        game.batch.end();
    }

    private void DrawAttack(Attack attack){

        game.batch.begin();

        game.batch.setColor(0.9f,0.9f,0.1f,1);

        game.batch.draw(textureAttack,
                attack.body.getPosition().x*MainGame.PPM - attack.baseRadius,
                attack.body.getPosition().y*MainGame.PPM - attack.baseRadius,
                attack.baseRadius *2,attack.baseRadius *2);
        game.batch.end();
    }

    private int setID(){

        ID = ID+1;

        return ID-1;
    }
}

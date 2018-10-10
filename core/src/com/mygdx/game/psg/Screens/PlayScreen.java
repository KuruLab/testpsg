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
import com.mygdx.game.psg.Engine.CollisionDetector;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Attack;
import com.mygdx.game.psg.Sprites.Cell;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.*;
import static com.mygdx.game.psg.Sprites.Cell.Clear;
import static com.mygdx.game.psg.Sprites.Cell.DamageCell;

public class PlayScreen implements Screen{
    private Stage stage;

    public enum Team {
        NEUTRAL,
        PLAYER,
        BOT1,
        BOT2,
        BOT3,
        BOT4;
    }

    //other variables
    public static Vector2 positionCamera, sizeViewport, positionSelected, positionTarget, positionA, positionB, attackDirection;
    public static Team selectedTeam;
    public static Color selectedColor;
    public static boolean oneSelected, oneTarget;
    public static int selectedID, targetID;
    public static float selectedRadius;

    public static ArrayList<Vector2> contact = new ArrayList<Vector2>();

    //game elements
    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    public static World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    //load textures
    private Texture textureCell = new Texture("cell.png");
    private Texture textureAttack = new Texture("attack.png");

    //initial values
    private int ID = 0, numberOfNeltral = 20, teamCells = 2;

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


        //world.setAutoClearForces(true);
    }

    @Override
    public void show() {
         attackDirection = new Vector2();

        //add units on stage
        stage = new Stage();

        firstGame();



        //action Bot
        for(Actor Bot : stage.getActors()){


        }
    }

    @Override
    public void render(float delta) {
        //physics time execute
        world.step(1/60f, 6,2);

        Gdx.gl.glClearColor(0.8f,0.8f,0.8f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw body
        Matrix4 matrix4 = game.batch.getProjectionMatrix().cpy().scale(MainGame.PPM, MainGame.PPM, 0);
        box2DDebugRenderer.render(world, matrix4);
        game.batch.setProjectionMatrix(camera.combined);


        //update players
        stage.act();

        //Collisions
        updateCollision();



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
                if(((Attack) actor).remove) {
                    world.destroyBody(((Attack) actor).body);
                    actor.remove();
                }else{
                    DrawAttack((Attack) actor);
                }
            }
        }



        createAttack();

        //other updates
        updateCamera(delta);

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

    private void controlCamera(float delta){

        if (Gdx.input.isTouched()) {

            camera.position.x -= Gdx.input.getDeltaX() * MainGame.V_Width / sizeViewport.x;
            camera.position.y += Gdx.input.getDeltaY() * MainGame.V_Height / sizeViewport.y;

        }else{

            if(oneSelected) {

                camera.position.x = camera.position.x + (positionSelected.x * MainGame.PPM * sizeViewport.x / MainGame.V_Width + sizeViewport.x / 2
                        - positionCamera.x * sizeViewport.x / MainGame.V_Width - (sizeViewport.x / 2))*delta;

                camera.position.y = camera.position.y + (positionSelected.y * MainGame.PPM * sizeViewport.y / MainGame.V_Height + sizeViewport.y / 2
                        - positionCamera.y * sizeViewport.y / MainGame.V_Height - (sizeViewport.y / 2))*delta;
                }
        }

        if(camera.position.x < -MainGame.V_Width/2){
            camera.position.x = -MainGame.V_Width/2;
        }
        if(camera.position.x > MainGame.V_Width*3/2){
            camera.position.x = MainGame.V_Width*3/2;
        }
        if(camera.position.y < 0){
            camera.position.y = 0;
        }
        if(camera.position.y > MainGame.V_Height) {
            camera.position.y = MainGame.V_Height;
        }
        positionCamera.set(camera.position.x, camera.position.y);
    }

    private void updateCamera(float delta){
        //update camera
        controlCamera(delta);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
        positionCamera.set(camera.position.x, camera.position.y);
        camera.update();
    }

    private void DrawCell(Cell cell){
        game.batch.begin();
        if(cell.selected){

            game.batch.setColor(
                    cell.getColor().r*0.7f,
                    cell.getColor().g*0.7f,
                    cell.getColor().b*0.7f,
                    cell.getColor().a*1f);
        }else{
            game.batch.setColor(cell.getColor());}

            //draw cell
            game.batch.draw(
                    textureCell,
                   cell.body.getPosition().x*MainGame.PPM - cell.baseRadius,
                   cell.body.getPosition().y*MainGame.PPM - cell.baseRadius,
                   cell.baseRadius *2,
                   cell.baseRadius *2);

        game.batch.setColor(
                cell.getColor().r*0.8f,
                cell.getColor().g*0.8f,
                cell.getColor().b*0.8f,
                cell.getColor().a*1f);

            //draw energy
            game.batch.draw(textureCell,
                    cell.body.getPosition().x*MainGame.PPM - cell.radiusEnergy,
                    cell.body.getPosition().y*MainGame.PPM - cell.radiusEnergy,
                    cell.radiusEnergy*2,
                    cell.radiusEnergy*2);

            game.batch.end();
    }

    private void DrawAttack(Attack attack){

        game.batch.begin();
        game.batch.setColor(attack.getColor());

        game.batch.draw(textureCell,
                attack.body.getPosition().x*MainGame.PPM - attack.baseRadius,
                attack.body.getPosition().y*MainGame.PPM - attack.baseRadius,
                attack.baseRadius *2,attack.baseRadius *2);
        game.batch.end();
    }

    private int setID(){

        ID = ID+1;

        return ID-1;
    }

    private void createAttack(){
        if(oneSelected && oneTarget){

            stage.addActor(new Attack((stage.getActors().get(selectedID)).getX()*MainGame.PPM,
                    (stage.getActors().get(selectedID)).getY()*MainGame.PPM, setID(), selectedRadius, selectedColor, selectedTeam));

            Clear((Cell)stage.getActors().get(selectedID));
            Clear((Cell)stage.getActors().get(targetID));

            oneSelected = false;
            oneTarget = false;
        }
    }

    public void updateCollision(){

        while(contact.size() > 0){

            positionA = contact.get(0);
            contact.remove(0);
            contact.trimToSize();

            positionB = contact.get(0);
            contact.remove(0);
            contact.trimToSize();

            for(int a = 0; a < stage.getActors().size; a++) {

                if (stage.getActors().items[a].getX() == positionA.x && stage.getActors().items[a].getY() == positionA.y) {

                    for (int b = 0; b < stage.getActors().size; b++) {

                        if (stage.getActors().items[b].getX() == positionB.x && stage.getActors().items[b].getY() == positionB.y) {

                            DamageCell(stage.getActors().get(a),stage.getActors().get(b));

                        }
                    }
                }
            }
        }
    }



    private void firstGame(){

        //Neutral
        for(int i = 0; i < numberOfNeltral; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width,MainGame.V_Width),
                    random(-MainGame.V_Height/2,MainGame.V_Height*3/2), setID(), Team.NEUTRAL));
        }

        //Player
        for(int i = 0; i < teamCells; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width,MainGame.V_Width),
                    random(-MainGame.V_Height/2,MainGame.V_Height*3/2), setID(), Team.PLAYER));
        }

        //Bot1
        for(int i = 0; i < teamCells; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width,MainGame.V_Width),
                    random(-MainGame.V_Height/2,MainGame.V_Height*3/2), setID(), Team.BOT1));
        }

        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height / 2, MainGame.V_Height * 3 / 2), setID(), Team.BOT2));
        }

        //Bot3
        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height / 2, MainGame.V_Height * 3 / 2), setID(), Team.BOT3));
        }

        //Bot4
        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height / 2, MainGame.V_Height * 3 / 2), setID(), Team.BOT4));
        }
    }
}

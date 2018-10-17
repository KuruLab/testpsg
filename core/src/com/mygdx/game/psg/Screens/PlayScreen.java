package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.psg.Engine.Detector;
import com.mygdx.game.psg.Engine.Gesture;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Attack;
import com.mygdx.game.psg.Sprites.Cell;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.psg.Sprites.Cell.Clear;
import static com.mygdx.game.psg.Sprites.Cell.Contact;
import static com.mygdx.game.psg.Sprites.Cell.Stop;

public class PlayScreen implements Screen{

    //game elements
    private Stage stage;
    public static World world;
    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Gesture gesture = new Gesture();

    //other variables
    public static boolean oneSelected, oneTarget;
    public static Vector2 positionCamera, sizeViewport, attackDirection;
    public static  Cell selectedCell, targetCell;
    public static ArrayList<Body> contact = new ArrayList<Body>();
    public static float touchRadius, zoom, zoomInit, zoomFinal;
    public static Attack.Type type;

    //load textures
    private Texture textureCircle = new Texture("circle.png");

    public PlayScreen(MainGame game){
        //set zoom
        zoomInit = ((MainGame.W_Width/MainGame.V_Width)+(MainGame.W_Height/MainGame.V_Height))/2;
        zoomFinal = MainGame.V_Width/MainGame.W_Width+MainGame.V_Height/MainGame.W_Height;
        zoom = zoomInit;
        touchRadius = 100*zoom;

        this.game = game;

        //create camera & create viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(MainGame.W_Width, MainGame.W_Height,camera);
        camera.position.set(0,0,0);
        Gdx.input.setInputProcessor(gesture);


        //update references
        positionCamera = new Vector2(camera.position.x, camera.position.y);
        sizeViewport = new Vector2();

        //box 2d
        world = new World(new Vector2(0,0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        Detector collisionDetector = new Detector();
        world.setContactListener(collisionDetector);
    }

    @Override
    public void show() {
         attackDirection = new Vector2();

        //add units on stage
        stage = new Stage();

        firstGame();

        //action Bot
        for(Actor bot : stage.getActors()){
            if(bot.getClass() == Cell.class){
                 if(((Cell)bot).team != Cell.Team.PLAYER){

                     BotAction((Cell)bot);

                 }
            }
        }
    }

    @Override
    public void render(float delta) {
        if(Math.abs(zoom - zoomFinal) > 0.005f){
            zoom = zoom + (zoomFinal - zoom)*delta*2.5f;
        }else{
            zoom = zoomFinal;
        }


        //physics time execute
        world.step(1/60f, 6,2);

        //clear board
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw body
        //Matrix4 matrix4 = game.batch.getProjectionMatrix().cpy().scale(MainGame.PPM, MainGame.PPM, 0);
        //box2DDebugRenderer.render(world, matrix4);
        game.batch.setProjectionMatrix(camera.combined);

        //update players
        stage.act();
        createAttack();


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

        //other updates
        updateCollision();
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

        textureCircle.dispose();

    }

    @Override
    public void dispose() {
        textureCircle.dispose();
        world.dispose();
        stage.dispose();
        box2DDebugRenderer.dispose();
    }

    private void controlCamera(float delta){

        if(!gesture.zoom) {

            if (Gdx.input.isTouched()) {

                camera.position.x -= Gdx.input.getDeltaX() * zoom;
                camera.position.y += Gdx.input.getDeltaY() * zoom;


            } else {

                if (oneSelected && selectedCell.team == Cell.Team.PLAYER) {

                    camera.position.x = camera.position.x + (selectedCell.body.getPosition().x * MainGame.PPM * MainGame.W_Width / MainGame.V_Width + sizeViewport.x / 2
                            - positionCamera.x * sizeViewport.x / MainGame.V_Width - (MainGame.W_Width / 2)) * (delta*2.5f)/zoom;

                    camera.position.y = camera.position.y + (selectedCell.body.getPosition().y * MainGame.PPM * MainGame.W_Height / MainGame.V_Height + sizeViewport.y / 2
                            - positionCamera.y * sizeViewport.y / MainGame.V_Height - (MainGame.W_Height / 2)) * (delta*2.5f)/zoom;
                }
            }
        }else{if(oneSelected){Cell.Stop(selectedCell);}}


        if (camera.position.x < MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.V_Width) {
            camera.position.x = MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.V_Width;

        }

        if (camera.position.x > -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.V_Width) {
            camera.position.x = -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.V_Width;
        }

        if (camera.position.y < MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.V_Height) {
            camera.position.y = MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.V_Height;
        }

        if (camera.position.y > -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.V_Height) {
            camera.position.y = -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.V_Height;
        }




        positionCamera.set(camera.position.x, camera.position.y);
        sizeViewport.set(MainGame.V_Height/2, MainGame.V_Width/2);

    }

    private void updateCamera(float delta){
        //update camera
        controlCamera(delta);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
        positionCamera.set(camera.position.x, camera.position.y);
        camera.zoom = zoom;
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
                    textureCircle,
                   cell.body.getPosition().x* MainGame.PPM - cell.baseRadius,
                   cell.body.getPosition().y* MainGame.PPM - cell.baseRadius,
                   cell.baseRadius *2,
                   cell.baseRadius *2);

        game.batch.setColor(
                cell.getColor().r*0.8f,
                cell.getColor().g*0.8f,
                cell.getColor().b*0.8f,
                cell.getColor().a*1f);

            //draw energy
            game.batch.draw(textureCircle,
                    cell.body.getPosition().x* MainGame.PPM - cell.radiusEnergy,
                    cell.body.getPosition().y* MainGame.PPM - cell.radiusEnergy,
                    cell.radiusEnergy*2,
                    cell.radiusEnergy*2);

            game.batch.end();
    }

    private void DrawAttack(Attack attack){

        game.batch.begin();
        game.batch.setColor(attack.getColor());

        game.batch.draw(textureCircle,
                attack.body.getPosition().x* MainGame.PPM - attack.energyRadius,
                attack.body.getPosition().y* MainGame.PPM - attack.energyRadius,
                attack.energyRadius *2,attack.energyRadius *2);
        game.batch.end();
    }

    private void createAttack(){
        if(oneSelected && oneTarget && selectedCell.team == Cell.Team.PLAYER){
            stage.addActor(new Attack(selectedCell, targetCell, type));
            Clear(selectedCell);
        }
    }

    private void updateCollision(){

        while(contact.size() > 0){

            Body bodyA = contact.get(0);
            contact.remove(0);
            contact.trimToSize();

            Body bodyB = contact.get(0);
            contact.remove(0);
            contact.trimToSize();

            for(int a = 0; a < stage.getActors().size; a++){

                if (stage.getActors().get(a).getClass() == Cell.class){

                    if (((Cell)stage.getActors().get(a)).body == bodyA || ((Cell)stage.getActors().get(a)).body == bodyB){

                        for (int b = 0; b < stage.getActors().size; b++) {

                            if (stage.getActors().get(b).getClass() == Attack.class) {

                                 if(((Attack)stage.getActors().get(b)).body == bodyA || ((Attack)stage.getActors().get(b)).body == bodyB){

                                        if((stage.getActors().get(a)).getClass() == Cell.class){

                                        Contact(stage.getActors().get(a), stage.getActors().get(b));
                                 }else
                                        Contact(stage.getActors().get(b), stage.getActors().get(a));
                                }
                            }
                        }
                    }
                 }
            }
        }
    }

    private void firstGame(){

        int test = 0;
        for(int i = 0; i < test; i++){
            stage.addActor(new Cell(0,0, Cell.Team.PLAYER));
        }


        //Neutral
        int teamNeutral = 20;
        for(int i = 0; i < teamNeutral; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.NEUTRAL));
        }

        //Player
        int teamCells = 3;
        for(int i = 0; i < teamCells; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.PLAYER));
        }

        //Bot1
        for(int i = 0; i < teamCells; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT1));
        }

        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT2));
        }

        //Bot3
        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT3));
        }

        //Bot4
        //Bot2
        for(int i = 0; i < teamCells; i++) {
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT4));
        }
    }

    private void BotAction(Cell bot){

    }

}

package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
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
import com.mygdx.game.psg.Engine.SaveGame;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Attack;
import com.mygdx.game.psg.Sprites.Cell;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.mygdx.game.psg.Sprites.Cell.Clear;

public class PlayScreen implements Screen{

    //game elements
    private Stage stage;
    public static World world;
    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    public static int player, bots, neutral;
    private Vector2 velocity = new Vector2();
    public SaveGame saveGame = new SaveGame();

    //other variables
    public static boolean oneSelected, oneTarget, oneMove;
    private static Vector2 sizeViewport;
    public static  Vector2 positionCamera;
    public static  Cell selectedCell, targetCell;
    public static ArrayList<Body> contact = new ArrayList<Body>();
    public static float touchRadius, zoom, zoomInit, zoomFinal, attackDirection;
    public static Attack.Type typeAttack;
    private float camX, camY;
    private static int explosionCount, initCount = 300;
    public static int numberAttack, restartCount;

    //load textures
    private Texture textureCircle = new Texture("circle.png");
    private Texture textureSelect = new Texture("select.png");
    private Texture textureAttack = new Texture("attack.png");
    private Texture textureCell = new Texture("cell.png");

    public PlayScreen(MainGame game) throws IOException {
        //set zoom
        zoomInit = ((MainGame.W_Width/MainGame.V_Width)+(MainGame.W_Height/MainGame.V_Height))/2;
        zoom = MainGame.V_Width/MainGame.W_Width + MainGame.V_Height/MainGame.W_Height;
        touchRadius = 100*zoomInit;
        zoomFinal = zoom/2;
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

    @Override
    public void show() {
        //add units on stage
        stage = new Stage();
        firstGame();
        saveGame.Print();
    }

    @Override
    public void render(float delta) {

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
        createAttack();
        explosionCount++;

        //count players
        player = 0;
        neutral = 0;
        bots = 0;

        //other updates
        updateCollision();
        updateCamera(delta);
        Draw();
        WinOrLose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    @Override
    public void pause() {
        saveGame.SaveStage(stage);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        textureCircle.dispose();
        textureSelect.dispose();
        textureAttack.dispose();
        textureCell.dispose();
        world.dispose();
        stage.dispose();
        box2DDebugRenderer.dispose();
    }

    private void updateCamera(float delta){
        //update camera
        if(!Gesture.zoom) {

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


        }else{if(oneSelected){Cell.Stop(selectedCell);}}


        if (camera.position.x < MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.V_Width) {
            camera.position.x = MainGame.W_Width * zoom - MainGame.W_Width * zoom / 2 - MainGame.V_Width;
            camX = camera.position.x;
        }

        if (camera.position.x > -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.V_Width) {
            camera.position.x = -MainGame.W_Width * zoom + MainGame.W_Width * zoom / 2 + MainGame.V_Width;
            camX = camera.position.x;
        }

        if (camera.position.y < MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.V_Height) {
            camera.position.y = MainGame.W_Height * zoom - MainGame.W_Height * zoom / 2 - MainGame.V_Height;
            camY = camera.position.y;
        }

        if (camera.position.y > -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.V_Height) {
            camera.position.y = -MainGame.W_Height * zoom + MainGame.W_Height * zoom / 2 + MainGame.V_Height;
            camY = camera.position.y;
        }

        positionCamera.set(camera.position.x, camera.position.y);
        sizeViewport.set(viewport.getScreenWidth(),viewport.getScreenHeight());
        positionCamera.set(camera.position.x, camera.position.y);
        camera.zoom = zoom;
        camera.update();
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

                                        if((stage.getActors().get(a)).getClass() == Cell.class)
                                            Contact(stage.getActors().get(a), stage.getActors().get(b));
                                        else
                                        Contact(stage.getActors().get(b), stage.getActors().get(a));
                                }
                            }
                        }
                    }
                 }
            }
        }
    }

    private static void Contact(Actor actorA, Actor actorB){

        if(actorA.getClass() == Cell.class){

            if(((Cell) actorA).team == ((Attack) actorB).team ){
                if( ((Attack) actorB).type != Attack.Type.EXPLOSION){
                ((Cell)actorA).actualEnergy = ((Cell)actorA).actualEnergy + ((Attack)actorB).actualEnergy*0.75f;
                }else{
                ((Cell)actorA).actualEnergy = ((Cell)actorA).actualEnergy + ((Attack)actorB).actualEnergy*0.3f;
                }
            }else {
                ((Cell) actorA).actualEnergy = ((Cell) actorA).actualEnergy - ((Attack) actorB).actualEnergy;
            }

            if(((Cell)actorA).actualEnergy < 0){
                ((Cell)actorA).actualEnergy = (-1)*((Cell)actorA).actualEnergy;
                ((Cell)actorA).team = ((Attack)actorB).team;
            }

            ((Attack)actorB).actualEnergy = ((Attack)actorB).actualEnergy - ((Attack)actorB).actualEnergy*0.3f;
            ((Attack)actorB).modifyEnergy = true;
            ((Attack)actorB).inativity = 0;

            if(((Attack)actorB).actualEnergy <= ((Attack)actorB).baseAttack){
                ((Attack)actorB).remove = true;
            }

        }else{

            if(((Cell)actorB).team == ((Attack)actorA).team) {
                if(((Attack)actorA).type != Attack.Type.EXPLOSION){
                ((Cell)actorB).actualEnergy = ((Cell)actorB).actualEnergy + ((Attack)actorA).actualEnergy * 0.75f;
                }else{
                ((Cell)actorB).actualEnergy = ((Cell)actorB).actualEnergy + ((Attack)actorA).actualEnergy * 0.3f;
                }
            }else {
                ((Cell) actorB).actualEnergy = ((Cell) actorB).actualEnergy - ((Attack) actorA).actualEnergy;
            }

            if(((Cell)actorB).actualEnergy < 0){
                ((Cell)actorB).actualEnergy = (-1)*((Cell)actorB).actualEnergy;
                ((Cell)actorB).team = ((Attack)actorA).team;
            }

            ((Attack)actorA).actualEnergy = ((Attack)actorA).actualEnergy - ((Attack)actorA).actualEnergy*0.3f;
            ((Attack)actorA).modifyEnergy = true;
            ((Attack)actorA).inativity = 0;

            if(((Attack)actorA).actualEnergy < ((Attack)actorA).baseAttack){
                ((Attack)actorA).remove = true;
            }
        }
    }

    private void WinOrLose() {

        //condition win and lose
        if(player == 0 || bots == 0) {
            PlayScreen.zoomFinal = 2f / PlayScreen.zoomInit;
            oneTarget = false;
            oneSelected = false;
            restartCount++;
        }else{restartCount = 0;}
    }

    private void firstGame() {
        int total = (int) (MainGame.V_Width*MainGame.V_Height/((1080*1920)/32));
        int max = (int) (1 + MainGame.V_Width*MainGame.V_Height/((1080*1920)/4));
        int min = (int) (1 + MainGame.V_Width*MainGame.V_Height/((1080*1920)/2));

        player = 0;
        bots = 0;

        //Player
        int teamCells = 0;
        while (teamCells == 0){
            teamCells = random(min, max);
        }
        for (int i = 0; i < teamCells; i++) {
            if(i == 0){
                stage.addActor(new Cell(0, 0, Cell.Team.PLAYER));
                selectedCell = (Cell)stage.getActors().get(0);
                oneSelected = true;
            }else {
                stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                        random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.PLAYER));
            }
            player++;
        }

        while (bots == 0 || player == 0) {
            //Bot1
            if (randomBoolean()) {
                for (int i = 0; i < teamCells; i++) {
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT1));
                    bots++;
                }
            }
            //Bot2
            if (randomBoolean()) {
                for (int i = 0; i < teamCells; i++) {
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT2));
                    bots++;
                }
            }
            //Bot3
            if (randomBoolean()) {
                for (int i = 0; i < teamCells; i++) {
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT3));
                    bots++;
                }
            }

            //Bot4
            if (randomBoolean()) {
                for (int i = 0; i < teamCells; i++) {
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT4));
                    bots++;
                }
            }
            //Bot5
            if (randomBoolean()) {
                for (int i = 0; i < teamCells; i++) {
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT5));
                    bots++;
                }
            }
        }

        //Neutral
        int teamNeutral = random(total/4, total/2);
        for(int i = 0; i < teamNeutral; i++){
            stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                    random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.NEUTRAL));
        }
    }

    private void createAttack(){

        if(oneSelected && oneTarget && selectedCell.team == Cell.Team.PLAYER){
            stage.addActor(new Attack(selectedCell, targetCell, typeAttack, attackDirection));
            Clear(selectedCell);
        }
/*
        if(oneMove){
            oneMove = false;
            oneSelected = false;
        }
*/
    }

    private void Explosion(Cell cell){
        int angle = 0;
        explosionCount = 0;

        for(int i = 0; i < 20; i++){

            stage.addActor(new Attack(cell, targetCell, Attack.Type.EXPLOSION, angle));
            angle += 18;

        }

        cell.actualEnergy = 1;
    }

    private void Draw(){
        game.batch.begin();

        //draw selected
        DrawInfo();

        //draw light
        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Cell.class) {

                DrawLightCell((Cell) actor);
            }
        }

        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Attack.class) {
                DrawLightAttack((Attack)actor);
            }

        }

        //draw cell
        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Cell.class) {
                if (((Cell)actor).actualEnergy == ((Cell)actor).maxEnergy && explosionCount > 60 ){
                    Explosion((Cell)actor);
                }
                if(((Cell)actor).team == Cell.Team.PLAYER){
                    player++;
                }
                if(((Cell)actor).team == Cell.Team.NEUTRAL){
                    neutral++;
                }
                if(((Cell)actor).team != Cell.Team.PLAYER && ((Cell)actor).team != Cell.Team.NEUTRAL){
                    BotAction((Cell)actor);
                    bots++;
                }
                DrawCell((Cell) actor);
            }
        }

        //draw energy
        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Cell.class) {

                DrawEnergy((Cell) actor);
            }
        }

        numberAttack = 0;
        for(Actor actor : stage.getActors())
        {
            if(actor.getClass()== Attack.class) {
                if(((Attack)actor).remove) {
                    ((Attack)actor).body.destroyFixture(((Attack)actor).body.getFixtureList().pop());
                    world.destroyBody(((Attack)actor).body);
                    actor.remove();
                }else{
                    numberAttack++;
                    DrawAttack((Attack) actor);
                }
            }
        }
        game.batch.end();
    }

    private void DrawLightAttack(Attack attack){

        game.batch.setColor(
                attack.getColor().r,
                attack.getColor().g,
                attack.getColor().b,
                attack.getColor().a*0.8f);

        game.batch.draw(textureAttack,
                attack.body.getPosition().x* MainGame.PPM - attack.energyRadius*3f,
                attack.body.getPosition().y* MainGame.PPM - attack.energyRadius*3f,
                attack.energyRadius *6,attack.energyRadius *6);

    }

    private void DrawLightCell(Cell cell){

        game.batch.setColor(
                cell.getColor().r,
                cell.getColor().g,
                cell.getColor().b,
                cell.getColor().a*0.5f);

        //draw energy
        game.batch.draw(textureAttack,
                cell.body.getPosition().x * MainGame.PPM  - (cell.radiusEnergy * 3f),
                cell.body.getPosition().y * MainGame.PPM -  (cell.radiusEnergy * 3f),
                (cell.radiusEnergy * 3f) * 2f,
                (cell.radiusEnergy * 3f) * 2f);
    }

    private  void DrawInfo(){
        if(oneSelected){

            //draw select
            game.batch.setColor(
                    selectedCell.getColor().r,
                    selectedCell.getColor().g,
                    selectedCell.getColor().b,
                    selectedCell.getColor().a*0.5f);

            game.batch.draw(
                    textureAttack,

                    selectedCell.body.getPosition().x* MainGame.PPM - (selectedCell.baseRadius + touchRadius*zoom) * 2f,
                    selectedCell.body.getPosition().y* MainGame.PPM - (selectedCell.baseRadius + touchRadius*zoom) * 2f,
                    (selectedCell.baseRadius + touchRadius*zoom) * 4f,
                    (selectedCell.baseRadius + touchRadius*zoom) * 4f);

            game.batch.setColor(
                    selectedCell.getColor().r,
                    selectedCell.getColor().g,
                    selectedCell.getColor().b,
                    selectedCell.getColor().a*0.9f);

            game.batch.draw(
                    textureSelect,
                    selectedCell.body.getPosition().x* MainGame.PPM - selectedCell.baseRadius - touchRadius*zoom,
                    selectedCell.body.getPosition().y* MainGame.PPM - selectedCell.baseRadius - touchRadius*zoom,
                    (selectedCell.baseRadius + touchRadius*zoom) * 2,
                    (selectedCell.baseRadius + touchRadius*zoom) * 2);
        }

        if(oneTarget){

            game.batch.setColor(
                    targetCell.getColor().r,
                    targetCell.getColor().g,
                    targetCell.getColor().b,
                    targetCell.getColor().a*0.5f);

            game.batch.draw(
                    textureAttack,
                    targetCell.body.getPosition().x* MainGame.PPM - (targetCell.baseRadius + touchRadius*zoom) * 2f,
                    targetCell.body.getPosition().y* MainGame.PPM - (targetCell.baseRadius + touchRadius*zoom) * 2f,
                    (targetCell.baseRadius + touchRadius*zoom) * 4,
                    (targetCell.baseRadius + touchRadius*zoom) * 4);

            game.batch.setColor(
                    targetCell.getColor().r,
                    targetCell.getColor().g,
                    targetCell.getColor().b,
                    targetCell.getColor().a*0.9f);

            //draw select
            game.batch.draw(
                    textureSelect,
                    targetCell.body.getPosition().x* MainGame.PPM - targetCell.baseRadius - touchRadius*zoom ,
                    targetCell.body.getPosition().y* MainGame.PPM - targetCell.baseRadius - touchRadius*zoom ,
                    (targetCell.baseRadius + touchRadius*zoom) * 2,
                    (targetCell.baseRadius + touchRadius*zoom) * 2);

        }


    }

    private void DrawCell(Cell cell){


        game.batch.setColor(cell.getColor());

        //draw cell
        game.batch.draw(
                textureCell,
                cell.body.getPosition().x* MainGame.PPM - cell.baseRadius,
                cell.body.getPosition().y* MainGame.PPM - cell.baseRadius,
                cell.baseRadius *2,
                cell.baseRadius *2);


    }

    private void DrawEnergy(Cell cell){


        game.batch.setColor(cell.getColor().mul(Color.WHITE));

        game.batch.draw(textureCircle,
                cell.body.getPosition().x * MainGame.PPM - cell.radiusEnergy,
                cell.body.getPosition().y * MainGame.PPM - cell.radiusEnergy,
                cell.radiusEnergy * 2,
                cell.radiusEnergy * 2);


    }

    private void DrawAttack(Attack attack){



        game.batch.setColor(attack.getColor());

        game.batch.draw(textureCircle,
                attack.body.getPosition().x* MainGame.PPM - attack.energyRadius,
                attack.body.getPosition().y* MainGame.PPM - attack.energyRadius,
                attack.energyRadius *2,attack.energyRadius *2);


    }

    private void BotAction(Cell bot) {

        if(random(100 ) < 1) {
            if (bot.actualEnergy / bot.maxEnergy * random(100) > 10) {
                stage.addActor(new Attack(bot, targetCell, typeAttack, random(360)));
                Cell.Stop(bot);
            }
            else {

                if ((bot.actualEnergy / bot.maxEnergy) * random(100) < 5) {
                    velocity.set(bot.baseMove, bot.baseMove).setAngle(random(360));
                    bot.body.setLinearVelocity(velocity);

                }
            }
        }
    }

}
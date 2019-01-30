package com.mygdx.game.psg.screens;

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
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.old_engine.Actions;
import com.mygdx.game.psg.old_engine.BotAction;
import com.mygdx.game.psg.old_engine.Detector;
import com.mygdx.game.psg.old_engine.Gesture;
import com.mygdx.game.psg.old_engine.History;
import com.mygdx.game.psg.old_engine.OldAttribute;
import com.mygdx.game.psg.old_engine.Population;
import com.mygdx.game.psg.old_engine.SaveGame;
import com.mygdx.game.psg.old_engine.Wheel;
import com.mygdx.game.psg.sprites.Attack;
import com.mygdx.game.psg.sprites.Cell;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.mygdx.game.psg.sprites.Cell.Clear;

public class PlayScreen implements Screen{

    //game elements
    public static Stage stage;
    public static World world;
    private MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    public static int player, bots, neutral;
    private Vector2 velocity = new Vector2();
    public static Actions actions = new Actions();
    public Wheel wheel;

    //load game
    public static OldAttribute oldAttribute;
    public BotAction botAction = new BotAction();
    public History history = new History();
    public Population population = new Population();
    public SaveGame saveGame = new SaveGame();

    //other variables
    public static boolean oneSelected, oneTarget, oneFire, move;
    public Actor targetBot = new Actor();
    private static Vector2 sizeViewport;
    public static Vector2 positionCamera;
    public static Cell selectedCell, targetCell;
    public static Cell[] selectedBots = new Cell[6];
    public static ArrayList<Body> contact = new ArrayList<Body>();
    public static float touchRadius, zoom, zoomInit, zoomFinal, attackDirection;
    public static OldAttribute.AttributeType typeAttack, botAttack;
    private float camX, camY;
    private static int explosionCount, initCount;
    public static int numberAttack, restartCount, timeAttack;
    public static boolean[] botActive = new boolean[5];

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
        initCount = 300;
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

        //Other
        botAction.setBotActions(MainGame.actionsLoad);
        history.setHistory(MainGame.historiesLoad);
        timeAttack = MainGame.timeAttack;

        MainGame.lose = false;
        MainGame.win = false;
        move = false;
    }

    @Override
    public void show() {
        //add units on stage
        stage = new Stage();
        newGame();
    }

    private static void Contact(Actor actorA, Actor actorB) {

        if (actorA.getClass() == Cell.class) {

            if (((Cell) actorA).team == ((Attack) actorB).team) {
                if (((Attack) actorB).type != OldAttribute.AttributeType.SIZE) {
                    ((Cell) actorA).actualEnergy = ((Cell) actorA).actualEnergy + ((Attack) actorB).actualEnergy * 0.75f;
                } else {
                    ((Cell) actorA).actualEnergy = ((Cell) actorA).actualEnergy + ((Attack) actorB).actualEnergy * 0.3f;
                }
            } else {
                ((Cell) actorA).actualEnergy = ((Cell) actorA).actualEnergy - ((Attack) actorB).actualEnergy;
            }

            if (((Cell) actorA).actualEnergy < 0) {
                ((Cell) actorA).actualEnergy = (-1) * ((Cell) actorA).actualEnergy;
                ((Cell) actorA).team = ((Attack) actorB).team;
            }

            ((Attack) actorB).actualEnergy = ((Attack) actorB).actualEnergy - ((Attack) actorB).actualEnergy * 0.3f;
            ((Attack) actorB).modifyEnergy = true;
            ((Attack) actorB).inativity = 0;

            if (((Attack) actorB).actualEnergy <= ((Attack) actorB).baseAttack) {
                ((Attack) actorB).remove = true;
            }

        } else {

            if (((Cell) actorB).team == ((Attack) actorA).team) {
                if (((Attack) actorA).type != OldAttribute.AttributeType.SIZE) {
                    ((Cell) actorB).actualEnergy = ((Cell) actorB).actualEnergy + ((Attack) actorA).actualEnergy * 0.75f;
                } else {
                    ((Cell) actorB).actualEnergy = ((Cell) actorB).actualEnergy + ((Attack) actorA).actualEnergy * 0.3f;
                }
            } else {
                ((Cell) actorB).actualEnergy = ((Cell) actorB).actualEnergy - ((Attack) actorA).actualEnergy;
            }

            if (((Cell) actorB).actualEnergy < 0) {
                ((Cell) actorB).actualEnergy = (-1) * ((Cell) actorB).actualEnergy;
                ((Cell) actorB).team = ((Attack) actorA).team;
            }

            ((Attack) actorA).actualEnergy = ((Attack) actorA).actualEnergy - ((Attack) actorA).actualEnergy * 0.3f;
            ((Attack) actorA).modifyEnergy = true;
            ((Attack) actorA).inativity = 0;

            if (((Attack) actorA).actualEnergy < ((Attack) actorA).baseAttack) {
                ((Attack) actorA).remove = true;
            }
        }
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
        if(!Gesture.zoom && restartCount == 0) {

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

    @Override
    public void render(float delta) {

        if (initCount == 0) {
            if (Math.abs(zoom - zoomFinal) > 0.005f) {
                zoom = zoom + (zoomFinal - zoom) * delta * 2.5f;
            } else {
                zoom = zoomFinal;
            }
        } else {
            zoom = zoom + (zoomFinal - zoom) * delta * 0.5f;
            initCount--;
        }

        if (move) {
            actions.AddAction(Cell.Team.PLAYER, OldAttribute.AttributeType.SPEED);
            move = false;
        }

        //physics time execute
        world.step(1 / 60f, 6, 2);

        //clear board
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
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
        if (restartCount == 0) {
            updateCollision();
        }
        updateCamera(delta);
        Draw();
        try {
            WinOrLose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WinOrLose() throws IOException {
        //condition win and lose
        if(player == 0 || bots == 0) {
            PlayScreen.zoomFinal = 2f / PlayScreen.zoomInit;
            oneTarget = false;
            oneSelected = false;
            restartCount++;

            if(restartCount == 1){
                if (bots == 0) {
                    MainGame.win = true;
                }
                if (player == 0) {
                    MainGame.lose = true;
                }

                actions.getFitness();
            }


            if(restartCount == 25){
                wheel = new Wheel(MainGame.attributesLoad,actions.getFitness());
            }

            if(restartCount == 50){
                wheel.generateWheel(0);
                wheel.PreCrossover(0);
            }

            if(restartCount == 75){
                wheel.generateWheel(1);
                wheel.PreCrossover(1);
            }

            if(restartCount == 100){
                wheel.generateWheel(2);
                wheel.PreCrossover(2);
            }

            if(restartCount == 125){
                wheel.generateWheel(3);
                wheel.PreCrossover(3);
            }

            if(restartCount == 150){
                wheel.generateWheel(4);
                wheel.PreCrossover(4);
            }

            if(restartCount == 175){
                wheel.generateWheel(5);
                wheel.PreCrossover(5);
            }

            if(restartCount == 200){
                wheel.generateWheel(6);
                wheel.PreCrossover(6);
            }

            if(restartCount == 225){
                population.setPopulation(wheel.NewGeneration());
            }

            if(restartCount == 250){
                saveGame.SavePopulation(population);
            }

            if(restartCount == 275){
                MainGame.historiesLoad[random(0,9)] = MainGame.win;
                MainGame.wins = 0;
                MainGame.loses = 0;

                for(int i = 0; i < 9; i++){
                    if(MainGame.historiesLoad[i]){
                        MainGame.wins++;
                    }else{
                        MainGame.loses++;
                    }
                }
            }

            if(restartCount == 300){
                botAction.Adjust();
                saveGame.SaveBot(botAction);
            }

            if(restartCount == 325) {
                MainGame.alterated = true;
                MainGame.controler = MainGame.Controler.RESTART;
            }

        }else{restartCount = 0;}
    }

    private void newGame() {
        int total = (int) (MainGame.V_Width*MainGame.V_Height/((1080*1920)/32));
        int max = (int) (1 + MainGame.V_Width*MainGame.V_Height/((1080*1920)/4));
        int min = (int) (1 + MainGame.V_Width*MainGame.V_Height/((1080*1920)/2));

        player = 0;
        bots = 0;


        //PlayerModel
            int teamCells = 0;
            while (teamCells == 0) {
                teamCells = random(min, max);
            }

            for (int i = 0; i < teamCells; i++) {
                if (i == 0) {
                    oldAttribute = MainGame.oldAttributes[random(0, 24)];
                    stage.addActor(new Cell(0, 0, Cell.Team.PLAYER));
                    selectedCell = (Cell) stage.getActors().get(0);
                    oneSelected = true;
                } else {
                    oldAttribute = MainGame.oldAttributes[random(0, 24)];
                    stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                            random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.PLAYER));
                }
                player++;
            }

            while (bots == 0 || player == 0) {
                //Bot1
                if (randomBoolean()) {
                    for (int i = 0; i < teamCells; i++) {
                        oldAttribute = MainGame.oldAttributes[random(25, 49)];
                        stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                                random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT1));
                        bots++;
                    }
                    botActive[0] = true;
                }
                //Bot2
                if (randomBoolean()) {
                    for (int i = 0; i < teamCells; i++) {
                        oldAttribute = MainGame.oldAttributes[random(50, 74)];
                        stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                                random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT2));
                        bots++;
                    }
                    botActive[1] = true;
                }
                //Bot3
                if (randomBoolean()) {
                    for (int i = 0; i < teamCells; i++) {
                        oldAttribute = MainGame.oldAttributes[random(75, 99)];
                        stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                                random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT3));
                        bots++;
                    }
                    botActive[2] = true;
                }

                //Bot4
                if (randomBoolean()) {
                    for (int i = 0; i < teamCells; i++) {
                        oldAttribute = MainGame.oldAttributes[random(100, 124)];
                        stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                                random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT4));
                        bots++;
                    }
                    botActive[3] = true;
                }
                //Bot5
                if (randomBoolean()) {
                    for (int i = 0; i < teamCells; i++) {
                        oldAttribute = MainGame.oldAttributes[random(125, 149)];
                        stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                                random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.BOT5));
                        bots++;
                    }
                    botActive[4] = true;
                }
            }

            //Neutral
            int teamNeutral = random(total / 4, total / 2);
            for (int i = 0; i < teamNeutral; i++) {
                oldAttribute = MainGame.oldAttributes[random(150, 174)];
                stage.addActor(new Cell(random(-MainGame.V_Width, MainGame.V_Width),
                        random(-MainGame.V_Height, MainGame.V_Height), Cell.Team.NEUTRAL));
            }

    }

    private void createAttack(){


        if(oneSelected && oneTarget && selectedCell.team == Cell.Team.PLAYER || oneFire){

            if (typeAttack == OldAttribute.AttributeType.DEFENSE) {
                actions.AddAction(Cell.Team.PLAYER, typeAttack);
                float angle1 = attackDirection;
                float angle2 = attackDirection;
                stage.addActor(new Attack(selectedCell, targetCell, OldAttribute.AttributeType.DEFENSE, attackDirection));
                for(int i = 0; i < 2; i++){

                    angle1 += 30;
                    stage.addActor(new Attack(selectedCell, targetCell, OldAttribute.AttributeType.DEFENSE, angle1));
                    angle2 -= 30;
                    stage.addActor(new Attack(selectedCell, targetCell, OldAttribute.AttributeType.DEFENSE, angle2));

                }
                selectedCell.actualEnergy = selectedCell.actualEnergy * 0.8f;
                Clear(selectedCell);
            }else{
                stage.addActor(new Attack(selectedCell, targetCell, typeAttack, attackDirection));
                actions.AddAction(Cell.Team.PLAYER, typeAttack);
                Clear(selectedCell);
            }
        }
    }

    private void Explosion(Cell cell){
        int angle = 0;
        explosionCount = 0;

        for(int i = 0; i < 20; i++){

            stage.addActor(new Attack(cell, targetCell, OldAttribute.AttributeType.SIZE, angle));
            angle += 18;

        }
        actions.AddAction(cell.team, OldAttribute.AttributeType.SIZE);
        cell.team = Cell.Team.NEUTRAL;
        cell.actualEnergy = cell.maxEnergy * 0.75f;
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
                    if(restartCount == 0) {
                        Explosion((Cell) actor);
                    }
                }
                if(((Cell)actor).team == Cell.Team.PLAYER){
                    player++;
                }
                if(((Cell)actor).team == Cell.Team.NEUTRAL){
                    neutral++;
                }
                if(((Cell)actor).team != Cell.Team.PLAYER && ((Cell)actor).team != Cell.Team.NEUTRAL){
                    bots++;
                    BotAction((Cell) actor);
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
                    selectedCell.getColor().a*0.3f);

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
            game.batch.setColor(
                    selectedCell.getColor().r,
                    selectedCell.getColor().g,
                    selectedCell.getColor().b,
                    selectedCell.getColor().a*0.1f);

            game.batch.draw(
                    textureSelect,
                    selectedCell.body.getPosition().x* MainGame.PPM - (selectedCell.baseRadius)*1.2f,
                    selectedCell.body.getPosition().y* MainGame.PPM - (selectedCell.baseRadius)*1.2f,
                    (selectedCell.baseRadius) * 2.4f,
                    (selectedCell.baseRadius) * 2.4f);
        }

        if(oneTarget){

            game.batch.setColor(
                    MainGame.colors.get(0).r,
                    MainGame.colors.get(0).g,
                    MainGame.colors.get(0).b,
                    MainGame.colors.get(0).a*0.5f);

            game.batch.draw(
                    textureAttack,
                    targetCell.body.getPosition().x* MainGame.PPM - (targetCell.baseRadius + touchRadius*zoom) * 2f,
                    targetCell.body.getPosition().y* MainGame.PPM - (targetCell.baseRadius + touchRadius*zoom) * 2f,
                    (targetCell.baseRadius + touchRadius*zoom) * 4,
                    (targetCell.baseRadius + touchRadius*zoom) * 4);

            game.batch.setColor(
                    MainGame.colors.get(0).r,
                    MainGame.colors.get(0).g,
                    MainGame.colors.get(0).b,
                    MainGame.colors.get(0).a*0.9f);

            //draw select
            game.batch.draw(
                    textureSelect,
                    targetCell.body.getPosition().x* MainGame.PPM - targetCell.baseRadius/2 - touchRadius*zoom ,
                    targetCell.body.getPosition().y* MainGame.PPM - targetCell.baseRadius/2 - touchRadius*zoom ,
                    (targetCell.baseRadius/2 + touchRadius*zoom) * 2f,
                    (targetCell.baseRadius/2 + touchRadius*zoom) * 2f);

            game.batch.setColor(
                    targetCell.getColor().r,
                    MainGame.colors.get(0).g,
                    MainGame.colors.get(0).b,
                    MainGame.colors.get(0).a*0.1f);

            //draw select
            game.batch.draw(
                    textureSelect,
                    targetCell.body.getPosition().x* MainGame.PPM - targetCell.baseRadius * 1.2f ,
                    targetCell.body.getPosition().y* MainGame.PPM - targetCell.baseRadius * 1.2f ,
                    (targetCell.baseRadius) * 2.4f,
                    (targetCell.baseRadius) * 2.4f);

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

        targetBot = (stage.getActors().items)[random(0, stage.getActors().size - 1)];

        if(targetBot.getClass() == Cell.class && restartCount == 0) {

            botAttack = botAction.getAction(bot, targetBot);

            if (botAttack != null && targetBot.getClass() == Cell.class) {

                switch (botAttack) {

                    case ATTACK:
                        if (bot.team != Cell.Team.NEUTRAL) {
                            stage.addActor(new Attack(bot, (Cell) targetBot, botAttack, (((Cell) targetBot).body.getPosition()).sub(bot.body.getPosition()).angle()));
                            actions.AddAction(bot.team, botAttack);
                        }
                        break;
                    case SIZE:
                            selectedBots[botIndex(bot.team)] = bot;
                        break;
                    case REGEN:
                        if (bot.team != Cell.Team.NEUTRAL) {
                            stage.addActor(new Attack(bot, (Cell) targetBot, botAttack, (((Cell) targetBot).body.getPosition()).sub(bot.body.getPosition()).angle()));
                            actions.AddAction(bot.team, botAttack);
                        }
                        break;
                    case SPEED:
                        if (bot.team != Cell.Team.NEUTRAL) {
                            velocity.set(bot.baseMove, bot.baseMove).setAngle(random(0, 360));
                            bot.body.setLinearVelocity(velocity);
                            actions.AddAction(bot.team, botAttack);
                        }
                        break;
                    case DEFENSE:
                        if (bot.team != Cell.Team.NEUTRAL) {
                            float angle1 = (((Cell) targetBot).body.getPosition()).sub(bot.body.getPosition()).angle();
                            float angle2 = angle1;
                            stage.addActor(new Attack(bot, (Cell) targetBot, OldAttribute.AttributeType.DEFENSE, angle1));
                            for(int i = 0; i < 2; i++){

                                angle1 += 30;
                                stage.addActor(new Attack(bot, (Cell) targetBot, OldAttribute.AttributeType.DEFENSE, angle1));
                                angle2 -= 30;
                                stage.addActor(new Attack(bot, (Cell) targetBot, OldAttribute.AttributeType.DEFENSE, angle2));

                            }
                            actions.AddAction(bot.team, botAttack);
                            bot.actualEnergy = bot.actualEnergy * 0.8f;
                        }
                        break;
                }

                if(timeAttack < MainGame.timeAttack * (player/bots)){
                    timeAttack++;
                }else{
                    if(bot.team != Cell.Team.NEUTRAL){
                        stage.addActor(new Attack(bot, (Cell) targetBot, OldAttribute.AttributeType.ATTACK, random(0, 360)));
                        timeAttack = 0;
                    }
                }
            }
        }
    }

    private int botIndex(Cell.Team team){
        switch (team){
            case BOT1: return 0;
            case BOT2: return 1;
            case BOT3: return 2;
            case BOT4: return 3;
            case BOT5: return 4;
            case NEUTRAL: return 5;
        }

        return 0;
    }
}
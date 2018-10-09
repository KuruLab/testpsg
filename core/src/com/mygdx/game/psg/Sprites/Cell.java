package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Screens.PlayScreen;
import static com.mygdx.game.psg.Screens.PlayScreen.*;

public class Cell extends Actor {



    public Team team;
    public Body body;

    private Vector2 angle, move, bodyPosition, inputPosition, gamePosition, velocity;

    public float baseRadius = 80, baseRegeneration = 0.1f;
    private float baseMove = 1;

    public float maxEnergy, actualEnergy, radiusEnergy;

    public boolean selected, target, moving;

    public Cell(World world, float x, float y, int ID, Team team) {
        this.angle = new Vector2();
        this.move = new Vector2();
        this.bodyPosition = new Vector2();
        this.inputPosition = new Vector2();
        this.gamePosition = new Vector2();
        this.velocity = new Vector2(0,0);
        setZIndex(ID);



        this.team = team;
        setColor();
        maxEnergy = 200;
        actualEnergy = maxEnergy*0.25f;
        setX(x);
        setY(y);
        setCell(world);
    }

    private void setCell(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() / MainGame.PPM, getY() / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(baseRadius / MainGame.PPM);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.6f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        move.set(body.getPosition());
        body.setLinearVelocity(velocity.setToRandomDirection());
    }

    @Override
    public void act(float delta) {
        this.radiusEnergy = RadiusEnergy(actualEnergy);

        DelimiterBorder();
        SelectOrTarget();
        MoveOrAttack();
        //Regeneration();
        setColor();

        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    private boolean isTouched() {
        return (Gdx.input.justTouched() &&
                BodyPosition(bodyPosition).dst(InputPosition(inputPosition)) <
                        baseRadius * sizeViewport.x / MainGame.V_Width) ||
                (BodyPosition(bodyPosition).dst(InputPosition(inputPosition)) <
                        baseRadius * sizeViewport.y / MainGame.V_Height);
    }

    private Vector2 BodyPosition(Vector2 bodyPosition) {

        bodyPosition.set(body.getPosition().x * MainGame.PPM * sizeViewport.x / MainGame.V_Width
                        + sizeViewport.x / 2
                        - positionCamera.x * sizeViewport.x / MainGame.V_Width,
                body.getPosition().y * MainGame.PPM * sizeViewport.y / MainGame.V_Height
                        + sizeViewport.y / 2
                        - positionCamera.y * sizeViewport.y / MainGame.V_Height);

        return bodyPosition;
    }

    private Vector2 InputPosition(Vector2 inputPosition) {

        inputPosition.set(Gdx.input.getX(), sizeViewport.y - Gdx.input.getY());

        return inputPosition;
    }

    private void DelimiterBorder() {

        if (body.getPosition().x * MainGame.PPM - baseRadius < (-1) * MainGame.V_Width) {
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius > MainGame.V_Width + (MainGame.V_Width)) {

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius < (-1) * MainGame.V_Height / 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius > MainGame.V_Height + (MainGame.V_Height / 2)) {
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);
        }
    }

    private void SelectOrTarget() {

            if (Gdx.input.justTouched() && isTouched() && selected == PlayScreen.oneSelected) {
                body.setLinearVelocity(0, 0);
                PlayScreen.oneSelected = !PlayScreen.oneSelected;
                PlayScreen.selectedID = getZIndex();
                PlayScreen.selectedTeam = team;
                PlayScreen.positionSelected = body.getPosition();
                PlayScreen.selectedRadius = baseRadius;
                PlayScreen.selectedColor = getColor();
                selected = !selected;
                moving = false;
            } else {

                if (Gdx.input.justTouched() && isTouched() && PlayScreen.oneSelected && !selected) {
                    PlayScreen.positionTarget = body.getPosition();
                    PlayScreen.targetID = getZIndex();
                    PlayScreen.oneTarget = true;
                    target = true;
                }
            }

        if (selected && PlayScreen.oneSelected || target && PlayScreen.oneTarget) {
            PlayScreen.positionSelected = body.getPosition();
            PlayScreen.positionTarget = body.getPosition();
        }
    }

    private void MoveOrAttack() {
        if(moving) {
            if (Gdx.input.justTouched() && selected) {

                move.set(Gdx.input.getX(), Gdx.input.getY());
                angle.set(InputPosition(inputPosition).sub(bodyPosition));
                velocity.set(baseMove, baseMove).setAngle(angle.angle());
                body.setLinearVelocity(velocity);
                PlayScreen.attackDirection = velocity;

                if(InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) > baseRadius * sizeViewport.x/MainGame.V_Width
                            && InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) < (baseRadius + 100) *sizeViewport.x/MainGame.V_Width){

                    PlayScreen.positionTarget = InputPosition(inputPosition);
                    PlayScreen.oneTarget = true;
                }
            }
        }else{moving = true;}
    }

    private float CircleArea(float radius){

        return radius*radius*3.14f;
    }

    private float RadiusEnergy(float energy){

        return (float)Math.sqrt(energy*3.14f);
    }

    public static void Clear(Cell cell) {
        if(cell.selected) {
            cell.body.setLinearVelocity(0, 0);
        }

        cell.selected = false;
        cell.target = false;
    }

    public static void DamageCell(Actor actorA, Actor actorB){

        if(actorA.getClass() == Cell.class){

            System.out.println(((Cell)actorA).actualEnergy);
            System.out.println(((Attack)actorB).actualEnergy);

            ((Cell)actorA).actualEnergy = ((Cell)actorA).actualEnergy - ((Attack)actorB).actualEnergy*0.5f;


            if(((Cell)actorA).actualEnergy < 0){

                System.out.println(((Cell)actorA).team +" "+ ((Attack)actorB).team);

                ((Cell)actorA).actualEnergy = (-1)*((Cell)actorA).actualEnergy;
                ((Cell)actorA).team = ((Attack)actorB).team;

            }

            ((Attack)actorB).actualEnergy = ((Attack)actorB).actualEnergy - ((Attack)actorB).actualEnergy*0.5f;

            if(((Attack)actorB).actualEnergy <= ((Attack)actorB).baseAttack){
                ((Attack)actorB).remove = true;
            }

        }else{


            ((Cell)actorB).actualEnergy = ((Cell)actorB).actualEnergy - ((Attack)actorA).actualEnergy*0.5f;

            if(((Cell)actorB).actualEnergy < 0){

                ((Cell)actorB).actualEnergy = (-1)*((Cell)actorB).actualEnergy;
                ((Cell)actorB).team = ((Attack)actorA).team;

            }

            ((Attack)actorA).actualEnergy = ((Attack)actorA).actualEnergy - ((Attack)actorA).actualEnergy*0.5f;


            if(((Attack)actorA).actualEnergy < ((Attack)actorA).baseAttack){
                ((Attack)actorA).remove = true;
            }

        }


    }

    private void Regeneration(){
        actualEnergy = actualEnergy + baseRegeneration;
    }

    private void setColor(){

        switch (team){
            case NEUTRAL: setColor(Color.GRAY); break;
            case PLAYER: setColor(Color.BLUE); break;
            case BOT1: setColor(Color.RED); break;
            case BOT2: setColor(Color.YELLOW); break;
            case BOT3: setColor(Color.MAGENTA); break;
            case BOT4: setColor(Color.GREEN); break;
        }
    }
}


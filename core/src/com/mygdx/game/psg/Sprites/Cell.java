package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.Engine.Attribute;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Screens.PlayScreen;

public class Cell extends Actor {

    public enum Team {
        NEUTRAL,
        PLAYER,
        BOT1,
        BOT2,
        BOT3,
        BOT4
    }

    public Team team;
    public Body body;

    private Vector2 angle, move, bodyPosition, inputPosition, velocity;
    public float baseRadius, baseRegeneration, maxEnergy, actualEnergy, radiusEnergy, baseAttack, baseMove;

    public boolean selected, moving;

    public Cell(float x, float y, Team team) {
        angle = new Vector2();
        move = new Vector2();
        bodyPosition = new Vector2();
        inputPosition = new Vector2();
        velocity = new Vector2(0,0);
        Attribute DNA = new Attribute();

        baseRadius = 50 + (DNA.AttributeCount(Attribute.AttributeType.SIZE) * 4.5f);
        baseRegeneration = 0.5f + (DNA.AttributeCount(Attribute.AttributeType.REGEN) * 0.1f);
        baseAttack = 25 + (DNA.AttributeCount(Attribute.AttributeType.ATTACK) * 5.75f);
        baseMove = 1 + (DNA.AttributeCount(Attribute.AttributeType.SPEED) * 0.05f);

        maxEnergy = CircleArea(baseRadius);
        actualEnergy = maxEnergy *(DNA.AttributeCount(Attribute.AttributeType.DEFENSE) * 0.01f);
        radiusEnergy = baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);

        this.team = team;

        setX(x);
        setY(y);
        setColor();
        setCell();
    }

    private void setCell() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() / MainGame.PPM, getY() / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = PlayScreen.world.createBody(bodyDef);

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
        radiusEnergy = baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);
        DelimiterBorder();
        SelectOrTarget();
        MoveOrAttack();
        Regeneration();
        setColor();
    }



    private Vector2 BodyPosition(Vector2 bodyPosition) {

        bodyPosition.set(
                            body.getPosition().x * MainGame.PPM
                            + (MainGame.W_Width/2) * PlayScreen.zoom
                            - PlayScreen.positionCamera.x,

                             body.getPosition().y * MainGame.PPM
                            + (MainGame.W_Height/2) * PlayScreen.zoom
                            - PlayScreen.positionCamera.y);

        return bodyPosition;
    }

    private Vector2 InputPosition(Vector2 inputPosition) {

        inputPosition.set(Gdx.input.getX() * PlayScreen.zoom, (MainGame.W_Height - Gdx.input.getY()) * PlayScreen.zoom);

        return inputPosition;
    }

    private boolean isTouched() {

        return (Gdx.input.justTouched() &&
                (BodyPosition(bodyPosition).dst(InputPosition(inputPosition)) < baseRadius ||
                 BodyPosition(bodyPosition).dst(InputPosition(inputPosition)) < baseRadius));
    }

    private void DelimiterBorder() {

        if (body.getPosition().x * MainGame.PPM - baseRadius < -MainGame.V_Width) {
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius > MainGame.V_Width) {

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius < -MainGame.V_Height) {
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius > MainGame.V_Height) {
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);
        }
    }

    private void SelectOrTarget() {

            if (Gdx.input.justTouched() && isTouched() && selected == PlayScreen.oneSelected) {
                Select();
            } else {

                if (Gdx.input.justTouched() && isTouched() && PlayScreen.oneSelected && !selected) {
                    PlayScreen.oneTarget = true;
                    //PlayScreen.targetCell = this;
                    if(team == Team.PLAYER){PlayScreen.type = Attack.Type.TRANSFER;}
                    if(team == Team.NEUTRAL){PlayScreen.type = Attack.Type.DOMINATE;
                    }else{ PlayScreen.type = Attack.Type.DAMAGED;}
                }
            }
    }

    private void MoveOrAttack() {
        if(moving) {
            if (Gdx.input.justTouched() && selected && team == Team.PLAYER) {
                move.set(Gdx.input.getX(), Gdx.input.getY());
                angle.set(InputPosition(inputPosition).sub(bodyPosition));
                velocity.set(baseMove, baseMove).setAngle(angle.angle());
                body.setLinearVelocity(velocity);
                PlayScreen.attackDirection = velocity;

                if(InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) > baseRadius &&
                        InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) < (baseRadius + PlayScreen.touchRadius*PlayScreen.zoom)){
                    PlayScreen.type = Attack.Type.MIXER;
                    PlayScreen.oneTarget = true;
                }
            }
        }else{moving = true;}
    }

    private static float CircleArea(float radius){

        return radius*radius*3.14f;
    }

    private static float RadiusEnergy(float energy){

        return (float)Math.sqrt(energy*3.14f);
    }

    public static void Clear(Cell cell) {
        if(cell.selected) {
            Stop(cell);
        }
        cell.selected = false;
        PlayScreen.oneSelected = false;
        PlayScreen.oneTarget = false;
    }

    public static void Contact(Actor actorA, Actor actorB){

        if(actorA.getClass() == Cell.class){

            if(((Cell) actorA).team == ((Attack) actorB).team){
                ((Cell)actorA).actualEnergy = ((Cell)actorA).actualEnergy + ((Attack)actorB).actualEnergy*0.5f;
            }else {
                ((Cell) actorA).actualEnergy = ((Cell) actorA).actualEnergy - ((Attack) actorB).actualEnergy;
            }

            if(((Cell)actorA).actualEnergy < 0){
                ((Cell)actorA).actualEnergy = (-1)*((Cell)actorA).actualEnergy;
                ((Cell)actorA).team = ((Attack)actorB).team;
            }

            ((Attack)actorB).actualEnergy = ((Attack)actorB).actualEnergy - ((Attack)actorB).actualEnergy*0.25f;
            ((Attack)actorB).modifyEnergy = true;

            if(((Attack)actorB).actualEnergy <= ((Attack)actorB).baseAttack){
                ((Attack)actorB).remove = true;
            }

        }else{

            if(((Cell)actorB).team == ((Attack)actorA).team){
                ((Cell)actorB).actualEnergy = ((Cell)actorB).actualEnergy + ((Attack)actorA).actualEnergy * 0.5f;
            }else {
                ((Cell) actorB).actualEnergy = ((Cell) actorB).actualEnergy - ((Attack) actorA).actualEnergy;
            }

            if(((Cell)actorB).actualEnergy < 0){
                ((Cell)actorB).actualEnergy = (-1)*((Cell)actorB).actualEnergy;
                ((Cell)actorB).team = ((Attack)actorA).team;
            }

            ((Attack)actorA).actualEnergy = ((Attack)actorA).actualEnergy - ((Attack)actorA).actualEnergy*0.25f;
            ((Attack)actorA).modifyEnergy = true;

            if(((Attack)actorA).actualEnergy < ((Attack)actorA).baseAttack){
                ((Attack)actorA).remove = true;
            }
        }
    }

    private void Regeneration(){
        if(actualEnergy < maxEnergy) {
            actualEnergy = actualEnergy + baseRegeneration;
        }else{actualEnergy = maxEnergy;}
    }

    private void setColor(){

        switch (team){
            case NEUTRAL: setColor(Color.LIGHT_GRAY); break;
            case PLAYER: setColor(Color.ROYAL); break;
            case BOT1: setColor(Color.SCARLET); break;
            case BOT2: setColor(Color.YELLOW); break;
            case BOT3: setColor(Color.PINK); break;
            case BOT4: setColor(Color.GREEN); break;
        }
    }

    private void Select(){
        Stop(this);

        PlayScreen.oneSelected = !PlayScreen.oneSelected;
        PlayScreen.selectedCell = this;
        selected = !selected;

        if(team == Team.PLAYER){
        PlayScreen.selectedCell = this;}else{
            PlayScreen.oneTarget = !PlayScreen.oneTarget;
            PlayScreen.targetCell = this;
        }
    }

    public static void Stop(Cell cell){
        if(cell.team == Team.PLAYER)
        cell.body.setLinearVelocity(0,0);
        cell.moving = false;
    }
}


package com.mygdx.game.psg.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.old_engine.OldAttribute;
import com.mygdx.game.psg.screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.randomBoolean;

public class Cell extends Actor {

    public enum Team {
        NEUTRAL,
        PLAYER,
        BOT1,
        BOT2,
        BOT3,
        BOT4,
        BOT5
    }

    public enum Status{
        LOW,
        MID,
        HI
    }

    public Team team;
    public Body body;
    public OldAttribute DNA;
    public static Status status;

    private Vector2 bodyPosition, inputPosition, velocity;
    private float  baseRegeneration;
    public  float baseRadius, radiusEnergy, baseAttack, baseMove, actualEnergy, maxEnergy;
    private int[] resume = new int[5];

    public Cell(float x, float y, Team team) {

        bodyPosition = new Vector2();
        inputPosition = new Vector2();
        velocity = new Vector2(0,0);
        if(MainGame.load){
            DNA = PlayScreen.oldAttribute;
        }else {
            DNA = new OldAttribute();
        }

        // 0 = size, 1 = attack, 2 = defense, 3 = speed, 4 = regen
        this.resume = DNA.getResume();
        baseRadius = 100 + 5f * resume[0];
        maxEnergy = CircleArea(baseRadius);

        baseRegeneration = 5f + resume[4] * 0.05f;
        baseAttack = CircleArea(10) + resume[1] * CircleArea(5);
        baseMove = 1f + resume[4] * 0.25f;

        actualEnergy = maxEnergy * resume[2] * 0.01f;
        radiusEnergy = baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);

        this.team = team;
        this.status = Status(this);
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

        if(team != Team.PLAYER) {
            body.setLinearVelocity(velocity.setToRandomDirection());
        }
    }

    @Override
    public void act(float delta) {
        radiusEnergy = baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);

        if(PlayScreen.oneSelected && PlayScreen.selectedCell.team != Cell.Team.PLAYER){
            PlayScreen.oneSelected = false;
        }


        if(PlayScreen.oneTarget && PlayScreen.oneSelected) {

        }else{

            if(PlayScreen.oneTarget && PlayScreen.targetCell.team == Cell.Team.PLAYER){
                PlayScreen.oneTarget = false;
            }

            if (PlayScreen.restartCount == 0) {
                DelimiterBorder();
                MoveOrAttack();
                SelectOrTarget();
                Regeneration();
            } else {
                if (team == Team.PLAYER) {
                    DelimiterBorder();
                }
            }
        }

        if(PlayScreen.restartCount ==1 && team != Team.PLAYER){
            velocity.set(baseMove, baseMove);
            body.setLinearVelocity(velocity.setToRandomDirection());
        }

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
            if(body.getLinearVelocity().x < 0){
            body.setLinearVelocity((-1)*body.getLinearVelocity().x, body.getLinearVelocity().y);
            }
            body.setLinearVelocity(body.getLinearVelocity().x + baseMove*0.01f, body.getLinearVelocity().y);
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius > MainGame.V_Width) {

            if(body.getLinearVelocity().x > 0){
               body.setLinearVelocity((-1)*body.getLinearVelocity().x, body.getLinearVelocity().y);
            }
            body.setLinearVelocity(body.getLinearVelocity().x - baseMove*0.01f, body.getLinearVelocity().y);
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius < -MainGame.V_Height) {
            if(body.getLinearVelocity().y < 0){
                body.setLinearVelocity(body.getLinearVelocity().x, (-1)*body.getLinearVelocity().y);
            }
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y + baseMove*0.01f);
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius > MainGame.V_Height) {
            if(body.getLinearVelocity().y > 0){
                body.setLinearVelocity(body.getLinearVelocity().x, (-1)*body.getLinearVelocity().y);
            }
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y - baseMove*0.01f);
        }
    }

    private void SelectOrTarget() {
        if (Gdx.input.justTouched() && isTouched()){
            Select();
            if(PlayScreen.oneSelected && PlayScreen.oneTarget && !PlayScreen.oneFire) {

                PlayScreen.attackDirection = PlayScreen.targetCell.body.getPosition().sub(PlayScreen.selectedCell.body.getPosition()).angle();

                if (PlayScreen.targetCell.team == Team.PLAYER) {

                    PlayScreen.typeAttack = OldAttribute.AttributeType.REGEN;

                } else {

                    if (PlayScreen.targetCell.team == Team.NEUTRAL) {

                        PlayScreen.typeAttack = OldAttribute.AttributeType.SPEED;

                    } else {

                        PlayScreen.typeAttack = OldAttribute.AttributeType.ATTACK;

                    }
                }
            }
        }
    }

    private void MoveOrAttack() {

        if(Gdx.input.justTouched() && PlayScreen.oneSelected && PlayScreen.selectedCell == this && team == Team.PLAYER) {
            velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle());
            body.setLinearVelocity(velocity);
            PlayScreen.move = true;

            if (InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) > baseRadius &&
                    InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) < (baseRadius + PlayScreen.touchRadius * PlayScreen.zoom)) {
                PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();

                PlayScreen.typeAttack = OldAttribute.AttributeType.DEFENSE;
                PlayScreen.oneFire = true;
            }
        }
    }

    private static float CircleArea(float radius){

        return radius*radius*3.14f;
    }

    private static float RadiusEnergy(float energy){

        return (float)Math.sqrt(energy*3.14f);
    }

    public static void Clear(Cell cell) {
        if(cell.team == Team.PLAYER) {
            Stop(cell);
            PlayScreen.oneFire = false;
            PlayScreen.oneSelected = false;
            PlayScreen.oneTarget = false;
        }
    }

    private void Regeneration(){
        actualEnergy += baseRegeneration;
        if(actualEnergy > maxEnergy){
            actualEnergy = maxEnergy;
        }
    }

    private void setColor(){
        switch (team){
            case PLAYER: setColor(MainGame.colors.get(0)); break;
            case BOT1: setColor(MainGame.colors.get(1)); break;
            case BOT2: setColor(MainGame.colors.get(2)); break;
            case BOT3: setColor(MainGame.colors.get(3)); break;
            case BOT4: setColor(MainGame.colors.get(4)); break;
            case BOT5: setColor(MainGame.colors.get(5)); break;
            case NEUTRAL: setColor(MainGame.colors.get(6)); break;
        }
    }

    private void Select() {
        if(!PlayScreen.oneSelected && !PlayScreen.oneTarget){
            if(team == Team.PLAYER) {
                PlayScreen.selectedCell = this;
                PlayScreen.oneSelected = true;
                Stop(this);
            }else {
                PlayScreen.oneTarget = true;
                PlayScreen.targetCell = this;
            }
        }else{

        if (PlayScreen.oneSelected && !PlayScreen.oneTarget) {
                if(PlayScreen.selectedCell == this){
                    PlayScreen.oneSelected = false;
                }else{
                    PlayScreen.oneTarget = true;
                    PlayScreen.targetCell = this;
                }
            } else {

                if (!PlayScreen.oneSelected && PlayScreen.oneTarget) {
                    if (PlayScreen.targetCell == this) {
                        PlayScreen.oneTarget = false;
                    } else {
                        if (team == Team.PLAYER) {
                            PlayScreen.selectedCell = this;
                            PlayScreen.oneSelected = true;
                            }
                        else {
                            PlayScreen.targetCell = this;
                            PlayScreen.oneTarget = true;
                        }
                    }
                }
            }
        }
    }

    public static void Stop(Cell cell){
        if(cell.team == Team.PLAYER)
        cell.body.setLinearVelocity(0,0);
    }

    public static Status Status(Cell cell){

        if(cell.actualEnergy < cell.maxEnergy * 0.01f){
            return Status.LOW;
        }

        if(cell.actualEnergy > cell.maxEnergy * 0.8f){
            return Status.HI;
        }

        return Status.MID;
    }

    public void Avoid(Attack attack){
        velocity.set(baseMove, baseMove);

        if(randomBoolean()) {

            body.setLinearVelocity(velocity.setAngle(body.getPosition().sub(attack.body.getPosition()).angle() + 90));

        }else{

            body.setLinearVelocity(velocity.setAngle(body.getPosition().sub(attack.body.getPosition()).angle() - 90));

        }
    }
}


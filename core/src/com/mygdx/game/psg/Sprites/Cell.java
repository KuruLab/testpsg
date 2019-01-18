package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.Engine.Actions;
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
    public Attribute DNA;

    private Vector2 bodyPosition, inputPosition, velocity;
    private float  baseRegeneration;
    public  float baseRadius, radiusEnergy, baseAttack, baseMove, actualEnergy, maxEnergy;

    int size, attack, defense, speed, regen;

    private boolean moving;

    public Cell(float x, float y, Team team) {

        bodyPosition = new Vector2();
        inputPosition = new Vector2();
        velocity = new Vector2(0,0);
        if(MainGame.load){
            DNA = PlayScreen.attribute;
        }else {
            DNA = new Attribute();
        }

        size = DNA.AttributeCount(Attribute.AttributeType.SIZE);
        attack = DNA.AttributeCount(Attribute.AttributeType.ATTACK);
        defense = DNA.AttributeCount(Attribute.AttributeType.DEFENSE);
        speed = DNA.AttributeCount(Attribute.AttributeType.SPEED);
        regen = DNA.AttributeCount(Attribute.AttributeType.REGEN);

        baseRadius = 100 + 5f * size;
        maxEnergy = CircleArea(baseRadius);

        baseRegeneration = 5f + regen * 0.5f;
        baseAttack = CircleArea(10) + attack * CircleArea(1);
        baseMove = 1f + speed * 0.1f;

        actualEnergy = maxEnergy * defense * 0.01f;
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

        if(team != Team.PLAYER) {
            body.setLinearVelocity(velocity.setToRandomDirection());
        }
    }

    @Override
    public void act(float delta) {
        radiusEnergy = baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);

        if(PlayScreen.oneSelected && PlayScreen.selectedCell.team != Cell.Team.PLAYER){
            PlayScreen.oneTarget = true;
            PlayScreen.oneSelected = false;
            PlayScreen.targetCell = PlayScreen.selectedCell;
        }


        if(PlayScreen.oneTarget && PlayScreen.oneSelected) {

        }else{

            if(PlayScreen.oneTarget && PlayScreen.targetCell.team == Cell.Team.PLAYER){
                PlayScreen.oneTarget = false;
                PlayScreen.oneSelected = true;
                PlayScreen.selectedCell = PlayScreen.targetCell;
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
            moving = false;
            if(PlayScreen.oneSelected && PlayScreen.selectedCell != this) {
                if (team == Team.PLAYER) {
                    PlayScreen.typeAttack = Attribute.AttributeType.REGEN;
                }else {
                    PlayScreen.typeAttack = Attribute.AttributeType.ATTACK;
                }
            }
        }
    }

    private void MoveOrAttack() {
        if(PlayScreen.oneSelected && PlayScreen.selectedCell == this) {
            if (moving) {
                if (Gdx.input.justTouched()) {
                    if (team == Team.PLAYER) {
                        velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle());
                        PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();
                        body.setLinearVelocity(velocity);
                        PlayScreen.oneMove = true;
                        moving = false;

                    }

                    if (InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) > baseRadius &&
                            InputPosition(inputPosition).dst(BodyPosition(bodyPosition)) < (baseRadius + PlayScreen.touchRadius * PlayScreen.zoom)) {
                        velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle());
                        PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();
                        PlayScreen.typeAttack = Attribute.AttributeType.DEFENSE;
                        PlayScreen.oneTarget = true;
                        PlayScreen.targetCell = this;
                        moving = false;
                    }
                }
            } else {
                moving = true;
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
        }

        switch (PlayScreen.typeAttack){
            case REGEN: PlayScreen.oneSelected = true;
                PlayScreen.selectedCell = PlayScreen.targetCell;
                PlayScreen.oneTarget = false; break;
            case DEFENSE: PlayScreen.oneSelected = false;
                PlayScreen.oneTarget = false; break;
            case ATTACK: PlayScreen.oneSelected = true;
                PlayScreen.oneTarget = false; break;
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
                    velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle()-180);
                    PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();
                }
            } else {

                if (!PlayScreen.oneSelected && PlayScreen.oneTarget) {
                    if (PlayScreen.targetCell == this) {
                        PlayScreen.oneTarget = false;
                    } else {
                        if (team == Team.PLAYER) {
                            velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle());
                            PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();
                            PlayScreen.selectedCell = this;
                            PlayScreen.oneSelected = true;
                            }
                        else {
                            velocity.set(baseMove, baseMove).setAngle(InputPosition(inputPosition).sub(bodyPosition).angle() - 180);
                            PlayScreen.attackDirection = InputPosition(inputPosition).sub(bodyPosition).angle();
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
        cell.moving = false;
    }

    public static Status Status(Cell cell){

        if(cell.actualEnergy < cell.maxEnergy * 0.3f){
            return Status.LOW;
        }

        if(cell.actualEnergy > cell.maxEnergy * 0.7f){
            return Status.HI;
        }

        return Status.MID;
    }
}


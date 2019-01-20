package com.mygdx.game.psg.Sprites;

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

public class Attack extends Actor {


    public Cell.Team team;
    public Body body;
    private Vector2 velocity = new Vector2(0,0);
    public Attribute.AttributeType type;

    public float baseAttack,maxEnergy, actualEnergy, energyRadius, baseRadius, baseMove;
    public boolean remove, modifyEnergy;

    private  int resize;
    public int inativity;

    private CircleShape circleShape = new CircleShape();
    private FixtureDef fixtureDef = new FixtureDef();

    public Attack(Cell cell, Cell target, Attribute.AttributeType type, float angle){
        baseMove = cell.baseMove*2;
        baseAttack = cell.baseAttack;
        baseRadius = cell.baseRadius;
        maxEnergy = cell.maxEnergy;
        setColor(cell.getColor());
        this.team = cell.team;
        this.type = type;


        switch (type){
            case SIZE:
                actualEnergy = 4f * cell.baseAttack + cell.maxEnergy/20;
                energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);break;
            case REGEN:
                cell.actualEnergy = cell.actualEnergy * 0.4f;
                actualEnergy = 2f * baseAttack + cell.actualEnergy * 0.2f;
                energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy); break;
            case SPEED:
                cell.actualEnergy = cell.actualEnergy * 0.5f;
                actualEnergy = 3f * baseAttack + cell.actualEnergy * 0.6f;
                energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);break;
            case ATTACK:
                cell.actualEnergy = cell.actualEnergy * 0.6f;
                actualEnergy = 5f * baseAttack + cell.actualEnergy * 0.5f;
                energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy); break;
            case DEFENSE:
                actualEnergy = 3 * baseAttack;
                cell.actualEnergy = cell.actualEnergy * 0.7f;
                energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);break;
        }

        setAttack(cell, angle);
    }

    private void setAttack(Cell cell, float angle){

        switch (type){
            case SIZE: velocity.set(cell.baseRadius + energyRadius,cell.baseRadius + energyRadius).setAngle(angle);break;
            case DEFENSE: velocity.set(cell.baseRadius + energyRadius,1).setAngle(angle);break;
            case SPEED: velocity.set(cell.baseRadius + energyRadius,1).setAngle(angle);break;
            case ATTACK: velocity.set(cell.baseRadius + energyRadius,1).setAngle(angle);break;
            case REGEN: velocity.set(cell.baseRadius + energyRadius, energyRadius).setAngle(angle);break;


        }

        SetBody(cell, velocity.x, velocity.y, angle);
    }

    @Override
    public void act(float delta) {

        if(PlayScreen.restartCount == 0) {
            DelimiterBorder();
            RefactorEnergy(delta);
        }

        RefactorEnergy(delta);
        RefactorFixture();
    }

    private float RadiusEnergy(float energy){

        return (float)Math.sqrt(energy*(float)Math.PI);
    }

    private void DelimiterBorder() {

        if (body.getPosition().x * MainGame.PPM - baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) < -MainGame.V_Width) {
            if(body.getLinearVelocity().x < 0){
                body.setLinearVelocity((-1)*body.getLinearVelocity().x, body.getLinearVelocity().y);
            }
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) > MainGame.V_Width) {
            if(body.getLinearVelocity().x > 0){
                body.setLinearVelocity((-1)*body.getLinearVelocity().x, body.getLinearVelocity().y);
            }
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) < -MainGame.V_Height) {
            if(body.getLinearVelocity().y < 0){
                body.setLinearVelocity(body.getLinearVelocity().x, (-1)*body.getLinearVelocity().y);
            }
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) > MainGame.V_Height) {
            if(body.getLinearVelocity().y > 0){
                body.setLinearVelocity(body.getLinearVelocity().x, (-1)*body.getLinearVelocity().y);
            }

        }
    }

    private FixtureDef SetFixtureDef(){

        circleShape.setRadius((baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy)) /MainGame.PPM);
        fixtureDef.shape = circleShape;

        switch (type){
            case REGEN:
                fixtureDef.isSensor = true;
            case ATTACK:
                fixtureDef.density = 5f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.5f;break;
            case SPEED:
                fixtureDef.density = 0.01f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.5f;break;
            case DEFENSE:
                fixtureDef.density = 10000000f;
                fixtureDef.friction = 0.000001f;
                fixtureDef.restitution = 0.0000001f;break;
            case SIZE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 1f;
                fixtureDef.restitution = 1f;break;
        }

        return fixtureDef;
    }

    private  void SetBody(Cell cell, float x, float y, float angle){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(cell.body.getPosition().x + x/MainGame.PPM,
                cell.body.getPosition().y + y/MainGame.PPM);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = PlayScreen.world.createBody(bodyDef);


        body.createFixture(SetFixtureDef());
        body.setBullet(true);

        switch (type){
            case SIZE:
                velocity.set(baseMove*4, baseMove*4).setAngle(angle);
                body.setLinearVelocity(velocity);break;
            case DEFENSE:
                velocity.set(baseMove/4, baseMove/4).setAngle(angle);
                body.setLinearVelocity(velocity);break;
            case SPEED:
                velocity.set(baseMove*2, baseMove*2).setAngle(angle);
                body.setLinearVelocity(velocity);break;
            case ATTACK:
                velocity.set(baseMove, baseMove).setAngle(angle);
                body.setLinearVelocity(velocity);break;
            case REGEN:
                velocity.set(baseMove/2, baseMove/2).setAngle(angle);
                body.setLinearVelocity(velocity);break;
        }
    }

    private void RefactorFixture(){
        if(resize > 15 || modifyEnergy) {

            body.destroyFixture(body.getFixtureList().pop());
            body.createFixture(SetFixtureDef());

            energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);
            modifyEnergy = false;
            resize = 0;
        }
    }

    private void RefactorEnergy(float delta){

        inativity++;
        resize++;

        switch (type){
            case SPEED:
                actualEnergy = actualEnergy + 20f * baseMove * (body.getLinearVelocity().x + body.getLinearVelocity().x) * delta ; break;
            case ATTACK:
                if (energyRadius > 70) {
                    actualEnergy = actualEnergy + 20 * PlayScreen.numberAttack * delta; } break;
            case REGEN:
                    actualEnergy = actualEnergy + 0.01f * maxEnergy * delta; break;
            case DEFENSE:
                if (inativity > 60) {
                    actualEnergy = actualEnergy - 0.001f * maxEnergy * delta; } break;
            case SIZE:
                actualEnergy = actualEnergy - 20 * PlayScreen.numberAttack * delta; break;
        }



        if (inativity > 180) {
            actualEnergy = actualEnergy - 10*PlayScreen.numberAttack*delta;
        }

        if (inativity > 360) {
            actualEnergy = actualEnergy - 10*PlayScreen.numberAttack*delta;
        }

        if (energyRadius > 35) {
            actualEnergy = actualEnergy - 10*PlayScreen.numberAttack*delta;
        }

        if (energyRadius > 70) {
            actualEnergy = actualEnergy - 10*PlayScreen.numberAttack*delta;
        }

        if(actualEnergy < PlayScreen.numberAttack*delta){
            actualEnergy = 1;
            remove = true;
        }
    }

}



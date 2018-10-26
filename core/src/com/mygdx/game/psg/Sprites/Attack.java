package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Screens.PlayScreen;

public class Attack extends Actor {

    public enum Type{
        DAMAGED,
        TRANSFER,
        MIXER,
        DOMINATE,
        EXPLOSION
    }

    public Cell.Team team;
    public Body body;
    private Vector2 velocity = new Vector2(0,0);
    public Type type;

    public float baseAttack,maxEnergy, actualEnergy, energyRadius, baseRadius, baseMove;
    public boolean remove, modifyEnergy;

    private  int resize;
    public int inativity;

    private CircleShape circleShape = new CircleShape();
    private FixtureDef fixtureDef = new FixtureDef();

    public Attack(Cell cell, Cell target, Type type, float angle){
        baseMove = cell.baseMove*2;
        baseAttack = cell.baseAttack;
        baseRadius = cell.baseRadius;
        maxEnergy = cell.maxEnergy;
        setColor(cell.getColor());
        this.team = cell.team;
        this.type = type;

        if(type != Type.EXPLOSION) {

            actualEnergy = baseAttack + cell.actualEnergy * 0.5f;
            energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);
            cell.actualEnergy = cell.actualEnergy * 0.5f;

        }else{
            actualEnergy = 5f*cell.baseAttack + cell.maxEnergy/20;
            energyRadius = baseRadius * RadiusEnergy(actualEnergy) / RadiusEnergy(maxEnergy);
        }
        setAttack(cell, angle);
    }

    private void setAttack(Cell cell, float angle){

        if(type == Type.EXPLOSION){
            velocity.set(cell.baseRadius + energyRadius,cell.baseRadius + energyRadius).setAngle(angle);
        }else{
            velocity.set(cell.baseRadius + energyRadius,1).setAngle(angle);
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

        fixtureDef.density = 0.15f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.1f;

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

        if(type == Type.EXPLOSION){
            velocity.set(baseMove*2, baseMove*3).setAngle(angle);
            body.setLinearVelocity(velocity);
        }else {
            velocity.set(baseMove, baseMove).setAngle(angle);
            body.setLinearVelocity(velocity);
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

        if (type == Type.EXPLOSION) {
            actualEnergy = actualEnergy - 10 * PlayScreen.numberAttack * delta;
        }

        if (inativity > 180) {
            actualEnergy = actualEnergy - 20*PlayScreen.numberAttack*delta;
        }

        if (energyRadius > 35) {
            actualEnergy = actualEnergy - 30*PlayScreen.numberAttack*delta;
        }

        if (energyRadius > 70) {
            actualEnergy = actualEnergy - 40*PlayScreen.numberAttack*delta;
        }

        if(actualEnergy < baseAttack){
            remove = true;
        }
    }

}



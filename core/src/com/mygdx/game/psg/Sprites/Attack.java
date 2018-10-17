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
        DOMINATE
    }

    public Cell.Team team;
    public Body body;
    private Vector2 velocity = new Vector2(0,0);

    public float baseAttack,maxEnergy, actualEnergy, energyRadius, baseRadius, baseMove;
    public boolean remove, modifyEnergy;

    private  int resize;

    private CircleShape circleShape = new CircleShape();
    private FixtureDef fixtureDef = new FixtureDef();

    public Attack(Cell cell, Cell target, Type type){
        baseMove = cell.baseMove*2;
        baseAttack = cell.baseAttack;
        baseRadius = cell.baseRadius;
        maxEnergy = cell.maxEnergy;
        actualEnergy = baseAttack + cell.actualEnergy*0.3f;
        energyRadius = baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);

        setColor(cell.getColor());
        setAttack(cell);

        cell.actualEnergy = cell.actualEnergy*0.7f;
    }

    private void setAttack(Cell selected){

        velocity.set(selected.baseRadius + energyRadius,1).setAngle(PlayScreen.attackDirection.angle());
        SetBody(selected, velocity.x, velocity.y);

        this.team = selected.team;
    }

    @Override
    public void act(float delta) {

        DelimiterBorder();

        if(modifyEnergy || resize > 60){ RefactorFixture();}

        actualEnergy = actualEnergy - 0.1f;
        resize++;

        if(actualEnergy < baseAttack){
            remove = true;
        }

    }

    private float RadiusEnergy(float energy){

        return (float)Math.sqrt(energy*(float)Math.PI);
    }

    private void DelimiterBorder() {

        if (body.getPosition().x * MainGame.PPM - baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) < -MainGame.V_Width) {
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) > MainGame.V_Width) {

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) < -MainGame.V_Height) {
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy) > MainGame.V_Height) {
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);

        }
    }

    private FixtureDef SetFixtureDef(){

        circleShape.setRadius((baseRadius * RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy)) /MainGame.PPM);
        fixtureDef.shape = circleShape;

        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.1f;

        return fixtureDef;
    }

    private  void SetBody(Cell cell, float x, float y){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(cell.body.getPosition().x + x/MainGame.PPM,
                cell.body.getPosition().y + y/MainGame.PPM);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = PlayScreen.world.createBody(bodyDef);


        body.createFixture(SetFixtureDef());
        body.setBullet(true);

        velocity.set(baseMove,baseMove).setAngle(PlayScreen.attackDirection.angle());
        body.setLinearVelocity(velocity);
    }

    private void RefactorFixture(){
        body.destroyFixture(body.getFixtureList().pop());
        body.createFixture(SetFixtureDef());

        energyRadius = baseRadius*RadiusEnergy(actualEnergy)/RadiusEnergy(maxEnergy);
        modifyEnergy = false;
        resize = 0;
    }

}



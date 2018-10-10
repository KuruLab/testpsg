package com.mygdx.game.psg.Sprites;

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



public class Attack extends Actor {
    public PlayScreen.Team team;

    public  Body body;
    private Vector2 velocity;

    public boolean refactorBody, damaged;

    public float maxEnergy, actualEnergy, radiusEnergy;

    public float baseRadius = 25;
    private  float baseMove = 2;
    public  float baseAttack;

    public boolean remove;

    private CircleShape circleShape = new CircleShape();
    private FixtureDef fixtureDef = new FixtureDef();

    public Attack(float x, float y, int ID, float radius,  Color color, PlayScreen.Team team){
        actualEnergy = 100;
        maxEnergy = 100;
        baseAttack = 10;
        velocity = new Vector2();
        velocity.set(0,0);
        setZIndex(ID);
        setColor(color);
        setX(x);
        setY(y);
        setAttack(team, radius);

    }

    private void setAttack(PlayScreen.Team team, float radius){

        velocity.set(radius + baseRadius,1).setAngle(PlayScreen.attackDirection.angle());
        SetBody(velocity.x, velocity.y);

        this.team = team;
    }

    @Override
    public void act(float delta) {


        DelimiterBorder();
/*
        if(refactorBody) {
            radiusEnergy = baseRadius * actualEnergy / maxEnergy;
            RefactorBody();
        }

        if(damaged){
            refactorBody = true;
        }
*/
        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    private  float circleArea(float radius){

        return radius*radius*(float)Math.PI;
    }

    private float radiusEnergy(float energy){

        return (float)Math.sqrt(energy*(float)Math.PI);
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

    private FixtureDef SetFixtureDef(){

        circleShape.setRadius((baseRadius * actualEnergy/maxEnergy) /MainGame.PPM);
        fixtureDef.shape = circleShape;

        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.1f;

        return fixtureDef;
    }

    private  void SetBody(float x, float y){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set((getX()+x)/MainGame.PPM,
                (getY()+y)/MainGame.PPM);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = PlayScreen.world.createBody(bodyDef);


        body.createFixture(SetFixtureDef());
        body.setBullet(true);

        velocity.set(baseMove,baseMove).setAngle(PlayScreen.attackDirection.angle());
        body.setLinearVelocity(velocity);
    }

    private void RefactorBody(){
        velocity.set(body.getLinearVelocity());

        PlayScreen.world.destroyBody(this.body);
        SetBody(0,0);

        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(getX(), getY());

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = PlayScreen.world.createBody(bodyDef);


        body.createFixture(SetFixtureDef());
        body.setBullet(true);

        body.setLinearVelocity(velocity);

        damaged = false;
        refactorBody = false;
    }

}



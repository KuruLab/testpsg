package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;

public class Attack extends Actor {

    public enum Team {
        NEUTRAL,
        PLAYER,
        BOT1,
        BOT2,
        BOT3,
        BOT4;
    }

    public Team team;

    public Body body;

    private Vector2 temp;
    private Vector2 move, bodyPosition, gamePosition, velocity;

    public float baseRadius = 50;
    private float baseMove = 10;


    public Attack(World world, int targetID, float x, float y, int ID){

        this.temp = new Vector2();
        this.move = new Vector2();
        this.bodyPosition = new Vector2();
        this.gamePosition = new Vector2();
        this.velocity = new Vector2(0,0);
        this.setZIndex(ID);

        setX(x);
        setY(y);
        setAttack(world, team);
    }

    private void setAttack(World world, Team team){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX()/ MainGame.PPM, getY()/MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(baseRadius /MainGame.PPM);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
      //  body.setBullet(true);

        move.set(body.getPosition());
        this.team = team;
    }



    @Override
    public void act(float delta) {

        DelimiterBorder();

    }

    private void DelimiterBorder() {

        if (body.getPosition().x * MainGame.PPM - baseRadius < (-1) * MainGame.V_Width / 2) {
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().x * MainGame.PPM + baseRadius > MainGame.V_Width + (MainGame.V_Width / 2)) {

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if (body.getPosition().y * MainGame.PPM - baseRadius < (-1) * MainGame.V_Height / 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if (body.getPosition().y * MainGame.PPM + baseRadius > MainGame.V_Height + (MainGame.V_Height / 2)) {
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);

        }
    }
}

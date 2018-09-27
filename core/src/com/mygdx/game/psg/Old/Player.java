package com.mygdx.game.psg.Old;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

    private Texture cell;
    private World world;
    private Body body;
    private BodyDef bodydef;
    private Fixture fixture;
    private CircleShape circleshape;
    private int radius = 100;
    public boolean select, sleep;



    public Player(Texture cell, World world) {

        super();
        this.cell = cell;
        this.world = world;

        bodydef = new BodyDef();
        //this.bodydef.position.set(position);
        bodydef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodydef);

        circleshape = new CircleShape();
        circleshape.setRadius(radius);
        fixture = body.createFixture(circleshape, 10);

        circleshape.dispose();
    }

    @Override
    public void act(float delta) {

        if(Touch()==true){

            Select();
        }


        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        if(select == true){batch.setColor(1,0,1,1);}
        if(select == false){batch.setColor(0,0,1,1);}

        setPosition(body.getPosition().x , body.getPosition().y);


        batch.draw(cell,body.getPosition().x, body.getPosition().y, radius*2, radius*2);


        System.out.println(body.getPosition().x+" "+body.getPosition().y);
        System.out.println(getX()+" "+getY());
    }


    private int PositionY(){

        return -(Gdx.input.getY() - Gdx.graphics.getHeight());

    }

    private int PositionX(){

        return Gdx.input.getX();

    }

    private int CenterX(){

        return (int) (this.getX()+this.radius);
    }

    private int CenterY(){

        return (int) (this.getX()+this.radius);
    }

    public boolean Touch(){

        if(Math.sqrt(Math.pow(CenterX() - PositionX(), 2)+ Math.pow(CenterY() - PositionY(),2))<radius){
            return true;
        }
        return false;
    }

    public void Select(){

        this.select = !this.select;

    }

}

package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Screens.PlayScreen;


public class Cell extends Actor{

    public Body body;
    private World world;

    private Vector2 temp;
    private Vector2 move, bodyposition, inputposition, gameposition, velocity;

    public int radius = 300;

    private int baseMove = 5;
    public boolean selected = false;

    public Cell(World world, float x, float y){
        this.world = world;
        this.temp = new Vector2();
        this.move = new Vector2();
        this.bodyposition = new Vector2();
        this.inputposition = new Vector2();
        this.gameposition = new Vector2();
        this.velocity = new Vector2(0,0);

       // this.actionCell = new ActionCell();

        setX(x);
        setY(y);
        setCell();
    }

    @Override
    public void act(float delta) {

        if(Gdx.input.justTouched() && isTouched() && selected == PlayScreen.oneSelected) {
            body.setLinearVelocity(0, 0);
            PlayScreen.oneSelected = !PlayScreen.oneSelected;
            selected = !selected;
            }else{
                if(Gdx.input.justTouched() && selected){
                  move.set(gameposition);
                  temp.set(inputPosition(inputposition).sub(bodyposition));
                    velocity.set(baseMove, baseMove).setAngle(temp.angle());
                    body.setLinearVelocity(velocity);
                }
        }

        DelimiterBorder();

    }

    private void setCell(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX()/MainGame.PPM, getY()/MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius/MainGame.PPM);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.3f;
        body.createFixture(fixtureDef);

        move.set(body.getPosition());

    }

    private boolean isTouched(){

        return (Gdx.input.justTouched() &&
                BodyPosition(bodyposition).dst(inputPosition(inputposition)) <
                        radius * PlayScreen.sizeViewport.x / MainGame.V_Width) ||
                (BodyPosition(bodyposition).dst(inputPosition(inputposition)) <
                        radius * PlayScreen.sizeViewport.y / MainGame.V_Height);
    }

    private Vector2 BodyPosition(Vector2 temp){

        temp.set(body.getPosition().x*MainGame.PPM*PlayScreen.sizeViewport.y/MainGame.V_Height +
                        PlayScreen.sizeViewport.x/2 - PlayScreen.positionCamera.x*PlayScreen.sizeViewport.y/MainGame.V_Height,
                body.getPosition().y*MainGame.PPM*PlayScreen.sizeViewport.y/MainGame.V_Height
                        + PlayScreen.sizeViewport.y/2
                        - PlayScreen.positionCamera.y*PlayScreen.sizeViewport.y/MainGame.V_Height );

        return temp;
    }

    private Vector2 inputPosition(Vector2 temp){

        temp.set(Gdx.input.getX(), PlayScreen.sizeViewport.y-Gdx.input.getY());

        return temp;
    }

    private void DelimiterBorder(){

        if(body.getPosition().x*MainGame.PPM - radius  < (-1)*MainGame.V_Width/2){
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if(body.getPosition().x*MainGame.PPM + radius > MainGame.V_Width+(MainGame.V_Width/2)){

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if(body.getPosition().y*MainGame.PPM - radius  < (-1)*MainGame.V_Height/2){
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if(body.getPosition().y*MainGame.PPM + radius > MainGame.V_Height+(MainGame.V_Height/2)){
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);
        }

    }
}


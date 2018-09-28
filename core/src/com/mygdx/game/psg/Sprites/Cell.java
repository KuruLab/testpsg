package com.mygdx.game.psg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Screens.PlayScreen;
import static com.mygdx.game.psg.Screens.PlayScreen.*;

public class Cell extends Actor{

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
    private Vector2 move, bodyPosition, inputPosition, gamePosition, velocity;

    public float baseRadius = 100;

    private float baseMove = 1;
    public boolean selected = false, target = false;

    public Cell(World world, float x, float y, int ID){
        this.temp = new Vector2();
        this.move = new Vector2();
        this.bodyPosition = new Vector2();
        this.inputPosition = new Vector2();
        this.gamePosition = new Vector2();
        this.velocity = new Vector2(0,0);

       // this.actionCell = new ActionCell();

        setX(x);
        setY(y);
        setCell(world);
        setZIndex(ID);
    }

    private void setCell(World world){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX()/MainGame.PPM, getY()/MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(baseRadius /MainGame.PPM);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        move.set(body.getPosition());
    }

    @Override
    public void act(float delta) {

        if (Gdx.input.justTouched() && selected && !PlayScreen.oneTarget) {
            move.set(gamePosition);
            temp.set(inputPosition(inputPosition).sub(bodyPosition));
            velocity.set(baseMove, baseMove).setAngle(temp.angle());
            body.setLinearVelocity(velocity);
        }

        if(Gdx.input.justTouched() && isTouched() && selected == PlayScreen.oneSelected) {
            body.setLinearVelocity(0, 0);
            PlayScreen.oneSelected = !PlayScreen.oneSelected;
            selected = !selected;
        }else { if(Gdx.input.justTouched() && isTouched() && selected != PlayScreen.oneSelected){
                    target = !target;
                }
        }

        DelimiterBorder();
        velocity.set(body.getLinearVelocity());
        body.setLinearVelocity(velocity.x,velocity.y);

        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    private boolean isTouched(){

        return (Gdx.input.justTouched() &&
                BodyPosition(bodyPosition).dst(inputPosition(inputPosition)) <
                        baseRadius * sizeViewport.x / MainGame.V_Width) ||
                (BodyPosition(bodyPosition).dst(inputPosition(inputPosition)) <
                        baseRadius * sizeViewport.y / MainGame.V_Height);
    }

    private Vector2 BodyPosition(Vector2 temp){

        temp.set(body.getPosition().x*MainGame.PPM* sizeViewport.y/MainGame.V_Height +
                        sizeViewport.x/2 - positionCamera.x* sizeViewport.y/MainGame.V_Height,
                body.getPosition().y*MainGame.PPM* sizeViewport.y/MainGame.V_Height
                        + sizeViewport.y/2
                        - positionCamera.y* sizeViewport.y/MainGame.V_Height );

        return temp;
    }

    private Vector2 inputPosition(Vector2 temp){

        temp.set(Gdx.input.getX(), sizeViewport.y-Gdx.input.getY());

        return temp;
    }

    private void DelimiterBorder(){

        if(body.getPosition().x*MainGame.PPM - baseRadius < (-1)*MainGame.V_Width/2){
            body.setLinearVelocity(baseMove, body.getLinearVelocity().y);
        }
        if(body.getPosition().x*MainGame.PPM + baseRadius > MainGame.V_Width+(MainGame.V_Width/2)){

            body.setLinearVelocity(-baseMove, body.getLinearVelocity().y);
        }
        if(body.getPosition().y*MainGame.PPM - baseRadius < (-1)*MainGame.V_Height/2){
            body.setLinearVelocity(body.getLinearVelocity().x, baseMove);
        }
        if(body.getPosition().y*MainGame.PPM + baseRadius > MainGame.V_Height+(MainGame.V_Height/2)){
            body.setLinearVelocity(body.getLinearVelocity().x, -baseMove);
        }
    }
}


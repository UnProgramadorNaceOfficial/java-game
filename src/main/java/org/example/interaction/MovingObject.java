package org.example.interaction;

import org.example.entities.*;
import org.example.graphics.Assets;
import org.example.state.GameState;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class MovingObject extends GameObject {

    protected Vector2D velocity;
    protected AffineTransform transform;
    protected double angle;
    protected double maxVelocity;
    protected int width;
    protected int height;
    protected GameState gameState;
    private Sound explosion;

    public MovingObject(Vector2D position, Vector2D velocity, double maxVelocity, BufferedImage texture, GameState gameState) {
        super(position, texture);
        this.velocity = velocity;
        this.maxVelocity = maxVelocity;
        angle = 0;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.gameState = gameState;
        explosion = new Sound(Assets.explosion);
    }

    protected void collidesWith(){
        ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();

        for(int i = 0; i < movingObjects.size(); i++){

            MovingObject m  = movingObjects.get(i);

            if(m.equals(this))
                continue;

            double distance = m.getCenter().subtract(getCenter()).getMagnitude();

            if(distance < m.width/2 + width/2 && movingObjects.contains(this)){
                objectCollision(m, this);
            }
        }
    }

    private void objectCollision(MovingObject a, MovingObject b){

        if(a instanceof Player && ((Player)a).isSpawning()) {
            return;
        }
        if(b instanceof Player && ((Player)b).isSpawning()) {
            return;
        }


        if(!(a instanceof Meteor && b instanceof Meteor)){
            gameState.playExplosion(getCenter());
            a.destroy();
            b.destroy();
        }
    }

    protected void destroy(){
        gameState.getMovingObjects().remove(this);
        if(!(this instanceof Laser))
            explosion.play();
    }

    protected Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
    }
}

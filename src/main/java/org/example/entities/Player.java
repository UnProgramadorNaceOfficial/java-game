package org.example.entities;

import org.example.graphics.Assets;
import org.example.interaction.KeyBoard;
import org.example.interaction.MovingObject;
import org.example.state.GameState;
import org.example.util.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends MovingObject {

    private Vector2D heading;
    private Vector2D aceleration;
    private final double acelerationValue = 0.2;
    private final double deltaAngle = 0.1;
    private boolean acelerating = false;
    private Chronometer chronometer;

    private int counter = 0;
    private boolean spawning;
    private boolean visible;
    private Chronometer spawnTime;
    private Chronometer flickerTime;

    private Sound shoot;
    private Sound loose;

    public Player(Vector2D position, Vector2D velocity, double maxVelocity, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVelocity, texture, gameState);
        heading = new Vector2D(0, 1);
        aceleration = new Vector2D(0, 0);
        chronometer = new Chronometer();
        spawnTime = new Chronometer();
        flickerTime = new Chronometer();
        shoot = new Sound(Assets.playerShoot);
        loose = new Sound(Assets.playerLoose);
    }

    private void resetValues() {

        angle = 0;
        velocity = new Vector2D();
        position = new Vector2D(Constants.WIDTH/2 - Assets.player.getWidth()/2,
                Constants.HEIGHT/2 - Assets.player.getHeight()/2);
    }

    @Override
    public void destroy() {
        spawning = true;
        spawnTime.run(Constants.SPAWNING_TIME);
        loose.play();
        resetValues();
        gameState.subtractLife();
    }

    @Override
    public void update()
    {

        if(!spawnTime.isRunning()) {
            spawning = false;
            visible = true;
        }

        if(spawning) {

            if(!flickerTime.isRunning()) {

                flickerTime.run(Constants.FLICKER_TIME);
                visible = !visible;

            }

        }

        if(KeyBoard.SHOOT &&  !chronometer.isRunning() && !spawning)
        {
            gameState.getMovingObjects().add(0,new Laser(
                    getCenter().add(heading.scale(width)),
                    heading,
                    Constants.LASER_VEL,
                    angle,
                    Assets.laserBlue,
                    gameState
            ));
            chronometer.run(Constants.FIRERATE);
            shoot.play();
        }

        if(shoot.getFramePosition() > 8500) {
            shoot.stop();
        }

        if(KeyBoard.RIGHT)
            angle += Constants.DELTAANGLE;
        if(KeyBoard.LEFT)
            angle -= Constants.DELTAANGLE;

        if(KeyBoard.UP)
        {
            aceleration = heading.scale(Constants.ACC);
            acelerating = true;
        }else
        {
            if(velocity.getMagnitude() != 0)
                aceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC/2);
            acelerating = false;
        }

        velocity = velocity.add(aceleration);

        velocity = velocity.limit(maxVelocity);

        heading = heading.setDirection(angle - Math.PI/2);

        position = position.add(velocity);

        if(position.getX() > Constants.WIDTH)
            position.setX(0);
        if(position.getY() > Constants.HEIGHT)
            position.setY(0);

        if(position.getX() < -width)
            position.setX(Constants.WIDTH);
        if(position.getY() < -height)
            position.setY(Constants.HEIGHT);


        chronometer.update();
        spawnTime.update();
        flickerTime.update();
        collidesWith();
    }

    @Override
    public void draw(Graphics g) {

        if(isSpawning() == false && counter == 0){
            spawning = true;
            spawnTime.run(Constants.SPAWNING_TIME);
            counter++;
        }

        if(!visible)
            return;

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform propulsion = AffineTransform.getTranslateInstance(position.getX() + width / 2 - 32, position.getY() + height / 2 + 30);
        propulsion.rotate(angle, 32, -30);

        transform = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        transform.rotate(angle, width / 2, height / 2);
        g2d.drawImage(Assets.player, transform, null);

        if (acelerating) {
            g2d.drawImage(Assets.speed, propulsion, null);
        }

    }

    public Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
    }

    public boolean isSpawning() {return spawning;}
}

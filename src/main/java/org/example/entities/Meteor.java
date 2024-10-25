package org.example.entities;

import org.example.interaction.MovingObject;
import org.example.state.GameState;
import org.example.util.Constants;
import org.example.util.Size;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Meteor extends MovingObject{

    private Size size;

    public Meteor(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState, Size size) {
        super(position, velocity, maxVel, texture, gameState);
        this.size = size;
        this.velocity = velocity.scale(maxVel);

    }

    @Override
    public void update() {
        position = position.add(velocity);

        if(position.getX() > Constants.WIDTH - 100)
            position.setX(-width);
        if(position.getY() > Constants.HEIGHT - 100)
            position.setY(-height);

        if(position.getX() < -width)
            position.setX(Constants.WIDTH - 100);
        if(position.getY() < -height)
            position.setY(Constants.HEIGHT - 100);

        angle += Constants.DELTAANGLE/2;

    }

    @Override
    public void destroy(){
        gameState.divideMeteor(this);
        gameState.addScore(Constants.METEOR_SCORE, position);
        super.destroy();
    }


    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        transform = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        transform.rotate(angle, width/2, height/2);

        g2d.drawImage(texture, transform, null);
    }

    public Size getSize(){
        return size;
    }
}
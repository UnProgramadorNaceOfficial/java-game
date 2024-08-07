package org.example.state;


import org.example.entities.*;
import org.example.graphics.Animation;
import org.example.graphics.Assets;
import org.example.interaction.MovingObject;
import org.example.util.Constants;
import org.example.util.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameState {

    private Player player;
    private ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private ArrayList<Animation> explosions = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private Sound background;

    private int score = 0;
    private int lives = 3;
    private int meteors;
    private int waves = 1;

    public GameState() {
        player = new Player(new Vector2D(Constants.WIDTH / 2 - Assets.player.getWidth() / 2,
                Constants.HEIGHT / 2 - Assets.player.getHeight() / 2), new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);
        movingObjects.add(player);
        background = new Sound(Assets.backgroundMusic);
        background.loop();


        meteors = 1;

        startWave();

    }

    public void addScore(int value, Vector2D position) {
        score += value;
        messages.add(new Message(position, true, "+"+value+" score", Color.WHITE, false, Assets.fontMed, this));
    }

    public void divideMeteor(Meteor meteor) {

        Size size = meteor.getSize();

        BufferedImage[] textures = size.textures;

        Size newSize = null;

        switch (size) {
            case BIG:
                newSize = Size.MED;
                break;
            case MED:
                newSize = Size.SMALL;
                break;
            default:
                return;
        }

        for (int i = 0; i < size.quantity; i++) {
            movingObjects.add(new Meteor(
                    meteor.getPosition(),
                    new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
                    Constants.METEOR_VEL * Math.random() + 1,
                    textures[(int) (Math.random() * textures.length)],
                    this,
                    newSize
            ));
        }
    }


    private void startWave(){
        messages.add(new Message(new Vector2D(Constants.WIDTH/2, Constants.HEIGHT/2), false,
                "WAVE "+waves, Color.WHITE, true, Assets.fontBig, this));

        double x, y;

        for(int i = 0; i < meteors; i++){

            x = i % 2 == 0 ? Math.random()*Constants.WIDTH : 0;
            y = i % 2 == 0 ? 0 : Math.random()*Constants.HEIGHT;

            BufferedImage texture = Assets.bigs[(int)(Math.random()*Assets.bigs.length)];

            movingObjects.add(new Meteor(
                    new Vector2D(x, y),
                    new Vector2D(0, 1).setDirection(Math.random()*Math.PI*2),
                    Constants.METEOR_VEL*Math.random() + 1,
                    texture,
                    this,
                    Size.BIG
            ));

        }
        meteors ++;
        spawnUfo();
    }

    public void playExplosion(Vector2D position) {
        explosions.add(new Animation(
                Assets.exp,
                50,
                position.subtract(new Vector2D(Assets.exp[0].getWidth() / 2, Assets.exp[0].getHeight() / 2))
        ));
    }

    private void spawnUfo() {

        int rand = (int) (Math.random() * 2);

        double x = rand == 0 ? (Math.random() * Constants.WIDTH) : 0;
        double y = rand == 0 ? 0 : (Math.random() * Constants.HEIGHT);

        ArrayList<Vector2D> path = new ArrayList<Vector2D>();

        double posX, posY;

        posX = Math.random() * Constants.WIDTH / 2;
        posY = Math.random() * Constants.HEIGHT / 2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * (Constants.WIDTH / 2) + Constants.WIDTH / 2;
        posY = Math.random() * Constants.HEIGHT / 2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * Constants.WIDTH / 2;
        posY = Math.random() * (Constants.HEIGHT / 2) + Constants.HEIGHT / 2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * (Constants.WIDTH / 2) + Constants.WIDTH / 2;
        posY = Math.random() * (Constants.HEIGHT / 2) + Constants.HEIGHT / 2;
        path.add(new Vector2D(posX, posY));

        movingObjects.add(new Ufo(
                new Vector2D(x, y),
                new Vector2D(),
                Constants.UFO_MAX_VEL,
                Assets.enemy,
                path,
                this
        ));

    }

    public void update() {
        for (int i = 0; i < movingObjects.size(); i++)
            movingObjects.get(i).update();

        for (int i = 0; i < explosions.size(); i++) {
            Animation anim = explosions.get(i);
            anim.update();
            if (!anim.isRunning()) {
                explosions.remove(i);
            }

        }

        for (int i = 0; i < movingObjects.size(); i++)
            if (movingObjects.get(i) instanceof Meteor)
                return;

        startWave();

    }

    private void drawScore(Graphics g) {

        Vector2D pos = new Vector2D(1200, 25);

        String scoreToString = Integer.toString(score);

        for (int i = 0; i < scoreToString.length(); i++) {

            g.drawImage(Assets.numbers[Integer.parseInt(scoreToString.substring(i, i + 1))],
                    (int) pos.getX(), (int) pos.getY(), null);
            pos.setX(pos.getX() + 20);

        }

    }

    private void drawLives(Graphics g) {

        Vector2D livePosition = new Vector2D(25, 25);

        g.drawImage(Assets.life, (int) livePosition.getX(), (int) livePosition.getY(), null);

        g.drawImage(Assets.numbers[10], (int) livePosition.getX() + 40,
                (int) livePosition.getY() + 5, null);

        String livesToString = Integer.toString(lives);

        Vector2D pos = new Vector2D(livePosition.getX(), livePosition.getY());

        for (int i = 0; i < livesToString.length(); i++) {
            int number = Integer.parseInt(livesToString.substring(i, i + 1));

            if (number <= 0)
                break;
            g.drawImage(Assets.numbers[number],
                    (int) pos.getX() + 60, (int) pos.getY() + 5, null);
            pos.setX(pos.getX() + 20);
        }

    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (int i = 0; i < messages.size(); i++)
            messages.get(i).draw(g2d);

        for (int i = 0; i < movingObjects.size(); i++)
            movingObjects.get(i).draw(g);

        for (int i = 0; i < explosions.size(); i++) {
            Animation anim = explosions.get(i);
            g2d.drawImage(anim.getCurrentFrame(), (int) anim.getPosition().getX(), (int) anim.getPosition().getY(),
                    null);
        }

        drawScore(g);
        drawLives(g);
    }

    public void subtractLife() {
        lives--;
    }

    public ArrayList<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}

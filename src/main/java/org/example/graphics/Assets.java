package org.example.graphics;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Assets {

    public static BufferedImage player;
    public static BufferedImage enemy;
    public static BufferedImage speed;
    public static BufferedImage laserGreen;
    public static BufferedImage laserBlue;
    public static BufferedImage laserRed;
    public static BufferedImage life;

    public static Font fontBig;
    public static Font fontMed;

    public static BufferedImage[] bigs = new BufferedImage[4];
    public static BufferedImage[] middle = new BufferedImage[4];
    public static BufferedImage[] small = new BufferedImage[2];

    public static BufferedImage[] exp = new BufferedImage[9];

    public static BufferedImage[] numbers = new BufferedImage[11];

    public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot;

    public static void init() throws IOException {
        player = Loader.loadImage("src/main/resources/images/ship.png");
        enemy = Loader.loadImage("src/main/resources/images/enemy.png");
        speed = Loader.loadImage("src/main/resources/images/propulsion.png");
        laserGreen = Loader.loadImage("src/main/resources/images/laserGreen.png");
        laserBlue = Loader.loadImage("src/main/resources/images/laserBlue.png");
        laserRed = Loader.loadImage("src/main/resources/images/laserRed.png");
        life = Loader.loadImage("src/main/resources/images/life.png");

        fontBig = new Font("KenVector Future", Font.PLAIN, 50);
        fontMed = new Font("KenVector Future", Font.PLAIN, 20);

        for (int i = 0; i <= bigs.length; i++) {
            String name = "src/main/resources/images/big" + (i + 1) + ".png";
            File file = new File(name);

            if (file.exists()) {
                bigs[i] = Loader.loadImage(name);
            }
        }

        for (int i = 0; i <= middle.length; i++) {
            String name = "src/main/resources/images/middle" + (i + 1) + ".png";
            File file = new File(name);

            if (file.exists()) {
                middle[i] = Loader.loadImage(name);
            }
        }

        for (int i = 0; i <= small.length; i++) {
            String name = "src/main/resources/images/small" + (i + 1) + ".png";
            File file = new File(name);
            if (file.exists()) {
                small[i] = Loader.loadImage(name);
            }
        }

        for (int i = 0; i <= exp.length; i++) {
            String name = "src/main/resources/images/explosion/" + (i + 1) + ".png";
            File file = new File(name);
            if (file.exists()) {
                exp[i] = Loader.loadImage(name);
            }
        }

        for(int i = 0; i < numbers.length; i++) {
            String name = "src/main/resources/images/numbers/" + i + ".png";
            File file = new File(name);
            if (file.exists()) {
                numbers[i] = Loader.loadImage(name);
            }
        }

        backgroundMusic = Loader.loadSound("src/main/resources/song/music.wav");
        explosion = Loader.loadSound("src/main/resources/song/explosion.wav");
        playerLoose = Loader.loadSound("src/main/resources/song/playerLoose.wav");
        playerShoot = Loader.loadSound("src/main/resources/song/playerShoot.wav");
        ufoShoot = Loader.loadSound("src/main/resources/song/ufoShoot.wav");
    }
}

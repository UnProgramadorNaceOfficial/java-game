package org.example.graphics;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Loader {

    public static BufferedImage loadImage(String imageName) throws IOException {
        return ImageIO.read(new File(imageName));
    }

    public static Font loadFont(String path, int size) {
        Font font = null;
        try (InputStream is = Loader.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("No se pudo encontrar el archivo de fuente: " + path);
            }
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    public static Clip loadSound(String path) {
        try {
            File file = new File(path);

            Clip clip = null;
            if(file.exists()){
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }

            return clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }
}

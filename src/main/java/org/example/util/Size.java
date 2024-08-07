package org.example.util;

import org.example.graphics.Assets;

import java.awt.image.BufferedImage;

public enum Size {
    BIG(2, Assets.middle), MED(2, Assets.small), SMALL(0, null);

    public int quantity;

    public BufferedImage[] textures;

    private Size(int quantity, BufferedImage[] textures){
        this.quantity = quantity;
        this.textures = textures;
    }

}

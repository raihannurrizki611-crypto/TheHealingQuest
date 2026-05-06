/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lenovo
 */
package com.mycompany.thehealingquest;

import java.awt.Graphics2D;

/**
 * NPCNasgor.java — Mang Nasgor di Depan Mesjid.
 * Animasi memasak 6 frame.
 */
public class NPCNasgor extends NPC {

    private static final int     NASGOR_X    = 500;
    private static final boolean FACING_LEFT = true;

    public NPCNasgor(AssetLoader assets) {
        super(
            "Mang Nasgor",
            NASGOR_X, 0,
            SceneIndex.DEPAN_MESJID.getIndex(),
            assets.getNpcNasgorFrames(),
            12,
            new String[]{}
        );
    }

    @Override public void update() { advanceFrame(); }

    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        drawSprite(g2d, W, H, NASGOR_X, FACING_LEFT);
    }

    public int getNasgorX() { return NASGOR_X; }
}

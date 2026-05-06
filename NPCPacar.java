/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lenovo
 */
package com.mycompany.thehealingquest;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * NPCPacar.java
 * NPC Pacar — hanya muncul di bad ending (Depan Mesjid).
 * 2 mode sprite: MARAH (idle marah) dan BICARA (saat dialog).
 *
 * KONSEP OOP :
 *  - Inheritance  : extends NPC
 *  - Enkapsulasi  : spriteMode private + setter
 *  - Polimorfisme : @Override draw()
 */
public class NPCPacar extends NPC {

    public enum SpriteMode { MARAH, BICARA }

    private SpriteMode     spriteMode = SpriteMode.MARAH;
    private BufferedImage[] marahFrames;  // Pacar_Marah1-2
    private boolean        playerSedangBicara = false;

    // Pacar muncul dari belakang player — posisi dinamis dekat player
    private static final boolean FACING_LEFT = true;
    private int pacarX = 500; // posisi default, bisa di-set

    public NPCPacar(AssetLoader assets) {
        super(
            "Pacar 💗",
            500, 0,
            SceneIndex.DEPAN_MESJID.getIndex(),
            assets.getPacarMarahFrames(),
            16,
            new String[]{}
        );
        this.marahFrames = assets.getPacarMarahFrames();
    }

    public void setPacarX(int x)        { this.pacarX = x; setX(x); }
    public void setSpriteMode(SpriteMode mode) {
        if (this.spriteMode == mode) return;
        this.spriteMode = mode;
        setFrames(marahFrames);
        setFrameSpeed(16);
        resetFrame();
    }

    public void setPlayerSedangBicara(boolean b) {
        this.playerSedangBicara = b;
        setCurrentFrame(b ? 1 : 0);
        setFrameDelay(0);
    }

    public SpriteMode getSpriteMode() { return spriteMode; }

    @Override
    public void update() {
        if (spriteMode != SpriteMode.BICARA) advanceFrame();
    }

    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();
        if (active == null || idx >= active.length || active[idx] == null) return;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int npcH = H / 3;
        int npcW = npcH;
        int npcY = H - npcH - (H / 12);

        if (FACING_LEFT) {
            g2d.drawImage(active[idx], pacarX + npcW, npcY, -npcW, npcH, null);
        } else {
            g2d.drawImage(active[idx], pacarX, npcY, npcW, npcH, null);
        }
    }
}

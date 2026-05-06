/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lenovo
 */
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
import java.awt.image.BufferedImage;

/**
 * NPCRadja.java
 * NPC Radja di Pos Ronda — punya 3 mode sprite:
 *  - IDLE  : merokok santai (sebelum ngobrol)
 *  - BICARA: 2 frame gantian saat dialog
 *  - MARAH : saat player abaikan dia demi mantan
 *
 * KONSEP OOP :
 *  - Inheritance  : extends NPC
 *  - Enkapsulasi  : spriteMode private + setter
 *  - Polimorfisme : @Override draw()
 */
public class NPCRadja extends NPC {

    // ── Enum mode sprite ──────────────────────────────────────────────────
    public enum SpriteMode { IDLE, BICARA, MARAH }

    // ── Field private ─────────────────────────────────────────────────────
    private SpriteMode     spriteMode = SpriteMode.IDLE;
    private BufferedImage[] idleFrames;   // Radja_Idle1-6 (merokok)
    private BufferedImage[] bicaraFrames; // Radja_Talk1-2
    private BufferedImage[] marahFrames;  // Radja_Marah1-2

    // ── Siapa yang sedang bicara (untuk ganti frame) ──────────────────────
    private boolean playerSedangBicara = false;

    // ── FIX 2: facingLeft now dynamic instead of hardcoded constant ───────
    private static final int RADJA_X = 520;
    private boolean facingLeft = true; // default: faces left toward player start

    // ── Constructor ───────────────────────────────────────────────────────
    public NPCRadja(AssetLoader assets) {
        super(
            "Radja",
            RADJA_X, 0,
            SceneIndex.POS_RONDA.getIndex(),
            assets.getRadjaIdleFrames(),
            14,
            new String[]{}   // dialog diurus StoryManager
        );
        this.idleFrames   = assets.getRadjaIdleFrames();
        this.bicaraFrames = assets.getRadjaTalkFrames();
        this.marahFrames  = assets.getRadjaMarahFrames();
    }

    // ── FIX 2: Call this every frame from Game2D to update facing ─────────
    /**
     * Updates which direction Radja faces based on the player's position.
     * Radja faces left when the player is to his left, right when to his right.
     */
    public void updateFacing(int playerX) {
        facingLeft = playerX < RADJA_X;
    }

    // ── Setter mode sprite ────────────────────────────────────────────────
    public void setSpriteMode(SpriteMode mode) {
        if (this.spriteMode == mode) return;
        this.spriteMode = mode;
        switch (mode) {
            case IDLE:   setFrames(idleFrames);   setFrameSpeed(14); break;
            case BICARA: setFrames(bicaraFrames); setFrameSpeed(20); break;
            case MARAH:  setFrames(marahFrames);  setFrameSpeed(18); break;
        }
        resetFrame();
    }

    /**
     * Saat mode BICARA, frame 0 = Radja bicara, frame 1 = Player bicara.
     * Dipanggil dari Game2D setiap kali ganti speaker.
     */
    public void setPlayerSedangBicara(boolean playerBicara) {
        if (spriteMode != SpriteMode.BICARA) return;
        this.playerSedangBicara = playerBicara;
        // Frame 0 = Radja bicara, Frame 1 = Player bicara (Radja diam)
        setCurrentFrame(playerBicara ? 1 : 0);
        setFrameDelay(0);
    }

    public SpriteMode getSpriteMode() { return spriteMode; }

    // ── POLIMORFISME : @Override draw() ──────────────────────────────────
    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        // Saat BICARA frame tidak di-advance otomatis (dikontrol manual)
        if (spriteMode != SpriteMode.BICARA) {
            drawSprite(g2d, W, H, RADJA_X, facingLeft);
        } else {
            drawSpriteManual(g2d, W, H, RADJA_X, facingLeft);
        }
    }

    /** Draw frame saat ini tanpa advance (untuk mode BICARA) */
    private void drawSpriteManual(Graphics2D g2d, int W, int H,
                                   int npcX, boolean flipped) {
        java.awt.image.BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();
        if (active == null || idx >= active.length || active[idx] == null) return;

        int npcH = H / 3;
        int npcW = npcH;
        int npcY = H - npcH - (H / 12);

        g2d.setRenderingHint(
            java.awt.RenderingHints.KEY_INTERPOLATION,
            java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (flipped)
            g2d.drawImage(active[idx], npcX + npcW, npcY, -npcW, npcH, null);
        else
            g2d.drawImage(active[idx], npcX, npcY, npcW, npcH, null);
    }

    // ── update() — hanya advance frame saat IDLE atau MARAH ──────────────
    @Override
    public void update() {
        if (spriteMode != SpriteMode.BICARA) {
            advanceFrame();
        }
        // Mode BICARA: frame dikontrol manual oleh setPlayerSedangBicara()
    }
}
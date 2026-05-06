/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thehealingquest;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * NPCOjol.java
 * NPC Ojol di Pegunungan Malam — 3 mode sprite + motor terpisah:
 *
 *  Mode sprite:
 *  - SANTAI : main hp, belum sadar motor rusak (awal bagian 1.2)
 *  - BICARA : 2 frame gantian saat dialog dengan player
 *  - PANIK  : sadar motor mogok
 *
 *  Motor digambar terpisah di sebelah ojol (Motor.png statis).
 *
 * KONSEP OOP :
 *  - Inheritance  : extends NPC
 *  - Enkapsulasi  : spriteMode, motorVisible private + setter
 *  - Polimorfisme : @Override draw() — motor digambar sebelum ojol
 */
public class NPCOjol extends NPC {

    // ── Enum mode sprite ──────────────────────────────────────────────────
    public enum SpriteMode { SANTAI, BICARA, PANIK }

    // ── Field private ─────────────────────────────────────────────────────
    private SpriteMode     spriteMode = SpriteMode.SANTAI;
    private BufferedImage[] santaiFrames; // Ojol_Santai1-4
    private BufferedImage[] bicaraFrames; // Ojol_Talk1-2
    private BufferedImage[] panikFrames;  // Ojol_Idle1-4
    private BufferedImage   motorImg;     // Motor.png (statis)
    private boolean         motorVisible = true; // motor tampil atau tidak

    // ── Siapa yang sedang bicara ──────────────────────────────────────────
    private boolean playerSedangBicara = false;

    // ── Posisi ────────────────────────────────────────────────────────────
    private static final int     OJOL_X      = 430;
    private static final boolean FACING_LEFT = true;
    // Motor diletakkan di sebelah kanan ojol
    private static final int     MOTOR_OFFSET_X = 80; // jarak motor dari ojol

    // ── Constructor ───────────────────────────────────────────────────────
    public NPCOjol(AssetLoader assets) {
        super(
            "Ojol",
            OJOL_X, 0,
            SceneIndex.PEGUNUNGAN_MALAM.getIndex(),
            assets.getOjolSantaiFrames(),
            14,
            new String[]{}
        );
        this.santaiFrames = assets.getOjolSantaiFrames();
        this.bicaraFrames = assets.getOjolTalkFrames();
        this.panikFrames  = assets.getOjolIdleFrames();
        this.motorImg     = assets.getMotorImg();
    }

    // ── Setter mode sprite ────────────────────────────────────────────────
    public void setSpriteMode(SpriteMode mode) {
        if (this.spriteMode == mode) return;
        this.spriteMode = mode;
        switch (mode) {
            case SANTAI: setFrames(santaiFrames); setFrameSpeed(14); break;
            case BICARA: setFrames(bicaraFrames); setFrameSpeed(20); break;
            case PANIK:  setFrames(panikFrames);  setFrameSpeed(12); break;
        }
        resetFrame();
    }

    /**
     * Saat mode BICARA:
     * frame 0 = Ojol bicara, frame 1 = Player bicara (Ojol diam).
     */
    public void setPlayerSedangBicara(boolean playerBicara) {
        if (spriteMode != SpriteMode.BICARA) return;
        this.playerSedangBicara = playerBicara;
        setCurrentFrame(playerBicara ? 1 : 0);
        setFrameDelay(0);
    }

    public void setMotorVisible(boolean v) { this.motorVisible = v; }
    public SpriteMode getSpriteMode()      { return spriteMode; }

    // ── update ────────────────────────────────────────────────────────────
    @Override
    public void update() {
        // Mode BICARA dikontrol manual
        if (spriteMode != SpriteMode.BICARA) advanceFrame();
    }

    // ── POLIMORFISME : @Override draw() ──────────────────────────────────
    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        int npcH = H / 3;
        int npcW = npcH;
        int npcY = H - npcH - (H / 12);

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // ── Gambar motor terlebih dahulu (di belakang ojol) ───────────────
        if (motorVisible && motorImg != null) {
            int motorW = (int)(npcW * 1.4);
            int motorH = (int)(npcH * 0.7);
            int motorX = OJOL_X + MOTOR_OFFSET_X;
            int motorY = H - motorH - (H / 12) + (npcH - motorH);
            g2d.drawImage(motorImg, motorX, motorY, motorW, motorH, null);
        }

        // ── Gambar ojol ───────────────────────────────────────────────────
        BufferedImage[] active = getFrames();
        int idx = (spriteMode == SpriteMode.BICARA)
                  ? getCurrentFrame()   // frame dikontrol manual
                  : getCurrentFrame();

        if (active == null || idx >= active.length || active[idx] == null) return;

        if (FACING_LEFT) {
            g2d.drawImage(active[idx], OJOL_X + npcW, npcY, -npcW, npcH, null);
        } else {
            g2d.drawImage(active[idx], OJOL_X, npcY, npcW, npcH, null);
        }
    }
}
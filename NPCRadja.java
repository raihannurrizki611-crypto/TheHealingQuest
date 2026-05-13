package com.mycompany.thehealingquest;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    private SpriteMode      spriteMode = SpriteMode.IDLE;
    private BufferedImage[] idleFrames;    // Radja_Idle1-6 (merokok)
    private BufferedImage[] bicaraFrames;  // Radja_Talk1-2
    private BufferedImage[] marahFrames;   // Radja_Marah1-2

    private boolean playerSedangBicara = false;
    private boolean facingLeft         = true; // default menghadap kiri ke arah player

    private static final int RADJA_X = 520;

    // ── Constructor ───────────────────────────────────────────────────────
    public NPCRadja(AssetLoader assets) {
        super(
            "Radja",
            RADJA_X, 0,
            SceneIndex.POS_RONDA.getIndex(),
            assets.getRadjaIdleFrames(),
            14,
            new String[]{}
        );
        this.idleFrames   = assets.getRadjaIdleFrames();
        this.bicaraFrames = assets.getRadjaTalkFrames();
        this.marahFrames  = assets.getRadjaMarahFrames();
    }

    // ── Update arah hadap sesuai posisi player ────────────────────────────
    public void updateFacing(int playerX) {
        // facingLeft = true → sprite di-flip (mirror)
        // Balik kondisi sesuai arah default sprite Radja
        facingLeft = playerX > RADJA_X;
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
     * Saat mode BICARA:
     * frame 0 = Radja bicara, frame 1 = Player bicara (Radja diam).
     */
    public void setPlayerSedangBicara(boolean playerBicara) {
        if (spriteMode != SpriteMode.BICARA) return;
        this.playerSedangBicara = playerBicara;
        setCurrentFrame(playerBicara ? 1 : 0);
        setFrameDelay(0);
    }

    public SpriteMode getSpriteMode() { return spriteMode; }
    public int        getRadjaX()     { return RADJA_X; }

    // ── POLIMORFISME : @Override update() ────────────────────────────────
    @Override
    public void update() {
        // Mode BICARA dikontrol manual — tidak di-advance otomatis
        if (spriteMode != SpriteMode.BICARA) advanceFrame();
    }

    // ── POLIMORFISME : @Override draw() ──────────────────────────────────
    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        if (spriteMode != SpriteMode.BICARA) {
            // Mode IDLE / MARAH — pakai helper dari NPC (ukuran H * 0.52)
            drawSprite(g2d, W, H, RADJA_X, facingLeft);
        } else {
            // Mode BICARA — frame dikontrol manual
            drawSpriteManual(g2d, W, H, RADJA_X, facingLeft);
        }
    }

    /**
     * Draw frame saat ini tanpa advance (untuk mode BICARA).
     * Ukuran SAMA dengan NPC.drawSprite() → H * 0.52
     */
    private void drawSpriteManual(Graphics2D g2d, int W, int H,
                                   int npcX, boolean flipped) {
        BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();
        if (active == null || idx >= active.length || active[idx] == null) return;

        // ── Ukuran konsisten dengan Player dan NPC lain ───────────────────
        int npcH = (int)(H * 0.40);
        int npcW = (int)(npcH * 0.79);
        int npcY = H - npcH - (int)(H * 0.09);

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (flipped)
            g2d.drawImage(active[idx], npcX + npcW, npcY, -npcW, npcH, null);
        else
            g2d.drawImage(active[idx], npcX, npcY, npcW, npcH, null);
    }
}

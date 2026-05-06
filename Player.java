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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Player.java
 * Karakter yang dikendalikan pemain.
 * Extends Character → Character extends GameObject.
 *
 * KONSEP OOP :
 *  - Inheritance  : extends Character, mewarisi semua field animasi + x, y, sceneIndex
 *  - Enkapsulasi  : speed, moveLeft, moveRight, animState → private + getter/setter
 *  - Polimorfisme : @Override update() dan draw() dengan perilaku khusus Player
 */
public class Player extends Character {

    // ── Field private khusus Player (enkapsulasi) ─────────────────────────
    private int       speed;       // kecepatan gerak horizontal
    private boolean   moveLeft;    // tombol A ditekan
    private boolean   moveRight;   // tombol D ditekan
    private AnimState animState;   // state animasi saat ini

    // ── Sprite khusus Player ──────────────────────────────────────────────
    private BufferedImage[] walkFrames; // 6 frame berjalan
    private BufferedImage[] idleFrames; // 4 frame diam

    // ── Constructor ───────────────────────────────────────────────────────
    public Player(AssetLoader assets, int startX, int startY) {
        super(
            "Player",                    // nama
            startX, startY,              // posisi awal
            SceneIndex.DEPAN_RUMAH.getIndex(), // mulai di scene 0
            assets.getIdleFrames(),      // frame awal = idle
            16                           // frameSpeed idle
        );
        this.walkFrames = assets.getWalkFrames();
        this.idleFrames = assets.getIdleFrames();
        this.speed      = 4;
        this.moveLeft   = false;
        this.moveRight  = false;
        this.animState  = AnimState.IDLE;
    }

    // ── Getter & Setter (enkapsulasi) ─────────────────────────────────────

    public int     getSpeed()               { return speed; }
    public void    setSpeed(int speed)      { this.speed = speed; }

    public boolean isMoveLeft()             { return moveLeft; }
    public void    setMoveLeft(boolean b)   { this.moveLeft = b; }

    public boolean isMoveRight()            { return moveRight; }
    public void    setMoveRight(boolean b)  { this.moveRight = b; }

    public AnimState getAnimState()                  { return animState; }
    public void      setAnimState(AnimState state)   { this.animState = state; }

    // ── POLIMORFISME : @Override update() ────────────────────────────────
    /**
     * Update posisi dan animasi player setiap tick.
     * Perilaku khusus Player: gerak horizontal + ganti sprite sesuai state.
     */
    @Override
    public void update() {
        // Gerak horizontal
        if (moveLeft)  setX(getX() - speed);
        if (moveRight) setX(getX() + speed);

        // Arah hadap
        if (moveLeft)  setFacingLeft(true);
        if (moveRight) setFacingLeft(false);

        // Ganti state animasi
        boolean isMoving  = moveLeft || moveRight;
        AnimState newState = isMoving ? AnimState.WALKING : AnimState.IDLE;

        if (newState != animState) {
            animState = newState;
            setFrames(animState == AnimState.WALKING ? walkFrames : idleFrames);
            setFrameSpeed(animState == AnimState.WALKING ? 13 : 16);
            resetFrame();
        }

        advanceFrame(); // dari Character — tidak perlu tulis ulang
    }

    // ── POLIMORFISME : @Override draw() ──────────────────────────────────
    /**
     * Gambar sprite player ke layar.
     * Ukuran proporsional terhadap layar, flip horizontal jika facingLeft.
     */
    @Override
    public void draw(Graphics2D g2d, int W, int H) {
        BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();

        if (idx >= active.length || active[idx] == null) return;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int charH = H / 3;
        int charW = charH;
        int charY = H - charH - (H / 12);
        int charX = getX();

        if (isFacingLeft()) {
            g2d.drawImage(active[idx], charX + charW, charY, -charW, charH, null);
        } else {
            g2d.drawImage(active[idx], charX, charY, charW, charH, null);
        }
    }
}

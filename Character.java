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
 * Character.java
 * Abstract class untuk semua karakter (Player dan NPC).
 * Extends GameObject — mendapat field x, y, sceneIndex + method update(), draw().
 *
 * KONSEP OOP :
 *  - Inheritance  : extends GameObject, mewarisi x, y, sceneIndex, update(), draw()
 *  - Enkapsulasi  : semua field animasi private, akses lewat getter/setter
 *  - Polimorfisme : update() dan draw() masih abstract — Player & NPC override sendiri
 */
public abstract class Character extends GameObject {

    // ── Field private (enkapsulasi) ───────────────────────────────────────
    private String          name;         // nama karakter
    private BufferedImage[] frames;       // array sprite animasi aktif
    private int             currentFrame; // frame yang sedang ditampilkan
    private int             frameDelay;   // penghitung delay antar frame
    private int             frameSpeed;   // kecepatan animasi (tick per frame)
    private boolean         facingLeft;   // arah hadap karakter

    // ── Constructor ───────────────────────────────────────────────────────
    public Character(String name, int x, int y, int sceneIndex,
                     BufferedImage[] frames, int frameSpeed) {
        super(x, y, sceneIndex);
        this.name         = name;
        this.frames       = frames;
        this.frameSpeed   = frameSpeed;
        this.currentFrame = 0;
        this.frameDelay   = 0;
        this.facingLeft   = false;
    }

    // ── Getter & Setter (enkapsulasi) ─────────────────────────────────────

    public String          getName()             { return name; }
    public void            setName(String name)  { this.name = name; }

    public BufferedImage[] getFrames()                  { return frames; }
    public void            setFrames(BufferedImage[] f) { this.frames = f; }

    public int  getCurrentFrame()        { return currentFrame; }
    public void setCurrentFrame(int f)   { this.currentFrame = f; }

    public int  getFrameDelay()          { return frameDelay; }
    public void setFrameDelay(int d)     { this.frameDelay = d; }

    public int  getFrameSpeed()          { return frameSpeed; }
    public void setFrameSpeed(int s)     { this.frameSpeed = s; }

    public boolean isFacingLeft()           { return facingLeft; }
    public void    setFacingLeft(boolean b) { this.facingLeft = b; }

    // ── Utility : advance frame animasi ──────────────────────────────────
    /**
     * Maju ke frame berikutnya sesuai frameSpeed.
     * Dipanggil oleh update() di child class — tidak perlu tulis ulang di Player/NPC.
     */
    protected void advanceFrame() {
        frameDelay++;
        if (frameDelay >= frameSpeed) {
            currentFrame = (currentFrame + 1) % frames.length;
            frameDelay   = 0;
        }
    }

    /**
     * Reset animasi ke frame pertama.
     * Dipanggil saat state animasi berubah (misal WALKING → IDLE).
     */
    protected void resetFrame() {
        currentFrame = 0;
        frameDelay   = 0;
    }

    // ── Abstract — wajib di-override tiap child class ─────────────────────
    @Override public abstract void update();
    @Override public abstract void draw(Graphics2D g2d, int W, int H);
}

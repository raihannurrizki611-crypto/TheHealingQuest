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
 * GameObject.java
 * Abstract class paling atas — induk dari semua objek dalam game.
 *
 * KONSEP OOP :
 *  - Enkapsulasi  : field x, y, sceneIndex disimpan private, akses lewat getter/setter
 *  - Inheritance  : semua karakter (Player, NPC) wajib extends class ini
 *  - Polimorfisme : method update() dan draw() bersifat abstract — wajib di-override
 *                   oleh setiap child class dengan perilaku masing-masing
 */
public abstract class GameObject {

    // ── Field private (enkapsulasi) ───────────────────────────────────────
    private int x;           // posisi horizontal objek di layar
    private int y;           // posisi vertikal objek di layar
    private int sceneIndex;  // objek ini berada di scene mana

    // ── Constructor ───────────────────────────────────────────────────────
    public GameObject(int x, int y, int sceneIndex) {
        this.x          = x;
        this.y          = y;
        this.sceneIndex = sceneIndex;
    }

    // ── Getter & Setter (enkapsulasi) ─────────────────────────────────────

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getSceneIndex() { return sceneIndex; }
    public void setSceneIndex(int sceneIndex) { this.sceneIndex = sceneIndex; }

    // ── Abstract Method (POLIMORFISME) ────────────────────────────────────
    /**
     * Update logika objek setiap tick game loop.
     * Wajib di-override oleh Player, NPC, dan child class lainnya.
     * Contoh: Player.update() → gerak kiri/kanan
     *         NPC.update()    → advance frame animasi idle
     */
    public abstract void update();

    /**
     * Gambar objek ke layar.
     * Wajib di-override oleh setiap child class.
     * Contoh: Player.draw() → gambar sprite player
     *         NPCRadja.draw() → gambar sprite Radja di Pos Ronda
     *
     * @param g2d Graphics2D dari paintComponent Game2D
     * @param W   lebar layar saat ini
     * @param H   tinggi layar saat ini
     */
    public abstract void draw(Graphics2D g2d, int W, int H);
}

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
 * NPC.java
 * Abstract class untuk semua NPC dalam game.
 * Extends Character → Character extends GameObject.
 *
 * KONSEP OOP :
 *  - Inheritance  : extends Character, mewarisi animasi + posisi
 *  - Enkapsulasi  : dialogues, dialogIndex → private + getter/setter
 *  - Polimorfisme : draw() di-override tiap NPC spesifik (NPCRadja, NPCOjol, dll)
 *
 * NPC hanya tampil di scene tertentu sesuai sceneIndex-nya.
 * update() sudah ada di sini — semua NPC cukup advance frame idle.
 * draw() masih abstract — tiap NPC bisa punya posisi/ukuran berbeda.
 */
public abstract class NPC extends Character {

    // ── Field private (enkapsulasi) ───────────────────────────────────────
    private String[] dialogues;    // array teks dialog NPC
    private int      dialogIndex;  // dialog yang sedang aktif

    // ── Constructor ───────────────────────────────────────────────────────
    public NPC(String name, int x, int y, int sceneIndex,
               BufferedImage[] frames, int frameSpeed, String[] dialogues) {
        super(name, x, y, sceneIndex, frames, frameSpeed);
        this.dialogues   = dialogues;
        this.dialogIndex = 0;
    }

    // ── Getter & Setter (enkapsulasi) ─────────────────────────────────────

    public String[] getDialogues()                 { return dialogues; }
    public void     setDialogues(String[] d)        { this.dialogues = d; }

    public int  getDialogIndex()                   { return dialogIndex; }
    public void setDialogIndex(int i)              { this.dialogIndex = i; }

    /** Ambil teks dialog saat ini */
    public String getCurrentDialogue() {
        if (dialogues == null || dialogues.length == 0) return "";
        return dialogues[dialogIndex];
    }

    /** Maju ke dialog berikutnya, berhenti di akhir */
    public void nextDialogue() {
        if (dialogIndex < dialogues.length - 1) dialogIndex++;
    }

    /** Cek apakah dialog sudah habis */
    public boolean isDialogueFinished() {
        return dialogues == null || dialogIndex >= dialogues.length - 1;
    }

    // ── POLIMORFISME : @Override update() ────────────────────────────────
    /**
     * Semua NPC cukup advance frame idle — tidak perlu override lagi di child.
     * Kecuali ada NPC yang punya perilaku khusus saat update.
     */
    @Override
    public void update() {
        advanceFrame(); // dari Character
    }

    // ── draw() masih abstract — tiap NPC override dengan posisi sendiri ───
    @Override
    public abstract void draw(Graphics2D g2d, int W, int H);

    // ── Utility draw yang bisa dipakai child class ────────────────────────
    /**
     * Helper draw sprite NPC dengan ukuran proporsional layar.
     * Dipanggil dari draw() di child class agar tidak duplikasi kode.
     *
     * @param g2d       Graphics2D
     * @param W         lebar layar
     * @param H         tinggi layar
     * @param npcX      posisi X NPC
     * @param facingLeft true = flip horizontal
     */
    protected void drawSprite(Graphics2D g2d, int W, int H,
                               int npcX, boolean facingLeft) {
        BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();

        if (active == null || idx >= active.length || active[idx] == null) return;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int npcH = (int)(H * 0.40);           // sedikit lebih kecil dari player
        int npcW = (int)(npcH * 0.79);        // rasio asli 912/1149 = 0.79
        int npcY = H - npcH - (int)(H * 0.09);

        if (facingLeft) {
            g2d.drawImage(active[idx], npcX + npcW, npcY, -npcW, npcH, null);
        } else {
            g2d.drawImage(active[idx], npcX, npcY, npcW, npcH, null);
        }
    }
}

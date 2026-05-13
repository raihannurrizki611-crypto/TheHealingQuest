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

/**
 * NPCNasgor.java — Mang Nasgor di Depan Mesjid.
 * Animasi memasak 6 frame.
 * Rasio asli sprite: 2014 × 1149 → lebar = tinggi × 1.754
 */
public class NPCNasgor extends NPC {

    // ── Posisi & ukuran ───────────────────────────────────────────────────
    // FACING_LEFT = false → sprite tidak diflip (tulisan gerobak terbaca normal)
    private static final int     NASGOR_X     = 460;   // posisi X gerobak
    private static final boolean FACING_LEFT  = false; // tidak diflip
    private static final double  RASIO_LEBAR  = 2014.0 / 1149.0; // ≈ 1.754
    private static final double  SKALA_TINGGI = 0.42;  // sesuaikan dengan background

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
        java.awt.image.BufferedImage[] active = getFrames();
        int idx = getCurrentFrame();
        if (active == null || idx >= active.length || active[idx] == null) return;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int npcH = (int)(H * SKALA_TINGGI);
        int npcW = (int)(npcH * RASIO_LEBAR);
        int npcY = H - npcH - (H / 20);

        // FACING_LEFT = false → gambar normal, tulisan gerobak terbaca benar
        if (FACING_LEFT) {
            g2d.drawImage(active[idx], NASGOR_X + npcW, npcY, -npcW, npcH, null);
        } else {
            g2d.drawImage(active[idx], NASGOR_X, npcY, npcW, npcH, null);
        }
    }

    public int getNasgorX() { return NASGOR_X; }
}

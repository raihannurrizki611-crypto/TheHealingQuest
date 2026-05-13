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
 * StressSystem.java — Bar beban pikiran (0–100).
 * Muncul sebentar saat berubah, lalu hilang otomatis.
 */
public class StressSystem {

    private int stressLevel = 50;
    private static final int MIN = 0, MAX = 100;

    private boolean visible      = false;
    private int     visibleTimer = 0;
    private static final int SHOW_DURATION = 180;

    private BufferedImage stressBarImg;

    public StressSystem(AssetLoader assets) {
        this.stressBarImg = assets.getStressBar();
    }

    public void increase(int n) { stressLevel = Math.min(MAX, stressLevel + n); showBar(); }
    public void decrease(int n) { stressLevel = Math.max(MIN, stressLevel - n); showBar(); }

    private void showBar() { visible = true; visibleTimer = SHOW_DURATION; }

    public void update() {
        if (visibleTimer > 0) { visibleTimer--; if (visibleTimer <= 0) visible = false; }
    }

    public void draw(Graphics2D g2d, int W, int H) {
        if (!visible) return;
        int barW = 200, barH = 20;
        int barX = W / 2 - barW / 2, barY = 16;

        g2d.setColor(new Color(30, 30, 30, 210));
        g2d.fillRoundRect(barX - 4, barY - 4, barW + 8, barH + 28, 10, 10);

        Color col = stressLevel >= 70 ? new Color(220,60,60)
                  : stressLevel >= 40 ? new Color(220,180,60)
                  : new Color(60,180,80);

        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRoundRect(barX, barY, barW, barH, 6, 6);

        int fillW = (int)((stressLevel / 100.0) * barW);
        if (fillW > 0) { g2d.setColor(col); g2d.fillRoundRect(barX, barY, fillW, barH, 6, 6); }

        if (stressBarImg != null)
            g2d.drawImage(stressBarImg, barX, barY, barW, barH, null);

        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.setColor(Color.WHITE);
        String label = "Beban Pikiran: " + stressLevel + "%";
        int lw = g2d.getFontMetrics().stringWidth(label);
        g2d.drawString(label, W/2 - lw/2, barY + barH + 14);

        if (visibleTimer < 40) {
            int alpha = (int)(255 * (1 - visibleTimer / 40f));
            g2d.setColor(new Color(0, 0, 0, Math.min(alpha, 255)));
            g2d.fillRoundRect(barX - 4, barY - 4, barW + 8, barH + 32, 10, 10);
        }
    }

    public int     getStressLevel() { return stressLevel; }
    public boolean isGameOver()     { return stressLevel >= 100; }
}

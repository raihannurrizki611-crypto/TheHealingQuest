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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * ChoiceSystem.java — Tampilkan 2 pilihan dengan ChoiceBox.png.
 * Tekan 1 atau 2 untuk memilih.
 */
public class ChoiceSystem {

    public enum ChoiceResult { NONE, CHOICE_1, CHOICE_2 }

    private boolean      active  = false;
    private String       option1, option2;
    private ChoiceResult result  = ChoiceResult.NONE;
    private BufferedImage choiceBoxImg;

    public ChoiceSystem(AssetLoader assets) {
        this.choiceBoxImg = assets.getChoiceBox();
    }

    public void show(String opt1, String opt2) {
        this.option1 = opt1;
        this.option2 = opt2;
        this.result  = ChoiceResult.NONE;
        this.active  = true;
    }

    public void handleKey(int keyCode) {
        if (!active) return;
        if (keyCode == KeyEvent.VK_1) { result = ChoiceResult.CHOICE_1; active = false; }
        if (keyCode == KeyEvent.VK_2) { result = ChoiceResult.CHOICE_2; active = false; }
    }

    public void draw(Graphics2D g2d, int W, int H) {
        if (!active) return;
        int boxW = 380, boxH = 110;
        int boxX = W / 2 - boxW / 2;
        int boxY = H / 2 - boxH / 2;

        if (choiceBoxImg != null) {
            g2d.drawImage(choiceBoxImg, boxX, boxY, boxW, boxH, null);
        } else {
            g2d.setColor(new Color(20, 20, 40, 220));
            g2d.fillRoundRect(boxX, boxY, boxW, boxH, 14, 14);
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.drawRoundRect(boxX, boxY, boxW, boxH, 14, 14);
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(255, 220, 80));
        g2d.drawString("[1]", boxX + 20, boxY + 38);
        g2d.drawString("[2]", boxX + 20, boxY + 76);

        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        g2d.setColor(Color.WHITE);
        g2d.drawString(option1, boxX + 52, boxY + 38);
        g2d.drawString(option2, boxX + 52, boxY + 76);
    }

    public boolean      isActive()  { return active; }
    public ChoiceResult getResult() { return result; }
    public boolean      hasResult() { return result != ChoiceResult.NONE; }
    public void         reset()     { result = ChoiceResult.NONE; active = false; }
}
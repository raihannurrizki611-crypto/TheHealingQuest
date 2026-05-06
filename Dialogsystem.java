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

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * DialogSystem.java
 * Cutscene (layar hitam) + Dialog (TextBox) + WA Chat (Phone.png).
 * KONSEP OOP : Enkapsulasi — semua field private
 */
public class Dialogsystem {

    public enum DialogState { CUTSCENE, DIALOG, WA_CHAT, FINISHED }

    // ── Data ──────────────────────────────────────────────────────────────
    private String[]      lines;
    private String[]      speakers;
    private DialogState[] lineStates;
    private int           currentIndex = 0;
    private DialogState   currentState = DialogState.FINISHED;

    // ── Typewriter ────────────────────────────────────────────────────────
    private int charIndex = 0;
    private int typeDelay = 0;
    private static final int TYPE_SPEED = 2;

    // ── Asset ─────────────────────────────────────────────────────────────
    private BufferedImage textBoxImg;
    private BufferedImage phoneImg;

    // ── Font ──────────────────────────────────────────────────────────────
    private static final Font DIALOG_FONT   = new Font("Arial", Font.PLAIN,  14);
    private static final Font NAME_FONT     = new Font("Arial", Font.BOLD,   14);
    private static final Font NARRATOR_FONT = new Font("Arial", Font.ITALIC, 15);
    private static final Font WA_FONT       = new Font("Arial", Font.PLAIN,  13);

    public Dialogsystem(AssetLoader assets) {
        this.textBoxImg = assets.getTextBox();
        this.phoneImg   = assets.getPhoneScreen();
    }

    // ── Start ─────────────────────────────────────────────────────────────
    public void start(String[] lines, String[] speakers) {
        this.lines        = lines;
        this.speakers     = speakers;
        this.lineStates   = buildLineStates(speakers);
        this.currentIndex = 0;
        this.charIndex    = 0;
        this.typeDelay    = 0;
        this.currentState = lineStates[0];
    }

    // ── Next (SPASI) ──────────────────────────────────────────────────────
    public void nextLine() {
        if (charIndex < lines[currentIndex].length()) {
            charIndex = lines[currentIndex].length();
            return;
        }
        currentIndex++;
        if (currentIndex >= lines.length) {
            currentState = DialogState.FINISHED;
            return;
        }
        currentState = lineStates[currentIndex];
        charIndex    = 0;
        typeDelay    = 0;
    }

    public void update() {
        if (currentState == DialogState.FINISHED) return;
        if (charIndex >= lines[currentIndex].length()) return;
        typeDelay++;
        if (typeDelay >= TYPE_SPEED) { charIndex++; typeDelay = 0; }
    }

    // ── Getter speaker saat ini ───────────────────────────────────────────
    /** Dipakai Game2D untuk tahu siapa yang sedang bicara (untuk sprite Radja) */
    public String getCurrentSpeaker() {
        if (currentState == DialogState.FINISHED) return "";
        return speakers[currentIndex];
    }

    // ── Draw ──────────────────────────────────────────────────────────────
    public void draw(Graphics2D g2d, int W, int H) {
        if (currentState == DialogState.FINISHED) return;
        switch (currentState) {
            case CUTSCENE: drawCutscene(g2d, W, H); break;
            case DIALOG:   drawDialog(g2d, W, H);   break;
            case WA_CHAT:  drawWAChat(g2d, W, H);   break;
        }
        if (charIndex >= lines[currentIndex].length()) drawSpaceHint(g2d, W, H);
    }

    // ─────────────────────────────────────────────────────────────────────
    private void drawCutscene(Graphics2D g2d, int W, int H) {
        g2d.setColor(new Color(0, 0, 0, 215));
        g2d.fillRect(0, 0, W, H);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String speaker = speakers[currentIndex];
        if (!speaker.isEmpty()) {
            g2d.setFont(NAME_FONT);
            g2d.setColor(new Color(255, 220, 100));
            int nw = g2d.getFontMetrics().stringWidth(speaker);
            g2d.drawString(speaker, W/2 - nw/2, H/2 - 30);
        }
        g2d.setFont(NARRATOR_FONT);
        g2d.setColor(Color.WHITE);
        drawWrapped(g2d, getCurrentText(), W/2, H/2 + 4, (int)(W * 0.65));
    }

    // ── FIX 1: Text stays inside the text box ────────────────────────────
    private void drawDialog(Graphics2D g2d, int W, int H) {
        int boxH = H / 4, boxY = H - boxH - 10, boxX = 10, boxW = W - 20;

        if (textBoxImg != null) {
            g2d.drawImage(textBoxImg, boxX, boxY, boxW, boxH, null);
        } else {
            g2d.setColor(new Color(0, 0, 0, 185));
            g2d.fillRoundRect(boxX, boxY, boxW, boxH, 14, 14);
            g2d.setColor(new Color(255, 255, 255, 70));
            g2d.drawRoundRect(boxX, boxY, boxW, boxH, 14, 14);
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ── Padding inside the text box image ────────────────────────────
        // Increase padTop if your TextBox.png has a thick top border,
        // e.g. try values between 35 and 65 until it looks right.
        int padLeft = 30;
        int padTop  = 50;

        String speaker = speakers[currentIndex];
        if (!speaker.isEmpty()) {
            g2d.setFont(NAME_FONT);
            g2d.setColor(new Color(255, 220, 100));
            g2d.drawString(speaker, boxX + padLeft, boxY + padTop);
        }

        g2d.setFont(DIALOG_FONT);
        g2d.setColor(Color.WHITE);

        // Text line sits 22px below the speaker name (or at padTop if no speaker)
        int textY = boxY + padTop + (speaker.isEmpty() ? 0 : 22);
        drawWrapped(g2d, getCurrentText(), boxX + padLeft, textY, boxW - padLeft * 2);
    }

    // ── WA Chat: tampilkan Phone.png + teks chat di atasnya ───────────────
    private void drawWAChat(Graphics2D g2d, int W, int H) {
        int phoneW = (int)(W * 0.55);
        int phoneH = (int)(H * 0.75);
        int phoneX = W / 2 - phoneW / 2;
        int phoneY = H / 2 - phoneH / 2;

        if (phoneImg != null) {
            g2d.drawImage(phoneImg, phoneX, phoneY, phoneW, phoneH, null);
        } else {
            // Fallback kotak hitam mirip HP
            g2d.setColor(new Color(20, 20, 20, 230));
            g2d.fillRoundRect(phoneX, phoneY, phoneW, phoneH, 20, 20);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRoundRect(phoneX, phoneY, phoneW, phoneH, 20, 20);
            // Header WA hijau
            g2d.setColor(new Color(37, 211, 102));
            g2d.fillRect(phoneX, phoneY, phoneW, 36);
            g2d.setFont(NAME_FONT);
            g2d.setColor(Color.WHITE);
            g2d.drawString("WhatsApp", phoneX + 12, phoneY + 22);
        }

        // Teks chat
        String speaker = speakers[currentIndex];
        String text    = getCurrentText();
        boolean isPlayer = speaker.equals("Player");

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(WA_FONT);
        FontMetrics fm = g2d.getFontMetrics();

        int bubbleMaxW = (int)(phoneW * 0.65);
        int bubbleH    = fm.getHeight() + 16;
        int bubbleY    = phoneY + phoneH / 2;

        if (isPlayer) {
            // Bubble kanan (hijau) — Player
            int bubbleX = phoneX + phoneW - bubbleMaxW - 16;
            g2d.setColor(new Color(37, 211, 102, 220));
            g2d.fillRoundRect(bubbleX, bubbleY, bubbleMaxW, bubbleH, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, bubbleX + 10, bubbleY + fm.getAscent() + 6);
        } else {
            // Bubble kiri (putih) — Mantan/lawan bicara
            int bubbleX = phoneX + 16;
            // Nama sender
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.setColor(new Color(255, 180, 80));
            g2d.drawString(speaker, bubbleX, bubbleY - 4);
            g2d.setFont(WA_FONT);

            g2d.setColor(new Color(255, 255, 255, 220));
            g2d.fillRoundRect(bubbleX, bubbleY, bubbleMaxW, bubbleH, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, bubbleX + 10, bubbleY + fm.getAscent() + 6);
        }
    }

    private void drawSpaceHint(Graphics2D g2d, int W, int H) {
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.setColor(new Color(200, 200, 200, 180));
        String hint = "[ SPASI ] lanjut";
        int hw = g2d.getFontMetrics().stringWidth(hint);
        g2d.drawString(hint, W - hw - 20, H - 16);
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private void drawWrapped(Graphics2D g2d, String text, int x, int y, int maxW) {
        FontMetrics fm = g2d.getFontMetrics();
        StringBuilder line = new StringBuilder();
        int lineY = y;
        for (String word : text.split(" ")) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > maxW && line.length() > 0) {
                g2d.drawString(line.toString(), x, lineY);
                lineY += fm.getHeight() + 2;
                line = new StringBuilder(word);
            } else { line = new StringBuilder(test); }
        }
        if (line.length() > 0) g2d.drawString(line.toString(), x, lineY);
    }

    private DialogState[] buildLineStates(String[] spk) {
        DialogState[] s = new DialogState[spk.length];
        for (int i = 0; i < spk.length; i++) {
            if (spk[i].isEmpty())                 s[i] = DialogState.CUTSCENE;
            else if (spk[i].equals("WA"))         s[i] = DialogState.WA_CHAT;
            else                                   s[i] = DialogState.DIALOG;
        }
        return s;
    }

    private String getCurrentText() {
        String full = lines[currentIndex];
        return full.substring(0, Math.min(charIndex, full.length()));
    }

    public DialogState getCurrentState() { return currentState; }
    public boolean isFinished()          { return currentState == DialogState.FINISHED; }
    public boolean isActive()            { return currentState != DialogState.FINISHED; }
}
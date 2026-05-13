/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lenovo
 */
package com.mycompany.thehealingquest;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class AssetLoader {

    // ── Player ────────────────────────────────────────────────────────────
    private BufferedImage[] walkFrames;
    private BufferedImage[] idleFrames;

    // ── Radja (3 mode) ────────────────────────────────────────────────────
    private BufferedImage[] radjaIdleFrames;
    private BufferedImage[] radjaTalkFrames;
    private BufferedImage[] radjaMarahFrames;

    // ── Ojol (3 mode + motor) ─────────────────────────────────────────────
    private BufferedImage[] ojolSantaiFrames;
    private BufferedImage[] ojolTalkFrames;
    private BufferedImage[] ojolIdleFrames;
    private BufferedImage   motorImg;

    // ── Nasgor ────────────────────────────────────────────────────────────
    private BufferedImage[] nasgorFrames;

    // ── Pacar (bad ending) ────────────────────────────────────────────────
    private BufferedImage[] pacarMarahFrames; // Pacar_Marah1-2

    // ── Background ────────────────────────────────────────────────────────
    private BufferedImage[] backgrounds;

    // ── UI ────────────────────────────────────────────────────────────────
    private BufferedImage textBox;
    private BufferedImage choiceBox;
    private BufferedImage stressBar;
    private BufferedImage phoneScreen;

    // ── Jumlah frame ──────────────────────────────────────────────────────
    private static final int WALK_COUNT        = 12;
    private static final int IDLE_COUNT        = 8;
    private static final int RADJA_IDLE_COUNT  = 12;
    private static final int RADJA_TALK_COUNT  = 2;
    private static final int RADJA_MARAH_COUNT = 2;
    private static final int OJOL_SANTAI_COUNT = 4;
    private static final int OJOL_TALK_COUNT   = 2;
    private static final int OJOL_IDLE_COUNT   = 4;
    private static final int NASGOR_COUNT      = 6;
    private static final int PACAR_MARAH_COUNT = 2;

    // ═════════════════════════════════════════════════════════════════════
    public AssetLoader() {
        walkFrames        = new BufferedImage[WALK_COUNT];
        idleFrames        = new BufferedImage[IDLE_COUNT];
        radjaIdleFrames   = new BufferedImage[RADJA_IDLE_COUNT];
        radjaTalkFrames   = new BufferedImage[RADJA_TALK_COUNT];
        radjaMarahFrames  = new BufferedImage[RADJA_MARAH_COUNT];
        ojolSantaiFrames  = new BufferedImage[OJOL_SANTAI_COUNT];
        ojolTalkFrames    = new BufferedImage[OJOL_TALK_COUNT];
        ojolIdleFrames    = new BufferedImage[OJOL_IDLE_COUNT];
        nasgorFrames      = new BufferedImage[NASGOR_COUNT];
        pacarMarahFrames  = new BufferedImage[PACAR_MARAH_COUNT];
        backgrounds       = new BufferedImage[SceneIndex.totalScenes()];

        loadPlayer();
        loadRadja();
        loadOjol();
        loadNasgor();
        loadPacar();
        loadBackgrounds();
        loadUI();
    }

    private void loadPlayer() {
        try {
            for (int i = 0; i < WALK_COUNT; i++)
                walkFrames[i] = load("/assets/Berjalan" + (i+1) + ".png");
            for (int i = 0; i < IDLE_COUNT; i++)
                idleFrames[i] = load("/assets/Idle_Animation" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Player: " + e.getMessage()); }
    }

    private void loadRadja() {
        try {
            for (int i = 0; i < RADJA_IDLE_COUNT; i++)
                radjaIdleFrames[i] = load("/assets/Radja_Idle" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Radja idle: " + e.getMessage()); }
        try {
            for (int i = 0; i < RADJA_TALK_COUNT; i++)
                radjaTalkFrames[i] = load("/assets/Radja_Talk" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Radja talk: " + e.getMessage()); }
        try {
            for (int i = 0; i < RADJA_MARAH_COUNT; i++)
                radjaMarahFrames[i] = load("/assets/Radja_Marah" + (i+1) + ".png");
        } catch (IOException e) {
            System.err.println("[AssetLoader] Radja marah fallback: " + e.getMessage());
            radjaMarahFrames = radjaTalkFrames;
        }
    }

    private void loadOjol() {
        try {
            for (int i = 0; i < OJOL_SANTAI_COUNT; i++)
                ojolSantaiFrames[i] = load("/assets/Ojol_Santai" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Ojol santai: " + e.getMessage()); }
        try {
            for (int i = 0; i < OJOL_TALK_COUNT; i++)
                ojolTalkFrames[i] = load("/assets/Ojol_Talk" + (i+1) + ".png");
        } catch (IOException e) {
            System.err.println("[AssetLoader] Ojol talk fallback: " + e.getMessage());
            ojolTalkFrames = new BufferedImage[]{ ojolSantaiFrames[0], ojolSantaiFrames[1] };
        }
        try {
            for (int i = 0; i < OJOL_IDLE_COUNT; i++)
                ojolIdleFrames[i] = load("/assets/Ojol_Panik" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Ojol panik: " + e.getMessage()); }
        try { motorImg = load("/assets/Motor.png"); }
        catch (IOException e) { System.err.println("[AssetLoader] Motor: " + e.getMessage()); }
    }

    private void loadNasgor() {
        try {
            for (int i = 0; i < NASGOR_COUNT; i++)
                nasgorFrames[i] = load("/assets/Nasgor" + (i+1) + ".png");
        } catch (IOException e) { System.err.println("[AssetLoader] Nasgor: " + e.getMessage()); }
    }

    private void loadPacar() {
        try {
            for (int i = 0; i < PACAR_MARAH_COUNT; i++)
                pacarMarahFrames[i] = load("/assets/Pacar_Marah" + (i+1) + ".png");
        } catch (IOException e) {
            System.err.println("[AssetLoader] Pacar marah (belum ada): " + e.getMessage());
            // Fallback: kotak merah sebagai placeholder
        }
    }

    private void loadBackgrounds() {
        try {
            for (SceneIndex s : SceneIndex.values())
                backgrounds[s.getIndex()] = load(s.getBgPath());
        } catch (IOException e) { System.err.println("[AssetLoader] Background: " + e.getMessage()); }
    }

    private void loadUI() {
        try { textBox     = load("/assets/TextBox.png");   } catch (IOException e) { System.err.println("[AssetLoader] TextBox: "    + e.getMessage()); }
        try { choiceBox   = load("/assets/ChoiceBox.png"); } catch (IOException e) { System.err.println("[AssetLoader] ChoiceBox: "  + e.getMessage()); }
        try { stressBar   = load("/assets/StressBar.png"); } catch (IOException e) { System.err.println("[AssetLoader] StressBar: "  + e.getMessage()); }
        try { phoneScreen = load("/assets/Phone.png");     } catch (IOException e) { System.err.println("[AssetLoader] Phone: "      + e.getMessage()); }
    }

    private BufferedImage load(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

    // ── Getter ────────────────────────────────────────────────────────────
    public BufferedImage[] getWalkFrames()       { return walkFrames; }
    public BufferedImage[] getIdleFrames()       { return idleFrames; }
    public BufferedImage[] getRadjaIdleFrames()  { return radjaIdleFrames; }
    public BufferedImage[] getRadjaTalkFrames()  { return radjaTalkFrames; }
    public BufferedImage[] getRadjaMarahFrames() { return radjaMarahFrames; }
    public BufferedImage[] getOjolSantaiFrames() { return ojolSantaiFrames; }
    public BufferedImage[] getOjolTalkFrames()   { return ojolTalkFrames; }
    public BufferedImage[] getOjolIdleFrames()   { return ojolIdleFrames; }
    public BufferedImage   getMotorImg()         { return motorImg; }
    public BufferedImage[] getNpcNasgorFrames()  { return nasgorFrames; }
    public BufferedImage[] getPacarMarahFrames() { return pacarMarahFrames; }
    public BufferedImage   getBackground(int i)  { return (i>=0&&i<backgrounds.length)?backgrounds[i]:null; }
    public BufferedImage   getBackground(SceneIndex s) { return backgrounds[s.getIndex()]; }
    public BufferedImage   getTextBox()          { return textBox; }
    public BufferedImage   getChoiceBox()        { return choiceBox; }
    public BufferedImage   getStressBar()        { return stressBar; }
    public BufferedImage   getPhoneScreen()      { return phoneScreen; }
}

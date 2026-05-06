/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lenovo
 */
package com.mycompany.thehealingquest;

/**
 * StoryManager.java
 * Semua percabangan cerita bagian 1.1, 1.2, dan 1.3 (ending).
 */
public class StoryManager {

    public enum StoryStage {
        // ── Intro ──────────────────────────────────────────────────────────
        INTRO,

        // ══ BAGIAN 1.1 ════════════════════════════════════════════════════
        PILIHAN_SAPA_RADJA,
        JALAN_TANPA_RADJA,
        NOTIF_MANTAN_TANPA_RADJA,
        PILIHAN_MANTAN_TANPA_RADJA,
        TOLAK_MANTAN_TANPA_RADJA,
        TERIMA_MANTAN_TANPA_RADJA,
        SAPA_RADJA,
        OBROLAN_AWAL_RADJA,
        NOTIF_MANTAN_SAAT_RADJA,
        PILIHAN_SAAT_NOTIF,
        BALAS_MANTAN,
        PILIHAN_MANTAN_SAAT_RADJA,
        TOLAK_MANTAN_KEMBALI_RADJA,
        TERIMA_MANTAN_RADJA_MARAH,
        RADJA_MARAH_DIALOG,
        PLAYER_MINTA_MAAF_RADJA,
        FOKUS_RADJA,
        GOOD_ENDING_1_1,
        BAD_ENDING_1_1,

        // ══ BAGIAN 1.2 ════════════════════════════════════════════════════
        MASUK_1_2,
        PILIHAN_OJOL,
        BODO_AMAT_OJOL,
        TANYA_OJOL,
        DIALOG_TANYA_OJOL,
        WA_MINTA_RADJA_GOOD,
        WA_MINTA_RADJA_BAD,
        DIALOG_MOTOR_BERES,
        WA_TERIMAKASIH_RADJA_GOOD,
        WA_TERIMAKASIH_RADJA_BAD,

        // ══ BAGIAN 1.3 ════════════════════════════════════════════════════
        MASUK_1_3,               // narasi tiba di Depan Mesjid
        DIALOG_PERPISAHAN_OJOL,  // dialog ucap terimakasih sama ojol
        TRIGGER_NASGOR,          // player mendekati nasgor
        DIALOG_NASGOR,           // obrolan dengan nasgor
        TUNGGU_PESANAN,          // narasi menunggu pesanan
        NOTIF_PACAR,             // notif WA dari pacar

        // ── Good ending ───────────────────────────────────────────────────
        WA_PACAR_GOOD,
        GOOD_ENDING_FINAL,

        // ── Bad ending ────────────────────────────────────────────────────
        WA_PACAR_BAD_HP,         // chat di HP dulu
        BAD_ENDING_PACAR_MUNCUL, // pacar muncul dari belakang
        BAD_ENDING_FINAL,

        // ── Layar ending ──────────────────────────────────────────────────
        LAYAR_GOOD_ENDING,
        LAYAR_BAD_ENDING,
        SELESAI
    }

    // ── State & flag ──────────────────────────────────────────────────────
    private StoryStage currentStage  = StoryStage.INTRO;
    private boolean    sapaRadja     = false;
    private boolean    selingkuh     = false;
    private boolean    radjaMarah    = false;
    private boolean    goodEnding11  = false;
    private boolean    bantuOjol     = false;

    // ── Getter / setter ───────────────────────────────────────────────────
    public StoryStage getCurrentStage()      { return currentStage; }
    public void       setStage(StoryStage s) { this.currentStage = s; }
    public boolean    isSapaRadja()          { return sapaRadja; }
    public boolean    isSelingkuh()          { return selingkuh; }
    public boolean    isRadjaMarah()         { return radjaMarah; }
    public boolean    isGoodEnding11()       { return goodEnding11; }
    public boolean    isBantuOjol()          { return bantuOjol; }

    // ═════════════════════════════════════════════════════════════════════
    //  BAGIAN 1.1
    // ═════════════════════════════════════════════════════════════════════
    public void pilihanSapaRadja(boolean sapa) {
        this.sapaRadja = sapa;
        currentStage   = sapa ? StoryStage.SAPA_RADJA : StoryStage.JALAN_TANPA_RADJA;
    }
    public void pilihanMantanTanpaRadja(boolean tolak) {
        this.selingkuh    = !tolak;
        this.goodEnding11 = tolak;
        currentStage = tolak ? StoryStage.TOLAK_MANTAN_TANPA_RADJA
                             : StoryStage.TERIMA_MANTAN_TANPA_RADJA;
    }
    public void pilihanSaatNotif(boolean balasMantan) {
        currentStage = balasMantan ? StoryStage.BALAS_MANTAN : StoryStage.FOKUS_RADJA;
    }
    public void pilihanMantanSaatRadja(boolean tolak) {
        this.selingkuh    = !tolak;
        this.radjaMarah   = !tolak;
        this.goodEnding11 = tolak;
        currentStage = tolak ? StoryStage.TOLAK_MANTAN_KEMBALI_RADJA
                             : StoryStage.TERIMA_MANTAN_RADJA_MARAH;
    }
    public void selesaiBagian11() { currentStage = StoryStage.MASUK_1_2; }

    // ═════════════════════════════════════════════════════════════════════
    //  BAGIAN 1.2
    // ═════════════════════════════════════════════════════════════════════
    public void pilihanOjol(boolean bantu) {
        this.bantuOjol = bantu;
        currentStage   = bantu ? StoryStage.TANYA_OJOL : StoryStage.BODO_AMAT_OJOL;
    }
    public void mintaPerkakas() {
        currentStage = goodEnding11 ? StoryStage.WA_MINTA_RADJA_GOOD
                                    : StoryStage.WA_MINTA_RADJA_BAD;
    }
    public void terimakasihRadja() {
        currentStage = goodEnding11 ? StoryStage.WA_TERIMAKASIH_RADJA_GOOD
                                    : StoryStage.WA_TERIMAKASIH_RADJA_BAD;
    }
    public void selesaiBagian12() { currentStage = StoryStage.MASUK_1_3; }

    // ═════════════════════════════════════════════════════════════════════
    //  BAGIAN 1.3
    // ═════════════════════════════════════════════════════════════════════
    /** Tentukan ending berdasarkan flag selingkuh */
    public void masukEnding() {
        currentStage = selingkuh ? StoryStage.WA_PACAR_BAD_HP
                                 : StoryStage.WA_PACAR_GOOD;
    }
    public void selesai() { currentStage = StoryStage.SELESAI; }
}
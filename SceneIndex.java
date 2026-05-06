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
 * SceneIndex.java
 * Enum untuk semua scene dalam game.
 *
 * URUTAN SCENE (kiri → kanan):
 *  PEGUNUNGAN_MALAM(0) ← DEPAN_RUMAH(1) → POS_RONDA(2) → DEPAN_MESJID(3)
 *
 * Batasan:
 *  - PEGUNUNGAN_MALAM : player tidak bisa ke kiri lagi (ujung kiri)
 *  - DEPAN_MESJID     : player tidak bisa ke kanan lagi (ujung kanan)
 */
public enum SceneIndex {

    PEGUNUNGAN_MALAM (0, "/assets/Pegunungan_Malam.png"),
    DEPAN_RUMAH      (1, "/assets/Depan_Rumah.png"),
    POS_RONDA        (2, "/assets/PosRonda.png"),
    DEPAN_MESJID     (3, "/assets/Depan_Mesjid.png");

    private final int    index;
    private final String bgPath;

    SceneIndex(int index, String bgPath) {
        this.index  = index;
        this.bgPath = bgPath;
    }

    public int    getIndex()  { return index; }
    public String getBgPath() { return bgPath; }

    /** Scene paling kiri — tidak bisa ke kiri lagi */
    public static int firstScene() { return PEGUNUNGAN_MALAM.index; }

    /** Scene paling kanan — tidak bisa ke kanan lagi */
    public static int lastScene()  { return DEPAN_MESJID.index; }

    public static SceneIndex fromIndex(int index) {
        for (SceneIndex s : values())
            if (s.index == index) return s;
        return DEPAN_RUMAH;
    }

    public static int totalScenes() { return values().length; }
}
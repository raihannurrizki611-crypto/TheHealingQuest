/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.thehealingquest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Game2D.java — Bagian 1.1 + 1.2 + 1.3 (Good/Bad Ending).
 */
public class Game2D extends JPanel implements ActionListener, KeyListener {

    private enum GameState { INTRO, PLAYING, DIALOG, CHOICE, ENDING }
    private GameState gameState = GameState.INTRO;

    // ── Sistem ────────────────────────────────────────────────────────────
    private AssetLoader  assets;
    private Dialogsystem dialogSystem;
    private ChoiceSystem choiceSystem;
    private StressSystem stressSystem;
    private StoryManager storyManager;

    // ── Karakter ──────────────────────────────────────────────────────────
    private Player    player;
    private NPCRadja  npcRadja;
    private NPCOjol   npcOjol;
    private NPCNasgor npcNasgor;
    private NPCPacar  npcPacar;

    // ── Scene ─────────────────────────────────────────────────────────────
    private int currentScene = SceneIndex.DEPAN_RUMAH.getIndex();

    // ── Transisi ──────────────────────────────────────────────────────────
    private boolean transitioning   = false;
    private int     transitionAlpha = 0;
    private int     transitionTarget;
    private boolean fadingOut       = true;
    private int     pendingSpawnX   = 60;

    // ── Flag trigger ──────────────────────────────────────────────────────
    private boolean triggerRadja    = false;
    private boolean triggerOjol     = false;
    private boolean triggerNasgor   = false;
    private static final int INTERACT_DIST = 140;

    // ── Layar ending ──────────────────────────────────────────────────────
    private boolean showEndingScreen = false;
    private boolean isGoodEnding     = false;
    private int     endingAlpha      = 0;   // fade in layar hitam
    private int     endingTextAlpha  = 0;   // fade in teks
    private static final int ENDING_FADE_SPEED = 3;

    private JFrame parentFrame;
    private Timer  timer;

    // ═════════════════════════════════════════════════════════════════════
    //  TEKS KUTIPAN ENDING
    // ═════════════════════════════════════════════════════════════════════
    private static final String KUTIPAN_GOOD =
        "\"Yang namanya manusia pasti selalu melakukan kesalahan dan pasti " +
        "selalu memiliki dosa, yang membedakan setiap manusia itu adalah " +
        "bagaimana cara mereka bertanggung jawab atas setiap kesalahan " +
        "yang telah mereka perbuat.\"";

    private static final String KUTIPAN_BAD =
        "\"Orang yang menghianatimu sekali pasti akan menghianatimu " +
        "berkali-kali. Tidak perlu meminum seluruh air laut untuk tahu " +
        "kalau air laut itu asin.\"";

    // ═════════════════════════════════════════════════════════════════════
    //  DIALOG BAGIAN 1.1
    // ═════════════════════════════════════════════════════════════════════
    private static final String[] INTRO_LINES = {
        "Malam itu, layar komputer tiba-tiba mati. Blue screen.",
        "Tumpukan tugas belum selesai. Kepala terasa penuh.",
        "Hadehh, ni tugas kapan beresnya ya",
        "Bisa gila gw lama-lama ini, lagian kenapa sih ni komputer...",
        "Capek bener gw...", "Gimana kalau keluar rumah dulu yaa? hmm",
        "Kek nya gw mending keluar dulu dah, sekalian jajan lah ya",
        "Ia pun melangkah keluar. Udara malam menyambutnya.",
        "*menghirup udara*  Huftt... seger juga udara malem ini.",
        "Sepi juga ya hari ini, perasaan kemarin jam segini masih rame.",
        "Hmm keknya jalan kaki aja deh yaa, mumpung sepi gini.."
    };
    private static final String[] INTRO_SPEAKERS = {
        "","","Player","Player","Player","Player","Player",
        "","Player","Player","Player"
    };

    private static final String[] LEWAT_RADJA_LINES = {
        "Permisi ya...", "Eh ada Radja, kayaknya lagi santai. Yaudah lanjut aja.",
        "Tiba-tiba hp bergetar. Ada notifikasi masuk."
    };
    private static final String[] LEWAT_RADJA_SPEAKERS = { "Player","Player","" };

    private static final String[] WA_MANTAN_LINES = {
        "p","p","p","p","Kamuu apaa kabar pal?","p","p","pal?",
        "Kamuu masih marah yaa sama akuu?",
        "Jir, kenapa lu?","Tumbenan ngechat","Kangenn","Balikan yuu pal"
    };
    private static final String[] WA_MANTAN_SPEAKERS = {
        "WA","WA","WA","WA","WA","WA","WA","WA","WA","WA","WA","WA","WA"
    };

    private static final String[] WA_TOLAK_LINES = {
        "Sorry gue udah ada yang baru",
        "Plsss, gw kangen banget sama lu 👉👈",
        "Udah tinggalin aja pacar lu yang sekarang, mending sama gw aja",
        "Idih najis, orang lu yang mutusin hubungan kita waktu itu",
        "*blokir nomor*","Cuh, lucu kali yaa","Dia yang pergi, dia juga yang ngajak balikan"
    };
    private static final String[] WA_TOLAK_SPEAKERS = {
        "WA","WA","WA","WA","","Player","Player"
    };

    private static final String[] WA_TERIMA_LINES = {
        "Jujur aku juga kangen jul","Gimana kalau kita balikan?",
        "Eh asliiii kamu mau balikan sama akuu 👉👈",
        "Gimana sih kamuu, kan tadi kamu yang ngajak balikan 😒",
        "Hehee, iyaa sihhh wkwkwkk",
        "Dan begitulah... mereka balikan. Padahal player sudah punya pacar."
    };
    private static final String[] WA_TERIMA_SPEAKERS = { "WA","WA","WA","WA","WA","" };

    private static final String[] SAPA_RADJA_LINES = {
        "Eyy yow Djaa, kumaha damang? *bersalaman*",
        "Ehh pal, alhamdulillah damang, ai kamu gimana",
        "Alhamdulillah sehat juga, btw lu kenapa",
        "Kayak yang sedih gitu gw liat-liat",
        "Iya nihh, urang kan tadi ngungkapin perasaan ke si Salwa",
        "Hah? Salwa? Salwa mana yeuh? Salwa SMP lain?",
        "Heeh si Salwa SMP..","Jirr pantes mane kayak yang caper ke si Salwa",
        "Gess ah, jangan di bahas soal eta. Era njirr","Haha karunya pican..",
        "Tiba-tiba notifikasi hp bergetar. Ada pesan masuk dari nomor tak dikenal..."
    };
    private static final String[] SAPA_RADJA_SPEAKERS = {
        "Player","Radja","Player","Player","Radja","Player","Radja",
        "Player","Radja","Player",""
    };

    private static final String[] WA_MANTAN_SAAT_RADJA_LINES = {
        "p","p","p","p","Kamuu apaa kabar pal?",
        "Kamuu masih marah yaa sama akuu?","Kangenn","Balikan yuu pal"
    };
    private static final String[] WA_MANTAN_SAAT_RADJA_SPEAKERS = {
        "WA","WA","WA","WA","WA","WA","WA","WA"
    };

    private static final String[] WA_TOLAK_KEMBALI_LINES = {
        "Sorry gue udah ada yang baru","*blokir nomor*",
        "Cuh, lucu kali yaa. Dia yang pergi, dia juga yang ngajak balikan.",
        "Player memasukkan hp dan kembali fokus ke Radja."
    };
    private static final String[] WA_TOLAK_KEMBALI_SPEAKERS = { "WA","","Player","" };

    private static final String[] WA_TERIMA_RADJA_LINES = {
        "Jujur aku juga kangen jul","Gimana kalau kita balikan?",
        "Eh asliiii kamu mau balikan sama akuu 👉👈","Hehee, iyaa sihhh wkwkwkk",
        "Chattan sama siapa sih mane?!!","Fokus amat ke hp?",
        "Ini mantan gw tiba-tiba ngechat minta balikan",
        "Lah terus daritadi lu ga dengerin gua ngomong?!!","Cukup tau sih",
        "Eh-eh ga gitu Dja","Ngges ah ga mood urang","Balik aja lah urang, nuhun ya"
    };
    private static final String[] WA_TERIMA_RADJA_SPEAKERS = {
        "WA","WA","WA","WA","Radja","Radja","Player","Radja","Radja","Player","Radja","Radja"
    };

    private static final String[] MINTA_MAAF_LINES = {
        "Eh sumpah gw minta maaf cok, kaget anjir mantan tiba-tiba nge chat",
        "Bodo amat! Udah mending lu sana pergi urusin mantan elo!",
        "Radja tidak mau berbicara lagi. Ia berbalik pergi."
    };
    private static final String[] MINTA_MAAF_SPEAKERS = { "Player","Radja","" };

    private static final String[] FOKUS_RADJA_LINES = {
        "Siapa pal?","Mantan gw, ngajak balikan dia...","Trus trus gimana?",
        "Gue si bodoamat yee, udah punya yang baru kata gue teh...",
        "Edann kelazz king..","Ah biasa aja, lagian ga ada keuntungan nya ngobrol sama dia",
        "Dia pacaran sama gue cuman karena urang banyak duit aja",
        "Njir di manfaatin mane","Iyalah coo, makanya urang putusin dia",
        "Obrolan mengalir panjang dari jam 19:00 sampai 19:45...",
        "Yaudahlah ya pal, udah malem juga ini",
        "Eh iyaa jirr, ga kerasa coo kita ngobrol lama juga ya",
        "Iya lagi, iya lagi, iya lagi...","Yodahlah urang duluan ya Dja","Yoyoy hati-hati pal!"
    };
    private static final String[] FOKUS_RADJA_SPEAKERS = {
        "Radja","Player","Radja","Player","Radja","Player","Player",
        "Radja","Player","","Radja","Player","Radja","Player","Radja"
    };

    private static final String[] GOOD_ENDING_11_LINES = {
        "Obrolan malam itu terasa ringan.",
        "Beban yang tadi memenuhi kepala, perlahan berkurang.",
        "Kadang yang kita butuhkan hanya seseorang yang mau mendengarkan."
    };
    private static final String[] GOOD_ENDING_11_SPEAKERS = { "","","" };

    private static final String[] BAD_ENDING_11_LINES = {
        "Malam itu terasa lebih berat dari sebelumnya.",
        "Ada rasa bersalah yang mengganjal.",
        "Perjalanan masih panjang. Dan kepala makin penuh."
    };
    private static final String[] BAD_ENDING_11_SPEAKERS = { "","","" };

    // ═════════════════════════════════════════════════════════════════════
    //  DIALOG BAGIAN 1.2
    // ═════════════════════════════════════════════════════════════════════
    private static final String[] MASUK_1_2_LINES = {
        "Jam 19:45. Player melanjutkan perjalanan ke Pegunungan.",
        "Udara malam terasa sejuk. Di kejauhan ada seseorang berdiri di pinggir jalan."
    };
    private static final String[] MASUK_1_2_SPEAKERS = { "","" };

    private static final String[] BODO_AMAT_OJOL_LINES = {
        "Ah biarin aja, gw mau santai dulu nih.",
        "Player bersantai menikmati pemandangan malam dari jembatan.",
        "Jam 19:45 sampai 20:20 berlalu dengan tenang.",
        "Perut mulai lapar. Mending makan dulu deh.",
        "Nasi goreng Mang Nasgor kayaknya enak nih."
    };
    private static final String[] BODO_AMAT_OJOL_SPEAKERS = { "Player","","","Player","Player" };

    private static final String[] DIALOG_TANYA_OJOL_LINES = {
        "Mogok kang motornya?","Eh iyaa kang mogok euy",
        "Alah karunya, kalau boleh tau awalnya gimana kang?",
        "Yaa gitu we kang mendet-mendet si motornya",
        "Padahal baru isi bensin tadi di depan","Hadeuh, aya aya wae nya kang",
        "Oiya kang, kira-kira bengkel deket sini dimana yaa?",
        "Ada deket kang, di deket tukang nasgor. Tapi malem gini biasanya udah tutup sih",
        "Ouh gitu yaa.. mau gamau harus dorong haha...",
        "Eh kang mau aku bantuin gaa",
        "Kebetulan aku ada pengalaman benerin motor nih kang",
        "Eh beneran nih kang? Ga enak saya",
        "Ya bener atuh kang, lagian akang mau jalan sampai mana",
        "Pasti udah pada tutup juga kalau ada",
        "Iya juga sih, tapi beneran nih gapapa? Takut ngerepotin akang",
        "Ahh nyantai aja we kang, btw akang ada peralatannya ga yaa",
        "Nah itu euy kang ga punya saya","Waduh, bentar atuh ya kang"
    };
    private static final String[] DIALOG_TANYA_OJOL_SPEAKERS = {
        "Player","Ojol","Player","Ojol","Ojol","Player",
        "Ojol","Player","Ojol","Player","Player","Ojol",
        "Player","Player","Ojol","Player","Ojol","Player"
    };

    private static final String[] WA_RADJA_GOOD_LINES = {
        "p","p","p","Radja bisa tolong bantuin gw gak?",
        "Kenapa pal","Ini ada kang ojol, mogok motornya",
        "Kamu punya perkakas bengkel gaa euy",
        "Kalau gasalah sih adaa, bentarr","Okee",
        "Singkat cerita, Radja membawa perkakas yang dibutuhkan.",
        "Radja memberikan perkakas lalu pergi karena ada urusan."
    };
    private static final String[] WA_RADJA_GOOD_SPEAKERS = {
        "WA","WA","WA","WA","WA","WA","WA","WA","WA","",""
    };

    private static final String[] WA_RADJA_BAD_LINES = {
        "p","p","p","Radja bisa tolong bantuin gw gak?",
        "Apaan sih, ngapain ngechat?!","Udah beres urusan mantan lo?",
        "Bisa bantuin gw ga pls, ini ada kang ojol mogok motornya",
        "Gw mau minjem, kasian ini tukang ojol ga bisa pulang",
        "Harus banget ke gw gitu minjemnya?","Soalnya lu yang paling deket ey",
        "Yaudah iyalah, kalau bukan karena kasian ke si ojol gw ga akan bantu lo",
        "Singkat cerita, Radja membawa perkakas lalu langsung pergi.",
        "Ia tidak ingin berlama-lama bersama player."
    };
    private static final String[] WA_RADJA_BAD_SPEAKERS = {
        "WA","WA","WA","WA","WA","WA","WA","WA","WA","WA","WA","",""
    };

    private static final String[] MOTOR_BERES_LINES = {
        "Alhamdulillah akhirnya bisa nyala juga",
        "Alhamdulillah, nuhun pisan yaa kang udah bantuin saya",
        "Maaf saya ga bisa ngasih banyak kang",
        "Ehh gapapa kang, buat akang aja uangnya",
        "Eh gapapa kang ambil aja, bentuk terimakasih saya...",
        "Aduh ga enak gini, makasii pisan ya kang",
        "Yaudah atuh yaa kang, saya dluan, makasii banget sebelumnya",
        "Eh iya sama sama kang",
        "Eh bentar kang, boleh ga yaa ikut sampe tukang nasgor di depan mesjid?",
        "Yaa bolehh atuh kang, hayu naik",
        "Alhamdulillah ga harus jalan lagii haha"
    };
    private static final String[] MOTOR_BERES_SPEAKERS = {
        "Player","Ojol","Ojol","Player","Ojol","Player","Ojol",
        "Player","Player","Ojol","Player"
    };

    private static final String[] WA_TRIMAKASIH_GOOD_LINES = {
        "Makasih ya Ja, udah mau minjemin peralatannya",
        "Iya sama-sama, btw jangan lupa di balikin ya","Aman aja santai",
        "Player pun naik motor Ojol menuju Depan Mesjid."
    };
    private static final String[] WA_TRIMAKASIH_GOOD_SPEAKERS = { "WA","WA","WA","" };

    private static final String[] WA_TRIMAKASIH_BAD_LINES = {
        "Makasih ya Ja, udah mau minjemin peralatannya",
        "Y","Balikin ya itu peralatan punya gw.","Aman aja santai",
        "Player pun naik motor Ojol menuju Depan Mesjid."
    };
    private static final String[] WA_TRIMAKASIH_BAD_SPEAKERS = { "WA","WA","WA","WA","" };

    // ═════════════════════════════════════════════════════════════════════
    //  DIALOG BAGIAN 1.3
    // ═════════════════════════════════════════════════════════════════════

    // Tiba di Depan Mesjid + perpisahan Ojol
    private static final String[] MASUK_1_3_LINES = {
        "Sampai juga di depan Mesjid. Aroma nasi goreng tercium dari kejauhan.",
        "Kang nuhun pisan udah nganterin",
        "Iya kang sama sama, saya dluan yaa kang. Assalamualaikum",
        "Waalaikumsalam, hati-hati kang!",
        "Ojol pun berlalu. Player berjalan mendekati warung Mang Nasgor."
    };
    private static final String[] MASUK_1_3_SPEAKERS = {
        "","Player","Ojol","Player",""
    };

    // Dialog dengan Mang Nasgor
    private static final String[] DIALOG_NASGOR_LINES = {
        "Mang kumaha damang?",
        "Eh si akang, alhamdulillah damang",
        "Alhamdulillah atuhh",
        "Mau beli apa kang?",
        "Biasa mang, apalagi sih saya kalau beli kesini, hehe",
        "Siap atuh, nasgor komplit pedesnya sedeng",
        "Sama kwetiau ga pake sayur nya ga pedas kan?",
        "Edan euy sampai ke level pedes na ge apalan si amang",
        "Bisa wae si akang mah, duduk dlu atuh kang",
        "Gausah lah mang, saya juga gak akan lama",
        "Nasi goreng sama kwetiau nya di bungkus aja"
    };
    private static final String[] DIALOG_NASGOR_SPEAKERS = {
        "Player","Mang Nasgor","Player","Mang Nasgor","Player",
        "Mang Nasgor","Mang Nasgor","Player","Mang Nasgor","Player","Player"
    };

    // Narasi menunggu pesanan
    private static final String[] TUNGGU_PESANAN_LINES = {
        "Beberapa saat kemudian...",
        "Sambil menunggu, player mengeluarkan hp.",
        "Ada notifikasi masuk. Dari seseorang yang familiar..."
    };
    private static final String[] TUNGGU_PESANAN_SPEAKERS = { "","","" };

    // ── GOOD ENDING — WA pacar ────────────────────────────────────────────
    private static final String[] WA_PACAR_GOOD_LINES = {
        "byy","byy","byy",
        "Kenapaaa sayyangg?",
        "Kamuu lagi dimanaa?","Akuuu lagi sendirian dirumahh",
        "Kita main yuu keluar, akuuu gaa beranii sendirii",
        "Sini sayang, aku lagi di nasgor langganan nih",
        "Jemput dongg sayangg",
        "Aku ga bawa motor eughh, ai kamuu di rumah ada motor gaa?",
        "Adaaa, kenapa emangnyaaa",
        "Aku kesanaa yaa, pake motor kamuu aja",
        "Nanti habis makan kita jalan-jalan okeyy",
        "Okee byy, aku siap-siap dulu yaaa",
        "Okee, akuu otewee",
        "Malam itu, dengan kehadiran sang pacar, semua beban perlahan menghilang.",
        "Bahkan blue screen dan tugas yang menumpuk pun terasa lebih ringan."
    };
    private static final String[] WA_PACAR_GOOD_SPEAKERS = {
        "WA","WA","WA",
        "Player","WA","WA","WA",
        "Player","WA",
        "Player","WA",
        "Player","Player",
        "WA","Player","",""
    };

    // ── BAD ENDING — WA pacar di HP dulu ─────────────────────────────────
    private static final String[] WA_PACAR_BAD_HP_LINES = {
        "p","p","p",
        "Kenapaaa sayyangg?",
        "Km lagi dimana?",
        "Lagi di Mang Nasgor langganan nih nyari angin, kenapa byy?"
    };
    private static final String[] WA_PACAR_BAD_HP_SPEAKERS = {
        "WA","WA","WA","Player","WA","Player"
    };

    // ── BAD ENDING — pacar muncul dari belakang ───────────────────────────
    private static final String[] BAD_ENDING_PACAR_LINES = {
        "*Tiba-tiba pundak Player ditepuk kencang dari belakang*",
        "Oh nyari angin... sambil asyik balikan sama mantan?!",
        "Eh-eh?! Sayang... kok kamu di sini?",
        "Halah gausah ngeles! Yang tadi ngechat pakai nomor Jule itu AKU!",
        "Ternyata bener yaa kata temen-temen, kamu emang ga bisa dipercaya!",
        "Hah gimana-gimana... jadi itu ketikan kamu?!",
        "Hih gimini gimin... jidi iti kitikin kimi?!",
        "Udah deh jangan banyak omong.",
        "Mulai sekarang kita PUTUS."
    };
    private static final String[] BAD_ENDING_PACAR_SPEAKERS = {
        "","Pacar 💗","Player","Pacar 💗","Pacar 💗",
        "Player","Pacar 💗","Pacar 💗","Pacar 💗"
    };

    // ═════════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ═════════════════════════════════════════════════════════════════════
    public Game2D() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        assets       = new AssetLoader();
        dialogSystem = new Dialogsystem(assets);
        choiceSystem = new ChoiceSystem(assets);
        stressSystem = new StressSystem(assets);
        storyManager = new StoryManager();

        player    = new Player(assets, 60, 0);
        npcRadja  = new NPCRadja(assets);
        npcOjol   = new NPCOjol(assets);
        npcNasgor = new NPCNasgor(assets);
        npcPacar  = new NPCPacar(assets);

        dialogSystem.start(INTRO_LINES, INTRO_SPEAKERS);
        storyManager.setStage(StoryManager.StoryStage.INTRO);
        gameState = GameState.INTRO;

        timer = new Timer(16, this);
        timer.start();
    }

    public void setParentFrame(JFrame f) { this.parentFrame = f; }

    // ═════════════════════════════════════════════════════════════════════
    //  PAINT
    // ═════════════════════════════════════════════════════════════════════
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int W = getWidth(), H = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // ── Layar ending (hitam + teks) ───────────────────────────────────
        if (gameState == GameState.ENDING) {
            drawEndingScreen(g2d, W, H);
            return;
        }

        boolean isCutscene = (gameState == GameState.INTRO || gameState == GameState.DIALOG)
                && dialogSystem.getCurrentState() == Dialogsystem.DialogState.CUTSCENE;

        if (isCutscene) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, W, H);
        } else {
            BufferedImage bg = assets.getBackground(currentScene);
            if (bg != null) g2d.drawImage(bg, 0, 0, W, H, null);
        }

        if (!isCutscene) {
            player.draw(g2d, W, H);
            if (currentScene == SceneIndex.POS_RONDA.getIndex())
                npcRadja.draw(g2d, W, H);
            if (currentScene == SceneIndex.PEGUNUNGAN_MALAM.getIndex())
                npcOjol.draw(g2d, W, H);
            if (currentScene == SceneIndex.DEPAN_MESJID.getIndex()) {
                npcNasgor.draw(g2d, W, H);
                // Pacar hanya muncul di bad ending
                if (storyManager.isSelingkuh() &&
                    storyManager.getCurrentStage() == StoryManager.StoryStage.BAD_ENDING_PACAR_MUNCUL)
                    npcPacar.draw(g2d, W, H);
            }
        }

        // Update sprite Radja saat dialog
        if (gameState == GameState.DIALOG &&
                currentScene == SceneIndex.POS_RONDA.getIndex() &&
                npcRadja.getSpriteMode() == NPCRadja.SpriteMode.BICARA) {
            npcRadja.setPlayerSedangBicara(dialogSystem.getCurrentSpeaker().equals("Player"));
        }

        // Update sprite Ojol saat dialog
        if (gameState == GameState.DIALOG &&
                currentScene == SceneIndex.PEGUNUNGAN_MALAM.getIndex() &&
                npcOjol.getSpriteMode() == NPCOjol.SpriteMode.BICARA) {
            npcOjol.setPlayerSedangBicara(dialogSystem.getCurrentSpeaker().equals("Player"));
        }

        // Update sprite Pacar saat dialog bad ending
        if (gameState == GameState.DIALOG &&
                storyManager.getCurrentStage() == StoryManager.StoryStage.BAD_ENDING_PACAR_MUNCUL) {
            npcPacar.setPlayerSedangBicara(dialogSystem.getCurrentSpeaker().equals("Player"));
        }

        stressSystem.draw(g2d, W, H);

        if (gameState == GameState.INTRO || gameState == GameState.DIALOG)
            dialogSystem.draw(g2d, W, H);
        if (gameState == GameState.CHOICE)
            choiceSystem.draw(g2d, W, H);

        if (transitioning) {
            g2d.setColor(new Color(0, 0, 0, Math.min(transitionAlpha, 255)));
            g2d.fillRect(0, 0, W, H);
        }
    }

    // ── Gambar layar ending hitam + teks ─────────────────────────────────
    private void drawEndingScreen(Graphics2D g2d, int W, int H) {
        // Fade in hitam
        g2d.setColor(new Color(0, 0, 0, Math.min(endingAlpha, 255)));
        g2d.fillRect(0, 0, W, H);

        if (endingAlpha < 255) return;

        // Fade in teks setelah layar hitam penuh
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int textAlpha = Math.min(endingTextAlpha, 255);

        // Label GOOD / BAD ENDING
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String endingLabel = isGoodEnding ? "✦ GOOD ENDING ✦" : "✦ BAD ENDING ✦";
        Color endingColor  = isGoodEnding
            ? new Color(100, 220, 120, textAlpha)
            : new Color(220, 80,  80,  textAlpha);
        g2d.setColor(endingColor);
        int labelW = g2d.getFontMetrics().stringWidth(endingLabel);
        g2d.drawString(endingLabel, W/2 - labelW/2, H/2 - 60);

        // Kutipan
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        g2d.setColor(new Color(220, 220, 220, textAlpha));
        String kutipan = isGoodEnding ? KUTIPAN_GOOD : KUTIPAN_BAD;
        drawWrappedCentered(g2d, kutipan, W/2, H/2 - 10, (int)(W * 0.70));

        // Tekan SPASI
        if (endingTextAlpha >= 200) {
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.setColor(new Color(160, 160, 160, Math.min(endingTextAlpha, 255)));
            String hint = "[ SPASI ] keluar";
            int hw = g2d.getFontMetrics().stringWidth(hint);
            g2d.drawString(hint, W/2 - hw/2, H - 30);
        }
    }

    private void drawWrappedCentered(Graphics2D g2d, String text, int cx, int y, int maxW) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > maxW && line.length() > 0) {
                int lw = fm.stringWidth(line.toString());
                g2d.drawString(line.toString(), cx - lw/2, lineY);
                lineY += fm.getHeight() + 4;
                line = new StringBuilder(word);
            } else { line = new StringBuilder(test); }
        }
        if (line.length() > 0) {
            int lw = fm.stringWidth(line.toString());
            g2d.drawString(line.toString(), cx - lw/2, lineY);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ═════════════════════════════════════════════════════════════════════
    @Override
    public void actionPerformed(ActionEvent e) {
        if (transitioning) { handleTransition(); repaint(); return; }

        stressSystem.update();

        // Animasi fade-in layar ending
        if (gameState == GameState.ENDING) {
            if (endingAlpha < 255)     endingAlpha     = Math.min(255, endingAlpha + ENDING_FADE_SPEED);
            else if (endingTextAlpha < 255) endingTextAlpha = Math.min(255, endingTextAlpha + 2);
            repaint(); return;
        }

        switch (gameState) {
            case INTRO: case DIALOG:
                dialogSystem.update();
                if (dialogSystem.isFinished()) handleDialogFinished();
                break;
            case CHOICE:
                if (choiceSystem.hasResult()) handleChoiceResult();
                break;
            case PLAYING:
                updatePlaying();
                break;
        }
        repaint();
    }

    private void handleTransition() {
        if (fadingOut) {
            transitionAlpha += 15;
            if (transitionAlpha >= 255) {
                currentScene = transitionTarget;
                player.setX(pendingSpawnX);
                player.setSceneIndex(currentScene);
                fadingOut = false;
            }
        } else {
            transitionAlpha -= 15;
            if (transitionAlpha <= 0) { transitionAlpha = 0; transitioning = false; }
        }
    }

    private void updatePlaying() {
        player.update();
        if (currentScene == SceneIndex.POS_RONDA.getIndex())        npcRadja.update();
        if (currentScene == SceneIndex.PEGUNUNGAN_MALAM.getIndex()) npcOjol.update();
        if (currentScene == SceneIndex.DEPAN_MESJID.getIndex())     npcNasgor.update();
        checkRadjaTrigger();
        checkOjolTrigger();
        checkNasgorTrigger();
        checkSceneChange();
    }

    private void checkRadjaTrigger() {
        if (triggerRadja || currentScene != SceneIndex.POS_RONDA.getIndex()) return;
        if (Math.abs(player.getX() - npcRadja.getX()) < INTERACT_DIST) {
            triggerRadja = true;
            choiceSystem.show("Sapa Radja  (eyy yow djaa!)", "Permisi aja, lanjut jalan");
            gameState = GameState.CHOICE;
            storyManager.setStage(StoryManager.StoryStage.PILIHAN_SAPA_RADJA);
        }
    }

    private void checkOjolTrigger() {
        if (triggerOjol || currentScene != SceneIndex.PEGUNUNGAN_MALAM.getIndex()) return;
        if (Math.abs(player.getX() - npcOjol.getX()) < INTERACT_DIST) {
            triggerOjol = true;
            npcOjol.setSpriteMode(NPCOjol.SpriteMode.PANIK);
            choiceSystem.show("Tanya ada masalah apa", "Bodo amat, lewat aja");
            gameState = GameState.CHOICE;
            storyManager.setStage(StoryManager.StoryStage.PILIHAN_OJOL);
        }
    }

    private void checkNasgorTrigger() {
        if (triggerNasgor || currentScene != SceneIndex.DEPAN_MESJID.getIndex()) return;
        if (Math.abs(player.getX() - npcNasgor.getX()) < INTERACT_DIST) {
            triggerNasgor = true;
            npcNasgor.update();
            dialogSystem.start(DIALOG_NASGOR_LINES, DIALOG_NASGOR_SPEAKERS);
            gameState = GameState.DIALOG;
            storyManager.setStage(StoryManager.StoryStage.DIALOG_NASGOR);
        }
    }

    private void checkSceneChange() {
        int px = player.getX();
        if (px < -30 && currentScene > SceneIndex.firstScene())
            goToScene(currentScene - 1, getWidth() - 180);
        if (px > getWidth() - 30 && currentScene < SceneIndex.lastScene())
            goToScene(currentScene + 1, 30);
        if (px < 10 && currentScene == SceneIndex.firstScene())     player.setX(10);
        if (px > getWidth()-60 && currentScene == SceneIndex.lastScene()) player.setX(getWidth()-60);
    }

    private void goToScene(int idx, int spawnX) {
        if (transitioning) return;
        transitionTarget = idx; transitioning = true;
        fadingOut = true; transitionAlpha = 0; pendingSpawnX = spawnX;
    }

    // ═════════════════════════════════════════════════════════════════════
    //  HANDLE DIALOG SELESAI
    // ═════════════════════════════════════════════════════════════════════
    private void handleDialogFinished() {
        StoryManager.StoryStage stage = storyManager.getCurrentStage();
        switch (stage) {

            // ── Bagian 1.1 ───────────────────────────────────────────────
            case INTRO:
                gameState = GameState.PLAYING;
                storyManager.setStage(StoryManager.StoryStage.PILIHAN_SAPA_RADJA);
                break;
            case JALAN_TANPA_RADJA:
                dialogSystem.start(WA_MANTAN_LINES, WA_MANTAN_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.NOTIF_MANTAN_TANPA_RADJA);
                break;
            case NOTIF_MANTAN_TANPA_RADJA:
                choiceSystem.show("Tolak ajakan balikan", "Terima ajakan balikan");
                gameState = GameState.CHOICE;
                storyManager.setStage(StoryManager.StoryStage.PILIHAN_MANTAN_TANPA_RADJA);
                break;
            case TOLAK_MANTAN_TANPA_RADJA:
                stressSystem.decrease(10);
                dialogSystem.start(GOOD_ENDING_11_LINES, GOOD_ENDING_11_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.GOOD_ENDING_1_1);
                break;
            case TERIMA_MANTAN_TANPA_RADJA:
                stressSystem.increase(5);
                dialogSystem.start(BAD_ENDING_11_LINES, BAD_ENDING_11_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.BAD_ENDING_1_1);
                break;
            case SAPA_RADJA:
                npcRadja.setSpriteMode(NPCRadja.SpriteMode.BICARA);
                dialogSystem.start(SAPA_RADJA_LINES, SAPA_RADJA_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.OBROLAN_AWAL_RADJA);
                break;
            case OBROLAN_AWAL_RADJA:
                dialogSystem.start(WA_MANTAN_SAAT_RADJA_LINES, WA_MANTAN_SAAT_RADJA_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.NOTIF_MANTAN_SAAT_RADJA);
                break;
            case NOTIF_MANTAN_SAAT_RADJA:
                choiceSystem.show("Balas pesan mantan", "Abaikan, fokus ngobrol sama Radja");
                gameState = GameState.CHOICE;
                storyManager.setStage(StoryManager.StoryStage.PILIHAN_SAAT_NOTIF);
                break;
            case BALAS_MANTAN:
                choiceSystem.show("Tolak ajakan balikan", "Terima ajakan balikan");
                gameState = GameState.CHOICE;
                storyManager.setStage(StoryManager.StoryStage.PILIHAN_MANTAN_SAAT_RADJA);
                break;
            case TOLAK_MANTAN_KEMBALI_RADJA:
                npcRadja.setSpriteMode(NPCRadja.SpriteMode.BICARA);
                dialogSystem.start(FOKUS_RADJA_LINES, FOKUS_RADJA_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.FOKUS_RADJA);
                break;
            case TERIMA_MANTAN_RADJA_MARAH:
                npcRadja.setSpriteMode(NPCRadja.SpriteMode.MARAH);
                dialogSystem.start(WA_TERIMA_RADJA_LINES, WA_TERIMA_RADJA_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.RADJA_MARAH_DIALOG);
                break;
            case RADJA_MARAH_DIALOG:
                dialogSystem.start(MINTA_MAAF_LINES, MINTA_MAAF_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.PLAYER_MINTA_MAAF_RADJA);
                break;
            case PLAYER_MINTA_MAAF_RADJA:
                stressSystem.increase(20);
                dialogSystem.start(BAD_ENDING_11_LINES, BAD_ENDING_11_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.BAD_ENDING_1_1);
                break;
            case FOKUS_RADJA:
                stressSystem.decrease(20);
                dialogSystem.start(GOOD_ENDING_11_LINES, GOOD_ENDING_11_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.GOOD_ENDING_1_1);
                break;
            case GOOD_ENDING_1_1:
            case BAD_ENDING_1_1:
                npcRadja.setSpriteMode(NPCRadja.SpriteMode.IDLE);
                dialogSystem.start(MASUK_1_2_LINES, MASUK_1_2_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.MASUK_1_2);
                break;

            // ── Bagian 1.2 ───────────────────────────────────────────────
            case MASUK_1_2:
                goToScene(SceneIndex.PEGUNUNGAN_MALAM.getIndex(), 60);
                gameState = GameState.PLAYING;
                break;
            case BODO_AMAT_OJOL:
                stressSystem.decrease(5);
                goToScene(SceneIndex.DEPAN_MESJID.getIndex(), 60);
                gameState = GameState.DIALOG;
                dialogSystem.start(MASUK_1_3_LINES, MASUK_1_3_SPEAKERS);
                storyManager.setStage(StoryManager.StoryStage.MASUK_1_3);
                break;
            case DIALOG_TANYA_OJOL:
                storyManager.mintaPerkakas();
                dialogSystem.start(
                    storyManager.isGoodEnding11() ? WA_RADJA_GOOD_LINES : WA_RADJA_BAD_LINES,
                    storyManager.isGoodEnding11() ? WA_RADJA_GOOD_SPEAKERS : WA_RADJA_BAD_SPEAKERS
                );
                gameState = GameState.DIALOG;
                break;
            case WA_MINTA_RADJA_GOOD:
            case WA_MINTA_RADJA_BAD:
                npcOjol.setSpriteMode(NPCOjol.SpriteMode.BICARA);
                dialogSystem.start(MOTOR_BERES_LINES, MOTOR_BERES_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.DIALOG_MOTOR_BERES);
                break;
            case DIALOG_MOTOR_BERES:
                npcOjol.setMotorVisible(false);
                stressSystem.decrease(15);
                storyManager.terimakasihRadja();
                dialogSystem.start(
                    storyManager.isGoodEnding11() ? WA_TRIMAKASIH_GOOD_LINES : WA_TRIMAKASIH_BAD_LINES,
                    storyManager.isGoodEnding11() ? WA_TRIMAKASIH_GOOD_SPEAKERS : WA_TRIMAKASIH_BAD_SPEAKERS
                );
                gameState = GameState.DIALOG;
                break;
            case WA_TERIMAKASIH_RADJA_GOOD:
            case WA_TERIMAKASIH_RADJA_BAD:
                // Tiba di Depan Mesjid
                goToScene(SceneIndex.DEPAN_MESJID.getIndex(), 80);
                dialogSystem.start(MASUK_1_3_LINES, MASUK_1_3_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.MASUK_1_3);
                break;

            // ── Bagian 1.3 ───────────────────────────────────────────────
            case MASUK_1_3:
                gameState = GameState.PLAYING;
                storyManager.setStage(StoryManager.StoryStage.TRIGGER_NASGOR);
                break;
            case DIALOG_NASGOR:
                // Setelah dialog nasgor → narasi menunggu
                dialogSystem.start(TUNGGU_PESANAN_LINES, TUNGGU_PESANAN_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.TUNGGU_PESANAN);
                break;
            case TUNGGU_PESANAN:
                // Tentukan ending
                storyManager.masukEnding();
                if (storyManager.isSelingkuh()) {
                    // BAD ENDING — chat di HP dulu
                    dialogSystem.start(WA_PACAR_BAD_HP_LINES, WA_PACAR_BAD_HP_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.WA_PACAR_BAD_HP);
                } else {
                    // GOOD ENDING
                    dialogSystem.start(WA_PACAR_GOOD_LINES, WA_PACAR_GOOD_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.WA_PACAR_GOOD);
                }
                break;

            // ── Good ending ───────────────────────────────────────────────
            case WA_PACAR_GOOD:
                stressSystem.decrease(30);
                // Masuk layar ending
                isGoodEnding = true;
                gameState    = GameState.ENDING;
                endingAlpha  = 0; endingTextAlpha = 0;
                storyManager.setStage(StoryManager.StoryStage.LAYAR_GOOD_ENDING);
                break;

            // ── Bad ending — HP selesai → pacar muncul ────────────────────
            case WA_PACAR_BAD_HP:
                // Pacar muncul dari belakang player
                npcPacar.setSpriteMode(NPCPacar.SpriteMode.MARAH);
                npcPacar.setPacarX(player.getX() + 100);
                dialogSystem.start(BAD_ENDING_PACAR_LINES, BAD_ENDING_PACAR_SPEAKERS);
                gameState = GameState.DIALOG;
                storyManager.setStage(StoryManager.StoryStage.BAD_ENDING_PACAR_MUNCUL);
                break;

            case BAD_ENDING_PACAR_MUNCUL:
                stressSystem.increase(30);
                isGoodEnding = false;
                gameState    = GameState.ENDING;
                endingAlpha  = 0; endingTextAlpha = 0;
                storyManager.setStage(StoryManager.StoryStage.LAYAR_BAD_ENDING);
                break;

            default:
                gameState = GameState.PLAYING;
                break;
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  HANDLE PILIHAN
    // ═════════════════════════════════════════════════════════════════════
    private void handleChoiceResult() {
        boolean pilih1 = choiceSystem.getResult() == ChoiceSystem.ChoiceResult.CHOICE_1;
        choiceSystem.reset();
        StoryManager.StoryStage stage = storyManager.getCurrentStage();

        switch (stage) {
            case PILIHAN_SAPA_RADJA:
                storyManager.pilihanSapaRadja(pilih1);
                if (pilih1) {
                    dialogSystem.start(SAPA_RADJA_LINES, SAPA_RADJA_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.OBROLAN_AWAL_RADJA);
                } else {
                    dialogSystem.start(LEWAT_RADJA_LINES, LEWAT_RADJA_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.JALAN_TANPA_RADJA);
                }
                break;
            case PILIHAN_MANTAN_TANPA_RADJA:
                storyManager.pilihanMantanTanpaRadja(pilih1);
                dialogSystem.start(
                    pilih1 ? WA_TOLAK_LINES   : WA_TERIMA_LINES,
                    pilih1 ? WA_TOLAK_SPEAKERS : WA_TERIMA_SPEAKERS
                );
                gameState = GameState.DIALOG;
                storyManager.setStage(pilih1
                    ? StoryManager.StoryStage.TOLAK_MANTAN_TANPA_RADJA
                    : StoryManager.StoryStage.TERIMA_MANTAN_TANPA_RADJA);
                break;
            case PILIHAN_SAAT_NOTIF:
                storyManager.pilihanSaatNotif(!pilih1);
                if (!pilih1) {
                    dialogSystem.start(WA_MANTAN_SAAT_RADJA_LINES, WA_MANTAN_SAAT_RADJA_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.BALAS_MANTAN);
                } else {
                    npcRadja.setSpriteMode(NPCRadja.SpriteMode.BICARA);
                    dialogSystem.start(FOKUS_RADJA_LINES, FOKUS_RADJA_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.FOKUS_RADJA);
                }
                break;
            case PILIHAN_MANTAN_SAAT_RADJA:
                storyManager.pilihanMantanSaatRadja(pilih1);
                if (pilih1) {
                    dialogSystem.start(WA_TOLAK_KEMBALI_LINES, WA_TOLAK_KEMBALI_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.TOLAK_MANTAN_KEMBALI_RADJA);
                } else {
                    dialogSystem.start(WA_TERIMA_LINES, WA_TERIMA_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.TERIMA_MANTAN_RADJA_MARAH);
                }
                break;
            case PILIHAN_OJOL:
                storyManager.pilihanOjol(pilih1);
                if (pilih1) {
                    npcOjol.setSpriteMode(NPCOjol.SpriteMode.BICARA);
                    dialogSystem.start(DIALOG_TANYA_OJOL_LINES, DIALOG_TANYA_OJOL_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.DIALOG_TANYA_OJOL);
                } else {
                    npcOjol.setSpriteMode(NPCOjol.SpriteMode.SANTAI);
                    dialogSystem.start(BODO_AMAT_OJOL_LINES, BODO_AMAT_OJOL_SPEAKERS);
                    gameState = GameState.DIALOG;
                    storyManager.setStage(StoryManager.StoryStage.BODO_AMAT_OJOL);
                }
                break;
            default:
                gameState = GameState.PLAYING;
                break;
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  KEY LISTENER
    // ═════════════════════════════════════════════════════════════════════
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Layar ending — SPASI keluar
        if (gameState == GameState.ENDING && key == KeyEvent.VK_SPACE) {
            if (endingTextAlpha >= 200) System.exit(0);
            return;
        }

        if (key == KeyEvent.VK_SPACE) {
            if (gameState == GameState.INTRO || gameState == GameState.DIALOG) {
                dialogSystem.nextLine(); return;
            }
        }
        if (gameState == GameState.CHOICE) { choiceSystem.handleKey(key); return; }
        if (gameState == GameState.PLAYING) {
            if (key == KeyEvent.VK_A) player.setMoveLeft(true);
            if (key == KeyEvent.VK_D) player.setMoveRight(true);
        }
        if (key == KeyEvent.VK_F11 && parentFrame != null) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                   .getDefaultScreenDevice();
            if (gd.getFullScreenWindow() == null) {
                parentFrame.dispose(); parentFrame.setUndecorated(true);
                gd.setFullScreenWindow(parentFrame);
            } else {
                gd.setFullScreenWindow(null); parentFrame.dispose();
                parentFrame.setUndecorated(false); parentFrame.setVisible(true);
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) player.setMoveLeft(false);
        if (e.getKeyCode() == KeyEvent.VK_D) player.setMoveRight(false);
    }
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Healing Quest");
        Game2D game  = new Game2D();
        game.setParentFrame(frame);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
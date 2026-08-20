package com.sholatapp.data

import com.sholatapp.model.DzikirCategory
import com.sholatapp.model.DzikirItem

object DzikirData {
    fun getAllDzikir(): List<DzikirItem> = listOf(

        // ==================== DZIKIR PAGI ====================
        DzikirItem(
            id = "pagi_subhanallah_33",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_alhamdulillah_33",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_allahu_akbar_33",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_laa_ilaaha_illallah_100",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ",
            latin = "Laa ilaaha illallah",
            translation = "Tidak ada ilah (yang berhak disembah) selain Allah",
            targetCount = 100,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_tahmid",
            arabic = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            latin = "Alhamdulillahi rabbil 'aalamiin",
            translation = "Segala puji bagi Allah, Tuhan seluruh alam",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_ayat_kursi",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illa huwal hayyul qayyuum. Laa ta'khudzuhuu sinatun wa laa naum. Lahuu maa fis samaawaati wa maa fil ardhi. Man dzal ladzii yasyfa'u 'indahuu illa biidznih. Ya'lamu maa baina aidiihim wa maa khalfahum. Wa laa yuhiithuuna bisyai'in min 'ilmihii illa bimaa syaa'. Wasi'a kursiyyuhus samaawaati wal ardhi. Wa laa ya'uuduhuu hifdzhuhumaa. Wahuwal 'aliyyul 'adzhiim.",
            translation = "Allah, tidak ada ilah (yang berhak disembah) selain Dia. Yang Maha Hidup, Yang Maha Berdiri Sendiri. Tidak mengantuk dan tidak tidur. Milik-Nya apa yang di langit dan di bumi. Tiada yang dapat memberi syafaat di sisi-Nya tanpa izin-Nya. Dia mengetahui apa yang di hadapan mereka dan di belakang mereka. Mereka tidak mengetahui sesuatu dari ilmu-Nya melainkan apa yang Dia kehendaki. Kursi-Nya meliputi langit dan bumi. Dia tidak merasa berat memelihara keduanya. Dan Dia Maha Tinggi, Maha Besar.",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_istighfar_100",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            latin = "Astaghfirullah",
            translation = "Aku memohon ampun kepada Allah",
            targetCount = 100,
            category = DzikirCategory.PAGI
        ),

        // ==================== DZIKIR PETANG ====================
        DzikirItem(
            id = "petang_subhanallah_33",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_alhamdulillah_33",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_allahu_akbar_33",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_al_ikhlas",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            latin = "Bismillahirrahmanirrahim.\nQul huwallahu ahad. Allahush shomad. Lam yalid wa lam yuulad. Wa lam yakul-lahuu kufuwan ahad.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Dialah Allah, Yang Maha Esa. Allah tempat bergantung. Dia tidak beranak dan tidak pula diperanakkan. Dan tidak ada sesuatu yang setara dengan-Nya.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_al_falaq",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            latin = "Bismillahirrahmanirrahim.\nQul a'udzu birobbil falaq. Min syarri maa kholaq. Wa min syarri ghaasiqin idzaa waqab. Wa min syarrin naffaatsaati fil 'uqad. Wa min syarri haasidin idzaa hasad.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Aku berlindung kepada Tuhan yang menguasai subuh, dari kejahatan apa yang Dia ciptakan, dan dari kejahatan malam apabila telah gelap, dan dari kejahatan penyihir perempuan yang meniup pada buhul-buhul, dan dari kejahatan orang yang dengki apabila ia dengki.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_an_naas",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَٰهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
            latin = "Bismillahirrahmanirrahim.\nQul a'udzu birabbin naas. Malikiin naas. Ilaahin naas. Min syarril waswaasil khannaas. Alladzii yuwaswisu fii shuduurin naas. Minal jinnati wannaas.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Aku berlindung kepada Tuhannya manusia. Raja manusia. Sesembahan manusia. Dari kejahatan (bisikan) setan yang biasa bersembunyi, yang membisikkan (kejahatan) ke dalam dada manusia, dari jin dan manusia.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),

        // ==================== SETELAH SHOLAT ====================
        DzikirItem(
            id = "setelah_sholat_istighfar_3",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            latin = "Astaghfirullaha-l'adziim",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung",
            targetCount = 3,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_ayat_kursi",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illa huwal hayyul qayyuum. Laa ta'khudzuhuu sinatun wa laa naum. Lahuu maa fis samaawaati wa maa fil ardhi. Man dzal ladzii yasyfa'u 'indahuu illa biidznih. Ya'lamu maa baina aidiihim wa maa khalfahum. Wa laa yuhiithuuna bisyai'in min 'ilmihii illa bimaa syaa'. Wasi'a kursiyyuhus samaawaati wal ardhi. Wa laa ya'uuduhuu hifdzhuhumaa. Wahuwal 'aliyyul 'adzhiim.",
            translation = "Allah, tidak ada ilah (yang berhak disembah) selain Dia. Yang Maha Hidup, Yang Maha Berdiri Sendiri. Tidak mengantuk dan tidak tidur. Milik-Nya apa yang di langit dan di bumi. Tiada yang dapat memberi syafaat di sisi-Nya tanpa izin-Nya. Dia mengetahui apa yang di hadapan mereka dan di belakang mereka. Mereka tidak mengetahui sesuatu dari ilmu-Nya melainkan apa yang Dia kehendaki. Kursi-Nya meliputi langit dan bumi. Dia tidak merasa berat memelihara keduanya. Dan Dia Maha Tinggi, Maha Besar.",
            targetCount = 1,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_subhanallah_33",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_alhamdulillah_33",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_allahu_akbar_33",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_tawhid",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            latin = "Laa ilaaha illallahu wahdahu laa syarikalah, lahul mulku wa lahul hamdu wa huwa 'alaa kulli syai-in qadiir.",
            translation = "Tidak ada ilah (yang berhak disembah) selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan pujian. Dia Maha Kuasa atas segala sesuatu.",
            targetCount = 1,
            category = DzikirCategory.SETELAH_SHOLAT
        ),

        // ==================== DZIKIR UMUM ====================
        DzikirItem(
            id = "umum_hasbunallah_33",
            arabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            latin = "Hasbunallah wa ni'mal wakiil",
            translation = "Cukuplah Allah bagi kami dan Dialah sebaik-baik penjaga",
            targetCount = 33,
            category = DzikirCategory.UMUM
        ),
        DzikirItem(
            id = "umum_laa_haula_33",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            latin = "Laa haula wa laa quwwata illa billah",
            translation = "Tidak ada daya dan kekuatan kecuali dengan pertolongan Allah",
            targetCount = 33,
            category = DzikirCategory.UMUM
        ),
        DzikirItem(
            id = "umum_subhanallahi_wabihamdihi_100",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            latin = "Subhanallahi wa bihamdihi",
            translation = "Maha Suci Allah dan segala puji bagi-Nya",
            targetCount = 100,
            category = DzikirCategory.UMUM
        )
    )
}

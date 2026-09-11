package com.sholatapp.data

import com.sholatapp.model.DzikirCategory
import com.sholatapp.model.DzikirItem

/**
 * Kumpulan dzikir harian — v2.5.
 *
 * Perubahan: setiap item kini memiliki [DzikirItem.title] (chip judul pada
 * kartu & judul pop up fokus) dan koleksi diperluas dengan item autentik
 * dzikir pagi/petang dari Hisnul Muslim (Sayyidul Istighfar, Asbahna/Amsayna,
 * Radhitu Billah, tasbih 'adada khalqih).
 */
object DzikirData {
    fun getAllDzikir(): List<DzikirItem> = listOf(

        // ==================== DZIKIR PAGI ====================
        DzikirItem(
            id = "pagi_subhanallah_33",
            title = "Tasbih",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_alhamdulillah_33",
            title = "Tahmid",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_allahu_akbar_33",
            title = "Takbir",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_laa_ilaaha_illallah_100",
            title = "Tahlil",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ",
            latin = "Laa ilaaha illallah",
            translation = "Tidak ada ilah (yang berhak disembah) selain Allah",
            targetCount = 100,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_tahmid",
            title = "Alhamdu Lillahi Rabbil 'Alamin",
            arabic = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            latin = "Alhamdulillahi rabbil 'aalamiin",
            translation = "Segala puji bagi Allah, Tuhan seluruh alam",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_ayat_kursi",
            title = "Ayat Kursi",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illa huwal hayyul qayyuum. Laa ta'khudzuhuu sinatun wa laa naum. Lahuu maa fis samaawaati wa maa fil ardhi. Man dzal ladzii yasyfa'u 'indahuu illa biidznih. Ya'lamu maa baina aidiihim wa maa khalfahum. Wa laa yuhiithuuna bisyai'in min 'ilmihii illa bimaa syaa'. Wasi'a kursiyyuhus samaawaati wal ardhi. Wa laa ya'uuduhuu hifdzhuhumaa. Wahuwal 'aliyyul 'adzhiim.",
            translation = "Allah, tidak ada ilah (yang berhak disembah) selain Dia. Yang Maha Hidup, Yang Maha Berdiri Sendiri. Tidak mengantuk dan tidak tidur. Milik-Nya apa yang di langit dan di bumi. Tiada yang dapat memberi syafaat di sisi-Nya tanpa izin-Nya. Dia mengetahui apa yang di hadapan mereka dan di belakang mereka. Mereka tidak mengetahui sesuatu dari ilmu-Nya melainkan apa yang Dia kehendaki. Kursi-Nya meliputi langit dan bumi. Dia tidak merasa berat memelihara keduanya. Dan Dia Maha Tinggi, Maha Besar.",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_istighfar_100",
            title = "Istighfar",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            latin = "Astaghfirullah",
            translation = "Aku memohon ampun kepada Allah",
            targetCount = 100,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_asbahna",
            title = "Asbahna wa Asbahal-Mulk",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ",
            latin = "Ashbahnaa wa ashbahal-mulku lillaah, wal-hamdu lillaah, laa ilaaha illallaahu wahdahuu laa syariika lah, lahul-mulku wa lahul-hamdu wa huwa 'alaa kulli syai-in qadiir. Rabbi as'aluka khaira maa fii haadzal-yaumi wa khaira maa ba'dah, wa a'uudzu bika min syarri maa fii haadzal-yaumi wa syarri maa ba'dah.",
            translation = "Kami memasuki waktu pagi dan kerajaan hanya milik Allah, segala puji bagi Allah. Tidak ada ilah yang berhak disembah selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan pujian, dan Dia Maha Kuasa atas segala sesuatu. Ya Rabb-ku, aku memohon kepada-Mu kebaikan pada hari ini dan sesudahnya, dan aku berlindung kepada-Mu dari keburukan hari ini dan sesudahnya.",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_sayyidul_istighfar",
            title = "Sayyidul Istighfar",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي، فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            latin = "Allahumma anta rabbi laa ilaaha illaa anta, khalaqtanii wa ana 'abduka, wa ana 'alaa 'ahdika wa wa'dika mastatha'tu, a'uudzu bika min syarri maa sana'tu, abuu'u laka bini'matika 'alayya, wa abuu'u bidzanbii fagfirlii, fa innahu laa yagfirudz-dzunuuba illaa anta.",
            translation = "Ya Allah, Engkau Tuhanku, tidak ada ilah yang berhak disembah kecuali Engkau. Engkau menciptakanku dan aku adalah hamba-Mu. Aku senantiasa berusaha menetapi perjanjianku dengan-Mu sesuai kemampuanku. Aku berlindung kepada-Mu dari keburukan perbuatanku. Aku mengakui nikmat-Mu kepadaku dan mengakui dosaku, maka ampunilah aku. Sesungguhnya tidak ada yang mengampuni dosa kecuali Engkau.",
            targetCount = 1,
            category = DzikirCategory.PAGI
        ),
        DzikirItem(
            id = "pagi_radhitu_billah",
            title = "Radhitu Billah",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            latin = "Radhiitu billaahi rabbaa, wa bil-islaami diinaa, wa bi-Muhammadin shallallahu 'alaihi wa sallama nabiyyaa.",
            translation = "Aku ridha Allah sebagai Tuhanku, Islam sebagai agamaku, dan Muhammad shallallahu 'alaihi wa sallam sebagai nabiku.",
            targetCount = 3,
            category = DzikirCategory.PAGI
        ),

        // ==================== DZIKIR PETANG ====================
        DzikirItem(
            id = "petang_subhanallah_33",
            title = "Tasbih",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_alhamdulillah_33",
            title = "Tahmid",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_allahu_akbar_33",
            title = "Takbir",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_al_ikhlas",
            title = "Surah Al-Ikhlas",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            latin = "Bismillahirrahmanirrahim.\nQul huwallahu ahad. Allahush shomad. Lam yalid wa lam yuulad. Wa lam yakul-lahuu kufuwan ahad.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Dialah Allah, Yang Maha Esa. Allah tempat bergantung. Dia tidak beranak dan tidak pula diperanakkan. Dan tidak ada sesuatu yang setara dengan-Nya.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_al_falaq",
            title = "Surah Al-Falaq",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            latin = "Bismillahirrahmanirrahim.\nQul a'udzu birobbil falaq. Min syarri maa kholaq. Wa min syarri ghaasiqin idzaa waqab. Wa min syarrin naffaatsaati fil 'uqad. Wa min syarri haasidin idzaa hasad.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Aku berlindung kepada Tuhan yang menguasai subuh, dari kejahatan apa yang Dia ciptakan, dan dari kejahatan malam apabila telah gelap, dan dari kejahatan penyihir perempuan yang meniup pada buhul-buhul, dan dari kejahatan orang yang dengki apabila ia dengki.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_an_naas",
            title = "Surah An-Naas",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\nقُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَٰهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
            latin = "Bismillahirrahmanirrahim.\nQul a'udzu birabbin naas. Malikiin naas. Ilaahin naas. Min syarril waswaasil khannaas. Alladzii yuwaswisu fii shuduurin naas. Minal jinnati wannaas.",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.\nKatakanlah, \"Aku berlindung kepada Tuhannya manusia. Raja manusia. Sesembahan manusia. Dari kejahatan (bisikan) setan yang biasa bersembunyi, yang membisikkan (kejahatan) ke dalam dada manusia, dari jin dan manusia.\"",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_amsayna",
            title = "Amsayna wa Amsal-Mulk",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا",
            latin = "Amsaynaa wa amsal-mulku lillaah, wal-hamdu lillaah, laa ilaaha illallaahu wahdahuu laa syariika lah, lahul-mulku wa lahul-hamdu wa huwa 'alaa kulli syai-in qadiir. Rabbi as'aluka khaira maa fii haadzihil-lailati wa khaira maa ba'dahaa, wa a'uudzu bika min syarri maa fii haadzihil-lailati wa syarri maa ba'dahaa.",
            translation = "Kami memasuki waktu petang dan kerajaan hanya milik Allah, segala puji bagi Allah. Tidak ada ilah yang berhak disembah selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan pujian, dan Dia Maha Kuasa atas segala sesuatu. Ya Rabb-ku, aku memohon kepada-Mu kebaikan pada malam ini dan sesudahnya, dan aku berlindung kepada-Mu dari keburukan malam ini dan sesudahnya.",
            targetCount = 1,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_sayyidul_istighfar",
            title = "Sayyidul Istighfar",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي، فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            latin = "Allahumma anta rabbi laa ilaaha illaa anta, khalaqtanii wa ana 'abduka, wa ana 'alaa 'ahdika wa wa'dika mastatha'tu, a'uudzu bika min syarri maa sana'tu, abuu'u laka bini'matika 'alayya, wa abuu'u bidzanbii fagfirlii, fa innahu laa yagfirudz-dzunuuba illaa anta.",
            translation = "Ya Allah, Engkau Tuhanku, tidak ada ilah yang berhak disembah kecuali Engkau. Engkau menciptakanku dan aku adalah hamba-Mu. Aku senantiasa berusaha menetapi perjanjianku dengan-Mu sesuai kemampuanku. Aku berlindung kepada-Mu dari keburukan perbuatanku. Aku mengakui nikmat-Mu kepadaku dan mengakui dosaku, maka ampunilah aku. Sesungguhnya tidak ada yang mengampuni dosa kecuali Engkau.",
            targetCount = 1,
            category = DzikirCategory.PETANG
        ),
        DzikirItem(
            id = "petang_radhitu_billah",
            title = "Radhitu Billah",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            latin = "Radhiitu billaahi rabbaa, wa bil-islaami diinaa, wa bi-Muhammadin shallallahu 'alaihi wa sallama nabiyyaa.",
            translation = "Aku ridha Allah sebagai Tuhanku, Islam sebagai agamaku, dan Muhammad shallallahu 'alaihi wa sallam sebagai nabiku.",
            targetCount = 3,
            category = DzikirCategory.PETANG
        ),

        // ==================== SETELAH SHOLAT ====================
        DzikirItem(
            id = "setelah_sholat_istighfar_3",
            title = "Istighfar",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            latin = "Astaghfirullaha-l'adziim",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung",
            targetCount = 3,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_ayat_kursi",
            title = "Ayat Kursi",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illa huwal hayyul qayyuum. Laa ta'khudzuhuu sinatun wa laa naum. Lahuu maa fis samaawaati wa maa fil ardhi. Man dzal ladzii yasyfa'u 'indahuu illa biidznih. Ya'lamu maa baina aidiihim wa maa khalfahum. Wa laa yuhiithuuna bisyai'in min 'ilmihii illa bimaa syaa'. Wasi'a kursiyyuhus samaawaati wal ardhi. Wa laa ya'uuduhuu hifdzhuhumaa. Wahuwal 'aliyyul 'adzhiim.",
            translation = "Allah, tidak ada ilah (yang berhak disembah) selain Dia. Yang Maha Hidup, Yang Maha Berdiri Sendiri. Tidak mengantuk dan tidak tidur. Milik-Nya apa yang di langit dan di bumi. Tiada yang dapat memberi syafaat di sisi-Nya tanpa izin-Nya. Dia mengetahui apa yang di hadapan mereka dan di belakang mereka. Mereka tidak mengetahui sesuatu dari ilmu-Nya melainkan apa yang Dia kehendaki. Kursi-Nya meliputi langit dan bumi. Dia tidak merasa berat memelihara keduanya. Dan Dia Maha Tinggi, Maha Besar.",
            targetCount = 1,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_subhanallah_33",
            title = "Tasbih",
            arabic = "سُبْحَانَ اللَّهِ",
            latin = "Subhanallah",
            translation = "Maha Suci Allah",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_alhamdulillah_33",
            title = "Tahmid",
            arabic = "الْحَمْدُ لِلَّهِ",
            latin = "Alhamdulillah",
            translation = "Segala puji bagi Allah",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_allahu_akbar_33",
            title = "Takbir",
            arabic = "اللَّهُ أَكْبَرُ",
            latin = "Allahu Akbar",
            translation = "Allah Maha Besar",
            targetCount = 33,
            category = DzikirCategory.SETELAH_SHOLAT
        ),
        DzikirItem(
            id = "setelah_sholat_tawhid",
            title = "Tawhid",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            latin = "Laa ilaaha illallahu wahdahu laa syarikalah, lahul mulku wa lahul hamdu wa huwa 'alaa kulli syai-in qadiir.",
            translation = "Tidak ada ilah (yang berhak disembah) selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan pujian. Dia Maha Kuasa atas segala sesuatu.",
            targetCount = 1,
            category = DzikirCategory.SETELAH_SHOLAT
        ),

        // ==================== DZIKIR UMUM ====================
        DzikirItem(
            id = "umum_hasbunallah_33",
            title = "Hasbunallah",
            arabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            latin = "Hasbunallah wa ni'mal wakiil",
            translation = "Cukuplah Allah bagi kami dan Dialah sebaik-baik penjaga",
            targetCount = 33,
            category = DzikirCategory.UMUM
        ),
        DzikirItem(
            id = "umum_laa_haula_33",
            title = "Laa Haula",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            latin = "Laa haula wa laa quwwata illa billah",
            translation = "Tidak ada daya dan kekuatan kecuali dengan pertolongan Allah",
            targetCount = 33,
            category = DzikirCategory.UMUM
        ),
        DzikirItem(
            id = "umum_subhanallahi_wabihamdihi_100",
            title = "Tasbih Bihamdih",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            latin = "Subhanallahi wa bihamdihi",
            translation = "Maha Suci Allah dan segala puji bagi-Nya",
            targetCount = 100,
            category = DzikirCategory.UMUM
        ),
        DzikirItem(
            id = "umum_adada_khalqih",
            title = "Subhanallahi 'Adada Khalqih",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ",
            latin = "Subhaanallaahi wa bihamdih, 'adada khalqih, wa ridhaa nafsih, wa zinata 'arsyih, wa midaada kalimaatih.",
            translation = "Maha Suci Allah dan segala puji bagi-Nya, sebanyak jumlah makhluk-Nya, seluas keridhaan-Nya, seberat timbangan 'arsy-Nya, dan sebanyak tinta (catatan) kalimat-Nya.",
            targetCount = 3,
            category = DzikirCategory.UMUM
        )
    )
}

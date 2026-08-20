package com.sholatapp.data

object QuranTextData {
    data class Ayah(val number: Int, val arabic: String)
    data class AyahFull(val number: Int, val arabic: String, val latin: String, val translation: String)

    fun isTextAvailable(surahNumber: Int): Boolean = surahNumber in shortSurahs.keys

    fun getSurahText(surahNumber: Int): List<Ayah> = shortSurahs[surahNumber] ?: emptyList()

    fun isFullTextAvailable(surahNumber: Int): Boolean = surahNumber in fullSurahs.keys

    fun getSurahFullText(surahNumber: Int): List<AyahFull> = fullSurahs[surahNumber] ?: emptyList()

    fun getTranslationSummary(surahNumber: Int): String = translationSummary[surahNumber] ?: ""

    // Al-Fatihah — Full text with Arabic, Latin, Indonesian (3 baris)
    private val fullSurahs: Map<Int, List<AyahFull>> = mapOf(
        1 to listOf(
            AyahFull(1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                "Bismillāhir-Raḥmānir-Raḥīm",
                "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang"),
            AyahFull(2, "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ",
                "Alḥamdulillāhi Rabbil-'ālamīn",
                "Segala puji bagi Allah, Tuhan seluruh alam"),
            AyahFull(3, "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                "Ar-Raḥmānir-Raḥīm",
                "Yang Maha Pengasih, Maha Penyayang"),
            AyahFull(4, "مَـٰلِكِ يَوْمِ ٱلدِّينِ",
                "Māliki Yaumid-Dīn",
                "Yang menguasai Hari Pembalasan"),
            AyahFull(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                "Iyyāka na'budu wa iyyāka nasta'īn",
                "Hanya kepada Engkau kami menyembah, dan hanya kepada Engkau kami meminta pertolongan"),
            AyahFull(6, "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
                "Ihdinaṣ-Ṣirāṭal-Mustaqīm",
                "Tunjukilah kami jalan yang lurus"),
            AyahFull(7, "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
                "Ṣirāṭal-lażīna an'amta 'alaihim, ghairil-magḍūbi 'alaihim walāḍ-ḍāllīn",
                "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya, bukan (jalan) mereka yang dimurkai dan bukan (pula) jalan mereka yang sesat")
        ),
        // An-Nas (114) — full 3-line text
        114 to listOf(
            AyahFull(1, "قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ",
                "Qul a'ūdhu birabbin-nās",
                "Katakanlah: Aku berlindung kepada Tuhan (yang memelihara dan) menguasai manusia"),
            AyahFull(2, "مَلِكِ ٱلنَّاسِ",
                "Malikin-nās",
                "Raja manusia"),
            AyahFull(3, "إِلَـٰهِ ٱلنَّاسِ",
                "Ilāhin-nās",
                "Sesembahan manusia"),
            AyahFull(4, "مِن شَرِّ ٱلْوَسْوَاسِ ٱلْخَنَّاسِ",
                "Min syarril-waswāsil-khannās",
                "Dari kejahatan (bisikan) syaitan yang biasa bersembunyi"),
            AyahFull(5, "ٱلَّذِي يُوَسْوِسُ فِي صُدُورِ ٱلنَّاسِ",
                "Al-lażī yuwaswisu fī ṣudūrin-nās",
                "Yang membisikkan (kejahatan) ke dalam dada manusia"),
            AyahFull(6, "مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ",
                "Minal-jinnati wan-nās",
                "Dari (golongan) jin dan manusia")
        ),
        // Al-Ikhlas (112) — full 3-line text
        112 to listOf(
            AyahFull(1, "قُلْ هُوَ ٱللَّهُ أَحَدٌ",
                "Qul huwallāhu aḥad",
                "Katakanlah: Dialah Allah, Yang Maha Esa"),
            AyahFull(2, "ٱللَّهُ ٱلصَّمَدُ",
                "Allāhuṣ-Ṣamad",
                "Allah tempat bergantung"),
            AyahFull(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                "Lam yalid wa lam yūlad",
                "Dia tidak beranak dan tidak pula diperanakkan"),
            AyahFull(4, "وَلَمْ يَكُن لَّهُۥ كُفُوًا أَحَدٌ",
                "Wa lam yakul-lahū kufuwan aḥad",
                "Dan tidak ada sesuatu yang setara dengan-Nya")
        ),
        // Al-Falaq (113) — full 3-line text
        113 to listOf(
            AyahFull(1, "قُلْ أَعُوذُ بِرَبِّ ٱلْفَلَقِ",
                "Qul a'ūdhu birabbil-falaq",
                "Katakanlah: Aku berlindung kepada Tuhan yang menguasai subuh"),
            AyahFull(2, "مِن شَرِّ مَا خَلَقَ",
                "Min syarri mā khalaq",
                "Dari kejahatan apa yang Dia ciptakan"),
            AyahFull(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
                "Wa min syarri ghāsiqin idhā waqab",
                "Dan dari kejahatan malam apabila telah gelap"),
            AyahFull(4, "وَمِن شَرِّ ٱلنَّفَّـٰثَـٰتِ فِي ٱلْعُقَدِ",
                "Wa min sharrin-naffāṡāti fil-'uqad",
                "Dan dari kejahatan wanita-wanita tukang sihir yang menghembus pada buhul-buhul"),
            AyahFull(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
                "Wa min syarri ḥāsidin idhā ḥasad",
                "Dan dari kejahatan pendengki bila ia dengki")
        ),
        // Al-Kafirun (109) — full 3-line text
        109 to listOf(
            AyahFull(1, "قُلْ يَـٰٓأَيُّهَا ٱلْكَـٰفِرُونَ",
                "Qul yā ayyuhal-kāfirūn",
                "Katakanlah: Hai orang-orang kafir"),
            AyahFull(2, "لَآ أَعْبُدُ مَا تَعْبُدُونَ",
                "Lā a'budu mā ta'budūn",
                "Aku tidak akan menyembah apa yang kamu sembah"),
            AyahFull(3, "وَلَآ أَنتُمْ عَـٰبِدُونَ مَآ أَعْبُدُ",
                "Wa lā antum 'ābidūna mā a'bud",
                "Dan kamu bukan penyembah Tuhan yang aku sembah"),
            AyahFull(4, "وَلَآ أَنَا عَابِدٌ مَّا عَبَدتُّمْ",
                "Wa lā ana 'ābidum mā 'abattum",
                "Dan aku tidak pernah menjadi penyembah apa yang kamu sembah"),
            AyahFull(5, "وَلَآ أَنتُمْ عَـٰبِدُونَ مَآ أَعْبُدُ",
                "Wa lā antum 'ābidūna mā a'bud",
                "Dan kamu tidak pernah (pula) menjadi penyembah Tuhan yang aku sembah"),
            AyahFull(6, "لَكُمْ دِينُكُمْ وَلِيَ دِينِ",
                "Lakum dīnukum waliya dīn",
                "Untukmu agamamu, dan untukkulah agamaku")
        ),
        // Al-Kautsar (108) — full 3-line text
        108 to listOf(
            AyahFull(1, "إِنَّآ أَعْطَيْنَـٰكَ ٱلْكَوْثَرَ",
                "Innā a'tainākal-kauṡar",
                "Sesungguhnya Kami telah memberikan kepadamu nikmat yang banyak"),
            AyahFull(2, "فَصَلِّ لِرَبِّكَ وَٱنْحَرْ",
                "Faṣalli lirabbika wanḥar",
                "Maka dirikanlah shalat karena Tuhanmu dan berkorbanlah"),
            AyahFull(3, "إِنَّ شَانِئَكَ هُوَ ٱلْأَبْتَرُ",
                "Inna syāni'aka huwal-abtar",
                "Sesungguhnya orang-orang yang membencimu dialah yang terputus (dari kebaikan)")
        )
    )

    // Only short surahs (93-114) Arabic-only text embedded
    private val shortSurahs: Map<Int, List<Ayah>> = mapOf(
        93 to listOf( // Ad-Duha
            Ayah(1, "وَٱلضُّحَىٰ"), Ayah(2, "وَٱلَّيۡلِ إِذَا سَجَىٰ"), Ayah(3, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ"),
            Ayah(4, "وَلَلۡأَخِرَةُ خَيۡرٞ لَّكَ مِنَ ٱلۡأُولَىٰ"), Ayah(5, "وَلَسَوۡفَ يُعۡطِيكَ رَبُّكَ فَتَرۡضَىٰ"),
            Ayah(6, "أَلَمۡ يَجِدۡكَ يَتِيمٗا فَـَٔاوَىٰ"), Ayah(7, "وَوَجَدَكَ ضَآلّٗا فَهَدَىٰ"),
            Ayah(8, "وَوَجَدَكَ عَآئِلٗا فَأَغۡنَىٰ"),
            Ayah(9, "فَأَمَّا ٱلۡيَتِيمَ فَلَا تَقۡهَرۡ"),
            Ayah(10, "وَأَمَّا ٱلسَّآئِلَ فَلَا تَنۡهَرۡ"), Ayah(11, "وَأَمَّا بِنِعۡمَةِ رَبِّكَ فَحَدِّثۡ")
        ),
        94 to listOf( // Ash-Sharh
            Ayah(1, "أَلَمۡ نَشۡرَحۡ لَكَ صَدۡرَكَ"), Ayah(2, "وَوَضَعۡنَا عَنكَ وِزۡرَكَ"),
            Ayah(3, "ٱلَّذِيٓ أَنقَضَ ظَهۡرَكَ"), Ayah(4, "وَرَفَعۡنَا لَكَ ذِكۡرَكَ"),
            Ayah(5, "فَإِنَّ مَعَ ٱلۡعُسۡرِ يُسۡرًا"), Ayah(6, "إِنَّ مَعَ ٱلۡعُسۡرِ يُسۡرًا"),
            Ayah(7, "فَإِذَا فَرَغۡتَ فَٱنصَبۡ"), Ayah(8, "وَإِلَىٰ رَبِّكَ فَٱرۡغَبۡ")
        ),
        95 to listOf( // At-Tin
            Ayah(1, "وَٱلتِّينِ وَٱلزَّيۡتُونِ"), Ayah(2, "وَطُورِ سِينِينَ"),
            Ayah(3, "وَهَٰذَا ٱلۡبَلَدِ ٱلۡأَمِينِ"), Ayah(4, "لَقَدۡ خَلَقۡنَا ٱلۡإِنسَـٰنَ فِيٓ أَحۡسَنِ تَقۡوِيمٖ"),
            Ayah(5, "ثُمَّ رَدَدۡنَٰهُ أَسۡفَلَ سَـٰفِلِينَ"),
            Ayah(6, "إِلَّا ٱلَّذِينَ ءَامَنُواْ وَعَمِلُواْ ٱلصَّـٰلِحَـٰتِ فَلَهُمۡ أَجۡرٌ غَيۡرُ مَمۡنُونٖ"),
            Ayah(7, "فَمَا يُكَذِّبُكَ بَعۡدُ بِٱلدِّينِ"),
            Ayah(8, "أَلَيۡسَ ٱللَّهُ بِأَحۡكَمِ ٱلۡحَـٰكِمِينَ")
        ),
        96 to listOf( // Al-'Alaq
            Ayah(1, "ٱقۡرَأۡ بِٱسۡمِ رَبِّكَ ٱلَّذِي خَلَقَ"), Ayah(2, "خَلَقَ ٱلۡإِنسَـٰنَ مِنۡ عَلَقٍ"),
            Ayah(3, "ٱقۡرَأۡ وَرَبُّكَ ٱلۡأَكۡرَمُ"), Ayah(4, "ٱلَّذِي عَلَّمَ بِٱلۡقَلَمِ"),
            Ayah(5, "عَلَّمَ ٱلۡإِنسَـٰنَ مَا لَمۡ يَعۡلَمۡ"), Ayah(6, "كَلَّآ إِنَّ ٱلۡإِنسَـٰنَ لَيَطۡغَىٰ"),
            Ayah(7, "أَن رَّءَاهُ ٱسۡتَغۡنَىٰ"), Ayah(8, "إِنَّ إِلَىٰ رَبِّكَ ٱلرُّجۡعَىٰ"),
            Ayah(9, "أَرَءَيۡتَ ٱلَّذِي يَنۡهَىٰ"), Ayah(10, "عَبۡدًا إِذَا صَلَّىٰ"),
            Ayah(11, "أَرَءَيۡتَ إِن كَانَ عَلَى ٱلۡهُدَىٰ"), Ayah(12, "أَوۡ أَمَرَ بِٱلتَّقۡوَىٰ"),
            Ayah(13, "أَرَءَيۡتَ إِن كَذَّبَ وَتَوَلَّىٰ"), Ayah(14, "أَلَمۡ يَعۡلَم بِأَنَّ ٱللَّهَ يَرَىٰ"),
            Ayah(15, "كَلَّا لَئِن لَّمۡ يَنتَهِ لَنَسۡفَعًۢا بِٱلنَّاصِيَةِ"),
            Ayah(16, "نَاصِيَةٖ قَوِيَّةٖ"), Ayah(17, "كَلَّآ أَطِعۡهُ وَٱسۡجُدۡ وَٱقۡتَرِبۢ")
        ),
        97 to listOf( // Al-Qadr
            Ayah(1, "إِنَّآ أَنزَلۡنَٰهُ فِي لَيۡلَةِ ٱلۡقَدۡرِ"),
            Ayah(2, "وَمَآ أَدۡرَىٰكَ مَا لَيۡلَةُ ٱلۡقَدۡرِ"),
            Ayah(3, "لَيۡلَةُ ٱلۡقَدۡرِ خَيۡرٌ مِّنۡ أَلۡفِ شَهۡرٖ"),
            Ayah(4, "تَنَزَّلُ ٱلۡمَلَـٰٓئِكَةُ وَٱلرُّوحُ فِيهَا بِإِذۡنِ رَبِّهِم مِّن كُلِّ أَمۡرٖ"),
            Ayah(5, "سَلَـٰمٌ هِيَ حَتَّىٰ مَطۡلَعِ ٱلۡفَجۡرِ")
        ),
        98 to listOf( // Al-Bayyinah
            Ayah(1, "لَمۡ يَكُنِ ٱلَّذِينَ كَفَرُواْ مِنۡ أَهۡلِ ٱلۡكِتَـٰبِ وَلَا ٱلۡمُشۡرِكِينَ مُنفَكِّينَ"),
            Ayah(2, "حَتَّىٰ تَأۡتِيَهُمُ ٱلۡبَيِّنَةُ"), Ayah(3, "رَسُولٞ مِّنَ ٱللَّهِ يَتۡلُوا صُحُفٗا مُّطَهَّرَةٗ"),
            Ayah(4, "فِيهَا كُتُبٞ قَيِّمَةٞ"), Ayah(5, "وَمَا تَفَرَّقَ ٱلَّذِينَ أُوتُواْ ٱلۡكِتَـٰبَ إِلَّا مِنۢ بَعۡدِ مَا جَآءَتۡهُمُ ٱلۡبَيِّنَةُ"),
            Ayah(6, "وَمَآ أُمِرُوٓاْ إِلَّا لِيَعۡبُدُواْ ٱللَّهَ مُخۡلِصِينَ لَهُ ٱلدِّينَ حُنَفَآءَ"),
            Ayah(7, "وَيُقِيمُواْ ٱلصَّلَوٰةَ وَيُؤۡتُواْ ٱلزَّكَوٰةَ وَذَٰلِكَ دِينُ ٱلۡقَيِّمَةِ"),
            Ayah(8, "إِنَّ ٱلَّذِينَ كَفَرُواْ مِنۡ أَهۡلِ ٱلۡكِتَـٰبِ وَٱلۡمُشۡرِكِينَ فِي نَارِ جَهَنَّمَ خَـٰلِدِينَ فِيهَآ ۚ أُو۟لَـٰٓئِكَ هُمۡ شَرُّ ٱلۡبَرِيَّةِ")
        ),
        99 to listOf( // Az-Zalzalah
            Ayah(1, "إِذَا زُلۡزِلَتِ ٱلۡأَرۡضُ زِلۡزَالَهَا"), Ayah(2, "وَأَخۡرَجَتِ ٱلۡأَرۡضُ أَثۡقَالَهَا"),
            Ayah(3, "وَقَالَ ٱلۡإِنسَـٰنُ مَا لَهَا"), Ayah(4, "يَوۡمَئِذٖ تُحَدِّثُ أَخۡبَارَهَا"),
            Ayah(5, "بِأَنَّ رَبَّكَ أَوۡحَىٰ لَهَا"), Ayah(6, "يَوۡمَئِذٖ يَصۡدُرُ ٱلنَّاسُ أَشۡتَاتٗا لِّيُرَوۡاْ أَعۡمَالَهُمۡ"),
            Ayah(7, "فَمَن يَعۡمَلۡ مِثۡقَالَ ذَرَّةٍ خَيۡرٗا يَرَهُۥ"),
            Ayah(8, "وَمَن يَعۡمَلۡ مِثۡقَالَ ذَرَّةٍ شَرّٗا يَرَهُۥ")
        ),
        100 to listOf( // Al-'Adiyat
            Ayah(1, "وَٱلۡعَـٰدِيَـٰتِ ضَبۡحٗا"), Ayah(2, "فَٱلۡمُورِيَـٰتِ قَدۡحًا"),
            Ayah(3, "فَٱلۡمُغِيرَٰتِ صُبۡحًا"), Ayah(4, "فَأَثَرۡنَ بِهِۦ نَقۡعٗا"),
            Ayah(5, "فَوَسَطۡنَ بِهِۦ جَمۡعًا"), Ayah(6, "إِنَّ ٱلۡإِنسَـٰنَ لِرَبِّهِۦ لَكَنُودٞ"),
            Ayah(7, "وَإِنَّهُۥ عَلَىٰ ذَٰلِكَ لَشَهِيدٞ"), Ayah(8, "وَإِنَّهُۥ لِحُبِّ ٱلۡخَيۡرِ لَشَدِيدٌ"),
            Ayah(9, "أَفَلَا يَعۡلَمُ إِذَا بُعۡثِرَ مَا فِي ٱلۡقُبُورِ"),
            Ayah(10, "وَحُصِّلَ مَا فِي ٱلصُّدُورِ"), Ayah(11, "إِنَّ رَبَّهُم بِهِمۡ يَوۡمَئِذٖ لَّخَبِيرُۢ")
        ),
        101 to listOf( // Al-Qari'ah
            Ayah(1, "ٱلۡقَارِعَةُ"), Ayah(2, "مَا ٱلۡقَارِعَةُ"), Ayah(3, "وَمَآ أَدۡرَىٰكَ مَا ٱلۡقَارِعَةُ"),
            Ayah(4, "يَوۡمَ يَكُونُ ٱلنَّاسُ كَٱلۡفَرَاشِ ٱلۡمَبۡثُوثِ"),
            Ayah(5, "وَتَكُونُ ٱلۡجِبَالُ كَٱلۡعِهۡنِ ٱلۡمَنーフُوشِ"),
            Ayah(6, "فَأَمَّا مَن ثَقُلَتۡ مَوَٰزِينُهُۥ"), Ayah(7, "فَهُوَ فِي عِيشَةٖ رَّاضِيَةٍ"),
            Ayah(8, "وَأَمَّا مَن خَفَّتۡ مَوَٰزِينُهُۥ"), Ayah(9, "فَأُمُّهُۥ هَاوِيَةٌ"),
            Ayah(10, "وَمَآ أَدۡرَىٰكَ مَا هِيَهَ"), Ayah(11, "نَارٌ حَامِيَةُۢ")
        ),
        102 to listOf( // At-Takatsur
            Ayah(1, "أَلۡهَٰكُمُ ٱلتَّكَاثُرُ"), Ayah(2, "تَكَثُّرُونَ"),
            Ayah(3, "حَتَّىٰ زُرۡتُمُ ٱلۡمَقَابِرَ"), Ayah(4, "كَلَّآ سَوۡفَ تَعۡلَمُونَ"),
            Ayah(5, "ثُمَّ كَلَّآ سَوۡفَ تَعۡلَمُونَ"), Ayah(6, "أَلَا لَوۡ تَعۡلَمُونَ عِلۡمَ ٱلۡيَقِينِ"),
            Ayah(7, "لَتَرَوُنَّ ٱلۡجَحِيمَ"), Ayah(8, "ثُمَّ لَتَرَوُنَّهَا عَيۡنَ ٱلۡيَقِينِ"),
            Ayah(9, "ثُمَّ لَتُسَـٔلُونَّ يَوۡمَئِذٍ عَنِ ٱلنَّعِيمِ")
        ),
        103 to listOf( // Al-'Asr
            Ayah(1, "وَٱلۡعَصۡرِ"), Ayah(2, "إِنَّ ٱلۡإِنسَـٰنَ لَفِي خُسۡرٍ"),
            Ayah(3, "إِلَّا ٱلَّذِينَ ءَامَنُواْ وَعَمِلُواْ ٱلصَّـٰلِحَـٰتِ وَتَوَاصَوۡاْ بِٱلۡحَقِّ وَتَوَاصَوۡاْ بِٱلصَّبۡرِ")
        ),
        104 to listOf( // Al-Humazah
            Ayah(1, "وَيۡلٞ لِّكُلِّ هُمَزَةٍ لُّمَزَةٍ"), Ayah(2, "ٱلَّذِي جَمَعَ مَالٗا وَعَدَّدَهُۥ"),
            Ayah(3, "يَحۡسَبُ أَنَّ مَالَهُۥٓ أَخۡلَدَهُۥ"), Ayah(4, "كَلَّآ ۖ لَيُنۢبَذَنَّ فِي ٱلۡحُطَمَةِ"),
            Ayah(5, "وَمَآ أَدۡرَىٰكَ مَا ٱلۡحُطَمَةُ"), Ayah(6, "نَارُ ٱللَّهِ ٱلۡمُوقَدَةُ"),
            Ayah(7, "ٱلَّتِي تَطَّلِعُ عَلَىٰ ٱلْأَفْئِدَةِ"), Ayah(8, "تَطَّلِعُ عَلَىٰ ٱلْقُلُوبِ"),
            Ayah(9, "إِنَّهَا عَلَيۡهِم مُّؤۡصَدَةٌ"), Ayah(10, "فِي عَمَدٍ مُّمَدَّدَةٍ")
        ),
        105 to listOf( // Al-Fil
            Ayah(1, "أَلَمۡ تَرَ كَيۡفَ فَعَلَ رَبُّكَ بِأَصۡحَـٰبِ ٱلۡفِيلِ"),
            Ayah(2, "أَلَمۡ يَجۡعَلۡ كَيۡدَهُمۡ فِي تَضۡلِيلٍ"),
            Ayah(3, "وَأَرۡسَلَ عَلَيۡهِمۡ طَيۡرًا أَبَابِيلَ"),
            Ayah(4, "تَرۡمِيهِم بِحِجَارَةٍ مِّن سِجِّيلٖ"),
            Ayah(5, "فَجَعَلَهُمۡ كَعَصۡفٖ مَّأۡكُولِۢ")
        ),
        106 to listOf( // Quraisy
            Ayah(1, "لِإِيلَـٰفِ قُرَيۡشٍ"), Ayah(2, "إِٖلَـٰفِهِمۡ رِحۡلَةَ ٱلشِّتَآءِ وَٱلصَّيۡفِ"),
            Ayah(3, "فَلۡيَعۡبُدُواْ رَبَّ هَٰذَا ٱلۡبَيۡتِ"),
            Ayah(4, "ٱلَّذِيٓ أَطۡعَمَهُم مِّن جُوعٖ وَءَامَنَهُم مِّنۡ خَوۡفِۢ")
        ),
        107 to listOf( // Al-Ma'un
            Ayah(1, "أَرَءَيۡتَ ٱلَّذِي يُكَذِّبُ بِٱلدِّينِ"), Ayah(2, "فَذَٰلِكَ ٱلَّذِي يَدُعُّ ٱلۡيَتِيمَ"),
            Ayah(3, "وَلَا يَحُضُّ عَلَىٰ طَعَامِ ٱلۡمِسۡكِينِ"),
            Ayah(4, "فَوَيۡلٌ لِّلۡمُصَلِّينَ"), Ayah(5, "ٱلَّذِينَ هُمۡ عَن صَلَاتِهِمۡ سَاهُونَ"),
            Ayah(6, "ٱلَّذِينَ هُمۡ يُرَآءُونَ"), Ayah(7, "وَيَمۡنَعُونَ ٱلۡمَاعُونَ")
        ),
        108 to listOf( // Al-Kautsar
            Ayah(1, "إِنَّآ أَعۡطَيۡنَـٰكَ ٱلۡكَوۡثَرَ"),
            Ayah(2, "فَصَلِّ لِرَبِّكَ وَٱنۡحَرۡ"),
            Ayah(3, "إِنَّ شَانِئَكَ هُوَ ٱلۡأَبۡتَرُ")
        ),
        109 to listOf( // Al-Kafirun
            Ayah(1, "قُلۡ يَـٰٓأَيُّهَا ٱلۡكَـٰفِرُونَ"),
            Ayah(2, "لَآ أَعۡبُدُ مَا تَعۡبُدُونَ"),
            Ayah(3, "وَلَآ أَنتُمۡ عَـٰبِدُونَ مَآ أَعۡبُدُ"),
            Ayah(4, "وَلَآ أَنَا عَابِدٌ مَّا عَبَدتُّمۡ"),
            Ayah(5, "وَلَآ أَنتُمۡ عَـٰبِدُونَ مَآ أَعۡبُدُ"),
            Ayah(6, "لَكُمۡ دِينُكُمۡ وَلِيَ دِينِ")
        ),
        110 to listOf( // An-Nashr
            Ayah(1, "إِذَا جَآءَ نَصۡرُ ٱللَّهِ وَٱلۡفَتۡحُ"),
            Ayah(2, "وَرَأَيۡتَ ٱلنَّاسَ يَدۡخُلُونَ فِي دِينِ ٱللَّهِ أَفۡوَاجًا"),
            Ayah(3, "فَسَبِّحۡ بِحَمۡدِ رَبِّكَ وَٱسۡتَغۡفِرۡهُ ۚ إِنَّهُۥ كَانَ تَوَّابًۢا")
        ),
        111 to listOf( // Al-Masad
            Ayah(1, "تَبَّتۡ يَدَآ أَبِي لَهَبٍ وَتَبَّ"),
            Ayah(2, "مَآ أَغۡنَىٰ عَنۡهُ مَالُهُۥ وَمَا كَسَبَ"),
            Ayah(3, "سَيَصۡلَىٰ نَارًا ذَاتَ لَهَبٖ"),
            Ayah(4, "وَٱمۡرَأَتُهُۥ حَمَّالَةَ ٱلۡحَطَبِ"),
            Ayah(5, "فِي جِيدِهَا حَبۡلٌ مِّن مَّسَدٍ")
        ),
        112 to listOf( // Al-Ikhlas
            Ayah(1, "قُلۡ هُوَ ٱللَّهُ أَحَدٌ"),
            Ayah(2, "ٱللَّهُ ٱلصَّمَدُ"),
            Ayah(3, "لَمۡ يَلِدۡ وَلَمۡ يُولَدۡ"),
            Ayah(4, "وَلَمۡ يَكُن لَّهُۥ كُفُوًا أَحَدٌ")
        ),
        113 to listOf( // Al-Falaq
            Ayah(1, "قُلۡ أَعُوذُ بِرَبِّ ٱلۡفَلَقِ"),
            Ayah(2, "مِن شَرِّ مَا خَلَقَ"),
            Ayah(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ"),
            Ayah(4, "وَمِن شَرِّ ٱلنَّفَّـٰثَـٰتِ فِي ٱلۡعُقَدِ"),
            Ayah(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ")
        ),
        114 to listOf( // An-Nas
            Ayah(1, "قُلۡ أَعُوذُ بِرَبِّ ٱلنَّاسِ"),
            Ayah(2, "مَلِكِ ٱلنَّاسِ"),
            Ayah(3, "إِلَـٰهِ ٱلنَّاسِ"),
            Ayah(4, "مِن شَرِّ ٱلۡوَسۡوَاسِ ٱلۡخَنَّاسِ"),
            Ayah(5, "ٱلَّذِي يُوَسۡوِسُ فِي صُدُورِ ٱلنَّاسِ"),
            Ayah(6, "مِنَ ٱلۡجِنَّةِ وَٱلنَّاسِ")
        )
    )

    private val translationSummary: Map<Int, String> = mapOf(
        93 to "Allah bersumpah dengan waktu duha dan malam bahwa Nabi Muhammad tidak pernah diabaikan. Surah ini mengingatkan untuk berbuat baik kepada anak yatim, orang miskin, dan mensyukuri nikmat Allah.",
        94 to "Allah telah melapangkan dada dan membuang beban Nabi. Setiap kesulitan pasti disertai kemudahan. Surah ini memberikan semangat dan motivasi.",
        95 to "Allah bersumpah dengan buah tin, Gunung Sinai, dan kota Mekah bahwa manusia diciptakan dalam bentuk terbaik. Kecuali orang beriman yang mendapat pahala tak terhingga.",
        96 to "Pesan pertama wahyu: Iqra! (Bacalah!). Allah mengajarkan manusia melalui tulisan. Peringatan keras bagi orang yang melampaui batas dan mendustakan.",
        97 to "Surah tentang Lailatul Qadr (Malam Kemuliaan) yang nilainya lebih baik dari 1000 bulan. Pada malam itu, para malaikat dan Jibril turun dengan izin Allah.",
        98 to "Orang kafir dan musyrik tidak akan berhenti mendustakan hingga datang bukti yang jelas. Perintah utama: menyembah Allah dengan ikhlas, mendirikan shalat, dan menunaikan zakat.",
        99 to "Ketika bumi berguncang dan mengeluarkan beban-bebannya, manusia akan bertanya apa yang terjadi. Setiap amal sekecil apapun akan diperhitung.",
        100 to "Bersumpah dengan kuda perang yang berlari kencang. Manusia sangat kikir terhadap nikmat Tuhannya. Kelak semua perbuatan akan diketahui.",
        101 to "Hari Kiamat ketika manusia seperti lalat terbang dan gunung seperti bulu yang dihamburkan. Timbangan amal yang berat mendapat kehidupan yang memuaskan.",
        102 to "Kecelakaan bagi orang yang lalai karena banyak bermegah-megahan dan mengumpulkan harta. Padahal mereka akan ditanya tentang nikmat yang diberikan.",
        103 to "Demi waktu, manusia sungguh dalam kerugian, kecuali yang beriman, beramal saleh, saling menasihati kebenaran, dan saling menasihati kesabaran.",
        104 to "Kecelakaan bagi pemfitnah dan pencela yang mengumpulkan harta. Kelak harta itu tidak menyelamatkannya dari neraka yang membakar hati.",
        105 to "Allah membinasakan pasukan gajah Abrahah dengan burung-burung Ababil yang melempar batu dari Sijjil.",
        106 to "Kebaikan Allah kepada Quraisy: perlindungan dalam perjalanan musim panas dan dingin. Karena itu, mereka harus menyembah Tuhan Ka'bah.",
        107 to "Celakalah orang yang mendustakan agama: tidak menyayangi anak yatim, tidak mendorong memberi makan orang miskin, lalai dalam shalat, riya, dan enggan memberi bantuan.",
        108 to "Allah memberikan nikmat Al-Kautsar (sungai di surga). Maka shalatlah dan berkurbanlah. Musuhmu yang membenci kamu, dialah yang terputus.",
        109 to "Pernyataan tegas kebebasan beragama: aku tidak menyembah yang kamu sembah, dan kamu tidak menyembah yang aku sembah. Untukmu agamamu, untukku agamaku.",
        110 to "Ketika pertolongan Allah dan kemenangan datang, manusia masuk Islam berbondong-bondong. Maka bertasbihlah, minta ampun, dan sesungguhnya Allah Maha Penerima Taubat.",
        111 to "Kebencian Abu Lahab dan istrinya Ummu Jamil. Kelak mereka masuk neraka dengan tali dari sabut.",
        112 to "Surah tentang keesaan Allah: Dia satu, tempat bergantung, tidak beranak dan tidak diperanakkan, dan tidak ada yang setara dengan-Nya.",
        113 to "Berlindung kepada Tuhan yang menguasai subuh dari kejahatan ciptaan-Nya, kegelapan malam, dan kejahatan penyihir perempuan serta orang yang dengki.",
        114 to "Berlindung kepada Tuhan manusia, Raja manusia, dan Sesembahan manusia dari godaan setan yang berbisik di dada manusia, dari jin dan manusia."
    )
}

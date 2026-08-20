package com.sholatapp.data

import com.sholatapp.model.DoaCategory
import com.sholatapp.model.DoaItem

object DoaData {
    fun getAllDoa(): List<DoaItem> = listOf(

        // ==================== BANGUN TIDUR ====================
        DoaItem(
            id = "bangun_tidur_1",
            title = "Doa Bangun Tidur",
            arabic = "اَلْحَمْدُ لِلَّهِ الَّذِيْ أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُوْرُ",
            latin = "Alhamdulillahilladzi ahyana ba'da ma amatana wa ilaihin nusyur.",
            translation = "Segala puji bagi Allah yang telah menghidupkan kami setelah mematikan kami, dan kepada-Nya lah kami dikembalikan.",
            category = DoaCategory.BANGUN_TIDUR
        ),
        DoaItem(
            id = "bangun_tidur_2",
            title = "Doa Sebelum Tidur",
            arabic = "بِسْمِكَ اللَّهُمَّ أَمُوْتُ وَأَحْيَا",
            latin = "Bismikallahumma amutu wa ahya.",
            translation = "Dengan nama-Mu ya Allah, aku mati dan aku hidup.",
            category = DoaCategory.BANGUN_TIDUR
        ),

        // ==================== MASJID ====================
        DoaItem(
            id = "masjid_1",
            title = "Doa Masuk Masjid",
            arabic = "اَللَّهُمَّ افْتَحْ لِيْ أَبْوَابَ رَحْمَتِكَ",
            latin = "Allahummaftah li abwaba rahmatik.",
            translation = "Ya Allah, bukakanlah untukku pintu-pintu rahmat-Mu.",
            category = DoaCategory.MASJID
        ),
        DoaItem(
            id = "masjid_2",
            title = "Doa Keluar Masjid",
            arabic = "اَللَّهُمَّ إِنِّيْ أَسْأَلُكَ مِنْ فَضْلِكَ",
            latin = "Allahumma inni as-aluka min fadhlik.",
            translation = "Ya Allah, sesungguhnya aku memohon kepada-Mu dari karunia (keutamaan)-Mu.",
            category = DoaCategory.MASJID
        ),

        // ==================== MAKAN ====================
        DoaItem(
            id = "makan_1",
            title = "Doa Sebelum Makan",
            arabic = "بِسْمِ اللَّهِ وَبَرَكَةِ اللَّهِ",
            latin = "Bismillahi wa 'ala barakatillah.",
            translation = "Dengan menyebut nama Allah dan dengan keberkahan Allah.",
            category = DoaCategory.MAKAN
        ),
        DoaItem(
            id = "makan_2",
            title = "Doa Sesudah Makan",
            arabic = "اَلْحَمْدُ لِلَّهِ الَّذِيْ أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِيْنَ",
            latin = "Alhamdulillahilladzi at'amana wa saqana wa ja'alana muslimin.",
            translation = "Segala puji bagi Allah yang telah memberi makan kami, memberi minum kami, dan menjadikan kami orang-orang yang Islam.",
            category = DoaCategory.MAKAN
        ),
        DoaItem(
            id = "makan_3",
            title = "Doa Berbuka Puasa",
            arabic = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوْقُ وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
            latin = "Dzahabaz-zama' wa wabtallatil-'uruuq wa tsabatal-ajru insyaallah.",
            translation = "Telah hilang dahaga, urat-urat telah basah, dan pahala telah ditetapkan insya Allah.",
            category = DoaCategory.MAKAN
        ),

        // ==================== RUMAH ====================
        DoaItem(
            id = "rumah_1",
            title = "Doa Masuk Rumah",
            arabic = "بِسْمِ اللَّهِ وَلَجْنَا وَبِسْمِ اللَّهِ خَرَجْنَا وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا",
            latin = "Bismillahi walajna wa bismillahi kharajna wa 'alallahir-rabbuna tawakkalna.",
            translation = "Dengan menyebut nama Allah kami masuk, dan dengan menyebut nama Allah kami keluar, dan kepada Allah Tuhan kami kami bertawakkal.",
            category = DoaCategory.RUMAH
        ),
        DoaItem(
            id = "rumah_2",
            title = "Doa Keluar Rumah",
            arabic = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            latin = "Bismillahi tawakkaltu 'alallah, laa haula wa laa quwwata illa billah.",
            translation = "Dengan menyebut nama Allah, aku bertawakkal kepada Allah, tidak ada daya dan kekuatan kecuali dengan pertolongan Allah.",
            category = DoaCategory.RUMAH
        ),
        DoaItem(
            id = "rumah_3",
            title = "Doa Memakai Pakaian",
            arabic = "اَلْحَمْدُ لِلَّهِ الَّذِيْ كَسَانِيْ هَذَا الثَّوْبَ وَرَزَقَنِيْهِ مِنْ غَيْرِ حَوْلٍ مِنِّيْ وَلَا قُوَّةٍ",
            latin = "Alhamdulillahilladzi kasanani hadza ats-tsawba wa razaqanihi min ghairi haulin minni wa quwwatin.",
            translation = "Segala puji bagi Allah yang telah memberi pakaian kepadaku dan yang telah memberi rizki kepadaku tanpa daya dan kekuatan dari diriku.",
            category = DoaCategory.RUMAH
        ),
        DoaItem(
            id = "rumah_4",
            title = "Doa Menyambut Tamu",
            arabic = "أَهْلًا وَسَهْلًا وَمَرْحَبًا",
            latin = "Ahlan wa sahlan wa marhaban.",
            translation = "Selamat datang, semoga merasa mudah (senang), dan semoga diberi kebaikan.",
            category = DoaCategory.RUMAH
        ),

        // ==================== PERJALANAN ====================
        DoaItem(
            id = "perjalanan_1",
            title = "Doa Naik Kendaraan",
            arabic = "سُبْحَانَ الَّذِيْ سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِيْنَ وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُوْنَ",
            latin = "Subhanalladzi sakhkhara lana hadza wa ma kunna lahu muqrinin wa inna ila rabbina lamunqalibun.",
            translation = "Maha Suci Allah yang telah menundukkan untuk kami ini (kendaraan), padahal sebelumnya kami tidak mampu menguasainya. Dan sesungguhnya kami akan kembali kepada Tuhan kami.",
            category = DoaCategory.PERJALANAN
        ),
        DoaItem(
            id = "perjalanan_2",
            title = "Doa Safar",
            arabic = "اَللَّهُمَّ إِنِّيْ أَعُوْذُ بِكَ مِنْ وَعْثَاءِ السَّفَرِ وَكَآبَةِ الْمُنْقَلَبِ وَمِنْ سُوْءِ الْمُنْقَلَبِ فِي الْمَالِ وَالْأَهْلِ",
            latin = "Allahumma inni a'udzubika min wa-tha-is-safari wa ka-aabatil-manzari wa suu-il-munqalabi fil-mali wal-ahli.",
            translation = "Ya Allah, sesungguhnya aku berlindung kepada-Mu dari kesulitan perjalanan, dari pemandangan yang menyedihkan, dan dari keburukan kembali (pulang) terhadap harta dan keluarga.",
            category = DoaCategory.PERJALANAN
        ),
        DoaItem(
            id = "perjalanan_3",
            title = "Doa Sampai di Tempat Tujuan",
            arabic = "أَعُوْذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ كُلِّ شَيْطَانٍ وَهَامَّةٍ وَمِنْ كُلِّ عَيْنٍ لَامَّةٍ",
            latin = "A'udzu bikalimatillahit-tammaati min syarri kulli syaithaanin wa haammah, wa min kulli 'ainin laammah.",
            translation = "Aku berlindung dengan kalimat-kalimat Allah yang sempurna dari kejahatan setiap setan dan binatang berbisa, serta dari kejahatan setiap mata yang mengganggu.",
            category = DoaCategory.PERJALANAN
        ),

        // ==================== SHOLAT ====================
        DoaItem(
            id = "sholat_1",
            title = "Doa Iftitah",
            arabic = "اَللَّهُ أَكْبَرُ كَبِيْرًا وَالْحَمْدُ لِلَّهِ كَثِيْرًا وَسُبْحَانَ اللَّهِ بُكْرَةً وَأَصِيْلًا. إِنِّيْ وَجَّهْتُ وَجْهِيَ لِلَّذِيْ فَطَرَ السَّمَاوَاتِ وَالْأَرْضَ حَنِيْفًا وَمَا أَنَا مِنَ الْمُشْرِكِيْنَ. إِنَّ صَلَاتِيْ وَنُسُكِيْ وَمَحْيَايَ وَمَمَاتِيْ لِلَّهِ رَبِّ الْعَالَمِيْنَ، لَا شَرِيْكَ لَهُ وَبِذَلِكَ أُمِرْتُ وَأَنَا أَوَّلُ الْمُسْلِمِيْنَ.",
            latin = "Allahu akbaru kabiira walhamdulillahi katsiira wa subhanallahi bukratan wa ashiilaa. Innii wajjahtu wajhiya lilladzii fatharas-samaawaati wal-ardha haniifaa wa maa ana minal-musyrikiin. Innii shalaatii wa nusukii wa mahyaaya wa mamaatii lillahi rabbil-'aalamiin, laa syarikalah wa bidzaalika umirtu wa ana awwalul-muslimiin.",
            translation = "Allah Maha Besar dengan kebesaran yang sebenar-benarnya, segala puji bagi Allah dengan pujian yang banyak, Maha Suci Allah pada waktu pagi dan petang. Sesungguhnya aku hadapkan wajahku kepada Tuhan yang menciptakan langit dan bumi dalam keadaan tulus ikhlas, dan aku tidak termasuk orang-orang musyrik. Sesungguhnya shalatku, ibadahku, hidup dan matiku adalah bagi Allah, Tuhan seluruh alam, tidak ada sekutu bagi-Nya, dan dengan itulah aku diperintahkan, dan aku adalah orang pertama yang berserah diri (Islam).",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_2",
            title = "Doa Ruku",
            arabic = "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ (٣ مرات)",
            latin = "Subhana rabbiyal-adziimi wabihamdih. (3 kali)",
            translation = "Maha Suci Tuhanku Yang Maha Agung dan dengan memuji-Nya. (3 kali)",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_3",
            title = "Doa I'tidal",
            arabic = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ، رَبَّنَا لَكَ الْحَمْدُ مِلْءَ السَّمَاوَاتِ وَمِلْءَ الْأَرْضِ وَمِلْءَ مَا شِئْتَ مِنْ شَيْءٍ بَعْدُ",
            latin = "Sami'allahu liman hamidah, rabbana lakal-hamdu mil'us-samawaati wa mil-ardhi wa mil'a ma syi'ta min syai-in ba'du.",
            translation = "Allah mendengar orang yang memuji-Nya. Wahai Tuhan kami, bagi-Mu segala puji, sepenuh langit dan sepenuh bumi, dan sepenuh apa saja yang Engkau kehendaki setelah itu.",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_4",
            title = "Doa Sujud",
            arabic = "سُبْحَانَ رَبِّيَ الْأَعْلَى وَبِحَمْدِهِ (٣ مرات)",
            latin = "Subhana rabbiyal-a'laa wabihamdih. (3 kali)",
            translation = "Maha Suci Tuhanku Yang Maha Tinggi dan dengan memuji-Nya. (3 kali)",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_5",
            title = "Doa Duduk Antar Dua Sujud",
            arabic = "رَبِّ اغْفِرْلِيْ، رَبِّ اغْفِرْلِيْ",
            latin = "Rabbighfirlii, rabbighfirlii.",
            translation = "Wahai Tuhanku, berilah ampun kepadaku, wahai Tuhanku, berilah ampun kepadaku.",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_6",
            title = "Doa Tasyahud Awal",
            arabic = "اَلتَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ. اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ. اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِيْنَ. أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُوْلُهُ.",
            latin = "At-tahiyyatu lillahi was-shalawatu wat-tayyibatu. Assalamu 'alaika ayyuhan-nabiyyu wa rahmatullahi wa barakatuh. Assalamu 'alaina wa 'ala 'ibadillahis-shalihiin. Asyhadu allaailaha illallah wa asyhadu anna muhammadan 'abduhu wa rasuluh.",
            translation = "Segala penghormatan, kebajikan, dan keberkahan adalah milik Allah. Semoga keselamatan, rahmat, dan keberkahan dilimpahkan kepadamu wahai Nabi. Semoga keselamatan dilimpahkan kepada kami dan kepada hamba-hamba Allah yang saleh. Aku bersaksi bahwa tidak ada ilah selain Allah dan aku bersaksi bahwa Muhammad adalah hamba dan utusan-Nya.",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_7",
            title = "Doa Tasyahud Akhir",
            arabic = "اَلتَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ. اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ. اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِيْنَ. أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُوْلُهُ. اَللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيْمَ وَعَلَى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَجِيْدٌ. اَللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيْمَ وَعَلَى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَجِيْدٌ.",
            latin = "At-tahiyyatu lillahi was-shalawatu wat-tayyibatu. Assalamu 'alaika ayyuhan-nabiyyu wa rahmatullahi wa barakatuh. Assalamu 'alaina wa 'ala 'ibadillahis-shalihiin. Asyhadu allaailaha illallah wa asyhadu anna muhammadan 'abduhu wa rasuluh. Allahumma shalli 'ala muhammad wa 'ala aali muhammad kama shallaita 'ala ibrahim wa 'ala aali ibrahim innaka hamidun majid. Allahumma barik 'ala muhammad wa 'ala aali muhammad kama barakta 'ala ibrahim wa 'ala aali ibrahim innaka hamidun majid.",
            translation = "Segala penghormatan, kebajikan, dan keberkahan adalah milik Allah. Semoga keselamatan, rahmat, dan keberkahan dilimpahkan kepadamu wahai Nabi. Semoga keselamatan dilimpahkan kepada kami dan kepada hamba-hamba Allah yang saleh. Aku bersaksi bahwa tidak ada ilah selain Allah dan aku bersaksi bahwa Muhammad adalah hamba dan utusan-Nya. Ya Allah, berikanlah rahmat kepada Muhammad dan keluarga Muhammad, sebagaimana Engkau telah memberikan rahmat kepada Ibrahim dan keluarga Ibrahim. Sesungguhnya Engkau Maha Terpuji lagi Maha Mulia. Ya Allah, berkahilah Muhammad dan keluarga Muhammad, sebagaimana Engkau telah memberkati Ibrahim dan keluarga Ibrahim. Sesungguhnya Engkau Maha Terpuji lagi Maha Mulia.",
            category = DoaCategory.SHOLAT
        ),
        DoaItem(
            id = "sholat_8",
            title = "Doa Sebelum Salam",
            arabic = "اَللَّهُمَّ إِنِّيْ أَعُوْذُ بِكَ مِنْ عَذَابِ جَهَنَّمَ وَمِنْ عَذَابِ الْقَبْرِ وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ وَمِنْ فِتْنَةِ الْمَسِيْحِ الدَّجَّالِ",
            latin = "Allahumma inni a'udzubika min 'adzabi jahannam wa min 'adzabil-qabri wa min fitnatil-mahya wal-mamat wa min fitnatil-masiihid-dajjal.",
            translation = "Ya Allah, sesungguhnya aku berlindung kepada-Mu dari siksa neraka Jahanam, dari siksa kubur, dari fitnah kehidupan dan kematian, serta dari fitnah Al-Masih Dajjal.",
            category = DoaCategory.SHOLAT
        ),

        // ==================== HARIAN ====================
        DoaItem(
            id = "harian_1",
            title = "Doa Memulai Sesuatu",
            arabic = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيْمِ",
            latin = "Bismillahir-rahmanir-rahim.",
            translation = "Dengan menyebut nama Allah Yang Maha Pengasih lagi Maha Penyayang.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_2",
            title = "Doa Mohon Ilmu yang Bermanfaat",
            arabic = "اَللَّهُمَّ إِنِّيْ أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا وَعَمَلًا مُتَقَبَّلًا",
            latin = "Allahumma inni as-aluka 'ilman naafi'an, wa rizqan thoyyiban, wa 'amalan mutaqabbalan.",
            translation = "Ya Allah, sesungguhnya aku memohon kepada-Mu ilmu yang bermanfaat, rizki yang baik, dan amal yang diterima.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_3",
            title = "Doa Mohon Perlindungan dari Kelemahan",
            arabic = "اَللَّهُمَّ إِنِّيْ أَعُوْذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ وَالْعَجْزِ وَالْكَسَلِ وَالْبُخْلِ وَالْجُبْنِ وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
            latin = "Allahumma inni a'udzubika minal-hammi wal-huzni wal-'ajzi wal-kasali wal-bukhli wal-jubni wad-dhaini wa ghalabatir-rijaali.",
            translation = "Ya Allah, sesungguhnya aku berlindung kepada-Mu dari kegelisahan dan kesedihan, dari kelemahan dan kemalasan, dari kekikiran dan sifat pengecut, dari beban hutang, dan dari tekanan (kekuasaan) orang-orang laki-laki.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_4",
            title = "Doa Bersyukur",
            arabic = "اَلْحَمْدُ لِلَّهِ الَّذِيْ بِنِعْمَتِهِ تَتِمُّ الصَّالِحَاتُ",
            latin = "Alhamdulillahilladzi bi-ni'matihi tatimmush-shalihaat.",
            translation = "Segala puji bagi Allah yang dengan nikmat-Nya segala kebaikan menjadi sempurna.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_5",
            title = "Doa Mohon Kemudahan",
            arabic = "رَبِّ يَسِّرْ وَلَا تُعَسِّرْ، رَبِّ تَمِّمْ بِالْخَيْرِ",
            latin = "Rabbi yassir wala tu'assir, rabbi tamim bil-khayr.",
            translation = "Wahai Tuhanku, mudahkanlah dan janganlah Engkau persulit. Wahai Tuhanku, sempurnakanlah dengan kebaikan.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_6",
            title = "Doa Pagi (1)",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ. رَبِّ أَسْأَلُكَ خَيْرَ مَا فِيْ هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ وَأَعُوْذُ بِكَ مِنْ شَرِّ مَا فِيْ هَذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ",
            latin = "Ashbahnaa wa ashbahal-mulku lillahi walhamdulillah, laa ilaaha illallah wahdahu laa syarikalah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli syai-in qadiir. Rabbi as-aluka khayra maa fii hadzal-yawmi wa khayra maa ba'dahu wa a'udzubika min syarri maa fii hadzal-yawmi wa syarri maa ba'dahu.",
            translation = "Kami telah memasuki waktu pagi dan kerajaan adalah milik Allah, segala puji bagi Allah. Tidak ada ilah selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan bagi-Nya pujian, dan Dia Maha Kuasa atas segala sesuatu. Wahai Tuhanku, aku memohon kepada-Mu kebaikan pada hari ini dan kebaikan setelahnya, dan aku berlindung kepada-Mu dari keburukan pada hari ini dan keburukan setelahnya.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_7",
            title = "Doa Pagi (2)",
            arabic = "اَللَّهُمَّ بِكَ أَصْبَحْنَا وَبِكَ أَمْسَيْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوْتُ وَإِلَيْكَ النُّشُوْرُ",
            latin = "Allahumma bika ashbahnaa wa bika amsainaa wa bika nahyaa wa bika namuutu wa ilaikan-nusyur.",
            translation = "Ya Allah, dengan rahmat dan pertolongan-Mu kami memasuki waktu pagi, dengan rahmat dan pertolongan-Mu kami memasuki waktu petang, dengan rahmat-Mu kami hidup, dan dengan rahmat-Mu kami mati, dan kepada-Mu lah kebangkitan.",
            category = DoaCategory.HARIAN
        ),
        DoaItem(
            id = "harian_8",
            title = "Doa Petang",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ. رَبِّ أَسْأَلُكَ خَيْرَ مَا فِيْ هَذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا وَأَعُوْذُ بِكَ مِنْ شَرِّ مَا فِيْ هَذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا",
            latin = "Amsainaa wa amsal-mulku lillahi walhamdulillah, laa ilaaha illallah wahdahu laa syarikalah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli syai-in qadiir. Rabbi as-aluka khayra maa fii hadzal-layli wa khayra maa ba'dahu wa a'udzubika min syarri maa fii hadzal-layli wa syarri maa ba'dahu.",
            translation = "Kami telah memasuki waktu petang dan kerajaan adalah milik Allah, segala puji bagi Allah. Tidak ada ilah selain Allah semata, tidak ada sekutu bagi-Nya. Milik-Nya kerajaan dan bagi-Nya pujian, dan Dia Maha Kuasa atas segala sesuatu. Wahai Tuhanku, aku memohon kepada-Mu kebaikan pada malam ini dan kebaikan setelahnya, dan aku berlindung kepada-Mu dari keburukan pada malam ini dan keburukan setelahnya.",
            category = DoaCategory.HARIAN
        ),

        // ==================== LAINNYA ====================
        DoaItem(
            id = "lainnya_1",
            title = "Doa Ketika Mendengar Petir",
            arabic = "اَللَّهُمَّ لَا تَقْتُلْنَا بِغَضَبِكَ وَلَا تُهْلِكْنَا بِعَذَابِكَ وَعَافِنَا قَبْلَ ذَلِكَ",
            latin = "Allahumma laa taqtulna bi-ghadhabik wa laa tuhlikna bi-'adhabik wa 'afina qabladzaalik.",
            translation = "Ya Allah, janganlah Engkau membunuh kami dengan murka-Mu dan janganlah Engkau membinasakan kami dengan siksa-Mu, dan berilah kami kesejahteraan sebelum itu.",
            category = DoaCategory.LAINNYA
        ),
        DoaItem(
            id = "lainnya_2",
            title = "Doa Ketika Hujan",
            arabic = "اَللَّهُمَّ صَيِّبًا نَافِعًا",
            latin = "Allahumma shayyiban naafi'an.",
            translation = "Ya Allah, turunkanlah hujan yang bermanfaat.",
            category = DoaCategory.LAINNYA
        ),
        DoaItem(
            id = "lainnya_3",
            title = "Doa Melihat Bulan Sabit",
            arabic = "هِلَالُ خَيْرٍ وَرُشْدٍ",
            latin = "Hilal khayrin wa rushdin.",
            translation = "Bulan sabit kebaikan dan petunjuk.",
            category = DoaCategory.LAINNYA
        ),
        DoaItem(
            id = "lainnya_4",
            title = "Doa untuk Orang Tua",
            arabic = "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِيْ صَغِيْرًا",
            latin = "Rabbir-hamhuma kama rabbayanii shaghira.",
            translation = "Wahai Tuhanku, sayangilah keduanya sebagaimana mereka telah mendidikku di waktu kecil.",
            category = DoaCategory.LAINNYA
        ),
        DoaItem(
            id = "lainnya_5",
            title = "Doa untuk Orang Sakit",
            arabic = "اَللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَأْسَ، اِشْفِ أَنْتَ الشَّافِيْ، لَا شِفَاءَ إِلَّا شِفَاؤُكَ، شِفَاءً لَا يُغَادِرُ سَقَمًا",
            latin = "Allahumma rabban-naasi adhhibil-ba's, isyfi antasy-syaaafi, laa syifaa-a illa syifaa-uka, syifaa-an laa yughadiru saqaman.",
            translation = "Ya Allah, Tuhan manusia, hilangkanlah penyakitnya, sembuhkanlah, Dialah Yang Maha Menyembuhkan, tidak ada kesembuhan kecuali kesembuhan dari-Mu, kesembuhan yang tidak meninggalkan penyakit.",
            category = DoaCategory.LAINNYA
        )
    )
}

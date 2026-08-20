package com.sholatapp.data

import com.sholatapp.model.SurahInfo

object QuranMetadata {

    private val allSurahs: List<SurahInfo> = listOf(
        SurahInfo(1, "Al-Fatihah", "الفاتحة", 7, 1, "Makkiyah"),
        SurahInfo(2, "Al-Baqarah", "البقرة", 286, 1, "Madaniyah"),
        SurahInfo(3, "Ali Imran", "آل عمران", 200, 3, "Madaniyah"),
        SurahInfo(4, "An-Nisa", "النساء", 176, 4, "Madaniyah"),
        SurahInfo(5, "Al-Ma'idah", "المائدة", 120, 6, "Madaniyah"),
        SurahInfo(6, "Al-An'am", "الأنعام", 165, 7, "Makkiyah"),
        SurahInfo(7, "Al-A'raf", "الأعراف", 206, 8, "Makkiyah"),
        SurahInfo(8, "Al-Anfal", "الأنفال", 75, 9, "Madaniyah"),
        SurahInfo(9, "At-Taubah", "التوبة", 129, 10, "Madaniyah"),
        SurahInfo(10, "Yunus", "يونس", 109, 11, "Makkiyah"),
        SurahInfo(11, "Hud", "هود", 123, 11, "Makkiyah"),
        SurahInfo(12, "Yusuf", "يوسف", 111, 12, "Makkiyah"),
        SurahInfo(13, "Ar-Ra'd", "الرعد", 43, 13, "Madaniyah"),
        SurahInfo(14, "Ibrahim", "إبراهيم", 52, 13, "Makkiyah"),
        SurahInfo(15, "Al-Hijr", "الحجر", 99, 14, "Makkiyah"),
        SurahInfo(16, "An-Nahl", "النحل", 128, 14, "Makkiyah"),
        SurahInfo(17, "Al-Isra'", "الإسراء", 111, 15, "Makkiyah"),
        SurahInfo(18, "Al-Kahf", "الكهف", 110, 15, "Makkiyah"),
        SurahInfo(19, "Maryam", "مريم", 98, 16, "Makkiyah"),
        SurahInfo(20, "Taha", "طه", 135, 16, "Makkiyah"),
        SurahInfo(21, "Al-Anbiya'", "الأنبياء", 112, 17, "Makkiyah"),
        SurahInfo(22, "Al-Hajj", "الحج", 78, 17, "Madaniyah"),
        SurahInfo(23, "Al-Mu'minun", "المؤمنون", 118, 18, "Makkiyah"),
        SurahInfo(24, "An-Nur", "النور", 64, 18, "Madaniyah"),
        SurahInfo(25, "Al-Furqan", "الفرقان", 77, 18, "Makkiyah"),
        SurahInfo(26, "Ash-Shu'ara'", "الشعراء", 227, 19, "Makkiyah"),
        SurahInfo(27, "An-Naml", "النمل", 93, 19, "Makkiyah"),
        SurahInfo(28, "Al-Qasas", "القصص", 88, 20, "Makkiyah"),
        SurahInfo(29, "Al-Ankabut", "العنكبوت", 69, 20, "Makkiyah"),
        SurahInfo(30, "Ar-Rum", "الروم", 60, 21, "Makkiyah"),
        SurahInfo(31, "Luqman", "لقمان", 34, 21, "Makkiyah"),
        SurahInfo(32, "As-Sajdah", "السجدة", 30, 21, "Makkiyah"),
        SurahInfo(33, "Al-Ahzab", "الأحزاب", 73, 21, "Madaniyah"),
        SurahInfo(34, "Saba'", "سبأ", 54, 22, "Makkiyah"),
        SurahInfo(35, "Fatir", "فاطر", 45, 22, "Makkiyah"),
        SurahInfo(36, "Ya Sin", "يس", 83, 22, "Makkiyah"),
        SurahInfo(37, "As-Shaffat", "الصافات", 182, 23, "Makkiyah"),
        SurahInfo(38, "Shad", "ص", 88, 23, "Makkiyah"),
        SurahInfo(39, "Az-Zumar", "الزمر", 75, 23, "Makkiyah"),
        SurahInfo(40, "Ghafir", "غافر", 85, 24, "Makkiyah"),
        SurahInfo(41, "Fussilat", "فصلت", 54, 24, "Makkiyah"),
        SurahInfo(42, "Ash-Shura", "الشورى", 53, 25, "Makkiyah"),
        SurahInfo(43, "Az-Zukhruf", "الزخرف", 89, 25, "Makkiyah"),
        SurahInfo(44, "Ad-Dukhan", "الدخان", 59, 25, "Makkiyah"),
        SurahInfo(45, "Al-Jathiyah", "الجاثية", 37, 25, "Makkiyah"),
        SurahInfo(46, "Al-Ahqaf", "الأحقاف", 35, 26, "Makkiyah"),
        SurahInfo(47, "Muhammad", "محمد", 38, 26, "Madaniyah"),
        SurahInfo(48, "Al-Fath", "الفتح", 29, 26, "Madaniyah"),
        SurahInfo(49, "Al-Hujurat", "الحجرات", 18, 26, "Madaniyah"),
        SurahInfo(50, "Qaf", "ق", 45, 26, "Makkiyah"),
        SurahInfo(51, "Adz-Dzariyat", "الذاريات", 60, 26, "Makkiyah"),
        SurahInfo(52, "At-Tur", "الطور", 49, 27, "Makkiyah"),
        SurahInfo(53, "An-Najm", "النجم", 62, 27, "Makkiyah"),
        SurahInfo(54, "Al-Qamar", "القمر", 55, 27, "Makkiyah"),
        SurahInfo(55, "Ar-Rahman", "الرحمن", 78, 27, "Madaniyah"),
        SurahInfo(56, "Al-Waqi'ah", "الواقعة", 96, 27, "Makkiyah"),
        SurahInfo(57, "Al-Hadid", "الحديد", 29, 27, "Madaniyah"),
        SurahInfo(58, "Al-Mujadilah", "المجادلة", 22, 28, "Madaniyah"),
        SurahInfo(59, "Al-Hashr", "الحشر", 24, 28, "Madaniyah"),
        SurahInfo(60, "Al-Mumtahanah", "الممتحنة", 13, 28, "Madaniyah"),
        SurahInfo(61, "Ash-Shaff", "الصف", 14, 28, "Madaniyah"),
        SurahInfo(62, "Al-Jumu'ah", "الجمعة", 11, 28, "Madaniyah"),
        SurahInfo(63, "Al-Munafiqun", "المنافقون", 11, 28, "Madaniyah"),
        SurahInfo(64, "At-Taghabun", "التغابن", 18, 28, "Madaniyah"),
        SurahInfo(65, "At-Talaq", "الطلاق", 12, 28, "Madaniyah"),
        SurahInfo(66, "At-Tahrim", "التحريم", 12, 28, "Madaniyah"),
        SurahInfo(67, "Al-Mulk", "الملك", 30, 29, "Makkiyah"),
        SurahInfo(68, "Al-Qalam", "القلم", 52, 29, "Makkiyah"),
        SurahInfo(69, "Al-Haqqah", "الحاقة", 52, 29, "Makkiyah"),
        SurahInfo(70, "Al-Ma'arij", "المعارج", 44, 29, "Makkiyah"),
        SurahInfo(71, "Nuh", "نوح", 28, 29, "Makkiyah"),
        SurahInfo(72, "Al-Jinn", "الجن", 28, 29, "Makkiyah"),
        SurahInfo(73, "Al-Muzzammil", "المزمل", 20, 29, "Makkiyah"),
        SurahInfo(74, "Al-Muddatsir", "المدثر", 56, 29, "Makkiyah"),
        SurahInfo(75, "Al-Qiyamah", "القيامة", 40, 29, "Makkiyah"),
        SurahInfo(76, "Al-Insan", "الإنسان", 31, 29, "Makkiyah"),
        SurahInfo(77, "Al-Mursalat", "المرسلات", 50, 29, "Makkiyah"),
        SurahInfo(78, "An-Naba'", "النبأ", 40, 30, "Makkiyah"),
        SurahInfo(79, "An-Nazi'at", "النازعات", 46, 30, "Makkiyah"),
        SurahInfo(80, "'Abasa", "عبس", 42, 30, "Makkiyah"),
        SurahInfo(81, "At-Takwir", "التكوير", 29, 30, "Makkiyah"),
        SurahInfo(82, "Al-Infitar", "الانفطار", 19, 30, "Makkiyah"),
        SurahInfo(83, "Al-Muthaffifin", "المطففين", 36, 30, "Makkiyah"),
        SurahInfo(84, "Al-Insyiqaq", "الانشقاق", 25, 30, "Makkiyah"),
        SurahInfo(85, "Al-Buruj", "البروج", 22, 30, "Makkiyah"),
        SurahInfo(86, "At-Thariq", "الطارق", 17, 30, "Makkiyah"),
        SurahInfo(87, "Al-A'la", "الأعلى", 19, 30, "Makkiyah"),
        SurahInfo(88, "Al-Ghasyiyah", "الغاشية", 26, 30, "Makkiyah"),
        SurahInfo(89, "Al-Fajr", "الفجر", 30, 30, "Makkiyah"),
        SurahInfo(90, "Al-Balad", "البلد", 20, 30, "Makkiyah"),
        SurahInfo(91, "Ash-Shams", "الشمس", 15, 30, "Makkiyah"),
        SurahInfo(92, "Al-Lail", "الليل", 21, 30, "Makkiyah"),
        SurahInfo(93, "Ad-Duha", "الضحى", 11, 30, "Makkiyah"),
        SurahInfo(94, "Ash-Sharh", "الشرح", 8, 30, "Makkiyah"),
        SurahInfo(95, "At-Tin", "التين", 8, 30, "Makkiyah"),
        SurahInfo(96, "Al-'Alaq", "العلق", 19, 30, "Makkiyah"),
        SurahInfo(97, "Al-Qadr", "القدر", 5, 30, "Makkiyah"),
        SurahInfo(98, "Al-Bayyinah", "البينة", 8, 30, "Madaniyah"),
        SurahInfo(99, "Az-Zalzalah", "الزلزلة", 8, 30, "Madaniyah"),
        SurahInfo(100, "Al-'Adiyat", "العاديات", 11, 30, "Makkiyah"),
        SurahInfo(101, "Al-Qari'ah", "القارعة", 11, 30, "Makkiyah"),
        SurahInfo(102, "At-Takatsur", "التكاثر", 8, 30, "Makkiyah"),
        SurahInfo(103, "Al-'Asr", "العصر", 3, 30, "Makkiyah"),
        SurahInfo(104, "Al-Humazah", "الهمزة", 9, 30, "Makkiyah"),
        SurahInfo(105, "Al-Fil", "الفيل", 5, 30, "Makkiyah"),
        SurahInfo(106, "Quraisy", "قريش", 4, 30, "Makkiyah"),
        SurahInfo(107, "Al-Ma'un", "الماعون", 7, 30, "Makkiyah"),
        SurahInfo(108, "Al-Kautsar", "الكوثر", 3, 30, "Makkiyah"),
        SurahInfo(109, "Al-Kafirun", "الكافرون", 6, 30, "Makkiyah"),
        SurahInfo(110, "An-Nashr", "النصر", 3, 30, "Madaniyah"),
        SurahInfo(111, "Al-Masad", "المسد", 5, 30, "Makkiyah"),
        SurahInfo(112, "Al-Ikhlas", "الإخلاص", 4, 30, "Makkiyah"),
        SurahInfo(113, "Al-Falaq", "الفلق", 5, 30, "Makkiyah"),
        SurahInfo(114, "An-Nas", "الناس", 6, 30, "Makkiyah")
    )

    fun getAllSurahs(): List<SurahInfo> = allSurahs

    private val juzEndMap: Map<Int, Int> by lazy {
        val map = mutableMapOf<Int, Int>()
        allSurahs.forEachIndexed { index, surah ->
            val juzEnd = if (index < allSurahs.lastIndex) {
                maxOf(surah.juzStart, allSurahs[index + 1].juzStart)
            } else {
                surah.juzStart
            }
            map[surah.number] = juzEnd
        }
        map
    }

    fun getSurahsInJuz(juz: Int): List<SurahInfo> {
        return allSurahs.filter { surah ->
            val end = juzEndMap[surah.number] ?: surah.juzStart
            surah.juzStart <= juz && end >= juz
        }
    }

    fun getDailyJuz(): Int {
        val cal = java.util.Calendar.getInstance()
        val dayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR)
        return ((dayOfYear - 1) % 30) + 1
    }
}

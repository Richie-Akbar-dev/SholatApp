package com.sholatapp.data

import android.content.Context
import android.content.SharedPreferences
import com.sholatapp.model.SurahInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ============================================================
// Data Classes
// ============================================================

data class TilawahAyahRef(
    val surahNumber: Int,
    val surahName: String,
    val surahNameArabic: String,
    val ayahStart: Int,
    val ayahEnd: Int,
    val globalStart: Int,
    val globalEnd: Int
)

data class TilawahAyahText(
    val arabic: String,
    val latin: String,
    val translation: String
)

data class TilawahProgressInfo(
    val startVerse: Int,
    val versesPerDay: Int,
    val targetDays: Int,
    val totalReadDays: Int,
    val currentStreak: Int,
    val todayRead: Boolean,
    val completionPercent: Float
)

// ============================================================
// Main TilawahData Object
// ============================================================

object TilawahData {

    const val TOTAL_QURAN_VERSES = 6236

    private const val PREFS_NAME = "tilawah_prefs"
    private const val KEY_START_VERSE = "start_verse"
    private const val KEY_VERSES_PER_DAY = "verses_per_day"
    private const val KEY_TARGET_DAYS = "target_days"
    private const val KEY_READ_DATES = "read_dates"

    // ----------------------------------------------------------
    // Cumulative verse counts: index = surahNumber - 1
    // Value = cumulative total up to and including that surah
    // ----------------------------------------------------------
    private val cumulativeVerses = intArrayOf(
        7,       // 1  Al-Fatihah
        293,     // 2  Al-Baqarah
        493,     // 3  Ali Imran
        669,     // 4  An-Nisa
        789,     // 5  Al-Ma'idah
        954,     // 6  Al-An'am
        1160,    // 7  Al-A'raf
        1235,    // 8  Al-Anfal
        1364,    // 9  At-Taubah
        1473,    // 10 Yunus
        1596,    // 11 Hud
        1707,    // 12 Yusuf
        1750,    // 13 Ar-Ra'd
        1802,    // 14 Ibrahim
        1901,    // 15 Al-Hijr
        2029,    // 16 An-Nahl
        2140,    // 17 Al-Isra'
        2250,    // 18 Al-Kahf
        2348,    // 19 Maryam
        2483,    // 20 Taha
        2595,    // 21 Al-Anbiya'
        2673,    // 22 Al-Hajj
        2791,    // 23 Al-Mu'minun
        2855,    // 24 An-Nur
        2932,    // 25 Al-Furqan
        3159,    // 26 Ash-Shu'ara'
        3252,    // 27 An-Naml
        3340,    // 28 Al-Qasas
        3409,    // 29 Al-Ankabut
        3469,    // 30 Ar-Rum
        3503,    // 31 Luqman
        3533,    // 32 As-Sajdah
        3606,    // 33 Al-Ahzab
        3660,    // 34 Saba'
        3705,    // 35 Fatir
        3788,    // 36 Ya Sin
        3970,    // 37 As-Shaffat
        4058,    // 38 Shad
        4133,    // 39 Az-Zumar
        4218,    // 40 Ghafir
        4272,    // 41 Fussilat
        4325,    // 42 Ash-Shura
        4414,    // 43 Az-Zukhruf
        4473,    // 44 Ad-Dukhan
        4510,    // 45 Al-Jathiyah
        4545,    // 46 Al-Ahqaf
        4583,    // 47 Muhammad
        4612,    // 48 Al-Fath
        4630,    // 49 Al-Hujurat
        4675,    // 50 Qaf
        4735,    // 51 Adz-Dzariyat
        4784,    // 52 At-Tur
        4846,    // 53 An-Najm
        4901,    // 54 Al-Qamar
        4979,    // 55 Ar-Rahman
        5075,    // 56 Al-Waqi'ah
        5104,    // 57 Al-Hadid
        5126,    // 58 Al-Mujadilah
        5150,    // 59 Al-Hashr
        5163,    // 60 Al-Mumtahanah
        5177,    // 61 Ash-Shaff
        5188,    // 62 Al-Jumu'ah
        5199,    // 63 Al-Munafiqun
        5217,    // 64 At-Taghabun
        5229,    // 65 At-Talaq
        5241,    // 66 At-Tahrim
        5271,    // 67 Al-Mulk
        5323,    // 68 Al-Qalam
        5375,    // 69 Al-Haqqah
        5419,    // 70 Al-Ma'arij
        5447,    // 71 Nuh
        5475,    // 72 Al-Jinn
        5495,    // 73 Al-Muzzammil
        5551,    // 74 Al-Muddatsir
        5591,    // 75 Al-Qiyamah
        5622,    // 76 Al-Insan
        5672,    // 77 Al-Mursalat
        5712,    // 78 An-Naba'
        5758,    // 79 An-Nazi'at
        5800,    // 80 'Abasa
        5829,    // 81 At-Takwir
        5848,    // 82 Al-Infitar
        5884,    // 83 Al-Muthaffifin
        5909,    // 84 Al-Insyiqaq
        5931,    // 85 Al-Buruj
        5948,    // 86 At-Thariq
        5967,    // 87 Al-A'la
        5993,    // 88 Al-Ghasyiyah
        6023,    // 89 Al-Fajr
        6043,    // 90 Al-Balad
        6058,    // 91 Ash-Shams
        6079,    // 92 Al-Lail
        6090,    // 93 Ad-Duha
        6098,    // 94 Ash-Sharh
        6106,    // 95 At-Tin
        6125,    // 96 Al-'Alaq
        6130,    // 97 Al-Qadr
        6138,    // 98 Al-Bayyinah
        6146,    // 99 Az-Zalzalah
        6157,    // 100 Al-'Adiyat
        6168,    // 101 Al-Qari'ah
        6176,    // 102 At-Takatsur
        6179,    // 103 Al-'Asr
        6188,    // 104 Al-Humazah
        6193,    // 105 Al-Fil
        6197,    // 106 Quraisy
        6204,    // 107 Al-Ma'un
        6207,    // 108 Al-Kautsar
        6213,    // 109 Al-Kafirun
        6216,    // 110 An-Nashr
        6221,    // 111 Al-Masad
        6225,    // 112 Al-Ikhlas
        6230,    // 113 Al-Falaq
        6236     // 114 An-Nas
    )

    // ----------------------------------------------------------
    // SharedPreferences Helpers
    // ----------------------------------------------------------

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
        return sdf.format(Date())
    }

    /**
     * Initialize tilawah tracking with default or custom settings.
     */
    fun initializeTracking(
        context: Context,
        startVerse: Int = 1,
        versesPerDay: Int = 20,
        targetDays: Int = (TOTAL_QURAN_VERSES + 19) / 20
    ) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putInt(KEY_START_VERSE, startVerse)
            .putInt(KEY_VERSES_PER_DAY, versesPerDay)
            .putInt(KEY_TARGET_DAYS, targetDays)
            .putStringSet(KEY_READ_DATES, emptySet())
            .apply()
    }

    // ----------------------------------------------------------
    // Core Lookup: Global Ayah Number → Surah Info
    // ----------------------------------------------------------

    /**
     * Given a global ayah number (1–6236), return the surah info,
     * the local ayah number within that surah, and the surah number.
     */
    fun getSurahByGlobalAyah(globalAyah: Int): Triple<SurahInfo, Int, Int> {
        require(globalAyah in 1..TOTAL_QURAN_VERSES) {
            "Global ayah must be between 1 and $TOTAL_QURAN_VERSES, got $globalAyah"
        }

        val allSurahs = QuranMetadata.getAllSurahs()
        var previousCumulative = 0

        for (i in cumulativeVerses.indices) {
            val cumulative = cumulativeVerses[i]
            if (globalAyah <= cumulative) {
                val surah = allSurahs[i]
                val localAyah = globalAyah - previousCumulative
                return Triple(surah, i + 1, localAyah)
            }
            previousCumulative = cumulative
        }

        // Fallback (should never reach here)
        val lastSurah = allSurahs.last()
        val lastLocal = globalAyah - cumulativeVerses[cumulativeVerses.lastIndex - 1]
        return Triple(lastSurah, 114, lastLocal)
    }

    // ----------------------------------------------------------
    // Today's Portion
    // ----------------------------------------------------------

    /**
     * Given a starting global ayah number and a count of verses per day,
     * return a list of [TilawahAyahRef] that may span multiple surahs.
     */
    fun getTodayPortion(
        startVerse: Int,
        versesPerDay: Int
    ): List<TilawahAyahRef> {
        val endVerse = minOf(startVerse + versesPerDay - 1, TOTAL_QURAN_VERSES)
        if (startVerse > TOTAL_QURAN_VERSES) return emptyList()

        val result = mutableListOf<TilawahAyahRef>()
        var currentGlobal = startVerse

        while (currentGlobal <= endVerse) {
            val (surah, surahNumber, localAyah) = getSurahByGlobalAyah(currentGlobal)

            // How many verses remain in this surah from the local ayah onward?
            val surahTotalAyahs = surah.ayahCount
            val remainingInSurah = surahTotalAyahs - localAyah + 1

            // How many verses remain in today's portion?
            val remainingToday = endVerse - currentGlobal + 1

            // The number of ayahs we can take from this surah
            val takeFromThisSurah = minOf(remainingInSurah, remainingToday)

            result.add(
                TilawahAyahRef(
                    surahNumber = surahNumber,
                    surahName = surah.nameIndonesian,
                    surahNameArabic = surah.nameArabic,
                    ayahStart = localAyah,
                    ayahEnd = localAyah + takeFromThisSurah - 1,
                    globalStart = currentGlobal,
                    globalEnd = currentGlobal + takeFromThisSurah - 1
                )
            )

            currentGlobal += takeFromThisSurah
        }

        return result
    }

    /**
     * Convenience overload: reads settings from SharedPreferences.
     */
    fun getTodayPortion(context: Context): List<TilawahAyahRef> {
        val prefs = getPrefs(context)
        val startVerse = prefs.getInt(KEY_START_VERSE, 1)
        val versesPerDay = prefs.getInt(KEY_VERSES_PER_DAY, 20)
        return getTodayPortion(startVerse, versesPerDay)
    }

    // ----------------------------------------------------------
    // Tracking Functions
    // ----------------------------------------------------------

    /**
     * Mark today as read and advance the start verse.
     */
    fun markTodayAsRead(context: Context) {
        val prefs = getPrefs(context)
        val today = getTodayDateString()

        // Add today to the read dates set
        val readDates = prefs.getStringSet(KEY_READ_DATES, emptySet())?.toMutableSet() ?: mutableSetOf()
        readDates.add(today)

        val editor = prefs.edit()
        editor.putStringSet(KEY_READ_DATES, readDates)

        // If today hasn't been marked before, advance the start verse
        val versesPerDay = prefs.getInt(KEY_VERSES_PER_DAY, 20)
        val currentStart = prefs.getInt(KEY_START_VERSE, 1)
        val nextStart = currentStart + versesPerDay

        if (nextStart <= TOTAL_QURAN_VERSES + 1) {
            editor.putInt(KEY_START_VERSE, nextStart)
        }

        editor.apply()
    }

    /**
     * Check whether today has already been marked as read.
     */
    fun isTodayRead(context: Context): Boolean {
        val prefs = getPrefs(context)
        val today = getTodayDateString()
        val readDates = prefs.getStringSet(KEY_READ_DATES, emptySet()) ?: emptySet()
        return today in readDates
    }

    /**
     * Get comprehensive progress information.
     */
    fun getProgress(context: Context): TilawahProgressInfo {
        val prefs = getPrefs(context)
        val startVerse = prefs.getInt(KEY_START_VERSE, 1)
        val versesPerDay = prefs.getInt(KEY_VERSES_PER_DAY, 20)
        val targetDays = prefs.getInt(KEY_TARGET_DAYS, (TOTAL_QURAN_VERSES + 19) / 20)
        val readDates = prefs.getStringSet(KEY_READ_DATES, emptySet()) ?: emptySet()

        val totalReadDays = readDates.size
        val totalVersesRead = totalReadDays * versesPerDay
        val completionPercent = if (TOTAL_QURAN_VERSES > 0) {
            (totalVersesRead.toFloat() / TOTAL_QURAN_VERSES) * 100f
        } else 0f

        return TilawahProgressInfo(
            startVerse = startVerse,
            versesPerDay = versesPerDay,
            targetDays = targetDays,
            totalReadDays = totalReadDays,
            currentStreak = getStreak(context),
            todayRead = isTodayRead(context),
            completionPercent = completionPercent.coerceAtMost(100f)
        )
    }

    /**
     * Calculate the current reading streak (consecutive days up to today).
     */
    fun getStreak(context: Context): Int {
        val prefs = getPrefs(context)
        val readDates = prefs.getStringSet(KEY_READ_DATES, emptySet()) ?: emptySet()
        if (readDates.isEmpty()) return 0

        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
        var streak = 0
        val calendar = java.util.Calendar.getInstance()

        // Check today first
        for (i in 0..365) {
            val dateStr = sdf.format(calendar.time)
            if (dateStr in readDates) {
                streak++
            } else {
                // Allow today to be unread (streak is consecutive days ending yesterday)
                if (i == 0) continue else break
            }
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
        }

        return streak
    }

    /**
     * Reset all progress tracking to defaults.
     */
    fun resetProgress(context: Context) {
        initializeTracking(
            context = context,
            startVerse = 1,
            versesPerDay = 20,
            targetDays = (TOTAL_QURAN_VERSES + 19) / 20
        )
    }

    /**
     * Update the verses-per-day setting.
     */
    fun setVersesPerDay(context: Context, count: Int) {
        val prefs = getPrefs(context)
        prefs.edit().putInt(KEY_VERSES_PER_DAY, count).apply()
    }

    /**
     * Update the target days setting.
     */
    fun setTargetDays(context: Context, days: Int) {
        val prefs = getPrefs(context)
        prefs.edit().putInt(KEY_TARGET_DAYS, days).apply()
    }

    // ----------------------------------------------------------
    // Sample Data: Al-Fatihah (Surah 1)
    // ----------------------------------------------------------

    val alFatihahSample: Map<Int, TilawahAyahText> = mapOf(
        1 to TilawahAyahText(
            arabic = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
            latin = "Bismillāhir-Raḥmānir-Raḥīm",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang"
        ),
        2 to TilawahAyahText(
            arabic = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ",
            latin = "Alḥamdulillāhi Rabbil-'ālamīn",
            translation = "Segala puji bagi Allah, Tuhan seluruh alam"
        ),
        3 to TilawahAyahText(
            arabic = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
            latin = "Ar-Raḥmānir-Raḥīm",
            translation = "Yang Maha Pengasih, Maha Penyayang"
        ),
        4 to TilawahAyahText(
            arabic = "مَـٰلِكِ يَوْمِ ٱلدِّينِ",
            latin = "Māliki Yaumid-Dīn",
            translation = "Yang menguasai Hari Pembalasan"
        ),
        5 to TilawahAyahText(
            arabic = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            latin = "Iyyāka na'budu wa iyyāka nasta'īn",
            translation = "Hanya kepada Engkau kami menyembah, dan hanya kepada Engkau kami meminta pertolongan"
        ),
        6 to TilawahAyahText(
            arabic = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
            latin = "Ihdinaṣ-Ṣirāṭal-Mustaqīm",
            translation = "Tunjukilah kami jalan yang lurus"
        ),
        7 to TilawahAyahText(
            arabic = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
            latin = "Ṣirāṭal-lażīna an'amta 'alaihim, ghairil-magḍūbi 'alaihim walāḍ-ḍāllīn",
            translation = "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya, bukan (jalan) mereka yang dimurkai dan bukan (pula) jalan mereka yang sesat"
        )
    )
}
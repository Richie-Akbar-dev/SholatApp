package com.sholatapp.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository Al-Qur'an lengkap 114 surah / 6236 ayat (v2.3).
 *
 * Sumber data: equran.id v2 (terjemahan Kemenag RI) — dibundel permanen
 * di assets/quran/ sehingga 100% offline tanpa internet:
 *   - quran/index.json       : metadata 114 surah
 *   - quran/surat/NNN.json   : teks per surah {arab, latin, arti}
 *
 * Setiap ayat punya 3 unsur (sesuai permintaan v2.3):
 *   ar = tulisan Arab, lt = arab-latin, id = arti (Bahasa Indonesia)
 */
object QuranRepository {

    data class Ayah(
        val number: Int,
        val arabic: String,
        val latin: String,
        val translation: String
    )

    data class Surah(
        val number: Int,
        val arabicName: String,
        val latinName: String,
        val ayahCount: Int,
        val revelation: String,
        val meaning: String,
        val ayahs: List<Ayah> = emptyList()
    ) {
        /** Contoh: "Al-Fatihah · Mekah · 7 ayat" */
        val subtitle: String
            get() = "$revelation · $ayahCount ayat"
    }

    private var indexCache: List<Surah>? = null
    private val surahCache = LinkedHashMap<Int, Surah>()

    /** Metadata seluruh 114 surah (tanpa teks ayat — ringan). */
    fun getSurahIndex(context: Context): List<Surah> {
        indexCache?.let { return it }
        val list = mutableListOf<Surah>()
        try {
            val json = context.assets.open("quran/index.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                list.add(
                    Surah(
                        number = o.getInt("nomor"),
                        arabicName = o.getString("nama"),
                        latinName = o.getString("namaLatin"),
                        ayahCount = o.getInt("jumlahAyat"),
                        revelation = o.getString("tempatTurun"),
                        meaning = o.getString("arti")
                    )
                )
            }
        } catch (_: Exception) {
            // assets tidak ditemukan / korup — kembalikan apa adanya (daftar kosong)
        }
        indexCache = list
        return list
    }

    /** Satu surah lengkap dengan 3 unsur teks per ayat (cache di memori). */
    fun getSurah(context: Context, number: Int): Surah? {
        surahCache[number]?.let { return it }
        return try {
            val json = context.assets
                .open("quran/surat/${number.toString().padStart(3, '0')}.json")
                .bufferedReader().use { it.readText() }
            val o = JSONObject(json)
            val ayatArr = o.getJSONArray("ayat")
            val ayahs = mutableListOf<Ayah>()
            for (i in 0 until ayatArr.length()) {
                val a = ayatArr.getJSONObject(i)
                ayahs.add(
                    Ayah(
                        number = a.getInt("n"),
                        arabic = a.getString("ar"),
                        latin = a.getString("lt"),
                        translation = a.getString("id")
                    )
                )
            }
            val surah = Surah(
                number = o.getInt("nomor"),
                arabicName = o.getString("nama"),
                latinName = o.getString("namaLatin"),
                ayahCount = o.getInt("jumlahAyat"),
                revelation = o.getString("tempatTurun"),
                meaning = o.getString("arti"),
                ayahs = ayahs
            )
            // Jaga memori: cache maksimal 5 surah terakhir
            if (surahCache.size >= 5) {
                surahCache.remove(surahCache.keys.first())
            }
            surahCache[number] = surah
            surah
        } catch (_: Exception) {
            null
        }
    }

    /** Total seluruh ayat Al-Qur'an (6236) — dipakai mesin khatam. */
    const val TOTAL_AYAH = 6236
}

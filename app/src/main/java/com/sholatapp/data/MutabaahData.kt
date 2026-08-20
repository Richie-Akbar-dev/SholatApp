package com.sholatapp.data

import com.sholatapp.model.MutabaahCategory
import com.sholatapp.model.MutabaahItem

object MutabaahData {

    fun getAllItems(): List<MutabaahItem> = listOf(

        // ==================== SHOLAT FARDHU ====================
        MutabaahItem(
            id = "sholat_fardhu_subuh",
            title = "Sholat Subuh",
            category = MutabaahCategory.SHOLAT_FARDHU
        ),
        MutabaahItem(
            id = "sholat_fardhu_dzuhur",
            title = "Sholat Dzuhur",
            category = MutabaahCategory.SHOLAT_FARDHU
        ),
        MutabaahItem(
            id = "sholat_fardhu_ashar",
            title = "Sholat Ashar",
            category = MutabaahCategory.SHOLAT_FARDHU
        ),
        MutabaahItem(
            id = "sholat_fardhu_maghrib",
            title = "Sholat Maghrib",
            category = MutabaahCategory.SHOLAT_FARDHU
        ),
        MutabaahItem(
            id = "sholat_fardhu_isya",
            title = "Sholat Isya",
            category = MutabaahCategory.SHOLAT_FARDHU
        ),

        // ==================== SHOLAT SUNNAH ====================
        MutabaahItem(
            id = "sholat_sunnah_rawatib_subuh",
            title = "Rawatib Subuh (2 rakaat sebelum Subuh)",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_rawatib_dzuhur",
            title = "Rawatib Dzuhur (4+2 rakaat sebelum/sesudah)",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_rawatib_maghrib",
            title = "Rawatib Maghrib (2 rakaat sesudah Maghrib)",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_rawatib_isya",
            title = "Rawatib Isya (2 rakaat sesudah Isya)",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_dhuha",
            title = "Sholat Dhuha",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_tahajjud",
            title = "Sholat Tahajjud",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),
        MutabaahItem(
            id = "sholat_sunnah_witr",
            title = "Sholat Witr",
            category = MutabaahCategory.SHOLAT_SUNNAH,
            isSunnah = true
        ),

        // ==================== AL-QUR'AN ====================
        MutabaahItem(
            id = "quran_tilawah",
            title = "Tilawah minimal 1 halaman",
            category = MutabaahCategory.QURAN
        ),
        MutabaahItem(
            id = "quran_tadabbur",
            title = "Tadabbur Al-Qur'an (1 ayah)",
            category = MutabaahCategory.QURAN
        ),
        MutabaahItem(
            id = "quran_membaca",
            title = "Membaca Al-Qur'an",
            category = MutabaahCategory.QURAN
        ),

        // ==================== DZIKIR & DOA ====================
        MutabaahItem(
            id = "dzikir_pagi",
            title = "Dzikir Pagi",
            category = MutabaahCategory.DZIKIR
        ),
        MutabaahItem(
            id = "dzikir_petang",
            title = "Dzikir Petang",
            category = MutabaahCategory.DZIKIR
        ),
        MutabaahItem(
            id = "dzikir_setelah_sholat",
            title = "Dzikir Setelah Sholat",
            category = MutabaahCategory.DZIKIR
        ),
        MutabaahItem(
            id = "dzikir_doa_harian",
            title = "Doa Harian",
            category = MutabaahCategory.DZIKIR
        ),

        // ==================== AKHLAK ====================
        MutabaahItem(
            id = "akhlak_sedekah",
            title = "Sedekah",
            category = MutabaahCategory.AKHLAK
        ),
        MutabaahItem(
            id = "akhlak_silaturahmi",
            title = "Silaturahmi",
            category = MutabaahCategory.AKHLAK
        ),
        MutabaahItem(
            id = "akhlak_menjaga_lisan",
            title = "Menjaga Lisan",
            category = MutabaahCategory.AKHLAK
        ),
        MutabaahItem(
            id = "akhlak_sholat_berjamaah",
            title = "Sholat Berjamaah",
            category = MutabaahCategory.AKHLAK
        )
    )
}

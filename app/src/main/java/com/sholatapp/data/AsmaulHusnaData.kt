package com.sholatapp.data

import com.sholatapp.model.AsmaulHusnaItem

/**
 * Data 99 Asmaul Husna (Nama-nama Allah yang Indah)
 * dengan teks Arab, transliterasi Latin, dan arti dalam Bahasa Indonesia.
 */
object AsmaulHusnaData {

    private val names = listOf(
        AsmaulHusnaItem(1, "الرَّحْمَنُ", "Ar-Rahman", "Yang Maha Pengasih"),
        AsmaulHusnaItem(2, "الرَّحِيمُ", "Ar-Rahim", "Yang Maha Penyayang"),
        AsmaulHusnaItem(3, "الْمَلِكُ", "Al-Malik", "Yang Maha Merajai / Raja sekelumit raja"),
        AsmaulHusnaItem(4, "الْقُدُّوسُ", "Al-Quddus", "Yang Maha Suci"),
        AsmaulHusnaItem(5, "السَّلَامُ", "As-Salam", "Yang Maha Memberi Kesejahteraan"),
        AsmaulHusnaItem(6, "الْمُؤْمِنُ", "Al-Mu'min", "Yang Maha Memberi Keamanan"),
        AsmaulHusnaItem(7, "الْمُهَيْمِنُ", "Al-Muhaimin", "Yang Maha Mengawasi"),
        AsmaulHusnaItem(8, "الْعَزِيزُ", "Al-'Aziz", "Yang Maha Perkasa"),
        AsmaulHusnaItem(9, "الْجَبَّارُ", "Al-Jabbar", "Yang Maha Memaksa"),
        AsmaulHusnaItem(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "Yang Maha Megah"),
        AsmaulHusnaItem(11, "الْخَالِقُ", "Al-Khaliq", "Yang Maha Pencipta"),
        AsmaulHusnaItem(12, "الْبَارِئُ", "Al-Bari'", "Yang Maha Melepaskan (membuat manusia dari tiada)"),
        AsmaulHusnaItem(13, "الْمُصَوِّرُ", "Al-Mushawwir", "Yang Maha Membentuk Rupa"),
        AsmaulHusnaItem(14, "الْغَفَّارُ", "Al-Ghaffar", "Yang Maha Pengampun"),
        AsmaulHusnaItem(15, "الْقَهَّارُ", "Al-Qahhar", "Yang Maha Menundukkan"),
        AsmaulHusnaItem(16, "الْوَهَّابُ", "Al-Wahhab", "Yang Maha Pemberi Karunia"),
        AsmaulHusnaItem(17, "الرَّزَّاقُ", "Ar-Razzaq", "Yang Maha Pemberi Rezeki"),
        AsmaulHusnaItem(18, "الْفَتَّاحُ", "Al-Fattah", "Yang Maha Pembuka Rahmat"),
        AsmaulHusnaItem(19, "اَلْعَلِيمُ", "Al-'Alim", "Yang Maha Mengetahui"),
        AsmaulHusnaItem(20, "الْقَابِضُ", "Al-Qabidh", "Yang Maha Menyempitkan"),
        AsmaulHusnaItem(21, "الْبَاسِطُ", "Al-Basith", "Yang Maha Melapangkan"),
        AsmaulHusnaItem(22, "الْخَافِضُ", "Al-Khafidh", "Yang Maha Merendahkan"),
        AsmaulHusnaItem(23, "الرَّافِعُ", "Ar-Rafi'", "Yang Maha Meninggikan"),
        AsmaulHusnaItem(24, "الْمُعِزُّ", "Al-Mu'izz", "Yang Maha Memuliakan"),
        AsmaulHusnaItem(25, "الْمُذِلُّ", "Al-Mudzill", "Yang Maha Menghinakan"),
        AsmaulHusnaItem(26, "السَّمِيعُ", "As-Sami'", "Yang Maha Mendengar"),
        AsmaulHusnaItem(27, "الْبَصِيرُ", "Al-Bashir", "Yang Maha Melihat"),
        AsmaulHusnaItem(28, "الْحَكَمُ", "Al-Hakam", "Yang Maha Menetapkan Hukum"),
        AsmaulHusnaItem(29, "الْعَدْلُ", "Al-'Adl", "Yang Maha Adil"),
        AsmaulHusnaItem(30, "اللَّطِيفُ", "Al-Lathif", "Yang Maha Lembut"),
        AsmaulHusnaItem(31, "الْخَبِيرُ", "Al-Khabir", "Yang Maha Mengetahui Rahasia"),
        AsmaulHusnaItem(32, "الْحَلِيمُ", "Al-Halim", "Yang Maha Penyabar"),
        AsmaulHusnaItem(33, "الْعَظِيمُ", "Al-'Azhim", "Yang Maha Agung"),
        AsmaulHusnaItem(34, "الْغَفُورُ", "Al-Ghafur", "Yang Maha Memberi Pengampunan"),
        AsmaulHusnaItem(35, "الشَّكُورُ", "Asy-Syakur", "Yang Maha Mensyukuri"),
        AsmaulHusnaItem(36, "الْعَلِيُّ", "Al-'Aliyy", "Yang Maha Tinggi"),
        AsmaulHusnaItem(37, "الْكَبِيرُ", "Al-Kabir", "Yang Maha Besar"),
        AsmaulHusnaItem(38, "الْحَفِيظُ", "Al-Hafizh", "Yang Maha Memelihara"),
        AsmaulHusnaItem(39, "الْمُقِيتُ", "Al-Muqit", "Yang Maha Pemberi Kecukupan"),
        AsmaulHusnaItem(40, "الْحَسِيبُ", "Al-Hasib", "Yang Maha Membuat Perhitungan"),
        AsmaulHusnaItem(41, "الْجَلِيلُ", "Al-Jalil", "Yang Maha Mulia"),
        AsmaulHusnaItem(42, "الْكَرِيمُ", "Al-Karim", "Yang Maha Pemurah"),
        AsmaulHusnaItem(43, "الرَّقِيبُ", "Ar-Raqib", "Yang Maha Mengendalikan"),
        AsmaulHusnaItem(44, "الْمُجِيبُ", "Al-Mujib", "Yang Maha Mengabulkan"),
        AsmaulHusnaItem(45, "الْوَاسِعُ", "Al-Wasi'", "Yang Maha Luas"),
        AsmaulHusnaItem(46, "الْحَكِيمُ", "Al-Hakim", "Yang Maha Bijaksana"),
        AsmaulHusnaItem(47, "الْوَدُودُ", "Al-Wadud", "Yang Maha Mencintai"),
        AsmaulHusnaItem(48, "الْمَجِيدُ", "Al-Majid", "Yang Maha Terpuji"),
        AsmaulHusnaItem(49, "الْبَاعِثُ", "Al-Ba'its", "Yang Maha Membangkitkan"),
        AsmaulHusnaItem(50, "الشَّهِيدُ", "Asy-Syahid", "Yang Maha Menyaksikan"),
        AsmaulHusnaItem(51, "الْحَقُّ", "Al-Haqq", "Yang Maha Benar"),
        AsmaulHusnaItem(52, "الْوَكِيلُ", "Al-Wakil", "Yang Maha Memelihara (perantara)"),
        AsmaulHusnaItem(53, "الْقَوِيُّ", "Al-Qawiyy", "Yang Maha Kuat"),
        AsmaulHusnaItem(54, "الْمَتِينُ", "Al-Matin", "Yang Maha Kokoh"),
        AsmaulHusnaItem(55, "الْوَلِيُّ", "Al-Waliyy", "Yang Maha Melindungi"),
        AsmaulHusnaItem(56, "الْحَمِيدُ", "Al-Hamid", "Yang Maha Terpuji"),
        AsmaulHusnaItem(57, "الْمُحْصِي", "Al-Mush'i", "Yang Maha Menghitung"),
        AsmaulHusnaItem(58, "الْمُبْدِئُ", "Al-Mubdi'", "Yang Maha Memulai"),
        AsmaulHusnaItem(59, "الْمُعِيدُ", "Al-Mu'id", "Yang Maha Mengembalikan"),
        AsmaulHusnaItem(60, "الْمُحْيِي", "Al-Muhyi", "Yang Maha Menghidupkan"),
        AsmaulHusnaItem(61, "الْمُمِيتُ", "Al-Mumit", "Yang Maha Mematikan"),
        AsmaulHusnaItem(62, "الْحَيُّ", "Al-Hayy", "Yang Maha Hidup"),
        AsmaulHusnaItem(63, "الْقَيُّومُ", "Al-Qayyum", "Yang Maha Berdiri Sendiri"),
        AsmaulHusnaItem(64, "الْوَاجِدُ", "Al-Wajid", "Yang Maha Menemukan"),
        AsmaulHusnaItem(65, "الْمَاجِدُ", "Al-Majid", "Yang Maha Mulia (kemuliaan milik-Nya)"),
        AsmaulHusnaItem(66, "الْوَاحِدُ", "Al-Wahid", "Yang Maha Tunggal"),
        AsmaulHusnaItem(67, "اَلاَحَدُ", "Al-Ahad", "Yang Maha Esa"),
        AsmaulHusnaItem(68, "الصَّمَدُ", "Ash-Shamad", "Yang Maha Dibutuhkan"),
        AsmaulHusnaItem(69, "الْقَادِرُ", "Al-Qadir", "Yang Maha Menentukan"),
        AsmaulHusnaItem(70, "الْمُقْتَدِرُ", "Al-Muqtadir", "Yang Maha Berkuasa"),
        AsmaulHusnaItem(71, "الْمُقَدِّمُ", "Al-Muqaddim", "Yang Maha Mendahulukan"),
        AsmaulHusnaItem(72, "الْمُؤَخِّرُ", "Al-Mu'akhkhir", "Yang Maha Mengakhirkan"),
        AsmaulHusnaItem(73, "الأوَّلُ", "Al-Awwal", "Yang Maha Awal"),
        AsmaulHusnaItem(74, "الآخِرُ", "Al-Akhir", "Yang Maha Akhir"),
        AsmaulHusnaItem(75, "الظَّاهِرُ", "Az-Zhahir", "Yang Maha Nyata"),
        AsmaulHusnaItem(76, "الْبَاطِنُ", "Al-Bathin", "Yang Maha Gaib"),
        AsmaulHusnaItem(77, "الْوَالِي", "Al-Wali", "Yang Maha Memerintah"),
        AsmaulHusnaItem(78, "الْمُتَعَالِي", "Al-Muta'ali", "Yang Maha Tinggi (di atas segala sesuatu)"),
        AsmaulHusnaItem(79, "الْبَرُّ", "Al-Barr", "Yang Maha Penderma (kebaikan)"),
        AsmaulHusnaItem(80, "التَّوَابُ", "At-Tawwab", "Yang Maha Penerima Taubat"),
        AsmaulHusnaItem(81, "الْمُنْتَقِمُ", "Al-Muntaqim", "Yang Maha Pembalas"),
        AsmaulHusnaItem(82, "العَفُوُّ", "Al-'Afuww", "Yang Maha Pemaaf"),
        AsmaulHusnaItem(83, "الرَّءُوفُ", "Ar-Ra'uf", "Yang Maha Pengasih (lembut)"),
        AsmaulHusnaItem(84, "مَالِكُ الْمُلْكِ", "Malikul-Mulk", "Yang Maha Pemilik Kerajaan"),
        AsmaulHusnaItem(85, "ذُوالْجَلَالِ وَالإكْرَامِ", "Dzul-Jalali wal-Ikram", "Yang Mempunyai Keagungan dan Kemuliaan"),
        AsmaulHusnaItem(86, "الْمُقْسِطُ", "Al-Muqsith", "Yang Maha Pemberi Keadilan"),
        AsmaulHusnaItem(87, "الْجَامِعُ", "Al-Jami'", "Yang Maha Mengumpulkan"),
        AsmaulHusnaItem(88, "الْغَنِيُّ", "Al-Ghaniyy", "Yang Maha Kaya"),
        AsmaulHusnaItem(89, "الْمُغْنِي", "Al-Mughni", "Yang Maha Memberi Kekayaan"),
        AsmaulHusnaItem(90, "الْمَانِعُ", "Al-Mani'", "Yang Maha Mencegah"),
        AsmaulHusnaItem(91, "الضَّارَّ", "Ad-Dharr", "Yang Maha Memberi Derita (bagi yang durhaka)"),
        AsmaulHusnaItem(92, "النَّافِعُ", "An-Nafi'", "Yang Maha Memberi Manfaat"),
        AsmaulHusnaItem(93, "النُّورُ", "An-Nur", "Yang Maha Bercahaya (Maha Terang)"),
        AsmaulHusnaItem(94, "الْهَادِي", "Al-Hadi", "Yang Maha Pemberi Petunjuk"),
        AsmaulHusnaItem(95, "الْبَدِيعُ", "Al-Badi'", "Yang Maha Pencipta (tanpa contoh)"),
        AsmaulHusnaItem(96, "اَلْبَاقِي", "Al-Baqi", "Yang Maha Kekal"),
        AsmaulHusnaItem(97, "الْوَارِثُ", "Al-Warith", "Yang Maha Pewaris"),
        AsmaulHusnaItem(98, "الرَّشِيدُ", "Ar-Rasyid", "Yang Maha membimbing (ke jalan benar)"),
        AsmaulHusnaItem(99, "الصَّبُورُ", "Ash-Shabur", "Yang Maha Sabar")
    )

    fun getAll(): List<AsmaulHusnaItem> = names

    fun search(query: String): List<AsmaulHusnaItem> {
        if (query.isBlank()) return names
        val q = query.trim().lowercase()
        return names.filter {
            it.latin.lowercase().contains(q) || it.meaning.lowercase().contains(q)
        }
    }
}

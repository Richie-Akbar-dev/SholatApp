# Changelog — SholatApp

## v2.1.0 (2026-09-06)

Update menyeluruh: fitur baru, perbaikan bug, penyegaran UI, dan upgrade teknologi.

### 🐛 Perbaikan Bug

1. **4 layar tidak bisa diakses (bug kritis)** — Mushaf Al-Qur'an, Doa Harian, Mutabaah, dan Tilawah sudah ada sejak v2.0 tetapi tidak terhubung ke mana pun (dead code). Kini dapat diakses lewat tombol **"Lainnya"** di Beranda.
2. **Error kompilasi di PuasaScreen** — `Modifier.height(20)` kehilangan `.dp` sehingga project v2.0 tidak dapat di-build.
3. **Highlight hari Jumat salah kolom** — sebelumnya menyorot hari Sabtu (`col == 5`), kini benar di Jumat (`col == 4`).
4. **Toggle puasa tidak memperbarui UI** — menandai/batal puasa kini langsung memperbarui kartu status dan kalender tanpa perlu pindah layar.
5. **Tombol notifikasi di Beranda kosong (TODO)** — digantikan Pusat Notifikasi yang berfungsi penuh.
6. **Bottom navigation selalu putih** — kini mengikuti tema gelap/terang.

### ✨ Fitur Baru

1. **Menu Lainnya** — hub akses untuk Mushaf, Doa Harian, Mutabaah, Tilawah, dan Asmaul Husna dari Beranda.
2. **Pusat Notifikasi** — status alarm sholat, alarm 20 menit, mode fokus (DND), dan daftar jadwal notifikasi hari ini.
3. **Asmaul Husna** — 99 Nama Allah (teks Arab, Latin, arti) lengkap dengan pencarian.
4. **Waktu Imsak** — tampil di kartu Subuh (Beranda) dan di daftar jadwal (Salat), standar KEMENAG RI (10 menit sebelum Subuh).
5. **Terakhir Dibaca (Mushaf)** — posisi bacaan terakhir otomatis tersimpan, dengan tombol "Lanjutkan".
6. **Penanda Puasa Sunnah** — kalender puasa menyorot Senin, Kamis, dan Ayyamul Bidh (13–15 Hijriah via kalender Islam perangkat) lengkap dengan keterangan.

### 🎨 UI/Design

1. **Mode Gelap / Terang** — toggle di Pengaturan › Tampilan; seluruh layar (termasuk bottom nav & status bar) mengikuti tema, preferensi tersimpan otomatis.

### ⚙️ Upgrade Teknologi

| Komponen | Sebelum | Sesudah |
|----------|---------|---------|
| Gradle Wrapper | 8.7 | 8.9 |
| Android Gradle Plugin | 8.5.0 | 8.7.3 |
| Kotlin | 2.0.0 | 2.1.0 |
| Compose BOM | 2024.06.00 | 2025.01.00 |
| Material 3 | 1.2.1 (pin) | ikut BOM (1.3.x) |
| Core KTX | 1.13.1 | 1.15.0 |
| Lifecycle | 2.8.3 | 2.8.7 |
| Activity Compose | 1.9.0 | 1.9.3 |
| compileSdk / targetSdk | 34 | 35 |
| versionName / versionCode | 2.0.0 / 1 | 2.1.0 / 2 |

### 📁 Berkas Baru

- `ui/screens/MenuLainnyaScreen.kt`
- `ui/screens/PusatNotifikasiScreen.kt`
- `ui/screens/AsmaulHusnaScreen.kt`
- `data/AsmaulHusnaData.kt`
- `model/AsmaulHusnaItem.kt`

### 📝 Catatan Build

- Buka di Android Studio terbaru (Ladybug+) dan biarkan Gradle sync mengunduh Gradle 8.9 + SDK 35.
- Tidak ada perubahan skema data yang merusak — seluruh preferensi lama (lokasi, dzikir, puasa, tilawah) tetap terbaca.

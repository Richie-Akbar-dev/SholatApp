# Changelog — SholatApp

## v2.6.0 (2026-09-12)

Fokus: halaman baru **Mushaf — Bacaan Terarah** sesuai arahan user: hanya teks Arab mengalir menerus (gaya mushaf asli), setelan khatam di Pengaturan, tanpa tautan keluar. Satu database Al-Qur'an, dua output (pembaca lengkap + bacaan jadwalan sistem).

### ✨ Fitur Baru

1. **Halaman Mushaf** — menampilkan porsi bacaan hari ini yang ditentukan sistem (mesin khatam, ayat/hari dari target): kartu hero (rentang surah-ayat, jumlah ayat, estimasi menit baca, ring emas % khatam, chip streak "hari beruntun", tombol Mulai Membaca) + lembar bacaan + tombol **Tandai Bacaan Selesai** (berubah hijau "Selesai · Sampai Besok", streak & progres ikut naik).
2. **Lembar Arab menerus** — teks Arab mengalir tanpa kartu terpisah; nomor ayat ditandai ornamen emas ﴿n﴾ (angka Arab-Indic) di dalam aliran; arah RTL penuh.
3. **Aturan basmalah** — basmalah hanya muncul bila bacaan dimulai di ayat 1 sebuah surah; **tidak untuk At-Taubah** (surah tanpa basmalah — hukum tidak dibacakan sebelum membacanya) dan Al-Fatihah (basmalah = ayat 1-nya sendiri).
4. **Banner "Bacaan Hari Ini" di halaman Al-Qur'an** — pintu masuk **satu arah** (Al-Qur'an → Mushaf); halaman Mushaf sendiri tidak menautkan ke halaman surat lengkap, sesuai keputusan user.
5. **State khatam tercapai** — saat rencana selesai, halaman menampilkan kartu "Alhamdulillah, Khatam Tercapai" + arahan mulai ulang.

### 🎨 Pengaturan (Bacaan Terarah)

1. Section "Target Khatam Al-Qur'an" diperluas menjadi **"Bacaan Terarah (Khatam)"** + tombol **Mulai Ulang Rencana** (kembali ke awal, hapus riwayat).
2. **Fix integrasi**: menyimpan target kini juga menghitung ulang **ayat/hari** (sebelumnya hanya `target_days` — jadwal harian tidak pernah berubah). Target kini benar-benar menggerakkan porsi harian halaman Mushaf.

### 🔧 Lainnya

- Database ayat = aset bersama `assets/quran` (114 surah) via `QuranRepository` — satu sumber untuk pembaca Al-Qur'an dan Mushaf.
- Wiring baru: `AppScreen.Mushaf` overlay di MainActivity; `QuranScreen(onMushafClick=...)`.
- versionCode 7, versionName 2.6.0.

## v2.5.0 (2026-09-12)

Fokus: rombak halaman **Zikir** menurut mockup & 5 keputusan user — chip kategori diperbaiki, counter lebih manusiawi (undo tahan 3 detik), pop up fokus menggantikan hero tasbih, konten dzikir diperluas dengan item autentik.

### 🐞 Perbaikan

1. **Chip kategori overflow (bug fungsional)** — 4 chip dalam `Row` statis membuat chip "Dzikir Umum" terpotong/tak terjangkau di hampir semua ukuran layar. Kini: label pendek (Pagi · Petang · Setelah Sholat · Umum) dalam baris scroll-safe.
2. **Salah hitung tak bisa dikoreksi** — kini tahan lingkaran ±3 detik untuk mengurangi 1 (getar lebih panjang sebagai penanda); sebelumnya salah tap tercatat sampai besok.
3. **Dua angka progres membingungkan** — bar progres 3dp tanpa label dihapus; diganti header section berlabel "Dzikir Pagi · 2/7 selesai" + bar emas.

### ✨ Fitur Baru / Perubahan

1. **Pop up fokus** — ketuk kartu dzikir → lembar bawah berisi judul, teks Arab/Latin/Arti **penuh** (bisa digulir), dan counter besar untuk menghitung nyaman (ketuk +1, tahan 3 detik −1).
2. **Kategori otomatis sesuai jam** — 04–10 buka Dzikir Pagi, 11–15 Setelah Sholat, selain itu Dzikir Petang; sebelumnya selalu terbuka di Pagi.
3. **Chip judul per dzikir** — tiap kartu kini bernama ("Tasbih", "Ayat Kursi", "Sayyidul Istighfar", dst.); field `title` baru di `DzikirItem`.
4. **Kartu selesai** — lingkaran berubah centang + label "Selesai", kartu ikut berubah hijau lembut (gaya mockup).
5. **Konten diperluas 22 → 29 item** — tambahan autentik Hisnul Muslim: *Sayyidul Istighfar* (pagi & petang), *Asbahna/Amsayna wa Asbahal-Mulk*, *Radhitu Billah* (pagi & petang), *Subhanallahi 'Adada Khalqih* (umum).
6. **Latin ditampilkan miring berkutip, arti dipotong 3 baris** — teks penuh tersedia di pop up fokus; daftar lebih ringkas.

### 🗑️ Dihapus

- Hero "Tasbih" terpisah (keputusan user: pop up fokus pada tiap kartu menggantikannya).
- Ikon aksi di header halaman.

### 🔧 Lainnya

- Palet warna tidak berubah; nav bawah tidak disentuh.
- `DzikirCategory` dapat `shortLabel`; `displayName` tetap dipertahankan.
- versionCode 6, versionName 2.5.0.

## v2.4.0 (2026-09-11)

Fokus: rombak halaman **Salat** (v2.4) menurut mockup & arahan user — jam analog 5 jarum masuk kartu hero, dua kolom dihapus, detail waktu naik, latar foto lokal offline.

### ✨ Fitur Baru / Perubahan

1. **Jam analog 5 jarum pindah ke kartu hero** — menggantikan posisi ring: 3 jarum waktu nyata (jam & menit putih, detik emas) + 2 jarum alarm hijau yang menunjuk waktu sholat berikutnya, lengkap titik hijau di tepi jam. Desain wajah jam dirapikan (angka minimalis 12/3/6/9), fitur 5 jarum tetap 100%.
2. **Legenda jam** — keterangan kecil "Panah hijau = sholat berikutnya" di bawah jam.
3. **Hitung mundur 3 kotak di halaman Salat** — gaya sama dengan Beranda (JAM : MENIT : DETIK) + keterangan "menuju adzan"; saat memasuki 30 menit pertama setelah adzan berubah menjadi status **"WAKTU SHOLAT — Laksanakan segera · ±X menit lagi"**.
4. **Tag "Besok"** — pil emas pada kartu hero & baris detail saat menghitung mundur menuju Subuh esok hari.
5. **Undo centang** — baris sholat yang sudah dicentang kini bisa diketuk lagi untuk membatalkan (fungsi `uncheckPrayer` baru); sebelumnya salah tekan tercatat sampai besok.
6. **Progres harian "x/5 selesai"** — tampil di baris judul "Detail Waktu Sholat" (pengganti kolom checklist yang dihapus).
7. **Footer "Lihat Kalender Bulanan ›"** — jembatan langsung dari jadwal harian ke halaman Kalender.
8. **Error state di atas + "Coba Lagi"** — saat lokasi gagal dideteksi, halaman tidak lagi kosong; menampilkan kartu error dengan tombol coba ulang (`detectLocation()`).
9. **Latar foto lokal (offline permanen)** — 4 foto siluet masjid dibundel di `drawable-nodpi` (pagi/siang/senja/malam, ±334 KB total) menggantikan hotlink Pinterest yang bisa mati; peralihan gambar memakai crossfade 300 ms.
10. **Latar ikut berganti waktu** — periode Pagi/Siang/Senja/Malam kini dihitung ulang setiap menit, bukan sekali saat halaman dibuka.

### 🎨 UI/Design (identitas hijau–emas TIDAK berubah)

1. **Header diringkas** — hanya judul "Jadwal Salat"; lokasi & tanggal Masehi-Hijriah cukup ditampilkan sekali di Beranda (tidak dobel lagi).
2. **Dua kolom (checklist + jam) dihapus** — pelacakan cukup lewat kartu Detail; layar lebih ringkas dan tidak dobel info.
3. **Detail Waktu Sholat naik** tepat di bawah kartu hero; Imsak & Terbit tetap info-only (tanpa ceklis).
4. **Pil "Berikutnya"** pada baris sholat yang akan datang di kartu Detail.
5. **Semua warna inline dipindahkan ke `SalatPeriodColors`** — nilai warna tidak ada yang diubah.

### 🗑️ Dihapus

- Kolom "Target Hari Ini" & kartu jam analog terpisah (digabung ke hero).
- Baris lokasi di header halaman Salat.
- `SalatBackgrounds` (URL Pinterest) & `SalatCheckItem` (checklist kolom kiri).

### 🔧 Lainnya

- Wiring baru: `SalatScreen(onKalenderClick=...)` → `AppScreen.Kalender` di MainActivity.
- versionCode 5, versionName 2.4.0.

## v2.3.0 (2026-09-11)

Fokus: perombakan Beranda (dashboard) sesuai mockup yang disetujui + Al-Qur'an lengkap 114 surah yang disatukan ke halaman Zikir.

### ✨ Fitur Baru

1. **Al-Qur'an lengkap 114 surah / 6236 ayat, 100% offline** — teks Arab, transliterasi Latin, dan arti (Kemenag RI) dibundel permanen di dalam aplikasi (`assets/quran/`, ±3,3 MB, sumber equran.id); tidak butuh internet sama sekali.
2. **Pembaca Al-Qur'an 3 unsur** — setiap ayat menampilkan **(A) tulisan Arab** (besar, rata kanan), **(B) Arab-Latin** (membantu yang belum lancar), dan **(C) arti** dalam Bahasa Indonesia.
3. **Entri Al-Qur'an di halaman Zikir** — kartu "Al-Qur'an · 114 Surah lengkap" di atas daftar dzikir (mushaf kini disatukan di satu tempat, sesuai permintaan).
4. **Pencarian surah** — cari berdasarkan nama (Al-Fatihah, "Kausar", arti) maupun nomor surah.
5. **Jam digital di Beranda** — jam hidup ditampilkan di tengah cincin progres kartu sholat, lengkap label zona waktu otomatis (WIB/WITA/WIT sesuai perangkat).
6. **Tanggal Hijriah di Beranda** — header kini menampilkan "Senin, 15 September 2026 · 3 Rabiul Awal 1447 H" (kalkulator offline sama dengan Kalender).
7. **Salam dinamis** — "Ahlan wa sahlan, Selamat pagi/siang/sore/malam" menyesuaikan jam perangkat.
8. **Status "Waktu Sholat sedang berlangsung"** — dalam 30 menit pertama setelah adzan, kartu utama berubah menjadi "WAKTU SHOLAT — Laksanakan segera · ±X menit lagi".
9. **Tag "Besok"** — setelah Isya, kartu sholat berikutnya menandai countdown menuju Subuh esok hari agar tidak salah paham.
10. **Tombol "Coba Lagi"** — saat deteksi lokasi gagal, kartu error kini tampil **di atas** (menggantikan posisi kartu utama) dengan tombol coba ulang; sebelumnya kartu error tersembunyi di dasar layar tanpa aksi.

### 🎨 UI/Design — Beranda (penataan ulang, warna TIDAK berubah)

1. **Cincin progres emas** — busur melingkar terisi dari sholat terakhir menuju sholat berikutnya; jam digital di tengahnya.
2. **Penempatan baru**: Mahfudzot pindah ke dasar Beranda sebagai penutup renungan; tinggi kartu kini mengikuti panjang teks (tidak ada pemotongan) dan tanpa sumber/periwayat sesuai permintaan.
3. **Pil "Berikutnya"** — kartu sholat yang akan datang diberi penanda pil emas di samping namanya.
4. **Tautan "Lihat Detail ›"** — grid jadwal kini memiliki affordance yang jelas menuju halaman Salat.
5. **Tindakan cepat 3 kartu**: **Kiblat · Al-Qur'an · Kalender** (Zikir & Puasa keluar karena keduanya sudah punya tab sendiri di navigasi bawah).
6. **Menu Lainnya dirapikan** — item Mushaf keluar (pindah total ke halaman Zikir); sisanya: Doa Harian, Mutabaah, Tilawah, Asmaul Husna.

### 🗑️ Dihapus

1. **MushafScreen lama dari navigasi** — digantikan QuranScreen baru (24-surah → 114-surah penuh); posisi "Terakhir Dibaca" lama tetap terbaca (preferensi sama).

### 📁 Berkas Baru

- `ui/screens/QuranScreen.kt` (browser + pembaca)
- `data/QuranRepository.kt` (pemuat aset JSON, tanpa dependensi baru)
- `assets/quran/index.json` + `assets/quran/surat/001–114.json` (114 surah, 6236 ayat)

### 📝 Catatan Build

- Ukuran APK bertambah ±1–2 MB (aset teks terkompresi) — tidak ada dependensi baru.
- Seluruh preferensi lama tetap kompatibel; tidak ada migrasi data.

## v2.2.0 (2026-09-11)

Fokus: konsistensi tema & perombakan navigasi (tombol back + transisi animasi).

### 🐛 Perbaikan Bug

1. **Tombol back sistem tidak berfungsi (bug kritis)** — sebelumnya menekan tombol/gesture back saat Kiblat, Kalender, Menu Lainnya, Pusat Notifikasi, pembaca Surah, atau pemilih Azan terbuka akan **langsung keluar dari aplikasi**. Kini back menutup halaman dulu (BackHandler di semua level navigasi: overlay › Menu Lainnya › pembaca Surah).
2. **11 entri MahfudzotData kehilangan tanda kurung penutup** (`),`) — bug bawaan v2.0 yang mencegah kompilasi; salah satu entri juga kehilangan baris terjemahan ("من حفر حفرة لأخيه وقع فيها") dan telah dilengkapi.
3. **1 kurung kurawal berlebih di akhir TilawahScreen** — error kompilasi bawaan v2.0.
4. **Kontras teks rendah pada header hijau tua** — 11 titik teks sekunder/input di header hijau tua (Settings, Mushaf, Dzikir, Mutabaah, Statistik, Asmaul Husna, Menu Lainnya, Pusat Notifikasi, kolom cari Doa) kini memakai warna terang khusus (`HeaderSubtitle`/`HeaderPlaceholder`).

### 🗑️ Dihapus

1. **Mode Gelap** — fitur dihapus seluruhnya sesuai kebutuhan; aplikasi kini memakai **tema terang konsisten** di semua layar. Toggle "Mode Gelap" keluar dari Pengaturan, palet warna disederhanakan menjadi satu palet statis (`DarkColors` → palet terang, nama dipertahankan demi kompatibilitas).

### 🧭 Navigasi

1. **State navigasi terpusat** — 6 boolean overlay terpisah di MainActivity diganti satu sealed class `AppScreen` (Kiblat, Kalender, MenuLainnya, PusatNotifikasi, AzanPicker); lebih aman, mudah dikembangkan, dan bebas bug saling timpa.
2. **Transisi animasi antar tab** — fade + slide vertikal halus (220 ms) saat berpindah tab bottom nav.
3. **Transisi overlay** — halaman overlay slide masuk dari kanan (280 ms) dan slide keluar saat ditutup.
4. **Navigasi internal beranimasi** — Menu Lainnya (menu › sub-halaman) dan Mushaf (daftar surah › pembaca) kini memakai transisi slide + fade yang sama; sistem back terintegrasi di setiap level.
5. **Label & ikon bottom nav diperjelas** — tab "Profil" → "**Pengaturan**" (ikon Settings), tab "Tasbih" → "**Zikir**".

### 🎨 UI/Design

1. **Satu sumber warna** — Beranda, Puasa, Tilawah, Kiblat, dan Kalender yang sebelumnya memakai palet hardcode kini konsisten dengan palet global (latar terang, aksen hijau tua + emas).
2. **Status bar & navigation bar terang** — ikon sistem gelap di atas latar terang, konsisten di seluruh aplikasi.

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

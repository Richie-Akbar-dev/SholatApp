# Changelog — SholatApp

## v2.9.0 (2026-09-12)

Fokus: audit lintas aplikasi menemukan 9 halaman sekunder yang belum tersentuh update. Rilis ini memperbaiki **bug kritis kalkulator Hijriah (meleset ±26 tahun)** yang mengena ke Beranda, Kalender, dan Puasa, lalu merombak halaman **Kiblat** menurut mockup & keputusan user (badge dial diadopsi, kontainer ikon pola aplikasi).

### 🐞 Perbaikan

1. **KRITIS — Kalkulator Hijriah meleset ±26 tahun** — rumus `jdToHijri` lama (konstanta 531/283/19.45/285 + offset epoch salah) menghasilkan tanggal absurd, mis. 1 Mar 2025 terbaca "7 Muharram 1472 H" padahal 1 Ramadhan 1446 H. Diganti algoritma Kuwaiti (tabular) standar yang diverifikasi terhadap tanggal KEMENAG RI (1 Ramadhan 1446, 1 Syawal 1446, 27 Rajab 1446 tepat; 10 Dzulhijjah 1446 selisih 1 hari — wajar antara tabular dan rukyat). Berdampak ke: tanggal Hijriah di header Beranda, seluruh grid Kalender Islam, chip Hijriah halaman Puasa, dan **banner Ramadhan otomatis yang sebelumnya tidak akan pernah muncul**.
2. **Hari Besar Islam salah bulan** — "Hari Arafah (9 Dzulhijjah)" dan "Idul Adha" terdaftar di bulan 10 (Syawal), bukan 12 (Dzulhijjah); entri Idul Adha ganda dihapus. Kini tampil di bulan yang benar.
3. **Aturan tahun kabur Hijriah salah** — aturan modulo lama bukan algoritma Kuwaiti; diganti aturan tabular standar `(11y + 14) mod 30 < 11`.
4. **KRITIS — Kompas Kiblat meleset 90°** — skala derajat & label mata angin digambar tanpa koreksi −90° sementara jarum kiblat memakainya, sehingga dial dan jarum tidak pernah cocok (huruf "U" tergambar di kanan saat menghadap utara). Kini semua elemen dial memakai satu rumus sudut yang sama.
5. **Kiblat dihitung dari koordinat (0,0)** — jika lokasi belum terdeteksi, arah & jarak tampil dengan angka omong kosong. Kini ada kartu "Menunggu Lokasi" + tombol Deteksi Lokasi.
6. **Format angka Kiblat** — derajat & jarak kini memakai format Indonesia (koma desimal, titik ribuan) sesuai mockup.

### ✨ Fitur Baru / Perubahan — Halaman Kiblat (v2.9)

1. **Header baru** — hijau tua `143A2E` sudut membulat + judul putih "Kiblat" + subtitle "Penunjuk arah Ka'bah"; konten latar terang `F5F5F0` (ganti tema gelap lama).
2. **Kompas dalam kartu putih** — cincin emas, tick 360°, label mata angin Indonesia (U/TL/T/TG/S/BD/B/BL), marker segitiga hijau di puncak sebagai target sejajar, label Arab "كعبة" pada ujung jarum emas.
3. **Badge sudut kiblat** — pill "KIBLAT 294,5°" di bawah dial (adopsi tambahan mockup Stitch).
4. **Banner status dinamis** — "Putar perangkat hingga jarum sejajar tanda hijau" → berubah hijau "**Arah Kiblat Terkunci**" saat selisih ≤ 4°, disertai getaran singkat.
5. **Mode terkunci dengan getar** — saat arah cocok, cincin dial berubah hijau; getar satu kali setiap kali masuk kondisi terkunci.
6. **Chip kalibrasi** — saat akurasi magnetometer rendah, muncul chip amber "gerakkan perangkat membentuk angka 8".
7. **Sensor lebih stabil** — sumber azimuth beralih ke `TYPE_ROTATION_VECTOR` (fallback accel+mag tetap ada) + `remapCoordinateSystem` agar benar di orientasi layar apa pun.
8. **Deteksi sensor tidak tersedia** — perangkat tanpa magnetometer kini menampilkan pesan jujur, bukan kompas mati dengan angka 0°.
9. **Kartu informasi gaya baru** — "Arah Kiblat" & "Jarak ke Ka'bah" dengan kontainer ikon hijau muda `E8F0EA` (pola aplikasi), plus chip lokasi di bawah header dan kartu instruksi kalibrasi.

### 🔧 Teknis

1. `KiblatScreen.kt` ditulis ulang penuh (~700 baris) dengan palet terang sendiri (KiblatColors) mengikuti bahasa visual v2.4–v2.8.
2. `HijriCalculator` diuji port-Python terhadap 8 tanggal referensi sebelum dipindahkan ke Kotlin; pembagian memakai `floorDiv` agar identik dengan semantik pembulatan JS/Python.
3. Wiring baru `onDetectLocation` dari `MainActivity` ke halaman Kiblat.
4. Bersih-bersih kecil: variabel mati `qiblaRelative` dihapus.

## v2.8.0 (2026-09-12)

Fokus: rombak halaman **Pengaturan** menurut mockup & keputusan user — kartu profil + ubah nama, kartu Status Perizinan Android, perbarui lokasi, grid metode KEMENAG, khatam dengan progress bar, konfirmasi reset, riwayat versi, dan footer. Halaman terakhir dengan pola UI lama kini mengikuti bahasa visual v2.4–v2.7.

### 🐞 Perbaikan

1. **Suara Azan tidak refresh** — label "Aktif: …" tetap menampilkan azan lama setelah ganti azan (nilai dibaca sekali, halaman tidak keluar komposisi saat pemilih azan terbuka). Kini di-refresh via counter `azanVersion` yang naik setiap kembali dari pemilih azan.
2. **Progress khatam tidak update** — setelah "Mulai Ulang Rencana" / "Simpan Target", angka progress tetap lama. Kini dibaca ulang lewat key `khatamRefresh` setiap aksi.
3. **Aksi destruktif tanpa konfirmasi** — "Mulai Ulang Rencana" dan "Reset Progress Dzikir" kini memunculkan dialog konfirmasi sebelum eksekusi (keduanya diberi warna merah/tombol Reset merah sesuai keputusan user).
4. **Simpan target tanpa feedback** — kini memunculkan Toast "Target khatam disimpan".
5. **Format koordinat** — kini memakai `Locale.US` (titik desimal konsisten di semua perangkat).
6. **Izin kritis tak terlihat** — izin notifikasi & alarm tepat sebelumnya hanya diminta sekali saat aplikasi dibuka; jika ditolak, semua pengingat mati diam-diam. Kini ada kartu **Status Perizinan Android**.

### ✨ Fitur Baru / Perubahan

1. **Header baru** — hijau tua sudut membulat + judul putih + subtitle, menggantikan header pola lama.
2. **Kartu Profil** — avatar inisial hijau, nama user, sub-teks lokasi · metode KEMENAG RI, ikon pensil emas. Ketuk → dialog **Ubah Nama**; nama langsung ikut berubah di salam Beranda (state hidup di MainActivity).
3. **Kartu Status Perizinan Android** — tiga chip status: izin notifikasi, alarm tepat, izin DND. Chip hijau = aman; chip merah bisa diketuk untuk langsung membuka pengaturan sistem terkait. Status otomatis segar saat kembali ke aplikasi (observer ON_RESUME).
4. **Perbarui Lokasi** — tombol kecil pada baris lokasi memicu deteksi ulang GPS (`detectLocation()`), alamat langsung memperbarui.
5. **Ikon leading semua baris** — kotak hijau muda 40dp gaya Setelan Google (lonceng, alarm, DND, pin, bola dunia, kalkulator, perisai, dll).
6. **Metode Perhitungan grid 2×2** — OTORITAS KEMENAG RI · Sudut Fajr 20° · Sudut Isya 18° · Mazhab Ashar Syafi'i.
7. **Kartu Target Khatam baru** — badge emas "x% Selesai", progress bar emas, "n dari 6.236 ayat", preset 30 hari/6 bulan/1 tahun (chip aktif + tanda centang), kolom custom, tombol gelap "Simpan Target" berikon.
8. **Tautan Pusat Notifikasi** — baris baru di section Alarm (sebelumnya hanya terjangkau dari lonceng Beranda).
9. **Section Tentang Aplikasi** — Versi (chip v2.8.0), **100% Offline** (data tidak keluar perangkat), Sumber Waktu KEMENAG RI, dan **Riwayat Versi** (pop-up changelog v2.1–v2.8).
10. **Footer** — "SholatApp v2.8.0 · Dibuat dengan cinta untuk umat".

### 🔧 Teknis

- `SettingsScreen` ditulis ulang penuh (~1190 baris) — komponen reusable: `LeadingIconBox`, `PermissionStatusCard/Chip`, `MethodCard/Cell`, `KhatamTargetCard`, varian baris info/aksi/bahaya.
- `MainActivity`: `userName` menjadi state hidup (+`onNameChange`), counter `azanVersion`, wiring `onPusatNotifikasiClick` & `onRefreshLocation`.
- Helper baru: `exactAlarmAllowed()`, `openAppNotificationSettings()`, `openExactAlarmSettings()`, `rememberResumeTick()` (refresh status izin saat kembali ke aplikasi).
- versionCode 9, versionName 2.8.0.

## v2.7.0 (2026-09-12)

Fokus: rombak halaman **Puasa** menurut mockup & keputusan user — tema terang konsisten, countdown hidup, kalender menjadi mesin pencatatan (fungsi "Catat" digeser), jadwal sunnah konkret + pengingat notifikasi, statistik bermakna, Niat & Doa pop up.

### 🐞 Perbaikan

1. **Countdown beku** — angka imsak/berbuka dihitung sekali saat halaman dibuka. Kini dipicu ticker 1 detik ViewModel (`currentTotalSeconds`) sehingga berdetak live seperti halaman Salat.
2. **Jam Imsak salah** — label "Imsak (Subuh)" menampilkan jam Subuh. Kini memakai getter `imsak` (−10 menit, standar KEMENAG) yang sama dengan Beranda & Salat.
3. **Tanpa error state** — seluruh halaman terkurung `if (schedule != null)`. Kini hanya area hero yang bergantung jadwal; kalender, pencatatan, statistik, dan doa tetap berfungsi meski lokasi gagal; hero menampilkan kartu error + **Coba Lagi**.
4. **Tema menyimpang** — satu-satunya halaman gelap padahal aplikasi terang-murni sejak v2.2. Kini tema terang konsisten (hero hijau tua + konten putih, palet standar).
5. **Header** — alamat lokasi dihapus (konsisten pola v2.4); statistik "Persentase" lemah makna diganti kolom bermakna.

### ✨ Fitur Baru / Perubahan

1. **Hero satuan cerdas** — label MENUJU IMSAK / MENUJU BERBUKA / MENUJU IMSAK BESOK sesuai waktu, jam target, countdown 3 kotak live, chip tanggal Hijriah, bar "waktu puasa terlewati x%", dan info **Imsak** (menggantikan Subuh — pilihan user).
2. **Kalender tap-catat** — fungsi tombol "Catat" digeser: ketuk tanggal (lampau/hari ini/mendatang) = catat puasa pada tanggal itu; ketuk lagi = batalkan. Kartu status hari ini tetap bisa diketuk (sinkron dengan kalender) + chip jenis otomatis ("Wajib · Ramadhan" / "Sunnah · Senin" / "Sunnah · Kamis" / "Sunnah · Ayyamul Bidh").
3. **Jadwal Puasa Sunnah Bulan Ini** — daftar tanggal konkret (chip "Sen 7", "Kam 10", "Sel 13–15 · Ayyamul Bidh") menggantikan kartu info statis + label **"x Hari Lagi"** menuju hari sunnah terdekat.
4. **Pengingat Puasa Sunnah (notifikasi)** — notifikasi pukul 20:00 pada malam sebelum hari sunnah ("Besok Puasa Sunnah Senin…"). Rantai alarm mandiri + disetel ulang saat app dibuka & setelah reboot. Toggle di **Pusat Notifikasi**, kanal notifikasi sendiri.
5. **Statistik** — Bulan Ini / Total / Tahun Ini (tanpa target tahunan — dihapus sesuai keputusan user).
6. **Niat & Doa pop up** — chip "Niat Puasa" & "Doa Berbuka" memunculkan lembar bawah teks penuh (Arab + latin + arti); data satu sumber dengan Doa Harian (item baru "Doa Niat Puasa" ikut tampil di layar Doa).
7. **Banner Ramadhan otomatis** — saat bulan Hijriah 9: "Ramadhan hari ke-n dari 30" + progres + sisa hari menuju 1 Syawal (memanfaatkan HijriCalculator yang sudah ada).

### 🔧 Teknis

- Komponen baru: `SunnahDayCalculator` (logika bersama UI + notifikasi), `SunnahReminderReceiver`, kanal notifikasi `sunnah_reminder_channel`.
- UiState + `currentTotalSeconds` (ticker) & `isSunnahReminderEnabled`; `PusatNotifikasiScreen(onToggleSunnahReminder=…)`.
- versionCode 8, versionName 2.7.0.

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

package com.sholatapp.data

import java.util.Calendar

data class Mahfudzot(val arabic: String, val meaning: String)

object MahfudzotData {
    val all: List<Mahfudzot> = listOf(
        // ===== ILMU (Knowledge) =====
        Mahfudzot(
            arabic = "العلم نور والجهل ظلام",
            meaning = "Ilmu adalah cahaya dan kebodohan adalah kegelapan."
        ),
        Mahfudzot(
            arabic = "من طلب العلا سهر الليالي",
            meaning = "Barangsiapa mencari kemuliaan, ia akan begadang semalam-malaman."
        ),
        Mahfudzot(
            arabic = "اطلبوا العلم من المهد إلى اللحد",
            meaning = "Carilah ilmu sejak buaian hingga liang lahat."
        ),
        Mahfudzot(
            arabic = "العلم في الصغر كالنقش في الحجر",
            meaning = "Ilmu yang diperoleh saat kecil bagaikan ukiran di atas batu."
        ),
        Mahfudzot(
            arabic = "خير جليس في الزمان كتاب",
            meaning = "Sebaik-baik teman di setiap waktu adalah buku."
        ),
        Mahfudzot(
            arabic = "العلم يرفع بيتاً لا عماد له",
            meaning = "Ilmu meninggikan rumah yang tak memiliki tiang penyangga."
        ),
        Mahfudzot(
            arabic = "الجهل داء والعلم شفاء",
            meaning = "Kebodohan adalah penyakit dan ilmu adalah obatnya."
        ),
        Mahfudzot(
            arabic = "من لا يتألم لا يتعلم",
            meaning = "Barangsiapa tidak mau menderita, ia tidak akan belajar."
        ),
        Mahfudzot(
            arabic = "رأس الحكمة مخافة الله",
            meaning = "Puncak kebijaksanaan adalah ketakutan kepada Allah."
        ),
        Mahfudzot(
            arabic = "قيمة كل امرئ ما يحسنه",
            meaning = "Nilai setiap orang tergantung pada keahliannya."
        ),
        Mahfudzot(
            arabic = "من لم يزد شيئاً فهو نقصان",
            meaning = "Barangsiapa tidak bertambah sesuatu, maka ia berkurang."
        ),
        Mahfudzot(
            arabic = "العلم شجرة والعمل ثمارها",
            meaning = "Ilmu adalah pohon dan amal adalah buahnya."
        ),
        Mahfudzot(
            arabic = "لا تعظ الناس وقلبك فاسد",
            meaning = "Jangan menasihati orang lain padahal hatimu sendiri rusak."
        ),
        Mahfudzot(
            arabic = "تفقهوا قبل أن تُسَوَّدوا",
            meaning = "Pelajarilah ilmu agama sebelum kalian dijadikan pemimpin."
        ),
        Mahfudzot(
            arabic = "من عَلِمَ حَكَمَ ومن لم يعلم أَلْزَمَ",
            meaning = "Barangsiapa berilmu ia bijaksana, dan yang tidak berilmu ia akan terbebani."
        ),
        Mahfudzot(
            arabic = "العلم أفضل من المال لأن العلم يحرسك وأنت تحرس المال",
            meaning = "Ilmu lebih baik dari harta karena ilmu menjagamu sedangkan kamu menjaga harta."
        ),
        Mahfudzot(
            arabic = "مَنْ حَرَصَ على كثرة الأخذ قَلَّ بَرَكَةُ العِلْمِ عنده",
            meaning = "Barangsiapa terlalu berambisi menumpuk ilmu, berkah ilmu di hatinya akan berkurang."
        ),
        Mahfudzot(
            arabic = "العلم بالتعلم والحلم بالتحلم",
            meaning = "Ilmu didapat dengan belajar dan kesabarana didapat dengan berlatih bersabar."
        ),
        Mahfudzot(
            arabic = "ازهد في الدنيا يحبك الله وازهد فيما عند الناس يحبك الناس",
            meaning = "Zuhudlah terhadap dunia niscaya Allah mencintaimu, zuhudlah terhadap milik orang lain niscaya manusia mencintaimu."
        ),
        Mahfudzot(
            arabic = "إذا لم تزد شيئاً على الدنيا كنت أنت زائداً عليها",
            meaning = "Jika kamu tidak menambah sesuatu pada dunia, maka kamulah yang menjadi beban baginya."
        ),

        // ===== USAHA & KESUNGGUHAN (Effort & Diligence) =====
        Mahfudzot(
            arabic = "من جدّ وجد",
            meaning = "Barangsiapa bersungguh-sungguh pasti berhasil."
        ),
        Mahfudzot(
            arabic = "من سار على الدرب وصل",
            meaning = "Barangsiapa berjalan di jalan yang benar, pasti akan sampai."
        ),
        Mahfudzot(
            arabic = "إن مع العسر يسراً",
            meaning = "Sesungguhnya bersama kesulitan ada kemudahan."
        ),
        Mahfudzot(
            arabic = "لا تحقرن من المعروف شيئاً",
            meaning = "Janganlah kamu meremehkan sedikit pun kebaikan."
        ),
        Mahfudzot(
            arabic = "اجتهد فمن جدّ وجد",
            meaning = "Bersungguh-sungguhlah, karena barangsiapa sungguh-sungguh pasti berhasil."
        ),
        Mahfudzot(
            arabic = "بُنيَ الإسلام على خمس",
            meaning = "Islam dibangun di atas lima pilar."
        ),
        Mahfudzot(
            arabic = "النجاح ثمرة الاجتهاد",
            meaning = "Kesuksesan adalah buah dari kesungguhan."
        ),
        Mahfudzot(
            arabic = "من لم يزرع لم يحصد",
            meaning = "Barangsiapa tidak menanam, ia tidak akan menuai."
        ),
        Mahfudzot(
            arabic = "لا يَلدغ المؤمن من جحر مرتين",
            meaning = "Seorang mukmin tidak akan terkena sengatan dari lubang yang sama dua kali."
        ),
        Mahfudzot(
            arabic = "الدربة قيّادة",
            meaning = "Latihan yang rutin akan menghasilkan keahlian."
        ),
        Mahfudzot(
            arabic = "لا يدرك المجد إلا بالجد",
            meaning = "Kemuliaan tidak akan tercapai kecuali dengan kesungguhan."
        ),
        Mahfudzot(
            arabic = "على قدر أهل العزم تأتي العزائم",
            meaning = "Sesuai dengan kadar tekadnya, datanglah tantangan yang setara."
        ),
        Mahfudzot(
            arabic = "بقدر الكد تُكتَسَب المَعَالِي",
            meaning = "Sesuai dengan usaha keras, derajat yang tinggi akan diraih."
        ),
        Mahfudzot(
            arabic = "من لا يتعب لا يرى راحة",
            meaning = "Barangsiapa tidak mau lelah, ia tidak akan merasakan istirahat."
        ),
        Mahfudzot(
            arabic = "النجاح سلم لا تستطيع أن تتسلقه ويداك في جيبك",
            meaning = "Kesuksesan adalah tangga yang tak bisa didaki sementara tanganmu masih di dalam saku."
        ),
        Mahfudzot(
            arabic = "حبّ القراءة مفتاح كل تقدم",
            meaning = "Kecintaan membaca adalah kunci segala kemajuan."
        ),
        Mahfudzot(
            arabic = "من تأخر عن الفجر لم يدرك الظهر",
            meaning = "Barangsiapa terlambat dari fajar, ia tidak akan mengecap siang."
        ),
        Mahfudzot(
            arabic = "الهمة عالية والجد دؤوب",
            meaning = "Semangat tinggi dan usaha yang tekun."
        ),
        Mahfudzot(
            arabic = "من جدّ في الطلب أدرك ما طلب",
            meaning = "Barangsiapa tekun dalam menuntut, ia akan mendapat apa yang dicarinya."
        ),
        Mahfudzot(
            arabic = "إنما الأعمال بالنيات",
            meaning = "Sesungguhnya setiap amal tergantung pada niatnya."
        ),

        // ===== WAKTU (Time) =====
        Mahfudzot(
            arabic = "الوقت كالسيف إن لم تقطعه قطعك",
            meaning = "Waktu bagaikan pedang, jika kamu tidak memotongnya, ia akan memotongmu."
        ),
        Mahfudzot(
            arabic = "لا تؤجل عمل اليوم إلى الغد",
            meaning = "Jangan menunda pekerjaan hari ini ke esok hari."
        ),
        Mahfudzot(
            arabic = "إن الوقت كالذهب",
            meaning = "Sesungguhnya waktu bagaikan emas."
        ),
        Mahfudzot(
            arabic = "الوقت أنفس من الذهب",
            meaning = "Waktu lebih berharga dari pada emas."
        ),
        Mahfudzot(
            arabic = "أضاع الوقت من رفض العلم والعمل",
            meaning = "Telah menyia-nyiakan waktu orang yang menolak ilmu dan amal."
        ),
        Mahfudzot(
            arabic = "اغتنم خمساً قبل خمس",
            meaning = "Pergunakanlah lima hal sebelum lima hal datang."
        ),
        Mahfudzot(
            arabic = "فرط العمر فيما لا ينفع فكيف الندم بعد الموت",
            meaning = "Menghamburkan umur pada hal yang tak bermanfaat, bagaimana bisa menyesal setelah mati."
        ),
        Mahfudzot(
            arabic = "الصبا ناضج قبل أن ينضج",
            meaning = "Masa muda itu matang sebelum ia menjadi tua."
        ),
        Mahfudzot(
            arabic = "الدنيا ساعة فاجعلها طاعة",
            meaning = "Dunia hanyalah sesaat, maka jadikanlah ia untuk ketaatan."
        ),
        Mahfudzot(
            arabic = "لا ينتظر النجاح من نام عن بدايته",
            meaning = "Jangan mengharapkan kesuksesan dari orang yang tidur di awal perjalanan."
        ),
        Mahfudzot(
            arabic = "الزمن في أشد الهجوم",
            meaning = "Waktu terus-menerus menyerang dengan dahsyat."
        ),
        Mahfudzot(
            arabic = "ما مضى فات والمؤمل غيب ولك الساعة التي أنت فيها",
            meaning = "Yang telah lewat telah pergi, yang diharapkan masih samar, dan yang kamu miliki hanyalah saat ini."
        ),

        // ===== KESABARAN (Patience) =====
        Mahfudzot(
            arabic = "الصبر مفتاح الفرج",
            meaning = "Kesabaran adalah kunci kelapangan."
        ),
        Mahfudzot(
            arabic = "إن الصبر مع العسر يسراً",
            meaning = "Sesungguhnya kesabaran bersama kesulitan ada kemudahan."
        ),
        Mahfudzot(
            arabic = "من صبر ظفر",
            meaning = "Barangsiapa bersabar, ia akan beruntung."
        ),
        Mahfudzot(
            arabic = "الصبر مطية لا تُبَلِّغ صاحبها إلا الجنة",
            meaning = "Kesabaran adalah kendaraan yang tak akan membawa penunggangnya kecuali ke surga."
        ),
        Mahfudzot(
            arabic = "إنما يُعطى الصابرون أجرهم بغير حساب",
            meaning = "Sesungguhnya orang-orang yang sabar mendapat pahala tanpa perhitungan."
        ),
        Mahfudzot(
            arabic = "ما يُصيب المؤمن من نصب ولا وصب حتى الهمّ يهمّه إلا كفّر الله بها من خطاياه",
            meaning = "Tidaklah seorang mukmin tertimpa kelelahan, penyakit, atau kecemasan kecuali Allah menghapus dosa-dosanya karenanya."
        ),
        Mahfudzot(
            arabic = "اصبر على ما يقال ويُفعل",
            meaning = "Bersabarlah terhadap apa yang dikatakan dan dilakukan."
        ),
        Mahfudzot(
            arabic = "الصبر ضياء",
            meaning = "Kesabaran itu cahaya."
        ),
        Mahfudzot(
            arabic = "الصبران: الصبر على طاعة الله والصبر عن معصية الله",
            meaning = "Dua jenis kesabaran: sabar dalam ketaatan kepada Allah dan sabar dari kemaksiatan kepada Allah."
        ),
        Mahfudzot(
            arabic = "إنّما المؤمن الذي إذا أذنبه استغفر",
            meaning = "Sesungguhnya mukmin yang sejati adalah yang ketika berbuat dosa segera memohon ampun."
        ),
        Mahfudzot(
            arabic = "بَلِّغْ صبرك أن يبلغ بك",
            meaning = "Jadikanlah kesabaranmu mampu membawamu ke tujuan."
        ),
        Mahfudzot(
            arabic = "ربّ صبرٍ يُعقب خيراً",
            meaning = "Betapa banyak kesabaran yang berujung pada kebaikan."
        ),
        Mahfudzot(
            arabic = "البلاء موكل بالمنطق",
            meaning = "Ujian itu selalu menyertai ucapan."
        ),

        // ===== PERSAHABATAN (Friendship) =====
        Mahfudzot(
            arabic = "الصديق وقت الضيق",
            meaning = "Teman sejati baru terlihat saat kesulitan."
        ),
        Mahfudzot(
            arabic = "خير الأصحاب عند النوائب",
            meaning = "Sebaik-baik teman adalah yang hadir di saat kesusahan."
        ),
        Mahfudzot(
            arabic = "المرء على دين خليله",
            meaning = "Seseorang bergantung pada agama teman dekatnya."
        ),
        Mahfudzot(
            arabic = "لا تصحب إلا مؤمناً ولا يأكل طعامك إلا تقي",
            meaning = "Jangan berteman kecuali dengan orang mukmin dan jangan biarkan makananmu dimakan kecuali oleh orang yang bertakwa."
        ),
        Mahfudzot(
            arabic = "الجليس الصالح خير من الوحدة",
            meaning = "Teman yang baik lebih utama dari menyendiri."
        ),
        Mahfudzot(
            arabic = "من جالس الصالحين أمن من الفتنة",
            meaning = "Barangsiapa berteman dengan orang-orang shalih, ia akan aman dari fitnah."
        ),
        Mahfudzot(
            arabic = "لا تَغْتَرّن بالصديق قبل الاختبار",
            meaning = "Jangan tertipu dengan persahabatan sebelum diuji."
        ),
        Mahfudzot(
            arabic = "من أحب أخاه فليخبره",
            meaning = "Barangsiapa mencintai saudaranya, hendaklah ia memberitahunya."
        ),
        Mahfudzot(
            arabic = "قل الحق ولو كان مراً",
            meaning = "Ucapkanlah kebenaran meskipun pahit."
        ),
        Mahfudzot(
            arabic = "إياك وصحبة الأشرار",
            meaning = "Jauhilah berteman dengan orang-orang buruk."
        ),
        Mahfudzot(
            arabic = "الصاحب ساحب",
            meaning = "Teman itu menarik (kepribadiannya menular)."
        ),
        Mahfudzot(
            arabic = "خير الناس أنفعهم للناس",
            meaning = "Sebaik-baik manusia adalah yang paling bermanfaat bagi manusia lainnya."
        ),

        // ===== KEBENARAN & KEBENARAN (Truth & Honesty) =====
        Mahfudzot(
            arabic = "الصدق منجاة",
            meaning = "Kejujuran itu keselamatan."
        ),
        Mahfudzot(
            arabic = "إن الصدق يهدي إلى البر",
            meaning = "Sesungguhnya kejujuran membimbing kepada kebaikan."
        ),
        Mahfudzot(
            arabic = "عليكم بالصدق فإن الصدق يهدي إلى البر",
            meaning = "Berlaku jujurlah kalian, karena kejujuran membimbing kepada kebaikan."
        ),
        Mahfudzot(
            arabic = "الكذب يهدي إلى الفجور",
            meaning = "Kedustaan membimbing kepada kefasikan."
        ),
        Mahfudzot(
            arabic = "من صَدَقَ نَجَا",
            meaning = "Barangsiapa jujur, ia akan selamat."
        ),
        Mahfudzot(
            arabic = "لا يصلح الكذب في جد ولا هزل",
            meaning = "Dusta itu tidak baik baik dalam serius maupun bercanda."
        ),
        Mahfudzot(
            arabic = "الصدق طمأنينة والكذب ريبة",
            meaning = "Kejujuran memberi ketenangan dan kedustaan menimbulkan kecurigaan."
        ),
        Mahfudzot(
            arabic = "حُفّت الجنة بالمكاره وحُفّت النار بالشهوات",
            meaning = "Surga dikelilingi oleh hal-hal yang tidak disukai dan neraka dikelilingi oleh syahwat."
        ),
        Mahfudzot(
            arabic = "من عرف الحق سَكَنَ إليه",
            meaning = "Barangsiapa mengenal kebenaran, ia akan tenteram karenanya."
        ),
        Mahfudzot(
            arabic = "الحقيقة مرّة لكن نتائجها حلوة",
            meaning = "Kebenaran itu pahit, tapi hasilnya manis."
        ),

        // ===== KEBIJAKSANAAN (Wisdom) =====
        Mahfudzot(
            arabic = "الحكمة ضالّة المؤمن",
            meaning = "Kebijaksanaan adalah barang hilang orang mukmin, di mana saja ia menemukannya, dialah yang paling berhak."
        ),
        Mahfudzot(
            arabic = "العاقل من وعظ بغيره",
            meaning = "Orang bijak adalah yang bisa mengambil pelajaran dari orang lain."
        ),
        Mahfudzot(
            arabic = "لا تكن ممن يأمر بالمعروف ولا يأتيه",
            meaning = "Janganlah menjadi orang yang menyuruh kebaikan tapi tidak melakukannya."
        ),
        Mahfudzot(
            arabic = "أعقل الناس أعذرهُم للناس",
            meaning = "Orang paling berakal adalah yang paling mudah memberi maaf kepada orang lain."
        ),
        Mahfudzot(
            arabic = "رحم الله امرءاً عرف قدر نفسه",
            meaning = "Semoga Allah merahmati orang yang mengetahui kemampuan dirinya."
        ),
        Mahfudzot(
            arabic = "من عرف نفسه فقد عرف ربه",
            meaning = "Barangsiapa mengenal dirinya, maka ia mengenal Tuhannya."
        ),
        Mahfudzot(
            arabic = "لا تكن ممن إذا مدحته ارتفع وإذا ذممته انحدر",
            meaning = "Janganlah menjadi orang yang jika dipuji lalu meninggi dan jika dicela lalu jatuh."
        ),
        Mahfudzot(
            arabic = "الكيس من دان نفسه وعمل لما بعد الموت",
            meaning = "Orang yang cerdas adalah yang menghisab dirinya sendiri dan beramal untuk sesudah mati."
        ),
        Mahfudzot(
            arabic = "من حسن إسلام المرء تركه ما لا يعنيه",
            meaning = "Bagusnya Islam seseorang adalah ia meninggalkan apa yang tidak menyangkut urusannya."
        ),
        Mahfudzot(
            arabic = "خير الكلام ما قلّ ودلّ",
            meaning = "Sebaik-baik ucapan adalah yang sedikit namun mengandung makna."
        ),
        Mahfudzot(
            arabic = "إذا سكتّ عن الحق فهو الصمت السكوت",
            meaning = "Jika kamu diam dari kebenaran, maka itu adalah keheningan yang sejati."
        ),
        Mahfudzot(
            arabic = "لا تقل ما لا تعلم بل لا تقل كل ما تعلم",
            meaning = "Jangan mengatakan apa yang tidak kamu ketahui, dan jangan pula mengatakan semua yang kamu ketahui."
        ),
        Mahfudzot(
            arabic = "إذا رأيت هيئةً فتأمل فإن العبرة بالفكر",
            meaning = "Jika kamu melihat sesuatu, perhatikanlah, karena pelajaran ada pada pemikiran."
        ),
        Mahfudzot(
            arabic = "إن من البيان لسحراً",
            meaning = "Sesungguhnya di antara ucapan ada yang menyerupai sihir (sangat memikat)."
        ),
        Mahfudzot(
            arabic = "العاقل لا يخدع مرتين",
            meaning = "Orang yang berakal tidak akan tertipu dua kali."
        ),
        Mahfudzot(
            arabic = "التفكير نصف العبادة",
            meaning = "Berpikir itu separuh dari ibadah."
        ),
        Mahfudzot(
            arabic = "أخطب من خطب واعظ",
            meaning = "Lebih baik pemberi nasihat yang mengamalkan nasihatnya sendiri."
        ),
        Mahfudzot(
            arabic = "من استشاره الناس عدّ حازماً",
            meaning = "Barangsiapa yang dimintai nasihat oleh orang banyak, ia dianggap bijaksana."
        ),
        Mahfudzot(
            arabic = "خير الأمور أوسطها",
            meaning = "Sebaik-baik urusan adalah yang pertengahan (tengahan)."
        ),
        Mahfudzot(
            arabic = "لا تفرّطن في الحذر",
            meaning = "Jangan berlebihan dalam kewaspadaan."
        ),

        // ===== TAKWA & IMAN (Piety & Faith) =====
        Mahfudzot(
            arabic = "اتق الله حيثما كنت",
            meaning = "Bertakwalah kepada Allah di mana pun kamu berada."
        ),
        Mahfudzot(
            arabic = "وتابع السيئة الحسنة تمحها",
            meaning = "Dan ikutilah keburukan dengan kebaikan, niscaya ia akan menghapusnya."
        ),
        Mahfudzot(
            arabic = "وخالق الناس بخُلق حسن",
            meaning = "Dan bergaullah dengan manusia dengan akhlak yang baik."
        ),
        Mahfudzot(
            arabic = "إنما الأعمال بالنيات وإنما لكل امرئ ما نوى",
            meaning = "Sesungguhnya setiap amal tergantung niatnya dan setiap orang akan mendapat sesuai niatnya."
        ),
        Mahfudzot(
            arabic = "لا يؤمن أحدكم حتى يحب لأخيه ما يحب لنفسه",
            meaning = "Tidak beriman salahseorang di antara kalian hingga ia mencintai saudaranya sebagaimana ia mencintai dirinya sendiri."
        ),
        Mahfudzot(
            arabic = "المؤمن مرآة أخيه",
            meaning = "Orang mukmin adalah cermin bagi saudaranya."
        ),
        Mahfudzot(
            arabic = "أحبّ الأعمال إلى الله أدومها وإن قلّ",
            meaning = "Amal yang paling dicintai Allah adalah yang paling kontinu meskipun sedikit."
        ),
        Mahfudzot(
            arabic = "الإيمان بضع وستون شعبة",
            meaning = "Iman itu ada puluhan cabang."
        ),
        Mahfudzot(
            arabic = "من أحبّ لله وأبغض لله وأعطى لله ومنع لله فقد استكمل الإيمان",
            meaning = "Barangsiapa mencintai karena Allah, membenci karena Allah, memberi karena Allah dan menahan karena Allah, maka ia telah menyempurnakan iman."
        ),
        Mahfudzot(
            arabic = "حبّ الدنيا رأس كل خطيئة",
            meaning = "Cinta dunia adalah sumber segala dosa."
        ),
        Mahfudzot(
            arabic = "الدنيا سجن المؤمن وجنة الكافر",
            meaning = "Dunia adalah penjara bagi orang mukmin dan surga bagi orang kafir."
        ),
        Mahfudzot(
            arabic = "اعمل لدنياك كأنك تعيش أبداً واعمل لآخرتك كأنك تموت غداً",
            meaning = "Bekerjalah untuk duniamu seakan-akan kamu hidup selamanya, dan kerjakan untuk akhiratmu seakan-akan kamu mati besok."
        ),
        Mahfudzot(
            arabic = "القلب الصالح يخرج منه الصالح",
            meaning = "Hati yang bersih akan menghasilkan perbuatan yang baik."
        ),
        Mahfudzot(
            arabic = "التقوى هاهنا",
            meaning = "Takwa itu di sini (sambil menunjuk ke dada)."
        ),
        Mahfudzot(
            arabic = "من اتقى الله وقاه",
            meaning = "Barangsiapa bertakwa kepada Allah, Allah akan melindunginya."
        ),

        // ===== AKHLAK (Character) =====
        Mahfudzot(
            arabic = "خير الناس أحسنهم أخلاقاً",
            meaning = "Sebaik-baik manusia adalah yang paling baik akhlaknya."
        ),
        Mahfudzot(
            arabic = "إن من أحبكم إليّ وأقربكم مني مجالساً يوم القيامة أحاسنكم أخلاقاً",
            meaning = "Sesungguhnya orang yang paling aku cintai dan paling dekat denganku di hari kiamat adalah yang paling baik akhlaknya."
        ),
        Mahfudzot(
            arabic = "لا يدخل الجنة من لا يأمن جاره بوائقه",
            meaning = "Tidak masuk surga orang yang tetangganya tidak merasa aman dari gangguannya."
        ),
        Mahfudzot(
            arabic = "ما كان الرفق في شيء إلا زانه",
            meaning = "Tidaklah kelembutan ada pada sesuatu kecuali ia akan memperindahnya."
        ),
        Mahfudzot(
            arabic = "إن الله يحب الرفق في الأمر كله",
            meaning = "Sesungguhnya Allah mencintai kelembutan dalam segala urusan."
        ),
        Mahfudzot(
            arabic = "المسلم من سلم المسلمون من لسانه ويده",
            meaning = "Orang Muslim adalah yang kaum Muslimin lainnya selamat dari lisan dan tangannya."
        ),
        Mahfudzot(
            arabic = "لا تغضب فإذا غضبت فاسكت",
            meaning = "Jangan marah, dan jika kamu marah maka diamlah."
        ),
        Mahfudzot(
            arabic = "ليس الشديد بالصرعة إنما الشديد الذي يملك نفسه عند الغضب",
            meaning = "Orang kuat bukanlah yang pandai bergulat, tetapi yang bisa mengendalikan dirinya saat marah."
        ),
        Mahfudzot(
            arabic = "الحياء شعبة من الإيمان",
            meaning = "Malu adalah cabang dari iman."
        ),
        Mahfudzot(
            arabic = "إياك والكبر فإن الكبر ردّ على صاحبه",
            meaning = "Jauhilah kesombongan, karena kesombongan akan menolak pemiliknya."
        ),
        Mahfudzot(
            arabic = "لا يدخل الجنة من كان في قلبه مثقال ذرة من كبرياء",
            meaning = "Tidak masuk surga orang yang di dalam hatinya ada kesombongan sebesar zarrah."
        ),
        Mahfudzot(
            arabic = "تواضعوا فإنه لا يدخل الجنة من كان في قلبه مثقال ذرة من كبرياء",
            meaning = "Rendahlah hatimu, karena tidak masuk surga orang yang di hatinya ada kesombongan."
        ),
        Mahfudzot(
            arabic = "من لانَ عَودُه كثُر أتباعُه",
            meaning = "Barangsiapa lembut kebiasaannya, banyaklah pengikutnya."
        ),
        Mahfudzot(
            arabic = "أحبّ إلىّ أن أكون عبداً شكوراً",
            meaning = "Aku lebih suka menjadi hamba yang bersyukur."
        ),
        Mahfudzot(
            arabic = "من لم يشكر الناس لم يشكر الله",
            meaning = "Barangsiapa tidak berterima kasih kepada manusia, ia tidak berterima kasih kepada Allah."
        ),
        Mahfudzot(
            arabic = "الشكر حفظ النعمة وزيادتها",
            meaning = "Syukur itu menjaga dan menambah nikmat."
        ),
        Mahfudzot(
            arabic = "إن مع كل عسر يسراً إن مع العسر يسراً",
            meaning = "Sesungguhnya bersama kesulitan ada kemudahan, sesungguhnya bersama kesulitan ada kemudahan."
        ),
        Mahfudzot(
            arabic = "الاحترام بالتقديم",
            meaning = "Penghormatan didapat dengan mendahulukan orang lain."
        ),
        Mahfudzot(
            arabic = "البرّ حسن الخلق والإثم ما حاك في صدرك وكرهت أن يطلع عليه الناس",
            meaning = "Kebaikan adalah akhlak yang baik, dan dosa adalah apa yang berkecamuk di dada dan kamu tidak suka orang lain mengetahuinya."
        ),
        Mahfudzot(
            arabic = "تبسّمك في وجه أخيك صدقة",
            meaning = "Senyummu di wajah saudaramu adalah sedekah."
        ),

        // ===== HUMILITAS & KERENDAHAN HATI (Humility) =====
        Mahfudzot(
            arabic = "من تواضع لله رفعه",
            meaning = "Barangsiapa merendahkan diri karena Allah, Allah akan mengangkat derajatnya."
        ),
        Mahfudzot(
            arabic = "لو كنت فاضلاً لكان فضلك أولى بك من فخرِك",
            meaning = "Jika kamu orang yang utama, maka keutamaanmu seharusnya lebih patut daripada kebanggaanmu."
        ),
        Mahfudzot(
            arabic = "من رأى نفسه فقد رأى الحقيراً",
            meaning = "Barangsiapa memandang dirinya besar, sesungguhnya ia memandang sesuatu yang hina."
        ),
        Mahfudzot(
            arabic = "من وضع نفسه لم يرفعه أحد",
            meaning = "Barangsiapa merendahkan dirinya sendiri, tak ada yang bisa mengangkatnya."
        ),
        Mahfudzot(
            arabic = "إنّ من عباد الله من لو أقسم على الله لأبرّه",
            meaning = "Sesungguhnya di antara hamba Allah ada yang jika ia bersumpah atas nama Allah, pasti Allah mengabulkannya."
        ),
        Mahfudzot(
            arabic = "من جاءك معتذراً فاقبل عذره",
            meaning = "Barangsiapa datang kepadamu dengan permintaan maaf, terimalah permintaan maafnya."
        ),
        Mahfudzot(
            arabic = "عفواً لكي يعفو الله عنكم",
            meaning = "Maafkanlah (satu sama lain) agar Allah mengampuni kalian."
        ),
        Mahfudzot(
            arabic = "من عفا وأصلح فأجره على الله",
            meaning = "Barangsiapa memaafkan dan berbuat baik, pahalanya di sisi Allah."
        ),
        Mahfudzot(
            arabic = "العتاب صابون القلوب",
            meaning = "Teguran itu adalah sabun pembersih hati."
        ),
        Mahfudzot(
            arabic = "لا يحقرن أحدكم نفسه",
            meaning = "Janganlah salahseorang di antara kalian meremehkan dirinya sendiri."
        ),

        // ===== DOA & HARAPAN (Supplication & Hope) =====
        Mahfudzot(
            arabic = "ادعوا الله وأنتم موقنون بالإجابة",
            meaning = "Berdoalah kepada Allah dengan penuh keyakinan akan dikabulkan."
        ),
        Mahfudzot(
            arabic = "الدعاء سلاح المؤمن",
            meaning = "Doa adalah senjata orang mukmin."
        ),
        Mahfudzot(
            arabic = "ادعوا ربكم تضرعاً وخفية",
            meaning = "Berdoalah kepada Tuhanmu dengan rendah hati dan suara lembut."
        ),
        Mahfudzot(
            arabic = "إن الله لا يستحيي أن يضرب مثلاً",
            meaning = "Sesungguhnya Allah tidak segan untuk membuat perumpamaan."
        ),
        Mahfudzot(
            arabic = "ربّنا آتنا في الدنيا حسنة وفي الآخرة حسنة وقنا عذاب النار",
            meaning = "Ya Tuhan kami, berilah kami kebaikan di dunia dan kebaikan di akhirat, serta lindungilah kami dari azab neraka."
        ),
        Mahfudzot(
            arabic = "اللهمّ إنّي أعوذ بك من الهمّ والحزن",
            meaning = "Ya Allah, aku berlindung kepada-Mu dari kesedihan dan kegundahan."
        ),
        Mahfudzot(
            arabic = "لا تيأس من رَوح الله",
            meaning = "Jangan berputus asa dari rahmat Allah."
        ),
        Mahfudzot(
            arabic = "فإن مع العسر يسراً",
            meaning = "Maka sesungguhnya bersama kesulitan ada kemudahan."
        ),
        Mahfudzot(
            arabic = "حسبنا الله ونعم الوكيل",
            meaning = "Cukuplah Allah menjadi penolong kami dan Dialah sebaik-baik pelindung."
        ),
        Mahfudzot(
            arabic = "لا حول ولا قوة إلا بالله",
            meaning = "Tidak ada daya dan kekuatan kecuali dengan pertolongan Allah."
        ),

        // ===== KEMAUDULAN & KESIAPAN (Preparedness) =====
        Mahfudzot(
            arabic = "من استعدّ للشيء استعدّ له أهله",
            meaning = "Barangsiapa bersiap untuk sesuatu, maka ia telah bersiap dengannya."
        ),
        Mahfudzot(
            arabic = "الحذر لا يمنع القدر",
            meaning = "Kewaspadaan tidak menghalangi takdir."
        ),
        Mahfudzot(
            arabic = "اربط حصانك ثم توكّل",
            meaning = "Ikatlah kudamu, lalu bertawakkallah."
        ),
        Mahfudzot(
            arabic = "لا تضرعن في طلب الحاجات إلى من لا يقضيها",
            meaning = "Jangan merendahkan diri memohon keperluan kepada orang yang tidak bisa memenuhinya."
        ),
        Mahfudzot(
            arabic = "التوكل على الله في كل شيء",
            meaning = "Bertawakkallah kepada Allah dalam segala hal."
        ),
        Mahfudzot(
            arabic = "من لم يستعد للمنية لم ينل البغية",
            meaning = "Barangsiapa tidak bersiap untuk kematian, ia tidak akan mencapai cita-citanya."
        ),
        Mahfudzot(
            arabic = "الاحتراس لا يردّ القضاء",
            meaning = "Kewaspadaan tidak menolak takdir."
        ),
        Mahfudzot(
            arabic = "الأخذ بالأسباب سُنّة",
            meaning = "Mengupayakan sebab-sebab adalah sunnah."
        ),

        // ===== PERBANDINGAN & METAFORA (Metaphors & Analogies) =====
        Mahfudzot(
            arabic = "القلب كالطير يطير حيث شاء",
            meaning = "Hati bagaikan burung yang terbang ke mana ia suka."
        ),
        Mahfudzot(
            arabic = "الدنيا كظلّ",
            meaning = "Dunia ini bagaikan bayangan."
        ),
        Mahfudzot(
            arabic = "الموت بحر والناس فيه كالسفن",
            meaning = "Kematian adalah lautan dan manusia di dalamnya bagaikan kapal-kapal."
        ),
        Mahfudzot(
            arabic = "العمر أقصر من أن يُضيع في الغيّ",
            meaning = "Umur lebih pendek dari pada disia-siakan dalam kesesatan."
        ),
        Mahfudzot(
            arabic = "الجسد خادم والقلب مَلِك",
            meaning = "Jasad itu pelayan dan hati itu raja."
        ),
        Mahfudzot(
            arabic = "الدنيا مزرعة الآخرة",
            meaning = "Dunia adalah ladang untuk akhirat."
        ),
        Mahfudzot(
            arabic = "كما تدين تُدان",
            meaning = "Sebagaimana kamu berbuat, begitulah kamu akan diperlakukan."
        ),
        Mahfudzot(
            arabic = "الجزاء من جنس العمل",
            meaning = "Balasan itu sesuai dengan jenis perbuatan."
        ),
        Mahfudzot(
            arabic = "ليس كل ما يُلمع ذهباً",
            meaning = "Tidak semua yang berkilau itu emas."
        ),
        Mahfudzot(
            arabic = "كل سيف لا يقطع نجادَه",
            meaning = "Setiap pedang pasti ada sarungnya."
        ),
        Mahfudzot(
            arabic = "القناعة كنز لا يفنى",
            meaning = "Kerelaan (qana'ah) adalah harta yang tak akan habis."
        ),
        Mahfudzot(
            arabic = "الصبر جميل والله المستعان",
            meaning = "Kesabaran itu indah dan kepada Allah-lah kita memohon pertolongan."
        ),
        Mahfudzot(
            arabic = "النار لا تولد إلا من نار",
            meaning = "Api tidak lahir kecuali dari api."
        ),
        Mahfudzot(
            arabic = "لا يقطع الشجرة إلا شجرة",
            meaning = "Pohon hanya bisa ditebang dengan pohon."
        ),

        // ===== KEMULIAAN & KEHORMATAN (Honor & Dignity) =====
        Mahfudzot(
            arabic = "من أكرم نفسه أهان الدنيا",
            meaning = "Barangsiapa menghormati dirinya, ia akan menghinakan dunia."
        ),
        Mahfudzot(
            arabic = "أعزّ الناس من غلب هواه",
            meaning = "Orang paling mulia adalah yang mengalahkan hawa nafsunya."
        ),
        Mahfudzot(
            arabic = "لا عزّ لمن لا دين له",
            meaning = "Tidak ada kemuliaan bagi orang yang tidak beragama."
        ),
        Mahfudzot(
            arabic = "العزّة لله ولرسوله وللمؤمنين",
            meaning = "Kemuliaan itu hanya milik Allah, Rasul-Nya, dan orang-orang mukmin."
        ),
        Mahfudzot(
            arabic = "من عزّ بلا سلطان ذلّ",
            meaning = "Barangsiapa berlaku sombong tanpa kekuasaan, ia akan hina."
        ),
        Mahfudzot(
            arabic = "لا يُعرف الشريف إلا عند المكاره",
            meaning = "Orang yang mulia baru dikenal di saat kesulitan."
        ),
        Mahfudzot(
            arabic = "من صان نفسه صانته",
            meaning = "Barangsiapa menjaga dirinya, ia akan dijaga."
        ),
        Mahfudzot(
            arabic = "الكرم في النائبات",
            meaning = "Kemurahan hati itu terlihat di saat kesulitan."
        ),

        // ===== HARTA & KEMISKINAN (Wealth & Poverty) =====
        Mahfudzot(
            arabic = "ليس الغنى عن كثرة العرض",
            meaning = "Kekayaan bukanlah karena banyaknya harta."
        ),
        Mahfudzot(
            arabic = "إنما الغنى غنى النفس",
            meaning = "Sesungguhnya kekayaan yang sebenarnya adalah kekayaan jiwa."
        ),
        Mahfudzot(
            arabic = "من قلّ ماله كثر دعاؤه",
            meaning = "Barangsiapa sedikit hartanya, banyaklah doanya."
        ),
        Mahfudzot(
            arabic = "لا تَحْقِرَنَّ من المعروف شيئاً ولو أن تلقى أخاك بوجه طَلْق",
            meaning = "Jangan meremehkan kebaikan meskipun hanya menemui saudaramu dengan wajah berseri."
        ),
        Mahfudzot(
            arabic = "اليد العليا خير من اليد السفلى",
            meaning = "Tangan yang di atas (memberi) lebih baik dari tangan yang di bawah (menerima)."
        ),
        Mahfudzot(
            arabic = "المال لا يبقى والعمر لا يعود",
            meaning = "Harta tidak akan tetap dan umur tidak akan kembali."
        ),
        Mahfudzot(
            arabic = "كنزُ الكنوز القناعة",
            meaning = "Harta karun yang paling berharga adalah kerelaan hati (qana'ah)."
        ),
        Mahfudzot(
            arabic = "الفقر فقر النفس لا فقر المال",
            meaning = "Kemiskinan yang sebenarnya adalah kemiskinan jiwa, bukan kemiskinan harta."
        ),

        // ===== KEMATIAN & AKHIRAT (Death & Hereafter) =====
        Mahfudzot(
            arabic = "كُن في الدنيا كأنك غريب أو عابر سبيل",
            meaning = "Hiduplah di dunia seakan-akan kamu orang asing atau pengembara."
        ),
        Mahfudzot(
            arabic = "أكثروا من ذكر هاذم اللذات",
            meaning = "Perbanyaklah mengingat pemutus kelezatan (kematian)."
        ),
        Mahfudzot(
            arabic = "الموت حقّ",
            meaning = "Kematian itu kepastian."
        ),
        Mahfudzot(
            arabic = "كل نفس ذائقة الموت",
            meaning = "Setiap jiwa akan merasakan kematian."
        ),
        Mahfudzot(
            arabic = "ما بعد الموت إلا الجنة أو النار",
            meaning = "Setelah kematian tidak ada kecuali surga atau neraka."
        ),
        Mahfudzot(
            arabic = "من مات فقد قُضيَ عمله إلا من ثلاث",
            meaning = "Barangsiapa telah meninggal, terputuslah amalnya kecuali tiga hal."
        ),
        Mahfudzot(
            arabic = "يا دنيا غُرّي غيري",
            meaning = "Wahai dunia, tipulah orang lain (bukan aku)."
        ),
        Mahfudzot(
            arabic = "تزوّدوا فإن خير الزاد التقوى",
            meaning = "Bekalilah dirimu, dan sebaik-baik bekal adalah takwa."
        ),
        Mahfudzot(
            arabic = "اليوم عمل ولا حساب وغداً حساب ولا عمل",
            meaning = "Hari ini beramal tanpa hisab, dan besok hisab tanpa amal."
        ),
        Mahfudzot(
            arabic = "القبر صندوق العمل",
            meaning = "Kubur adalah kotak penyimpan amal."
        ),

        // ===== KEADILAN (Justice) =====
        Mahfudzot(
            arabic = "العدل أساس الملك",
            meaning = "Keadilan adalah pondasi kerajaan."
        ),
        Mahfudzot(
            arabic = "إن الله يأمركم أن تؤدوا الأمانات إلى أهلها",
            meaning = "Sesungguhnya Allah menyuruh kalian menunaikan amanah kepada yang berhak."
        ),
        Mahfudzot(
            arabic = "من استعدل استقام",
            meaning = "Barangsiapa berlaku adil, ia akan lurus."
        ),
        Mahfudzot(
            arabic = "لا تظلم كما لا تُظلم",
            meaning = "Jangan berlaku zalim sebagaimana kamu tidak mau dizalimi."
        ),
        Mahfudzot(
            arabic = "الظلم ظُلمات يوم القيامة",
            meaning = "Kedhaliman itu kegelapan di hari kiamat."
        ),
        Mahfudzot(
            arabic = "انصر أخاك ظالماً أو مظلوماً",
            meaning = "Bantulah saudaramu, baik ketika ia zalim atau dizalimi."
        ),
        Mahfudzot(
            arabic = "إياكم والظلم فإن الظلم ظلمات يوم القيامة",
            meaning = "Jauhilah kedhaliman, karena kedhaliman itu kegelapan di hari kiamat."
        ),

        // ===== BERSYUKUR & NIKAH MAT (Gratitude & Contentment) =====
        Mahfudzot(
            arabic = "لئن شكرتم لأزيدنكم",
            meaning = "Jika kamu bersyukur, niscaya Aku akan menambah nikmat kepadamu."
        ),
        Mahfudzot(
            arabic = "اشكروا نعمة الله لا تزول",
            meaning = "Bersyukurlah atas nikmat Allah agar nikmat itu tidak hilang."
        ),
        Mahfudzot(
            arabic = "قنع بما رُزقت تكن أغنى الناس",
            meaning = "Relakanlah apa yang diberikan kepadamu, niscaya kamu menjadi orang terkaya."
        ),
        Mahfudzot(
            arabic = "الرضا بقضاء الله من أعظم العبادة",
            meaning = "Ridha terhadap takdir Allah termasuk ibadah yang paling agung."
        ),
        Mahfudzot(
            arabic = "من رضي بما قُسِمَ له استغنى",
            meaning = "Barangsiapa ridha dengan bagiannya, ia akan merasa cukup."
        ),
        Mahfudzot(
            arabic = "نعم المولى ونعم النصير",
            meaning = "Sebaik-baik pelindung dan sebaik-baik penolong."
        ),

        // ===== TAUBAT & ISTIGHFAR (Repentance) =====
        Mahfudzot(
            arabic = "يا عبادي الذين أسرفوا على أنفسهم لا تقنطوا من رحمة الله",
            meaning = "Wahai hamba-hamba-Ku yang telah melampaui batas, jangan berputus asa dari rahmat Allah."
        ),
        Mahfudzot(
            arabic = "إن الله يبسط يده بالليل ليتوب مسيء النهار",
            meaning = "Sesungguhnya Allah mengulurkan tangan-Nya di malam hari untuk menerima taubat orang yang berdosa di siang hari."
        ),
        Mahfudzot(
            arabic = "التائب من الذنب كمن لا ذنب له",
            meaning = "Orang yang bertaubat dari dosa bagaikan orang yang tidak berdosa."
        ),
        Mahfudzot(
            arabic = "من تاب تاب الله عليه",
            meaning = "Barangsiapa bertaubat, Allah akan menerima taubatnya."
        ),
        Mahfudzot(
            arabic = "اللهمّ أنت ربّي لا إله إلا أنت خلقتني وأنا عبدك",
            meaning = "Ya Allah, Engkau adalah Tuhanku, tidak ada ilah selain Engkau, Engkau menciptakanku dan aku adalah hamba-Mu."
        ),
        Mahfudzot(
            arabic = "ربّ اغفر لي وتب عليّ إنك أنت التواب الرحيم",
            meaning = "Ya Tuhanku, ampunilah aku dan terimalah taubatku, sesungguhnya Engkau Maha Penerima Taubat lagi Maha Penyayang."
        ),

        // ===== SABAR & IKHLAS (Patience & Sincerity) =====
        Mahfudzot(
            arabic = "إنما يُوَفّى الصابرون أجرهم بغير حساب",
            meaning = "Sesungguhnya orang-orang yang sabar akan diberi balasan tanpa perhitungan."
        ),
        Mahfudzot(
            arabic = "ما أصابك لم يكن ليخطئك وما أخطأك لم يكن ليصيبك",
            meaning = "Apa yang menimpamu tidak mungkin meleset darimu, dan apa yang meleset darimu tidak mungkin menimpamu."
        ),
        Mahfudzot(
            arabic = "قدر الله وما شاء فعل",
            meaning = "Takdir Allah dan apa yang Dia kehendaki pasti terjadi."
        ),
        Mahfudzot(
            arabic = "الإخلاص نور القلب",
            meaning = "Ikhlas adalah cahaya hati."
        ),
        Mahfudzot(
            arabic = "من أخلص لله كفاه الله مؤونة الناس",
            meaning = "Barangsiapa ikhlas karena Allah, Allah akan mencukupkan urusannya dari manusia."
        ),
        Mahfudzot(
            arabic = "نِعْمَتَانِ مغبونٌ فيهما كثيرٌ من الناس: الصحة والفراغ",
            meaning = "Dua nikmat yang banyak disepelekan manusia: kesehatan dan waktu luang."
        ),

        // ===== UCAPAN & PERKATAAN (Speech) =====
        Mahfudzot(
            arabic = "قل خيراً أو اصمت",
            meaning = "Ucapkan kebaikan atau diamlah."
        ),
        Mahfudzot(
            arabic = "لا يؤمن العبد حتى يستحلّ ما حَرّم القرآن",
            meaning = "Seorang hamba tidak beriman hingga ia menghalalkan apa yang diharamkan Al-Quran."
        ),
        Mahfudzot(
            arabic = "إياك وما يُقال عنك",
            meaning = "Hati-hatilah dengan apa yang dikatakan orang tentangmu."
        ),
        Mahfudzot(
            arabic = "من لاحاه الناس استراح",
            meaning = "Barangsiapa dibiarkan oleh manusia, ia akan merasa tenang."
        ),
        Mahfudzot(
            arabic = "ربّ كلمةٍ قالها صاحبها لا يلقي لها بالاً هوتْ به في النار",
            meaning = "Betapa banyak kata yang diucapkan tanpa dipikirkan, lalu menjatuhkan pemiliknya ke neraka."
        ),
        Mahfudzot(
            arabic = "المؤمن لا يُلدغ من جحر مرتين",
            meaning = "Orang mukmin tidak akan terkena sengatan dari lubang yang sama dua kali."
        ),
        Mahfudzot(
            arabic = "من حفظ لسانه حفظ عرضه",
            meaning = "Barangsiapa menjaga lisannya, ia menjaga kehormatannya."
        ),

        // ===== MUSIBAH & UJIAN (Trials & Tribulations) =====
        Mahfudzot(
            arabic = "عجباً لأمر المؤمن إن أمره كله له خير",
            meaning = "Mengagumkan urusan orang mukmin, sesungguhnya semua urusannya adalah kebaikan."
        ),
        Mahfudzot(
            arabic = "إن عافيتك أحبّ إليّ من بلواك",
            meaning = "Sesungguhnya kesehatan yang Engkau berikan lebih aku cintai dari ujian."
        ),
        Mahfudzot(
            arabic = "لا يُصاب المؤمن بنصب حتى الخدش إلا كفّر الله بها من خطيئته",
            meaning = "Tidaklah seorang mukmin tertimpa kelelahan bahkan goresan pun, kecuali Allah menghapus dosanya dengannya."
        ),
        Mahfudzot(
            arabic = "ما يزال البلاء بالمؤمن والمؤمنة حتى يلقى الله وليس عليه خطيئة",
            meaning = "Ujian terus-menerus menimpa mukmin dan mukminah hingga ia menemui Allah dalam keadaan tanpa dosa."
        ),
        Mahfudzot(
            arabic = "ابتُلِيَ المؤمن في جسده وأهله وماله",
            meaning = "Orang mukmin diuji dalam jasadnya, keluarganya, dan hartanya."
        ),
        Mahfudzot(
            arabic = "إنّ مع كل عسر يسراً",
            meaning = "Sesungguhnya bersama kesulitan ada kemudahan."
        ),

        // ===== KEDERMAWANAN & SEDEKAH (Generosity & Charity) =====
        Mahfudzot(
            arabic = "ما نقص مال من صدقة",
            meaning = "Harta tidak akan berkurang karena sedekah."
        ),
        Mahfudzot(
            arabic = "تصدّقوا فإن الصدقة تطفئ غضب الرب",
            meaning = "Bersedekahlah, karena sedekah itu memadamkan murka Tuhan."
        ),
        Mahfudzot(
            arabic = "الصدقة برهان",
            meaning = "Sedekah itu adalah bukti (keimanan)."
        ),
        Mahfudzot(
            arabic = "الجود بالمال قليل",
            meaning = "Kedermawanan dengan harta adalah sesuatu yang sedikit dibandingkan kedermawanan jiwa."
        ),
        Mahfudzot(
            arabic = "داووا مرضاكم بالصدقة",
            meaning = "Obatilah orang sakit di antara kalian dengan sedekah."
        ),
        Mahfudzot(
            arabic = "من ذا الذي يقرض الله قرضاً حسناً",
            meaning = "Siapakah yang mau memberi pinjaman kepada Allah dengan pinjaman yang baik?"
        ),

        // ===== SEDIH & SUKA (Joy & Sorrow) =====
        Mahfudzot(
            arabic = "لا تحزن إن الله معنا",
            meaning = "Jangan bersedih, sesungguhnya Allah bersama kita."
        ),
        Mahfudzot(
            arabic = "ولا تهنوا ولا تحزنوا وأنتم الأعلون إن كنتم مؤمنين",
            meaning = "Janganlah kamu merasa lemah dan janganlah kamu bersedih, padahal kamulah orang-orang yang paling tinggi jika kamu beriman."
        ),
        Mahfudzot(
            arabic = "ربّ اشرح لي صدري ويسّر لي أمري",
            meaning = "Ya Tuhanku, lapangkanlah dadaku dan mudahkanlah urusanku."
        ),
        Mahfudzot(
            arabic = "قد يُخرج الله من المحن منحاً",
            meaning = "Kadang-kadang Allah mengeluarkan karunia dari ujian."
        ),
        Mahfudzot(
            arabic = "البلايا تُظهر الرجال",
            meaning = "Musibah itu menampakkan kehebatan para lelaki."
        ),
        Mahfudzot(
            arabic = "الفرج مع الصبر",
            meaning = "Keluarga itu bersama kesabaran."
        ),

        // ===== KEPEMIMPINAN (Leadership) =====
        Mahfudzot(
            arabic = "كلكم راع وكلكم مسؤول عن رعيته",
            meaning = "Kalian semua adalah pemimpin dan kalian semua bertanggung jawab atas yang dipimpin."
        ),
        Mahfudzot(
            arabic = "من استرعاه الله رعية فلم يحطها بنصحه لم يرح رائحة الجنة",
            meaning = "Barangsiapa diberi amanah memimpin tapi tidak memberikan nasihat, ia tidak akan mencium bau surga."
        ),
        Mahfudzot(
            arabic = "الإمام العادل ظلّ الله في الأرض",
            meaning = "Pemimpin yang adil adalah naungan Allah di muka bumi."
        ),
        Mahfudzot(
            arabic = "من أطاع أميراً فقد أطاعني",
            meaning = "Barangsiapa taat kepada pemimpinnya, berarti ia telah taat kepadaku."
        ),
        Mahfudzot(
            arabic = "لا طاعة لمخلوق في معصية الخالق",
            meaning = "Tidak ada ketaatan kepada makhluk dalam bermaksiat kepada Khalik."
        ),
        Mahfudzot(
            arabic = "سيد القوم خادمهم",
            meaning = "Pemimpin suatu kaum adalah pelayan mereka."
        ),

        // ===== PERJALANAN HIDUP (Life's Journey) =====
        Mahfudzot(
            arabic = "من سلك طريقاً يلتمس فيه علماً سهّل الله له به طريقاً إلى الجنة",
            meaning = "Barangsiapa menempuh jalan untuk mencari ilmu, Allah memudahkan baginya jalan ke surga."
        ),
        Mahfudzot(
            arabic = "المرء مع من أحب",
            meaning = "Seseorang bersama orang yang ia cintai (di akhirat)."
        ),
        Mahfudzot(
            arabic = "إن لكل شيء مفتاحاً ومفتاح الجنة الشهادتان",
            meaning = "Sesungguhnya setiap sesuatu ada kuncinya, dan kunci surga adalah dua kalimat syahadat."
        ),
        Mahfudzot(
            arabic = "الحب في الله والبغض في الله من أوثق عرى الإيمان",
            meaning = "Mencintai karena Allah dan membenci karena Allah adalah ikatan iman yang paling kuat."
        ),
        Mahfudzot(
            arabic = "سبعة يظلّهم الله في ظلّه يوم لا ظلّ إلا ظلّه",
            meaning = "Tujuh golongan yang akan dinaungi Allah pada hari tidak ada naungan kecuali naungan-Nya."
        ),
        Mahfudzot(
            arabic = "لا تستبطئوا الرزق فإنه لم يكن عبد يموت حتى يستكمل رزقه",
            meaning = "Jangan terlalu terburu-buru mengharapkan rizki, karena seorang hamba tidak akan mati sebelum rizkinya sempurna."
        ),
        Mahfudzot(
            arabic = "إن الله كتب الحسنات والسيئات",
            meaning = "Sesungguhnya Allah telah menuliskan kebaikan dan keburukan."
        ),

        // ===== BERTANYA & KEINGINTAHUAN (Inquiry & Curiosity) =====
        Mahfudzot(
            arabic = "من سأل عن شيء وهو يعلم لم يُفَقّه",
            meaning = "Barangsiapa bertanya tentang sesuatu yang ia sudah tahu, ia tidak akan dibuat paham."
        ),
        Mahfudzot(
            arabic = "هل عندكم من علم فتخرجوه لنا",
            meaning = "Apakah kalian memiliki ilmu sehingga kalian mengeluarkannya untuk kami?"
        ),
        Mahfudzot(
            arabic = "اسألوا أهل الذكر إن كنتم لا تعلمون",
            meaning = "Bertanyalah kepada ahli dzikir (orang berilmu) jika kalian tidak mengetahui."
        ),
        Mahfudzot(
            arabic = "من طلب العلم سَهّل الله له طريقاً إلى الجنة",
            meaning = "Barangsiapa menuntut ilmu, Allah memudahkan baginya jalan ke surga."
        ),

        // ===== SEMANGAT & TEKAD (Spirit & Determination) =====
        Mahfudzot(
            arabic = "إنما العلم بالتعلّم",
            meaning = "Sesungguhnya ilmu itu didapat dengan belajar."
        ),
        Mahfudzot(
            arabic = "لا يصلح العطار ما أفسده الدهر",
            meaning = "Tidak bisa diobati oleh tabib apa yang dirusak oleh waktu."
        ),
        Mahfudzot(
            arabic = "العمر قصير والعمل كثير",
            meaning = "Umur itu pendek dan amal itu banyak."
        ),
        Mahfudzot(
            arabic = "اعمل لدنياك كأنك تعيش أبداً",
            meaning = "Bekerjalah untuk duniamu seakan-akan kamu hidup selamanya."
        ),
        Mahfudzot(
            arabic = "لا تضيّع وقتك فيما لا ينفع",
            meaning = "Jangan sia-siakan waktumu pada hal yang tidak bermanfaat."
        ),
        Mahfudzot(
            arabic = "كل يوم يمرّ بك ينقص من عمرك",
            meaning = "Setiap hari yang lewat mengurangi umurmu."
        ),
        Mahfudzot(
            arabic = "لا تنس ذكر الله",
            meaning = "Jangan lupa berdzikir kepada Allah."
        ),
        Mahfudzot(
            arabic = "ألا بذكر الله تطمئن القلوب",
            meaning = "Ingatlah, hanya dengan mengingat Allah-lah hati menjadi tenteram."
        ),
        Mahfudzot(
            arabic = "إن في بدنك مُضغة إذا صلحت صلح الجسد كله",
            meaning = "Sesungguhnya di dalam jasad ada segumpal daging, jika baik maka baiklah seluruh jasad."
        ),
        Mahfudzot(
            arabic = "الطريق إلى الله مسدود على من طَرَدَ الله",
            meaning = "Jalan menuju Allah tertutup bagi orang yang diusir oleh Allah."
        ),
        Mahfudzot(
            arabic = "من عرف نفسه فقد عرف ربّه",
            meaning = "Barangsiapa mengenal dirinya, ia akan mengenal Tuhannya."
        ),

        // ===== MUHASABAH (Self-Reflection) =====
        Mahfudzot(
            arabic = "حاسبوا أنفسكم قبل أن تُحاسَبوا",
            meaning = "Hisablah dirimu sebelum kamu dihisab."
        ),
        Mahfudzot(
            arabic = "الكيّس من دان نفسه وعمل لما بعد الموت",
            meaning = "Orang yang cerdas adalah yang menghisab dirinya sendiri dan bekerja untuk sesudah mati."
        ),
        Mahfudzot(
            arabic = "من نَسِيَ نفسه فليس ينتفع بغيره",
            meaning = "Barangsiapa melupakan dirinya, ia tidak akan bisa memanfaatkan orang lain."
        ),
        Mahfudzot(
            arabic = "انظر إلى ما هو أدنى منك ولا تنظر إلى ما هو أعلى منك",
            meaning = "Lihatlah kepada orang yang di bawahmu dan jangan melihat kepada orang yang di atasmu."
        ),
        Mahfudzot(
            arabic = "العبد بين نِعمة وذنب فلا يركن إلى النعمة ولا ييأس من الذنب",
            meaning = "Seorang hamba berada antara nikmat dan dosa, jangan bergantung pada nikmat dan jangan berputus asa dari dosa."
        ),
        Mahfudzot(
            arabic = "لا تكن ممّن يأمر بالمعروف ولا يأتيه ولا ينهى عن المنكر ويأتيه",
            meaning = "Janganlah menjadi orang yang menyuruh kebaikan tapi tidak melakukannya, melarang kemungkaran tapi melakukannya."
        ),
        Mahfudzot(
            arabic = "كم من نعمة لا تُعرف",
            meaning = "Betapa banyak nikmat yang tidak disadari."
        ),

        // ===== TAQWA & WARA' (Piety & Asceticism) =====
        Mahfudzot(
            arabic = "اتق الله ولا تحقرن من المعروف شيئاً",
            meaning = "Bertakwalah kepada Allah dan jangan meremehkan kebaikan."
        ),
        Mahfudzot(
            arabic = "إن أقرب ما يكون العبد إلى ربّه وهو ساجد",
            meaning = "Sesungguhnya seorang hamba paling dekat kepada Tuhannya saat ia sujud."
        ),
        Mahfudzot(
            arabic = "الصلاة عماد الدين",
            meaning = "Shalat adalah tiang agama."
        ),
        Mahfudzot(
            arabic = "أول ما يُحاسب عليه العبد يوم القيامة الصلاة",
            meaning = "Hal pertama yang dihisab seorang hamba di hari kiamat adalah shalat."
        ),
        Mahfudzot(
            arabic = "من حافظ عليها كانت له نوراً وبرهاناً ونجاةً يوم القيامة",
            meaning = "Barangsiapa yang menjaga shalat, ia akan menjadi cahaya, bukti, dan keselamatan di hari kiamat."
        ),
        Mahfudzot(
            arabic = "ربّنا لا تُزغ قلوبنا بعد إذ هديتنا",
            meaning = "Ya Tuhan kami, janganlah Engkau sesatkan hati kami setelah Engkau memberi petunjuk."
        ),
        Mahfudzot(
            arabic = "اللهمّ إنّي أسألك علماً نافعاً ورزقاً طيّباً وعملاً متقبّلاً",
            meaning = "Ya Allah, aku memohon kepada-Mu ilmu yang bermanfaat, rizki yang baik, dan amal yang diterima."
        ),

        // ===== PERBEDAAN PENDAPAT (Differences of Opinion) =====
        Mahfudzot(
            arabic = "اختلاف الأمة رحمة",
            meaning = "Perbedaan pendapat umat adalah rahmat."
        ),
        Mahfudzot(
            arabic = "لا تنازعوا فتفشلوا وتذهب ريحكم",
            meaning = "Jangan bertengkar karena kalian akan lemah dan pengaruhmu akan hilang."
        ),
        Mahfudzot(
            arabic = "واعتصموا بحبل الله جميعاً ولا تفرّقوا",
            meaning = "Berpeganglah kalian semuanya pada tali Allah dan jangan bercerai-berai."
        ),
        Mahfudzot(
            arabic = "المؤمن للمؤمن كالبنيان يشدّ بعضه بعضاً",
            meaning = "Orang mukmin terhadap mukmin lainnya seperti satu bangunan yang saling menguatkan."
        ),
        Mahfudzot(
            arabic = "لا تباغضوا ولا تحاسدوا ولا تناجشوا",
            meaning = "Jangan saling membenci, saling hasud, dan saling menipu dalam jual beli."
        ),

        // ===== KELUARGA (Family) =====
        Mahfudzot(
            arabic = "خيركم خيركم لأهله وأنا خيركم لأهلي",
            meaning = "Sebaik-baik kalian adalah yang paling baik terhadap keluarganya, dan aku adalah yang paling baik terhadap keluargaku."
        ),
        Mahfudzot(
            arabic = "إذا مات الإنسان انقطع عمله إلا من ثلاث: صدقة جارية أو علم يُنتفع به أو ولد صالح يدعو له",
            meaning = "Jika seseorang meninggal, terputuslah amalnya kecuali tiga: sedekah jariyah, ilmu yang bermanfaat, atau anak shalih yang mendoakannya."
        ),
        Mahfudzot(
            arabic = "برّ الوالدين أفضل الأعمال بعد الصلاة",
            meaning = "Berbakti kepada kedua orang tua adalah amal terbaik setelah shalat."
        ),
        Mahfudzot(
            arabic = "رضا الربّ في رضا الوالدين",
            meaning = "Keridhaan Tuhan terletak pada keridhaan kedua orang tua."
        ),
        Mahfudzot(
            arabic = "الجنة تحت أقدام الأمهات",
            meaning = "Surga ada di bawah telapak kaki ibu."
        ),
        Mahfudzot(
            arabic = "إنّ من أبرّ البرّ صلة الرجل أهل ودّ أبيه",
            meaning = "Sesungguhnya silaturahmi yang paling utama adalah menyambung tali persahabatan ayahnya."
        ),
        Mahfudzot(
            arabic = "ليس منّا من لم يرحم صغيرنا ويوقّر كبيرنا",
            meaning = "Bukan termasuk golongan kami orang yang tidak menyayangi yang muda dan menghormati yang tua."
        ),
        Mahfudzot(
            arabic = "من عالَ جاريتين دخلاه الجنة",
            meaning = "Barangsiapa memelihara dua anak perempuan (dengan baik), ia akan masuk surga."
        ),

        Mahfudzot(
            arabic = "لا يشمّ رائحة الجنة من أثّر في قلبه شيء من الكبر",
            meaning = "Tidak akan mencium bau surga orang yang di hatinya ada sedikit pun kesombongan."
        ),
        Mahfudzot(
            arabic = "المؤمن قيّد نفسه",
            meaning = "Orang mukmin mengikat (membatasi) dirinya sendiri."
        ),
        Mahfudzot(
            arabic = "من يأمن البلاء لا يأمن العاقبة",
            meaning = "Barangsiapa merasa aman dari ujian, ia tidak akan aman dari akibatnya."
        ),
        Mahfudzot(
            arabic = "لا يزال العبد يُسأل يوم القيامة حتى يُسأل عن أربع",
            meaning = "Seorang hamba terus ditanya di hari kiamat hingga ia ditanya tentang empat hal."
        ),

        // ===== KEBERANIAN & KETEGUHAN (Courage & Fortitude) =====
        Mahfudzot(
            arabic = "من خافَ أدلَجَ ومن أدلَجَ بلغَ المنزل",
            meaning = "Barangsiapa takut, ia berjalan di malam hari, dan barangsiapa berjalan di malam hari, ia akan sampai ke tujuan."
        ),
        Mahfudzot(
            arabic = "لا تضعف ولا تحزن وأنت الأعلون إن كنتم مؤمنين",
            meaning = "Jangan lemah dan jangan bersedih, kamulah yang paling tinggi jika kalian beriman."
        ),
        Mahfudzot(
            arabic = "إن ينصركم الله فلا غالب لكم",
            meaning = "Jika Allah menolong kalian, tidak ada yang bisa mengalahkan kalian."
        ),
        Mahfudzot(
            arabic = "كم من جيشٍ قليل غلب جيشاً كثيراً بإذن الله",
            meaning = "Betapa banyak pasukan kecil yang mengalahkan pasukan besar dengan izin Allah."
        ),
        Mahfudzot(
            arabic = "إن النصر مع الصبر",
            meaning = "Sesungguhnya kemenangan itu bersama kesabaran."
        ),
        Mahfudzot(
            arabic = "الشجاع لا يهرب من الموت",
            meaning = "Orang yang berani tidak lari dari kematian."
        ),

        // ===== KECERDASAN & KEARIFAN (Intelligence & Wisdom) =====
        Mahfudzot(
            arabic = "العاقل لا يعجب من شيء",
            meaning = "Orang yang berakal tidak akan heran pada sesuatu."
        ),
        Mahfudzot(
            arabic = "من عرف قدر نفسه عرف قدر غيره",
            meaning = "Barangsiapa mengenal nilai dirinya, ia akan mengenal nilai orang lain."
        ),
        Mahfudzot(
            arabic = "إذا رأيت الناس مُسالمين فسلّم وإذا رأيتهم مُحاربين فاحترس",
            meaning = "Jika kamu melihat orang damai, maka berdamailah; jika kamu melihat mereka berperang, maka waspadalah."
        ),
        Mahfudzot(
            arabic = "لا يفلح الساحر إذا جاء الساحر",
            meaning = "Tukang sihir tidak akan berhasil jika datang tukang sihir lain."
        ),
        Mahfudzot(
            arabic = "فوق كل ذي علم عليم",
            meaning = "Di atas setiap orang yang berilmu, ada yang lebih mengetahui."
        ),
        Mahfudzot(
            arabic = "قليل الدراهم يمنع كثير المآثم",
            meaning = "Sedikit harta mencegah banyak dosa."
        ),

        // ===== PERGAULAN (Social Relations) =====
        Mahfudzot(
            arabic = "وَصُولَةُ الرحم مِفْتاحُ الجنة",
            meaning = "Menyambung silaturahmi adalah kunci surga."
        ),
        Mahfudzot(
            arabic = "من أحبّ أن يُبسط له في رزقه ويُنسأ له في أثره فليصل رحمه",
            meaning = "Barangsiapa ingin dilapangkan rizkinya dan dipanjangkan umurnya, hendaklah ia menyambung silaturahmi."
        ),
        Mahfudzot(
            arabic = "لا تدخلوا الجنة حتى تؤمنوا ولا تؤمنوا حتى تحابّوا",
            meaning = "Kalian tidak masuk surga hingga beriman, dan kalian tidak beriman hingga saling mencintai."
        ),
        Mahfudzot(
            arabic = "أفشوا السلام بينكم",
            meaning = "Sebarkanlah salam di antara kalian."
        ),
        Mahfudzot(
            arabic = "لا تحاسدوا ولا تنافسوا ولا تباغضوا",
            meaning = "Jangan saling hasud, saling menyaingi dalam keburukan, dan saling membenci."
        ),
        Mahfudzot(
            arabic = "المؤمن مرآة المؤمن",
            meaning = "Orang mukmin adalah cermin bagi mukmin lainnya."
        ),
        Mahfudzot(
            arabic = "لا يغتاب بعضكم بعضاً",
            meaning = "Janganlah sebagian kalian menggunjing sebagian yang lain."
        ),
        Mahfudzot(
            arabic = "أيّكم أحبّ إلىّ؟ قالوا: الله ورسوله أعلم، قال: أكثرهم سلاماً على الناس",
            meaning = "Siapa di antara kalian yang paling aku cintai? Mereka menjawab: Allah dan Rasul-Nya lebih tahu. Beliau bersabda: Yang paling banyak memberi salam."
        ),

        Mahfudzot(
            arabic = "أفلا أدلّكم على شيء إذا فعلتموه تحاببتم؟ أفشوا السلام بينكم",
            meaning = "Maukah kuku tunjukkan sesuatu yang jika dilakukan kalian akan saling mencintai? Sebarkanlah salam."
        ),
        Mahfudzot(
            arabic = "من أحبّ لأخيه ما يحب لنفسه فقد استكمل الإيمان",
            meaning = "Barangsiapa mencintai saudaranya sebagaimana mencintai dirinya sendiri, ia telah menyempurnakan iman."
        ),
        Mahfudzot(
            arabic = "لا تبغضوا ولا تحاسدوا وتكونوا عباد الله إخواناً",
            meaning = "Jangan saling membenci dan hasud, dan jadilah kalian hamba-hamba Allah yang bersaudara."
        ),

        // ===== ZIKIR & IBADAH (Dhikr & Worship) =====
        Mahfudzot(
            arabic = "ألا أنبّئكم بخير أعمالكم وأنزاه عند مليككم وأرفع في درجاتكم وخير لكم من إنفاق الذهب والورق؟",
            meaning = "Maukah kuberitahu amal terbaik kalian yang paling suci di sisi Raja kalian dan paling meninggikan derajat kalian serta lebih baik dari menginfakkan emas dan perak?"
        ),
        Mahfudzot(
            arabic = "ذكر الله",
            meaning = "Yaitu zikir kepada Allah."
        ),
        Mahfudzot(
            arabic = "مَنْ ذَكَرَني في نفسه ذكرتُه في نفسي ومن ذكرني في ملأ ذكرتُه في ملأ خير منهم",
            meaning = "Barangsiapa mengingat-Ku dalam dirinya, Aku akan mengingatnya dalam diri-Ku. Barangsiapa mengingat-Ku di tengah khalayak, Aku akan mengingatnya di tengah khalayak yang lebih baik."
        ),
        Mahfudzot(
            arabic = "مثل الذي يذكر ربّه والذي لا يذكر ربّه مثل الحيّ والميت",
            meaning = "Perumpamaan orang yang mengingat Tuhannya dengan yang tidak mengingat-Nya, bagaikan orang hidup dan orang mati."
        ),
        Mahfudzot(
            arabic = "التطوّع خير من النوم",
            meaning = "Amal sunnah lebih baik dari tidur."
        ),
        Mahfudzot(
            arabic = "صلاة الليل سرّ الأبرار",
            meaning = "Shalat malam adalah rahasia orang-orang shalih."
        ),
        Mahfudzot(
            arabic = "قيام الليل ركعتان خير من الدنيا وما فيها",
            meaning = "Shalat malam dua rakaat lebih baik dari dunia dan seisinya."
        ),

        Mahfudzot(
            arabic = "من عمل عملاً ليس عليه أمرنا فهو ردّ",
            meaning = "Barangsiapa beramal dengan amal yang tidak sesuai perintah kami, maka amalnya ditolak."
        ),
        Mahfudzot(
            arabic = "من سمع بنا ولم يُصدّقنا فليس منّا",
            meaning = "Barangsiapa mendengar tentang kami tapi tidak membenarkan kami, ia bukan dari golongan kami."
        ),
        Mahfudzot(
            arabic = "الدين النصيحة",
            meaning = "Agama itu adalah nasihat."
        ),
        Mahfudzot(
            arabic = "من أخلص لله أربعين صباحاً ظهرت ينابيع الحكمة من قلبه على لسانه",
            meaning = "Barangsiapa ikhlas kepada Allah selama empat puluh hari, mata air kebijaksanaan akan memancar dari hatinya ke lisannya."
        ),

        // ===== PERINGATAN & NASIHAT (Admonition & Advice) =====
        Mahfudzot(
            arabic = "لا تكونوا إمّعةً تقولون إن أحسن الناس أحسنا وإن ظلموا ظلمنا",
            meaning = "Janganlah kalian menjadi pengikut yang berkata jika orang berbuat baik kita ikut baik dan jika mereka zalim kita ikut zalim."
        ),
        Mahfudzot(
            arabic = "من لا يرحم لا يُرحم",
            meaning = "Barangsiapa tidak menyayangi, ia tidak akan disayangi."
        ),
        Mahfudzot(
            arabic = "ارحموا من في الأرض يرحمكم من في السماء",
            meaning = "Sayangilah penghuni bumi, niscaya penghuni langit akan menyayangi kalian."
        ),
        Mahfudzot(
            arabic = "الراحمون يرحمهم الرحمن",
            meaning = "Orang-orang yang penyayang akan disayangi oleh Ar-Rahman."
        ),
        Mahfudzot(
            arabic = "خيركم من يُرجى خيره ويُؤمن شرّه",
            meaning = "Sebaik-baik kalian adalah yang diharapkan kebaikannya dan diamankan keburukannya."
        ),
        Mahfudzot(
            arabic = "إذا أراد الله بعبد خيراً عَجّل له العقوبة في الدنيا",
            meaning = "Jika Allah menghendaki kebaikan pada seorang hamba, Allah menyegerakan hukumannya di dunia."
        ),
        Mahfudzot(
            arabic = "من استقام استقام له كل شيء",
            meaning = "Barangsiapa yang lurus (istiqamah), maka segala urusannya akan menjadi lurus."
        ),
        Mahfudzot(
            arabic = "الاستقامة على الطريقة أعظم من كثرة العمل",
            meaning = "Istiqamah di atas jalan lebih agung dari banyaknya amal."
        ),
        Mahfudzot(
            arabic = "من استغنى أغناه الله ومن استعفّ أعفّه الله",
            meaning = "Barangsiapa merasa cukup, Allah akan mencukupkannya, dan barangsiapa menjaga diri, Allah akan menjaganya."
        ),

        // ===== PERIBAHASA ARAB UMUM (General Arabic Proverbs) =====
        Mahfudzot(
            arabic = "العين بصيرة واليد قصيرة",
            meaning = "Mata melihat tapi tangan tidak sampai (ingin tetapi tidak mampu)."
        ),
        Mahfudzot(
            arabic = "الجار قبل الدار",
            meaning = "Tetangga lebih utama dari rumah (pilih tetangga baik sebelum memilih rumah)."
        ),
        Mahfudzot(
            arabic = "الوقت أنفس من الذهب والفضة",
            meaning = "Waktu lebih berharga dari emas dan perak."
        ),
        Mahfudzot(
            arabic = "الدرّة تنفق في محلّها",
            meaning = "Permata (nasihat) bermanfaat pada tempatnya."
        ),
        Mahfudzot(
            arabic = "من حفر حفرة لأخيه وقع فيها",
            meaning = "Barangsiapa menggali lubang untuk saudaranya, ia akan jatuh ke dalamnya."
        ),
        Mahfudzot(
            arabic = "القرد في عين أمّه غزال",
            meaning = "Kera di mata ibunya adalah rusa (setiap orang menganggap anaknya yang terbaik)."
        ),
        Mahfudzot(
            arabic = "من شبّ على شيء شاب عليه",
            meaning = "Barangsiapa dibesarkan atas sesuatu, ia akan terus meneruskannya sampai tua."
        ),
        Mahfudzot(
            arabic = "اليد الواحدة لا تصفق",
            meaning = "Satu tangan tidak bisa bertepuk tangan (butuh kerja sama)."
        ),
        Mahfudzot(
            arabic = "ركلتني فبكيت فجاء من رآني وقال لا تعاتب من ركلك بل عاتب من أمرك بركلي",
            meaning = "Dipukul lalu menangis, datanglah orang yang melihat dan berkata: jangan marah pada yang memukulmu, tapi marahlah pada yang menyuruhnya."
        ),
        Mahfudzot(
            arabic = "لا تاج إلا العلم",
            meaning = "Tidak ada mahkota selain ilmu."
        ),
        Mahfudzot(
            arabic = "من حفظ الشباب حفظ الدين",
            meaning = "Barangsiapa yang menjaga masa mudanya, ia menjaga agamanya."
        ),
        Mahfudzot(
            arabic = "الناس أعداء ما جهلوا",
            meaning = "Manusia itu musuh terhadap apa yang tidak mereka ketahui."
        ),
        Mahfudzot(
            arabic = "رب أخ لك لم تلده أمّك",
            meaning = "Betapa banyak saudara yang bukan lahir dari ibumu yang sama."
        ),
        Mahfudzot(
            arabic = "الآخرة خير وأبقى",
            meaning = "Akhirat itu lebih baik dan lebih kekal."
        ),
        Mahfudzot(
            arabic = "دار الدنيا دار مَمَرّ لا دار مَقَرّ",
            meaning = "Dunia ini adalah tempat singgah, bukan tempat tinggal."
        ),
        Mahfudzot(
            arabic = "ما أطال عبدٌ الأمل إلا أساء العمل",
            meaning = "Tidaklah seorang hamba memperpanjang angan-angan kecuali ia memperburuk amalnya."
        ),
        Mahfudzot(
            arabic = "من حاسب نفسه قبل أن يُحاسب خفّ حسابه",
            meaning = "Barangsiapa menghisab dirinya sebelum dihisab, ringanlah hisabnya."
        ),
        Mahfudzot(
            arabic = "الحكمة ضالّة المؤمن أنّى وجدها فهو أحقّ بها",
            meaning = "Kebijaksanaan adalah barang hilang orang mukmin, di mana saja ia menemukannya, dialah yang paling berhak."
        ),
        Mahfudzot(
            arabic = "من عاشر السعداء سعد ومن عاشر الأشقياء شقي",
            meaning = "Barangsiapa bergaul dengan orang beruntung, ia akan beruntung, dan bergaul dengan orang sengsara, ia akan sengsara."
        ),
        Mahfudzot(
            arabic = "لا تكن ممن يضحك وهو جائع ويبكي وهو شبعان",
            meaning = "Jangan menjadi orang yang tertawa saat lapar dan menangis saat kenyang."
        ),
        Mahfudzot(
            arabic = "من رام العلا سهر الليالي",
            meaning = "Barangsiapa menginginkan kemuliaan, ia akan mengorbankan malam-malamnya."
        ),
        Mahfudzot(
            arabic = "الجود بالنفس أبلغ من الجود بالمال",
            meaning = "Kedermawanan dengan jiwa lebih bermakna dari kedermawanan dengan harta."
        ),
        Mahfudzot(
            arabic = "لا خير في لذةٍ بعدها نار",
            meaning = "Tidak ada kebaikan dalam kesenangan yang diikuti neraka."
        ),
        Mahfudzot(
            arabic = "لا شرّ في بلوىٍ بعدها الجنة",
            meaning = "Tidak ada keburukan dalam ujian yang diikuti surga."
        ),
        Mahfudzot(
            arabic = "الدنيا ساعة فاجعلها طاعة لله",
            meaning = "Dunia hanyalah sebentar, jadikanlah ia untuk ketaatan kepada Allah."
        ),
        Mahfudzot(
            arabic = "العلم يرفع بيتاً لا عماد له والجهل يهدم بيت العزّ والكرم",
            meaning = "Ilmu meninggikan rumah yang tak bertiang, kebodohan meruntuhkan rumah kemuliaan dan kemurahan."
        ),
        Mahfudzot(
            arabic = "من لم يذق مرّ التعلم ساعة تجرّع ذلّ الجهل عمراً",
            meaning = "Barangsiapa tidak merasakan pahitnya belajar sesaat, ia akan menelan kehinaan kebodohan seumur hidup."
        ),
        Mahfudzot(
            arabic = "الصدق منجاة والكذب مهلكة",
            meaning = "Kejujuran adalah keselamatan dan kedustaan adalah kebinasaan."
        ),
        Mahfudzot(
            arabic = "من استكثر من شيء قلّت قيمته",
            meaning = "Barangsiapa berlebihan terhadap sesuatu, nilai sesuatu itu akan berkurang."
        ),
        Mahfudzot(
            arabic = "خير الناس أتقاهم لله",
            meaning = "Sebaik-baik manusia adalah yang paling bertakwa kepada Allah."
        ),
        Mahfudzot(
            arabic = "إن مع العسر يسراً إن مع العسر يسراً",
            meaning = "Sesungguhnya bersama kesulitan ada kemudahan, sesungguhnya bersama kesulitan ada kemudahan."
        ),
        Mahfudzot(
            arabic = "الهمّ نصف الهرم",
            meaning = "Kecemasan itu separuh dari kepikunan."
        ),
        Mahfudzot(
            arabic = "أخوك من صدقك لا أخوك من صدّقك",
            meaning = "Saudaramu adalah yang jujur kepadamu, bukan yang hanya membenarkanmu."
        ),
        Mahfudzot(
            arabic = "ما نال العلا من قعد عن الطّلب",
            meaning = "Tidak akan meraih kemuliaan orang yang duduk dari pengejaran."
        ),
        Mahfudzot(
            arabic = "لا يسمع الدعاء من قلب غافل لاهٍ",
            meaning = "Doa dari hati yang lalai tidak akan didengar."
        ),
        Mahfudzot(
            arabic = "إنّما يُخشى اللهَ من عباده العلماءُ",
            meaning = "Sesungguhnya yang takut kepada Allah di antara hamba-hamba-Nya adalah para ulama."
        ),
        Mahfudzot(
            arabic = "من استقام فقد فاز",
            meaning = "Barangsiapa yang istiqamah (teguh), ia telah beruntung."
        ),
        Mahfudzot(
            arabic = "خير الكلام ما قلّ ودلّ وخير العطاء ما جاء من غير مَنّ",
            meaning = "Sebaik-baik ucapan adalah yang sedikit namun bermakna dan sebaik-baik pemberian adalah yang datang tanpa meminta imbalan."
        ),
        Mahfudzot(
            arabic = "من لم يذق مرارة الصبر لم ينل حلاوة الأجر",
            meaning = "Barangsiapa tidak merasakan pahitnya kesabaran, ia tidak akan mendapatkan manisnya pahala."
        ),
        Mahfudzot(
            arabic = "عوّد نفسك الخير فإنّ النفس عوّادة",
            meaning = "Biasakan dirimu pada kebaikan, karena jiwa itu suka pada kebiasaan."
        ),
        Mahfudzot(
            arabic = "لا تكن ممّن يُبكي على اللبن بعد انقلاب القدح",
            meaning = "Jangan menjadi orang yang menangisi susu setelah gelas terbalik (menyesali setelah terlambat)."
        ),
        Mahfudzot(
            arabic = "الشفق على الصغار رحمة بالكبار",
            meaning = "Kasih sayang kepada yang kecil adalah kebaikan bagi yang besar."
        ),
        Mahfudzot(
            arabic = "من سلك الجادة سهل عليه الطريق",
            meaning = "Barangsiapa menempuh jalan yang lurus, mudahlah baginya perjalanannya."
        ),
        Mahfudzot(
            arabic = "إذا هبّت رياحك فاغتنمها",
            meaning = "Jika angin kebaikanmu bertiup, pergunakanlah sebaik-baiknya."
        ),
        Mahfudzot(
            arabic = "من اعتاد الخير حبّ إليه",
            meaning = "Barangsiapa terbiasa berbuat kebaikan, ia akan mencintainya."
        ),
        Mahfudzot(
            arabic = "العلم خزائنه في الصدور",
            meaning = "Ilmu itu perbendaharaannya ada di dalam dada."
        ),
        Mahfudzot(
            arabic = "لا يصلح آخر هذه الأمة إلا بما صلح أوّلها",
            meaning = "Umat ini di akhir zaman tidak akan baik kecuali dengan apa yang membaikkan mereka di awal."
        ),
        Mahfudzot(
            arabic = "المرء يطوى على ما كان يعمل",
            meaning = "Seseorang akan dilipat sesuai dengan apa yang ia kerjakan."
        ),
        Mahfudzot(
            arabic = "من كان في شيء كان فيه",
            meaning = "Barangsiapa sibuk dengan sesuatu, ia akan tercurah padanya."
        ),
        Mahfudzot(
            arabic = "إن الحكم بالعدل من الإيمان",
            meaning = "Sesungguhnya memutuskan dengan adil termasuk dari iman."
        ),
        Mahfudzot(
            arabic = "من عادى أولياء الله فقد آذنه بالحرب",
            meaning = "Barangsiapa memusuhi wali-wali Allah, sesungguhnya ia telah menyatakan perang terhadap Allah."
        ),
        Mahfudzot(
            arabic = "إن الله لا ينظر إلى صوركم وأجسادكم ولكن ينظر إلى قلوبكم وأعمالكم",
            meaning = "Sesungguhnya Allah tidak melihat rupa dan jasad kalian, tetapi Dia melihat hati dan amal kalian."
        ),
        Mahfudzot(
            arabic = "تفكّر ساعة خير من عبادة ستين سنة",
            meaning = "Berpikir sesaat lebih baik dari beribadah enam puluh tahun."
        ),
        Mahfudzot(
            arabic = "العلم يُستفاد ولا يُورَث",
            meaning = "Ilmu itu diperoleh dengan belajar, bukan diwariskan."
        ),
        Mahfudzot(
            arabic = "لا يُفتننّ بالدنيا فإنها غرّارة",
            meaning = "Janganlah terpedaya oleh dunia karena dunia itu sangat menipu."
        ),
        Mahfudzot(
            arabic = "من وجد الله وجد كل شيء ومن فقد الله فقد كل شيء",
            meaning = "Barangsiapa menemukan Allah, ia menemukan segalanya, dan barangsiapa kehilangan Allah, ia kehilangan segalanya."
        ),
        Mahfudzot(
            arabic = "بقدر التعلّق بالدنيا يكون العمى عن الآخرة",
            meaning = "Sesuai dengan keterikatan pada dunia, demikianlah kebutaan terhadap akhirat."
        ),
        Mahfudzot(
            arabic = "إنّما بُعثت لأتمّم مكارم الأخلاق",
            meaning = "Sesungguhnya aku diutus untuk menyempurnakan akhlak yang mulia."
        ),
        Mahfudzot(
            arabic = "لا تغتمّ فإن الغمّ لا يُنجّي من القدر",
            meaning = "Janganlah berduka cita, karena kesedihan tidak akan menyelamatkan dari takdir."
        ),
        Mahfudzot(
            arabic = "من عرف الدنيا حقّ معرفتها زهد فيها",
            meaning = "Barangsiapa mengenal dunia dengan sebenarnya, ia akan zuhud terhadapnya."
        ),
        Mahfudzot(
            arabic = "الناس نيام فإذا ماتوا انتبهوا",
            meaning = "Manusia itu tidur, dan ketika mereka mati, mereka terbangun."
        ),
        Mahfudzot(
            arabic = "ساعة أنس مع الله خير من ألف ساعة مع الناس",
            meaning = "Satu saat kebersamaan dengan Allah lebih baik dari seribu saat bersama manusia."
        ),
        Mahfudzot(
            arabic = "الراحة للقلب في قلة الرفقة",
            meaning = "Ketenangan hati terletak pada sedikitnya teman (karena berkualitas)."
        ),
        Mahfudzot(
            arabic = "من عرف ربّه استغنى عمّا سواه",
            meaning = "Barangsiapa mengenal Tuhannya, ia akan merasa cukup dari selain-Nya."
        ),
        Mahfudzot(
            arabic = "اللهمّ أرنا الحقّ حقّاً وارزقنا اتّباعه وأرنا الباطل باطلاً وارزقنا اجتنابه",
            meaning = "Ya Allah, tunjukkanlah kebenaran itu benar dan berikan kami petunjuk untuk mengikutinya, tunjukkanlah kebatilan itu batil dan berikan kami petunjuk untuk menjauhinya."
        ),
        Mahfudzot(
            arabic = "من أحبّ أن يكون أقوى الناس فليتوكّل على الله",
            meaning = "Barangsiapa ingin menjadi orang yang paling kuat, hendaklah ia bertawakkal kepada Allah."
        ),
        Mahfudzot(
            arabic = "في الصبر واليقين وحسن التوكل على الله عوّض الله خيراً مما أخذ",
            meaning = "Dengan kesabaran, keyakinan, dan tawakkal yang baik kepada Allah, Allah akan mengganti dengan yang lebih baik dari apa yang diambil."
        ),
        Mahfudzot(
            arabic = "لا تترك نفسك فريسة للأفكار السوداء",
            meaning = "Jangan biarkan dirimu menjadi mangsa pikiran-pikiran gelap."
        ),
        Mahfudzot(
            arabic = "العبد بين نعمة وبلية فلا يركن إلى النعمة ولا ييأس من البلية",
            meaning = "Seorang hamba berada antara nikmat dan ujian, jangan bergantung pada nikmat dan jangan berputus asa dari ujian."
        ),
        Mahfudzot(
            arabic = "من تأنّى ظفر",
            meaning = "Barangsiapa bersabar (tidak terburu-buru), ia akan berhasil."
        ),
        Mahfudzot(
            arabic = "من استشار الرجال شاركها في عقولها",
            meaning = "Barangsiapa berkonsultasi dengan orang-orang (bijak), ia berbagi akal dengan mereka."
        ),
        Mahfudzot(
            arabic = "من لانت كلمته وجبت محبّته",
            meaning = "Barangsiapa lembut ucapannya, pasti ia dicintai."
        ),
        Mahfudzot(
            arabic = "إن الرفق لا يكون في شيء إلا زانه",
            meaning = "Sesungguhnya kelembutan tidak ada pada suatu hal kecuali ia memperindahnya."
        ),
        Mahfudzot(
            arabic = "من قلّ ذلّ ومن كثر مَلّ",
            meaning = "Barangsiapa sedikit (perkataannya) ia akan dihormati, dan barangsiapa banyak ia akan dibenci."
        ),
        Mahfudzot(
            arabic = "الصبر ضياء واليأس ظلام",
            meaning = "Kesabaran adalah cahaya dan putus asa adalah kegelapan."
        ),
        Mahfudzot(
            arabic = "من لا يصبر على ذلّ التعليم بقي عمره في عماية الجهل",
            meaning = "Barangsiapa tidak sabar terhadap hinaan dalam belajar, ia akan hidup dalam kegelapan kebodohan."
        ),
        Mahfudzot(
            arabic = "خير الناس من نفع الناس",
            meaning = "Sebaik-baik manusia adalah yang bermanfaat bagi manusia lain."
        ),
        Mahfudzot(
            arabic = "أفضل العبادة أداء الفرائض ثم المحافظة على السنن ثم كثرة النوافل",
            meaning = "Ibadah terbaik adalah menunaikan kewajiban, kemudian menjaga sunnah, lalu memperbanyak amal sunnah."
        ),
        Mahfudzot(
            arabic = "من كثر كلامه كثر سقطه ومن قلّ كلامه قلّ سقطه",
            meaning = "Barangsiapa banyak bicara, banyaklah kesalahannya, dan barangsiapa sedikit bicara, sedikitlah kesalahannya."
        ),
        Mahfudzot(
            arabic = "من نظر في عيب نفسه اشتغل عن عيب غيره",
            meaning = "Barangsiapa memperhatikan aib dirinya, ia akan sibuk dari aib orang lain."
        ),
        Mahfudzot(
            arabic = "لا تذمّمنّ أحداً حتى تمشي مكانه",
            meaning = "Jangan mencela siapa pun sebelum kamu berada di posisinya."
        ),
        Mahfudzot(
            arabic = "الساكت عن الحق شيطان أخرس",
            meaning = "Orang yang diam dari kebenaran adalah setan yang bisu."
        ),
        Mahfudzot(
            arabic = "إنّ لله عباداً اختصّهم بقضاء حوائج الناس",
            meaning = "Sesungguhnya Allah memiliki hamba-hamba yang Ia khususkan untuk memenuhi kebutuhan manusia."
        ),
        Mahfudzot(
            arabic = "من سعادة ابن آدم استخارة الله ومن سعادة ابن آدم رضاه بما قضى الله",
            meaning = "Dari kebahagiaan anak Adam adalah memohon petunjuk (istikharah) kepada Allah dan ridha dengan takdir-Nya."
        ),
        Mahfudzot(
            arabic = "جالسوا أهل الصلاح تشبهوا بهم",
            meaning = "Bergaulah dengan orang-orang shalih, niscaya kalian akan menyerupai mereka."
        ),
        Mahfudzot(
            arabic = "لا يكوننّ أحدكم إمّعةً",
            meaning = "Janganlah salahseorang di antara kalian menjadi pengikut buta."
        ),
        Mahfudzot(
            arabic = "من عرف الله عزّ وجلّ أحبّه ومن عرف الدنيا زهد فيها",
            meaning = "Barangsiapa mengenal Allah, ia akan mencintai-Nya, dan barangsiapa mengenal dunia, ia akan zuhud terhadapnya."
        ),
        Mahfudzot(
            arabic = "اللهمّ إنّي أعوذ بك من علم لا ينفع ومن قلب لا يخشع ومن نفس لا تشبع ومن دعوة لا يُستجاب لها",
            meaning = "Ya Allah, aku berlindung kepada-Mu dari ilmu yang tidak bermanfaat, hati yang tidak khusyuk, jiwa yang tidak puas, dan doa yang tidak dikabulkan."
        ),
        Mahfudzot(
            arabic = "إذا أردت أن تكون أعظم الناس فكن عند نفسك أصغر الناس",
            meaning = "Jika kamu ingin menjadi orang terbesar, jadilah orang terkecil di mata dirimu sendiri."
        ),
        Mahfudzot(
            arabic = "من اعتدل في معيشته لم يفقر",
            meaning = "Barangsiapa berlaku moderat dalam kehidupannya, ia tidak akan menjadi miskin."
        ),
        Mahfudzot(
            arabic = "لا خير في كنز لا يُنفق ولا في علم لا يُنتفع به",
            meaning = "Tidak ada kebaikan dalam harta yang tidak dibelanjakan dan ilmu yang tidak dimanfaatkan."
        ),
        Mahfudzot(
            arabic = "من رضي بالله رباً رضي الله عنه",
            meaning = "Barangsiapa ridha menjadikan Allah sebagai Tuhannya, Allah akan ridha kepadanya."
        ),
        Mahfudzot(
            arabic = "عيشوا ما شئتم فإنكم ميتون وأحبوا ما شئتم فإنكم مفارقونه",
            meaning = "Hiduplah kalian sekehendak kalian, karena sesungguhnya kalian akan mati, dan cintai apa yang kalian mau, karena kalian akan berpisah dengannya."
        ),
        Mahfudzot(
            arabic = "اعمل بفراغ البال قبل انشغال القلب",
            meaning = "Bekerjalah di waktu hati masih lapang sebelum sibuk."
        ),
        Mahfudzot(
            arabic = "من قام بحقوق الله أدّى الله حقوقه",
            meaning = "Barangsiapa menunaikan hak Allah, Allah akan menunaikan haknya."
        ),
        Mahfudzot(
            arabic = "من خاف الله أخاف الله منه كل شيء",
            meaning = "Barangsiapa takut kepada Allah, Allah akan membuat segala sesuatu takut kepadanya."
        ),
        Mahfudzot(
            arabic = "من لم يخف الله خاف من كل شيء",
            meaning = "Barangsiapa tidak takut kepada Allah, ia akan takut dari segala sesuatu."
        ),
        Mahfudzot(
            arabic = "الهمّ نصف الهرم والكآبة تزيد الهرم",
            meaning = "Kecemasan adalah separuh kepikunan dan kesedihan menambah kepikunan."
        ),
        Mahfudzot(
            arabic = "النية في العمل كالروح في الجسد",
            meaning = "Niat dalam amal bagaikan roh dalam jasad."
        ),
        Mahfudzot(
            arabic = "أقرب ما يكون العبد من ربّه وهو ساجد فأكثروا الدعاء",
            meaning = "Seorang hamba paling dekat dengan Tuhannya saat sujud, maka perbanyaklah doa."
        ),
        Mahfudzot(
            arabic = "إنّما يُبلوَى المؤمن في جسده وماله وأهله",
            meaning = "Sesungguhnya orang mukmin diuji dalam jasad, harta, dan keluarganya."
        ),
        Mahfudzot(
            arabic = "من صبر على البلاء أعطاه الله أجره بغير حساب",
            meaning = "Barangsiapa sabar terhadap ujian, Allah akan memberinya pahala tanpa perhitungan."
        ),
        Mahfudzot(
            arabic = "ربّنا لا تُزغ قلوبنا بعد إذ هديتنا وهب لنا من لدنك رحمة",
            meaning = "Ya Tuhan kami, jangan Engkau sesatkan hati kami setelah Engkau beri petunjuk dan anugerahkanlah rahmat dari sisi-Mu."
        ),
        Mahfudzot(
            arabic = "من استكثر من شيعة قلّت قيمته ومن استقلّ من كثرة قلّت قيمته",
            meaning = "Barangsiapa berlebihan terhadap sesuatu, nilainya berkurang, dan barangsiapa menganggap sedikit dari kebanyakan, nilainya pun berkurang."
        ),
        Mahfudzot(
            arabic = "لا يصلح العطار ما أفسد الدهر",
            meaning = "Tidak dapat diperbaiki oleh tabib apa yang telah dirusak oleh waktu."
        ),
        Mahfudzot(
            arabic = "إنّ في الجسد مُضغة إذا صلحت صلح الجسد كله وإذا فسدت فسد الجسد كله",
            meaning = "Sesungguhnya di dalam jasad ada segumpal daging, jika baik maka baiklah seluruh jasad, dan jika rusak maka rusaklah seluruh jasad."
        ),
        Mahfudzot(
            arabic = "من طلب العلا سهر الليالي ومن طلب المعالي تجرّع المشاق",
            meaning = "Barangsiapa mencari kemuliaan, ia akan begadang semalam-malaman, dan yang mencari keutamaan, ia akan menelan kesulitan."
        ),
        Mahfudzot(
            arabic = "عقّلوا ألسنتكم فإنها تُورث الأزمّة",
            meaning = "Gunakanlah akal atas lisan kalian karena lisan itu menghasilkan belenggu (masalah)."
        ),
        Mahfudzot(
            arabic = "اللهمّ إنّي أسألك الهدى والتقى والعفاف والغنى",
            meaning = "Ya Allah, aku memohon kepada-Mu petunjuk, ketakwaan, kejujuran, dan kecukupan."
        ),
        Mahfudzot(
            arabic = "من كتم سرّه كان الخيار بيده ومن أفشاه كان الأسرار أسراره",
            meaning = "Barangsiapa menyembunyikan rahasianya, ia yang menguasai pilihan, dan yang membocorkannya, rahasia-rahasia akan menjadi milik semua orang."
        ),
        Mahfudzot(
            arabic = "إنّ أولياء الله لا خوفٌ عليهم ولا هم يحزنون",
            meaning = "Sesungguhnya wali-wali Allah tidak ada kekhawatiran pada mereka dan tidak pula mereka bersedih."
        ),
        Mahfudzot(
            arabic = "لا يُؤمن بالله واليوم الآخر من لا يأمن جاره بوائقه",
            meaning = "Tidak beriman kepada Allah dan hari akhir orang yang tetangganya tidak aman dari gangguannya."
        ),
        Mahfudzot(
            arabic = "من أطعم أخاه الطعام وأشبعه وكساه من عُرى كان في أمان من النار",
            meaning = "Barangsiapa memberi makan saudaranya hingga kenyang dan memberinya pakaian, ia aman dari neraka."
        ),
        Mahfudzot(
            arabic = "أفضل الأعمال عند الله أن تدخل على أخيك المؤمن سروراً",
            meaning = "Amal terbaik di sisi Allah adalah memasukkan kegembiraan kepada saudara mukminmu."
        ),
        Mahfudzot(
            arabic = "من سنّ في الإسلام سنّة حسنة فله أجرها وأجر من عمل بها",
            meaning = "Barangsiapa memulai kebaikan dalam Islam, ia mendapat pahalanya dan pahala orang yang mengamalkannya."
        ),
        Mahfudzot(
            arabic = "من سنّ في الإسلام سنّة سيئة كان عليه وزرها ووزر من عمل بها",
            meaning = "Barangsiapa memulai keburukan dalam Islam, ia menanggung dosanya dan dosa orang yang mengamalkannya."
        )
    )

    fun getTodayIndex(): Int {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return dayOfYear % all.size
    }

    fun getTodayMahfudzot(): Mahfudzot {
        return all[getTodayIndex()]
    }
}

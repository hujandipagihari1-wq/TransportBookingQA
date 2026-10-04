package com.industri.transportqa

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class FareCalculatorTest {
    private lateinit var calculator: FareCalculator

    @Before
    fun setUp() {
        calculator = FareCalculator()
    }

    @Test
    fun isValidNik_dengan16digitAngka_harusBernilaiTrue() {
        val validNik = "3209123456780001"
        val result = calculator.isValidNik(validNik)
        assertThat(result).isTrue()
    }

    @Test
    fun isValidNik_denganKurangDari16digit_harusBernilaiFalse() {
        val shortNik = "32091234"
        val result = calculator.isValidNik(shortNik)
        assertThat(result).isFalse()
    }

    @Test
    fun isValidNik_mengandungHuruf_harusBernilaiFalse() {
        val invalidNik = "320912345678ABCD"
        val result = calculator.isValidNik(invalidNik)
        assertThat(result).isFalse()
    }

    @Test
    fun calculateTotalFare_untukPenumpangLansiaUmur65Tahun_mendapatDiskon20persen() {
        val baseFare = 500000.0 // Tarif dasar Rp 500.000
        val total = calculator.calculateTotalFare(baseFare, passengerAge = 65, baggageWeightKg = 15)
        // Rp 500.000 - 20% = Rp 400.000 (Bagasi 15kg gratis)
        assertThat(total).isEqualTo(400000.0)
    }

    @Test
    fun calculateTotalFare_denganKelebihanBagasi25kg_dikenakanDendaBagasi() {
        val baseFare = 500000.0
        val total = calculator.calculateTotalFare(baseFare, passengerAge = 30, baggageWeightKg = 25)
        // Kelebihan: 5 kg x Rp 25.000 = Rp 125.000. Total = Rp 625.000
        assertThat(total).isEqualTo(625000.0)
    }

    // ===================== TUGAS 1: EDGE CASE UNIT TESTING =====================

    @Test
    fun calculateTotalFare_denganTarifDasarMinus_harusMelemparIllegalArgumentException() {
        val baseFareMinus = -10000.0

        try {
            calculator.calculateTotalFare(baseFareMinus, passengerAge = 30, baggageWeightKg = 10)
            // Tidak boleh sampai baris ini: sistem harus menolak tarif dasar negatif
            fail("Harus melempar IllegalArgumentException untuk tarif dasar minus, tetapi tidak ada exception")
        } catch (e: IllegalArgumentException) {
            // Pastikan pesan error sesuai aturan bisnis, bukan exception lain yang kebetulan sama
            assertThat(e).hasMessageThat().contains("Tarif dasar tidak boleh minus")
        }
    }

    @Test
    fun calculateTotalFare_untukBalitaUsia2Tahun_dapatDiskon100persenSehinggaTotalNol() {
        val baseFare = 500000.0

        // Balita (< 3 tahun) diskon 100%, jadi tarif tiket jadi Rp 0 selama bagasi <= 20 kg (gratis)
        val totalTanpaBagasi = calculator.calculateTotalFare(baseFare, passengerAge = 2, baggageWeightKg = 0)
        val totalDiBatasGratis = calculator.calculateTotalFare(baseFare, passengerAge = 2, baggageWeightKg = 20)

        assertThat(totalTanpaBagasi).isEqualTo(0.0)
        assertThat(totalDiBatasGratis).isEqualTo(0.0)
    }

    @Test
    fun calculateTotalFare_untukBalitaDenganKelebihanBagasi_tetapDikenakanDendaBagasi() {
        // Dokumentasi perilaku: diskon 100% hanya berlaku untuk tarif tiket,
        // biaya kelebihan bagasi tetap ditagihkan sehingga total bukan 0.0
        val baseFare = 500000.0
        val total = calculator.calculateTotalFare(baseFare, passengerAge = 2, baggageWeightKg = 25)
        // Ticket Rp 0 + kelebihan bagasi 5 kg x Rp 25.000 = Rp 125.000
        assertThat(total).isEqualTo(125000.0)
    }
}

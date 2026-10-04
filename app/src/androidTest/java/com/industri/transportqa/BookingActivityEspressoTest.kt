package com.industri.transportqa

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingActivityEspressoTest {

    // Luncurkan MainActivity (berperan sbg form booking) secara otomatis sebelum test dijalankan
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isiFormPemesanan_danTekanPesan_harusMenampilkanKonfirmasiTiket() {
        // 1. Ketik NIK Penumpang pada EditText
        onView(withId(R.id.etNik))
            .perform(typeText("3209123456780001"), closeSoftKeyboard())

        // 2. Ketik Nama Penumpang
        onView(withId(R.id.etName))
            .perform(typeText("Ahmad Dahlan"), closeSoftKeyboard())

        // 3. Tekan Tombol Pesan Tiket
        onView(withId(R.id.btnSubmitBooking))
            .perform(click())

        // 4. Verifikasi (Assert) bahwa status konfirmasi sukses muncul di layar
        onView(withId(R.id.tvBookingStatus))
            .check(matches(isDisplayed()))
            .check(matches(withText("Pemesanan Tiket Berhasil Diverifikasi")))
    }

    // ===================== TUGAS 2: NEGATIVE FLOW UI AUTOMATION =====================

    @Test
    fun tekanPesanTanpaMengisiNik_harusMenampilkanPeringatanNikTidakBolehKosong() {
        // 1. Field NIK (etNik) sengaja dibiarkan kosong, form tidak diisi sama sekali
        // 2. Tekan Tombol Pesan Tiket
        onView(withId(R.id.btnSubmitBooking))
            .perform(click())

        // 3. Verifikasi pesan error validasi NIK tampil di layar
        onView(withId(R.id.tvNikError))
            .check(matches(isDisplayed()))
            .check(matches(withText("NIK tidak boleh kosong")))

        // 4. Negative flow: konfirmasi sukses TIDAK boleh muncul saat validasi gagal
        onView(withId(R.id.tvBookingStatus))
            .check(matches(not(isDisplayed())))
            .check(matches(not(withText(containsString("Berhasil Diverifikasi")))))
    }
}

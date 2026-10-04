package com.industri.transportqa

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Form pemesanan tiket. Target pengujian UI Espresso:
 * - Sukses  : NIK + Nama terisi  -> tvBookingStatus menampilkan "Pemesanan Tiket Berhasil Diverifikasi"
 * - Negative: NIK kosong        -> tvNikError menampilkan "NIK tidak boleh kosong"
 */
class MainActivity : AppCompatActivity() {

    private val fareCalculator = FareCalculator()

    private lateinit var etNik: EditText
    private lateinit var etName: EditText
    private lateinit var btnSubmitBooking: Button
    private lateinit var tvNikError: TextView
    private lateinit var tvBookingStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNik = findViewById(R.id.etNik)
        etName = findViewById(R.id.etName)
        btnSubmitBooking = findViewById(R.id.btnSubmitBooking)
        tvNikError = findViewById(R.id.tvNikError)
        tvBookingStatus = findViewById(R.id.tvBookingStatus)

        btnSubmitBooking.setOnClickListener { submitBooking() }
    }

    private fun submitBooking() {
        tvBookingStatus.visibility = View.GONE

        val nik = etNik.text.toString().trim()
        val nama = etName.text.toString().trim()

        if (nik.isEmpty()) {
            showNikError(getString(R.string.error_nik_kosong))
            return
        }
        if (!fareCalculator.isValidNik(nik)) {
            showNikError(getString(R.string.error_nik_tidak_valid))
            return
        }
        if (nama.isEmpty()) {
            showNikError(getString(R.string.error_nama_kosong))
            return
        }

        tvNikError.visibility = View.GONE
        tvBookingStatus.text = getString(R.string.booking_berhasil)
        tvBookingStatus.visibility = View.VISIBLE
    }

    private fun showNikError(pesan: String) {
        tvNikError.text = pesan
        tvNikError.visibility = View.VISIBLE
    }
}

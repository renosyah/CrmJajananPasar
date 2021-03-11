package com.dwi.crmjajananpasar.ui.activity.success

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dwi.crmjajananpasar.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {

    // konteks yang dipakai
    lateinit var context: Context
    
    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@SuccessActivity

        home_button.setOnClickListener {
            finish()
        }
    }
}
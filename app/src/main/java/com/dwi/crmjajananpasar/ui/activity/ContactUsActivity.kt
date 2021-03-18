package com.dwi.crmjajananpasar.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.activity_contact_us.*

class ContactUsActivity : AppCompatActivity() {

    // deklarasi variabel
    lateinit var context: Context

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@ContactUsActivity

        back_imageview.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }

        whatsapp_layout.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+628128728858"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}
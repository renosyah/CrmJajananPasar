package com.dwi.crmjajananpasar.ui.activity.success

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dwi.crmjajananpasar.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {

    // konteks yang dipakai
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        // panggil fungsi init widget
        initWidget();
    }

    private fun initWidget() {
        this.context = this@SuccessActivity

        home_button.setOnClickListener {
            finish()
        }
    }
}
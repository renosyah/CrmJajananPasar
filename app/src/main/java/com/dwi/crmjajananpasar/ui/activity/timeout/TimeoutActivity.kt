package com.dwi.crmjajananpasar.ui.activity.timeout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dwi.crmjajananpasar.R
import kotlinx.android.synthetic.main.activity_timeout.*

class TimeoutActivity : AppCompatActivity() {

    // konteks yang dipakai
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeout)

        // panggil fungsi init widget
        initWidget();
    }

    private fun initWidget() {
        this.context = this@TimeoutActivity

        home_button.setOnClickListener {
            finish()
        }
    }
}
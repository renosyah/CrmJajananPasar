package com.dwi.crmjajananpasar.ui.activity.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivityContract
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivity
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    // konteks yang dipakai
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // panggil fungsi init widget
        initWidget();
    }

    private fun initWidget() {
        this.context = this@ProfileActivity

        back_imageview.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
            name_textview.text = customer.name
            email_textview.text = customer.email
        }

        logout_button.setOnClickListener {
            if (SerializableSave(context, SerializableSave.userDataFileSessionName).delete()){
                startActivity(Intent(context,LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(context, HomeActivity::class.java))
        finish()
    }
}
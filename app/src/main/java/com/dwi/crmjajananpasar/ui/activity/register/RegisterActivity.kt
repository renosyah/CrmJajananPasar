package com.dwi.crmjajananpasar.ui.activity.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivity
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_login.email_edittext
import kotlinx.android.synthetic.main.activity_login.password_edittext
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() , RegisterActivityContract.View {

    @Inject
    lateinit var presenter: RegisterActivityContract.Presenter

    // konteks yang dipakai
    lateinit var context: Context

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // panggil fungsi init widget
        initWidget();
    }

    private fun initWidget() {
        this.context = this@RegisterActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        register_button.setOnClickListener {
            if (username_edittext.text.toString().trim().isEmpty() || email_edittext.text.toString().trim().isEmpty()){
                return@setOnClickListener
            }
            if (password_edittext.text.toString() != confirm_password_edittext.text.toString()){
                return@setOnClickListener
            }
            presenter.register(Customer(0,username_edittext.text.toString(),"",email_edittext.text.toString(),password_edittext.text.toString()),true)
        }

        back_imageview.setOnClickListener {
            startActivity(Intent(context,LoginActivity::class.java))
            finish()
        }

        layout_register_linearlayout.visibility =  View.VISIBLE
        loading_layout.visibility =  View.GONE
    }

    override fun onRegister(customer: Customer) {
        if (SerializableSave(context, SerializableSave.userDataFileSessionName).save(customer)){
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }
    }

    override fun showProgressRegister(show: Boolean) {
        layout_register_linearlayout.visibility = if (show) View.GONE else View.VISIBLE
        loading_layout.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showErrorRegister(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(context,LoginActivity::class.java))
        finish()
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }
}


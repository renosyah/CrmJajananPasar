package com.dwi.crmjajananpasar.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.register.RegisterActivity
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: LoginActivityContract.Presenter
    lateinit var context: Context

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@LoginActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context,SerializableSave.userDataFileSessionName).load() != null){
            startActivity(Intent(context,HomeActivity::class.java))
            finish()
            return
        }

        login_button.setOnClickListener {
            if (email_edittext.text.toString().trim().isEmpty() || password_edittext.text.toString().trim().isEmpty()){
                return@setOnClickListener
            }
            presenter.login(Customer(0,"","",email_edittext.text.toString(),password_edittext.text.toString()),true)
        }

        register_textview.setOnClickListener {
            startActivity(Intent(context,RegisterActivity::class.java))
            finish()
        }

        layout_login_linearlayout.visibility =  View.VISIBLE
        loading_layout.visibility = View.GONE
    }

    override fun onLogin(customer: Customer) {
        if (SerializableSave(context,SerializableSave.userDataFileSessionName).save(customer)){
            startActivity(Intent(context,HomeActivity::class.java))
            finish()
        }
    }

    override fun showProgressLogin(show: Boolean) {
        layout_login_linearlayout.visibility = if (show) View.GONE else View.VISIBLE
        loading_layout.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showErrorLogin(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    // fungsi saat activity
    // dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    // fungsi inject
    // dependensi agar
    // presenter activity dapat
    // digunakan
    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }

}
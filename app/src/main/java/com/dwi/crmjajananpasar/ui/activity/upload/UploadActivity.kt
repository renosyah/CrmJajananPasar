package com.dwi.crmjajananpasar.ui.activity.upload

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.dwi.crmjajananpasar.BuildConfig
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.model.uploadResponse.UploadResponse
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.success.SuccessActivity
import com.dwi.crmjajananpasar.ui.util.ErrorLayout
import com.dwi.crmjajananpasar.ui.util.LoadingLayout
import com.dwi.crmjajananpasar.util.ImageRotation
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class UploadActivity : AppCompatActivity(), UploadActivityContract.View {

    // static variabel
    companion object {
        val CAMERA_REQUEST = 102
        val PICK_IMAGE = 103
    }

    // deklarasi variabel
    @Inject
    lateinit var presenter: UploadActivityContract.Presenter
    lateinit var context: Context
    lateinit var transaction: Transaction
    private val validateTransaction = ValidateTransaction()
    lateinit var uploadFile : ByteArray
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout


    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget(){
        this.context = this@UploadActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("transaction")){
            this.transaction = intent.getSerializableExtra("transaction") as Transaction
            this.validateTransaction.transactionId = transaction.id
        }


        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_upload))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            upload_layout.visibility = View.VISIBLE
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        change_image_button.setOnClickListener {

            val dialog = AlertDialog.Builder(context).create()

            val v = layoutInflater.inflate(R.layout.dialog_choose_upload_from,null)
            val cam : LinearLayout = v.findViewById(R.id.camera_layout)
            cam.setOnClickListener {
                openCamera()
                dialog.dismiss()
            }

            val gal : LinearLayout = v.findViewById(R.id.gallery_layout)
            gal.setOnClickListener {
                openGalery()
                dialog.dismiss()
            }

            if (dialog.window != null)  dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setView(v)
            dialog.setCancelable(false)
            dialog.show()
        }
        send_image_button.setOnClickListener {
            if (this::uploadFile.isInitialized) {
                uploadImage(uploadFile)
            }
        }
        send_image_button.visibility = View.GONE

        back_imageview.setOnClickListener {
            onBackPressed()
        }

        change_image_button.performClick()
    }

    // fungsi untuk membuka kamera
    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try { createImageFile() } catch (ignore : IOException) { null }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String

    // fungsi untuk membuat file gambar
    // semetara
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS",Locale.US).format(System.currentTimeMillis())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).also {
            currentPhotoPath = it.absolutePath
        }
    }

    // fungsi untuk membuka galery
    private fun openGalery(){
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, PICK_IMAGE)
    }

    // fungsi untuk menconvert bitmap ke array byte
    private fun bmpToByteArray(bmp :Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    // fungsi untuk mengupload gambar
    private fun uploadImage(content : ByteArray){
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS",Locale.US).format(System.currentTimeMillis()) + ".jpg"
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), content)
        val file = MultipartBody.Part.createFormData("file",name, requestFile)
        presenter.upload(file,true)
    }

    override fun onUploaded(uploadResponse: UploadResponse) {
        validateTransaction.imageUrl = "${BuildConfig.SERVER_URL}${uploadResponse.url}"
        presenter.addValidateTransaction(validateTransaction,true)
    }

    override fun showProgressUpload(show: Boolean) {
        upload_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.loading_upload))
        loading.setVisibility(show)
    }

    override fun showErrorUpload(e: String) {
        upload_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onValidated() {
        startActivity(Intent(context, SuccessActivity::class.java))
        finish()
    }

    override fun showProgressValidate(show: Boolean) {
        upload_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.loading_upload))
        loading.setVisibility(show)
    }

    override fun showErrorValidate(e: String) {
        upload_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    // fungsi untuk menangkap hasil
    // apakah gambar berhasil diambil
    // lalu di isi ke variabel
    // upload uploadFile
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){

            when (requestCode) {
                PICK_IMAGE -> {
                    if (data != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && data.data != null) {
                            val source = ImageDecoder.createSource(this.contentResolver, data.data!!)
                            val bmp = ImageDecoder.decodeBitmap(source)
                            image_preview_imageview.setImageBitmap(bmp)
                            uploadFile = bmpToByteArray(bmp)

                        } else {
                            val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                            image_preview_imageview.setImageBitmap(bmp)
                            uploadFile = bmpToByteArray(bmp)

                        }

                        upload_layout.visibility = View.VISIBLE
                        send_image_button.visibility = View.VISIBLE
                    }
                }
                CAMERA_REQUEST -> {

                    upload_layout.visibility = View.GONE
                    loading.setMessage(getString(R.string.process))
                    loading.show()

                    BitmapFactory.decodeFile(currentPhotoPath)?.also { bmp ->
                        val matrix = Matrix()
                        matrix.postRotate(90f)
                        val rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width,bmp.height, matrix, true)
                        image_preview_imageview.setImageBitmap(rotatedBitmap)
                        uploadFile = ImageRotation.getStreamByteFromImage(File(currentPhotoPath))

                        upload_layout.visibility = View.VISIBLE
                        send_image_button.visibility = View.VISIBLE
                        loading.hide()
                    }
                }
            }
        }
    }

    // fungsi saat user
    // menekan tombol back
    override fun onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.message_canceling_transaction))
            .setPositiveButton(getString(R.string.yes)){d, which ->
                super.onBackPressed()
                finish()
            }
            .setNegativeButton(getString(R.string.no),null)
            .setCancelable(false)
            .create()
            .show()
        return
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
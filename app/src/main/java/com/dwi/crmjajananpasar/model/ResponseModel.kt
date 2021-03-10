package com.dwi.crmjajananpasar.model

import com.google.gson.annotations.SerializedName

// ini adalah class yg akan digunakan
// untuk menerima response dari api
// untuk symbol T
// akan digunakan oleh salah satu variabel
class ResponseModel<T>(

    // variabel dengan type T adalah variebl yg nantinya bisa
    // di casting menjadi tipe apa saja dan tentu saja
    // dengan nama untuk serialisasi
    @SerializedName("data") var Data: T?,


    // variabel ini digunakan untuk
    // menampung pesan error dari api dan tentu saja
    // dengan nama untuk serialisasi
    @SerializedName("error") var Error: String?
)
package com.dwi.crmjajananpasar.base

// package ini memiliki satu buah file yakni sebuah base template yang akan di gunakan oleh setiap
// activity dan fragment contract demi menjaga konsistensi dari code pada setiap contract terutama
// fungsi2 yg bersifat mandatory.
class BaseContract {

    // interface ini akan diimplement
    // ke class presenter dari activity atau fragment
    interface Presenter<in T> {

        // fungsi yg dipanggil saat inisialisasi
        // activity atau fragment
        fun subscribe()

        // fungsi yg dipanggil saat
        // activity atau fragment
        // di destroy
        fun unsubscribe()

        // fungsi yg dipanggil saat inisialisasi
        // activity atau fragment
        // dengan tambahan parameter view dari activity
        // untuk operasi yg dilakukan
        // pada class presenter
        fun attach(view: T)
    }

    // interface ini akan diimplement
    // ke class view activity atau fragment
    interface View
}
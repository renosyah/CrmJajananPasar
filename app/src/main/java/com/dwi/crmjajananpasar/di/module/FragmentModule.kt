package com.dwi.crmjajananpasar.di.module

import androidx.fragment.app.Fragment
import com.dwi.crmjajananpasar.service.RetrofitService
import dagger.Module
import dagger.Provides

// ini adalah class dimana
// setiap melakukan injecksi
// presenter ke fragment
// maka akan di provide presenter
// untuk aktivity yg bersangkutan
@Module
class FragmentModule(private var fragment: Fragment) {

    // fungsi untuk provide activity
    // dengan nilai balik adalah variabel fragment
    // yg telah diinisialisasi
    @Provides
    fun provideFragment() : Fragment {
        return fragment
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // add for each new fragment
}
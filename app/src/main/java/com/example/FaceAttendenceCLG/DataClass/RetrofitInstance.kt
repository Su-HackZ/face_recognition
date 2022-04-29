package com.example.FaceAttendenceCLG.DataClass

import com.example.FaceAttendenceCLG.DataClass.cons.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


        val api by lazy {
            retrofit.create(NotifactionApi::class.java)
        }


    }
}
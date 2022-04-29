package com.example.FaceAttendenceCLG.DataClass

import com.example.FaceAttendenceCLG.DataClass.cons.Companion.CONSTANT_TYPE
import com.example.FaceAttendenceCLG.DataClass.cons.Companion.SEVER_KEY
import com.squareup.okhttp.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifactionApi {

    @Headers("Authorization: key=$SEVER_KEY","Content-Type:$CONSTANT_TYPE")
    @POST("fcm/send")
    suspend fun PostNotifaction(
        @Body notifactionData: PushNotifactionData

    ):retrofit2.Response<ResponseBody>

}
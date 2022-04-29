package com.example.FaceAttendenceCLG.DataClass

import android.content.Context
import android.content.SharedPreferences

class MyPreference(context: Context) {


    val PREFERENCE_NAME = "FaceAttendence"

    val ROLL_NUMBER= "RollNumber"
    val USER_NAME= "username"
    val URL= "URl"
    val EXAM_NAME= "EXAM_NAME"

    private val pref: SharedPreferences? = null
    private val editor: SharedPreferences.Editor? = null

    val perferenfe =context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)




    fun clear(){
        editor?.clear()
        editor?.commit()
    }



    fun getRollNumber() : String{
        return perferenfe.getString(ROLL_NUMBER,"RollNumber").toString()
    }

    fun setRollNumber(cont :String) {
        val editer = perferenfe.edit()
        editer.putString(ROLL_NUMBER,cont)
        editer.apply()
    }

    fun getusername() : String{
        return perferenfe.getString(USER_NAME,"Name").toString()
    }

    fun setusername(cont :String) {
        val editer = perferenfe.edit()
        editer.putString(USER_NAME,cont)
        editer.apply()
    }





    fun getUrl() : String{
        return perferenfe.getString(URL,"URL").toString()
    }

    fun setUrl(cont :String) {
        val editer = perferenfe.edit()
        editer.putString(URL,cont)
        editer.apply()
    }


    fun getexamNAme() : String{
        return perferenfe.getString(EXAM_NAME,"EXAM_NAME").toString()
    }


    fun setExamName(cont :String) {
        val editer = perferenfe.edit()
        editer.putString(EXAM_NAME,cont)
        editer.apply()
    }


}
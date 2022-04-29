package com.example.FaceAttendenceCLG.DataClass

 data class ExamList (

    var id :String="",
    var title :String =  "",
    var questions :MutableMap<String,Questions> = mutableMapOf()
     )



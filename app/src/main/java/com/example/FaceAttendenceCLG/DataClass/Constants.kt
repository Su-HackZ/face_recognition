package com.example.FaceAttendenceCLG.DataClass

object Constants {

    fun getattendence():ArrayList<AttendenceData>{

        val attendece =ArrayList<AttendenceData>()

        val att = AttendenceData("Date","202022")

        attendece.add(att)
        return getattendence()
    }
}
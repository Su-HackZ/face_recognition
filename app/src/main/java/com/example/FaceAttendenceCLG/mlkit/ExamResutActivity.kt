package com.example.FaceAttendenceCLG.mlkit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.FaceAttendenceCLG.DataClass.ExamList
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.DataClass.ResultData
import com.example.FaceAttendenceCLG.MainActivity
import com.example.FaceAttendenceCLG.databinding.ActivityExamResutBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ExamResutActivity : AppCompatActivity() {

    lateinit var quiz:  ExamList

    private lateinit var binding:ActivityExamResutBinding
    lateinit var  db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamResutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupview()
    }


    fun setupview(){
        val quizdata = intent.getStringExtra("Quiz")
        quiz = Gson().fromJson<ExamList>(quizdata, ExamList::class.java)


        calulateScore()

    }




    private fun calulateScore() {
        db = FirebaseFirestore.getInstance()

        val myPreference = MyPreference(this)
        var score = 0

        for (entry in quiz.questions.entries){
            val question = entry.value
            if (question.answer==question.useranswer){
                score += 10
            }
        }
        binding.totalscore.text = score.toString()







        val resultData = ResultData(myPreference.getusername(),score.toString(),myPreference.getRollNumber())

        db.collection("ExamResult").add(resultData).addOnSuccessListener {
        /*    Toast.makeText(
                this,
                "$Present",
                Toast.LENGTH_SHORT
            ).show()*/
        }

            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error try again",
                    Toast.LENGTH_SHORT
                ).show()        }




    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ExamResutActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
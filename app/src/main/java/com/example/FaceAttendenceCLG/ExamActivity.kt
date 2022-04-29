package com.example.FaceAttendenceCLG

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.FaceAttendenceCLG.Adaptor.firebaseOptionAdapter
import com.example.FaceAttendenceCLG.DataClass.ExamList
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.DataClass.Questions
import com.example.FaceAttendenceCLG.databinding.ActivityExamBinding
import com.example.FaceAttendenceCLG.mlkit.ExamResutActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ExamActivity() : AppCompatActivity() {

    var quizzes : MutableList<ExamList>? = null
    var questions : MutableMap<String, Questions>? = null
    var index =1
    private lateinit var optionAdapter: firebaseOptionAdapter


    private lateinit var binding:ActivityExamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFireStore("exam2")
     val   myPreference = MyPreference(this)

        Glide
            .with(this)
            .load(myPreference.getUrl())
            .centerCrop()
            .circleCrop()
            // .placeholder(R.drawable.imageView)
            .into(binding.imageview);



        btnclick()

    }


    fun setupFireStore( collection:String){

       val fireStore = FirebaseFirestore.getInstance()
       val titles:String = intent.getStringExtra("exam2").toString()

        binding.examtitle.text = titles.toString()


        fireStore.collection(collection)
            .get().addOnSuccessListener {
                quizzes=    it.toObjects(ExamList::class.java)
                questions = quizzes!![0].questions
                bindview()


            }




    }


    fun bindview(){
        binding.Subimt.visibility = View.GONE
        binding.next.visibility = View.GONE
        binding.Previous.visibility = View.GONE
        if (index ==1){
            binding.next.visibility = View.VISIBLE

        }else if ( index == questions!!.size){
            binding.Previous.visibility = View.VISIBLE
            binding.Subimt.visibility = View.VISIBLE

        }else{
            binding.Previous.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE

        }
        val question = questions!!["question$index"]
        question?.let {
            binding.questionTextView.text = it.description
            optionAdapter = firebaseOptionAdapter(this,it)
            binding.optionsRecycleView.layoutManager = LinearLayoutManager(this)
            binding.optionsRecycleView.adapter= optionAdapter
            binding.optionsRecycleView.setHasFixedSize(true)
        }



    }

    fun btnclick() {

        binding.next.setOnClickListener {
            index++
            bindview()
        }
        binding.Previous.setOnClickListener {
            index--
            bindview()
        }

        binding.Subimt.setOnClickListener {

            val intent = Intent(this@ExamActivity, ExamResutActivity::class.java)

            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("Quiz",json)
            startActivity(intent)
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ExamActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}



package com.example.FaceAttendenceCLG

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FaceAttendenceCLG.Adaptor.ExamListAdaptor
import com.example.FaceAttendenceCLG.DataClass.ExamList
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.databinding.ActivityExamListBinding
import com.google.firebase.firestore.FirebaseFirestore

class ExamListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityExamListBinding

    lateinit var  fireStore: FirebaseFirestore

    private var examList = mutableListOf<ExamList>()
    lateinit var adapters: ExamListAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebase("exam2")
        recyclerview()




    }
    fun recyclerview(){
        adapters  = ExamListAdaptor(this@ExamListActivity,examList)
        binding.recycleview.layoutManager = LinearLayoutManager(this@ExamListActivity)
        binding.recycleview.adapter= adapters
        binding.recycleview.setHasFixedSize(true)
    }



    fun firebase( titles: String) {

        fireStore = FirebaseFirestore.getInstance()


        val myPreference = MyPreference(this)

        val collection = fireStore.collection(/*"quizzes"*/titles)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            examList.clear()
            if (querySnapshot != null) {
                examList.addAll(querySnapshot.toObjects(ExamList::class.java))
                adapters.notifyDataSetChanged()
            }
        }


    }
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@ExamListActivity, MainActivity::class.java)
        startActivity(intent)
        finish()


    }




}
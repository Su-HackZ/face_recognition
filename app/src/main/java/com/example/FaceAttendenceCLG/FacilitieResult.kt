package com.example.FaceAttendenceCLG

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FaceAttendenceCLG.Adaptor.FacilitieAdapter
import com.example.FaceAttendenceCLG.Adaptor.ResultDataAdapter
import com.example.FaceAttendenceCLG.DataClass.AttendenceData
import com.example.FaceAttendenceCLG.DataClass.ResultData
import com.example.FaceAttendenceCLG.databinding.ActivityExamResutBinding
import com.example.FaceAttendenceCLG.databinding.ActivityFacilitieResultBinding
import com.google.firebase.firestore.FirebaseFirestore

class FacilitieResult : AppCompatActivity() {

    private lateinit var binding: ActivityFacilitieResultBinding
    private var resultdata = mutableListOf<ResultData>()
    lateinit var adapters: ResultDataAdapter
    lateinit var  fireStore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilitieResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebase("ExamResult")
        recyclerview()



    }



    fun recyclerview(){
        // adapters  = AttendenceAdapter(this@facilitieActivity,attendenceData)
        adapters  = ResultDataAdapter(this@FacilitieResult,resultdata)
        binding.recycleview.layoutManager = LinearLayoutManager(this@FacilitieResult)
        binding.recycleview.adapter= adapters
        binding.recycleview.setHasFixedSize(true)
    }



    fun firebase( titles: String) {

        fireStore = FirebaseFirestore.getInstance()

        val collection = fireStore.collection(/*"quizzes"*/titles)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            resultdata.clear()
            if (querySnapshot != null) {
                resultdata.addAll(querySnapshot.toObjects(ResultData::class.java))
                adapters.notifyDataSetChanged()
            }
        }
    }

}
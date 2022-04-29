package com.example.FaceAttendenceCLG

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FaceAttendenceCLG.Adaptor.AttendenceAdapter
import com.example.FaceAttendenceCLG.Adaptor.FacilitieAdapter
import com.example.FaceAttendenceCLG.DataClass.*
import com.example.FaceAttendenceCLG.databinding.ActivityFacilitieBinding
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class facilitieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacilitieBinding

    lateinit var  db: FirebaseFirestore

    private var attendenceData = mutableListOf<AttendenceData>()
    lateinit var  fireStore: FirebaseFirestore

   // lateinit var adapters: AttendenceAdapter
    lateinit var adapters: FacilitieAdapter

     var subjectname: String? = null
     var subjecttime: String? = null

    val TAG = "pushNotifaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilitieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()


        binding.searchbtn.setOnClickListener {
            upladdate()
        }

        binding.addlecturer.setOnClickListener {
            lectureslist()
        }



        binding.StudentResult.setOnClickListener {
            val intent = Intent(this@facilitieActivity, FacilitieResult::class.java)
            startActivity(intent)
        }


    }



    fun upladdate(){
        var studentname = binding.edittextsearch.text.toString()
        if (studentname.isBlank()){
            Toast.makeText(this,"Roll number  can't be Blank", Toast.LENGTH_SHORT).show()
            return }

        firebase(studentname.toString())
        recyclerview()
    }



    fun recyclerview(){
       // adapters  = AttendenceAdapter(this@facilitieActivity,attendenceData)
        adapters  = FacilitieAdapter(this@facilitieActivity,attendenceData)
        binding.recycleview.layoutManager = LinearLayoutManager(this@facilitieActivity)
        binding.recycleview.adapter= adapters
        binding.recycleview.setHasFixedSize(true)
    }



    fun firebase( titles: String) {

        fireStore = FirebaseFirestore.getInstance()

        val collection = fireStore.collection(/*"quizzes"*/titles)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            attendenceData.clear()
            if (querySnapshot != null) {
                attendenceData.addAll(querySnapshot.toObjects(AttendenceData::class.java))
                adapters.notifyDataSetChanged()
            }
        }
    }


    fun lectureslist(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.addlectures)
        val name = dialog.findViewById(R.id.name) as EditText
        val time = dialog.findViewById(R.id.time) as EditText
        val cancel = dialog.findViewById(R.id.cancel) as ImageView
        val button = dialog.findViewById(R.id.button) as TextView



        button.setOnClickListener {

            subjectname = name.text.toString()
            subjecttime = time.text.toString()
            if (subjectname!!.isBlank()){
                Toast.makeText(this,"Name  can't be Blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (subjecttime!!.isBlank()){
                Toast.makeText(this,"RollNumber  can't be Blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val className = ClassName(subjectname.toString(),subjecttime.toString())

            db.collection("MCA").add(className).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Subject add successful ",
                    Toast.LENGTH_SHORT

                ).show()

                dialog.dismiss()

            }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error try again",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }






    fun sendNotifaction(notifaction:PushNotifactionData) = CoroutineScope(Dispatchers.IO)
        .launch {


            try{


                val responce = RetrofitInstance.api.PostNotifaction(notifaction)

                if(responce.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(responce)}")
                } else {
                    Log.e(TAG, responce.errorBody().toString())
                }

            }catch (e:Exception){

                Log.e(TAG,e.toString())
            }

        }


}
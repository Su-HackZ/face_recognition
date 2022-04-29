package com.example.FaceAttendenceCLG

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FaceAttendenceCLG.Adaptor.AttendenceAdapter
import com.example.FaceAttendenceCLG.Adaptor.subjectlistadapter
import com.example.FaceAttendenceCLG.DataClass.AttendenceData
import com.example.FaceAttendenceCLG.DataClass.ClassName
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.databinding.ActivityAttendenceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*

class AttendenceActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var reference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    lateinit var  db: FirebaseFirestore

    lateinit var rollnumber: String
    lateinit var   sub: String

    private var attendenceData = mutableListOf<AttendenceData>()
    private var subjectdata = mutableListOf<ClassName>()

    lateinit var adapters: AttendenceAdapter
    lateinit var subjectadapters: subjectlistadapter

    private lateinit var binding: ActivityAttendenceBinding


    lateinit var  roll:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
       // reference = FirebaseDatabase.getInstance().getReference("StudentData")

        val mypreference = MyPreference(this)




         roll = mypreference.getRollNumber()


        
        binding.markin.setOnClickListener {
//            val intent = Intent(this@AttendenceActivity, AttendenceMlkit::class.java)
//            startActivity(intent)

//            startActivityForResult(intent,220)
           showDialog()
        }



        binding.appesent.setOnClickListener {
            markinattendence("A","absent"," ")

        }

        val getdata  = intent.getStringExtra("sucess")
         sub  = intent.getStringExtra("name").toString()

        if (getdata.equals("sucess")){

            markinattendence("P","present",sub.toString())

        }else{

        }


        //firebaseattendence(firebaseAuth.currentUser!!.uid.toString())
        firebaseattendence(roll.toString())
        recyclerview()

    }


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@AttendenceActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun markinattendence( attendence:String,Present:String,subjectname:String){

        val myPreference = MyPreference(this)

      val fcmtaoken =  FirebaseMessaging.getInstance().token.toString()


        val d = Date()
        val sdf = SimpleDateFormat("hh:mm a")
        val currentDateTimeString = sdf.format(d)
                    val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val imageurl = myPreference.getUrl();
        val studentNames = myPreference.getusername()
                    val attendence  =AttendenceData(currentDate,attendence,subjectname,currentDateTimeString,studentNames,imageurl)
                 db.collection(roll.toString()).add(attendence).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "$Present",
                            Toast.LENGTH_SHORT
                        ).show()        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Error try again",
                                Toast.LENGTH_SHORT
                            ).show()        }
    }







    fun recyclerview(){
        adapters  = AttendenceAdapter(this@AttendenceActivity,attendenceData)
        binding.recycleview.layoutManager = LinearLayoutManager(this@AttendenceActivity)
        binding.recycleview.adapter= adapters
        binding.recycleview.setHasFixedSize(true)
    }
    
    fun firebaseattendence( titles: String) {
        db = FirebaseFirestore.getInstance()
        val collection = db.collection(/*"quizzes"*/titles)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            attendenceData.clear()
            if (querySnapshot != null) {
                attendenceData.addAll(querySnapshot.toObjects(AttendenceData::class.java))
                adapters.notifyDataSetChanged()
            }
        }
    }






    private fun showDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(com.example.FaceAttendenceCLG.R.layout.subjectlis)
       val subjectrecycle = dialog.findViewById(com.example.FaceAttendenceCLG.R.id.subjectlist) as RecyclerView





        subjectadapters  = subjectlistadapter(this@AttendenceActivity,subjectdata)
        subjectrecycle.layoutManager = LinearLayoutManager(this@AttendenceActivity)
        subjectrecycle.adapter= subjectadapters
        subjectrecycle.setHasFixedSize(true)

        sublectlist("MCA")
        dialog.show()

    }


    fun sublectlist( titles: String) {
        db = FirebaseFirestore.getInstance()
        val collection = db.collection(/*"quizzes"*/titles)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            subjectdata.clear()
            if (querySnapshot != null) {
                subjectdata.addAll(querySnapshot.toObjects(ClassName::class.java))
                subjectadapters.notifyDataSetChanged()
            }
        }
    }


}
















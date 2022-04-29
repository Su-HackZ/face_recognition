package com.example.FaceAttendenceCLG

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.FaceAttendenceCLG.DataClass.ClassName
import com.example.FaceAttendenceCLG.DataClass.ExamList
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.DataClass.SignupData
import com.example.FaceAttendenceCLG.databinding.ActivityMainBinding
import com.example.FaceAttendenceCLG.mlkit.markinAttendence
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var    firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference
    private var progressDialog: ProgressDialog? = null

    lateinit var toggle: ActionBarDrawerToggle

    lateinit var  fireStore: FirebaseFirestore

    private var myPreference: MyPreference? = null


    private var LoginData = mutableListOf<SignupData>()


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreference = MyPreference(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!


        reference = FirebaseDatabase.getInstance().getReference("StudentData")


        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("UpDating Information")

        val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        binding.date.text = currentDate.toString()

      //  getUserData()
      //  getData()
        binding.exam.setOnClickListener {
            val intent = Intent(this@MainActivity, markinAttendence::class.java)
            intent.putExtra("examscreen", "examscreen")
            startActivity(intent)
            finish()
        }


        binding.attendece.setOnClickListener {
            val intent = Intent(this@MainActivity, AttendenceActivity::class.java)
            intent.putExtra("attendece", "attendece")
            startActivity(intent)
            finish()


        }



        binding.setticon.setOnClickListener {
            logoutDialog();
        }

    }


    override fun onStart() {
        super.onStart()
        getUserData()
    }

    fun getUserData() {
        progressDialog!!.show()
        val mypreference = MyPreference(this)
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseUser = firebaseAuth.currentUser!!


//        reference = FirebaseDatabase.getInstance().getReference("StudentData")
        reference.child(firebaseUser.uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("username").value
                val rollnumber = it.child("rollNumber").value
                val image = it.child("imageUri").value

                binding.email.text = firebaseAuth.currentUser!!.email.toString()


                binding.name.text = name.toString()
                binding.rollnumber.text = rollnumber.toString()

                Glide
                    .with(this)
                    .load(image)
                    .centerCrop()
                    .circleCrop()
                    // .placeholder(R.drawable.imageView)
                    .into(binding.imageview);

                mypreference.setRollNumber(rollnumber.toString())
                mypreference.setusername(name.toString())
                mypreference.setUrl(image.toString())


                progressDialog!!.dismiss()

            }

        }.addOnFailureListener {
            progressDialog!!.dismiss()

        }
    }

    fun logout() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        myPreference!!.clear()

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("are you sure want to logout")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            logout()

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->


            dialog.dismiss()

        }


        builder.show()

    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//
//    }





}




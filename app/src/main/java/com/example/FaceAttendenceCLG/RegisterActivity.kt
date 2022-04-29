package com.example.FaceAttendenceCLG

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.FaceAttendenceCLG.DataClass.SignupData
import com.example.FaceAttendenceCLG.databinding.ActivityRegisterBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var reference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser
   // lateinit var storage: FirebaseStorage
    lateinit var storage:StorageReference
    private var progressDialog: ProgressDialog? = null
    lateinit var  db: FirebaseFirestore

    private var imageUri: Uri? = null
      var   url: String? = null
    var imageulr = ""
    val REQUEST_IMAGE_CAPTURE = 1
    val MY_CAMERA_PERMISSION_CODE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        storage = FirebaseStorage.getInstance().reference

        progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Loading")

        reference = FirebaseDatabase.getInstance().getReference("StudentData")

        binding.sigunup.setOnClickListener {
            signupUser()
            //RegisterNewUser()
        }


        binding.lofinscreen.setOnClickListener {

            val intent = Intent(
                this@RegisterActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
            finish()

        }




        binding.imageview.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, REQUEST_IMAGE_CAPTURE)



            ImagePicker.with(this)
                .cameraOnly()	//User can only capture image using Camera
                .compress(1024)	//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start()


        }



       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                markinAttendence.REQUIRED_PERMISSIONS,
                markinAttendence.REQUEST_CODE_PERMISSION
            )
        }*/

    }





    fun signupUser(){
        val userEmil = binding.useremail.text.toString()
        val userPassword = binding.password.text.toString()
        val conformPassword = binding.condormPassword.text.toString()
        val name = binding.username.text.toString()
        val rollnumber = binding.rollnumber.text.toString()
        if (name.isBlank()){
            Toast.makeText(this,"Name  can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (rollnumber.isBlank()){
            Toast.makeText(this,"RollNumber  can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (userEmil.isBlank()){
            Toast.makeText(this,"Email can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword.isBlank()||conformPassword.isBlank()){
            Toast.makeText(this,"Password can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (userPassword != conformPassword){
            Toast.makeText(this,"Password and confirm password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if(imageUri== null){
            Toast.makeText(this,"please upload profile image ", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(userEmil,userPassword)

            .addOnCompleteListener(this){
                progressDialog!!.show()

                if (it.isSuccessful){
                    storage = FirebaseStorage.getInstance().getReference(/*"image/"+*/firebaseAuth.uid.toString()+".jpg")
                   // storage = FirebaseStorage.getInstance().getReference("image1")


                    storage.putFile(imageUri!!).addOnSuccessListener(this) {
                        val result = it.metadata!!.reference!!.downloadUrl;

                        result.addOnSuccessListener {

                            val imageLink = it.toString()
                             url = it.toString()
                            Log.d("Image",imageLink.toString())
                            val signupData = SignupData(imageLink.toString(), name, userEmil, userPassword,rollnumber)

                            reference.child(firebaseAuth.currentUser!!.uid).setValue(signupData)
                                .addOnCompleteListener(this) {
                                    if (it.isSuccessful) {
                                        progressDialog!!.dismiss()

                                        Toast.makeText(
                                            this,
                                            "Register successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@RegisterActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        progressDialog!!.dismiss()

                                        Toast.makeText(
                                            this,
                                            "server Error try later",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                        val intent = Intent(
                                            this@RegisterActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()

                                    }

                                }
                        }.addOnFailureListener {
                                progressDialog!!.dismiss()


                            val intent = Intent(
                                this@RegisterActivity,
                                LoginActivity::class.java
                            )
                            startActivity(intent)
                            finish()

                            }


                    }

                }

            }

            .addOnFailureListener {

                progressDialog!!.dismiss()

                Toast.makeText(this,"error ", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }




    }




    fun RegisterNewUser(){
        val userEmil = binding.useremail.text.toString()
        val userPassword = binding.password.text.toString()
        val conformPassword = binding.condormPassword.text.toString()
        val name = binding.username.text.toString()
        val rollnumber = binding.rollnumber.text.toString()
        if (name.isBlank()){
            Toast.makeText(this,"Name  can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (rollnumber.isBlank()){
            Toast.makeText(this,"RollNumber  can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (userEmil.isBlank()){
            Toast.makeText(this,"Email can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword.isBlank()||conformPassword.isBlank()){
            Toast.makeText(this,"Password can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (userPassword != conformPassword){
            Toast.makeText(this,"Password and confirm password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if(imageUri== null){
            Toast.makeText(this,"please upload profile image ", Toast.LENGTH_SHORT).show()
            return
        }


        firebaseAuth.createUserWithEmailAndPassword(userEmil,userPassword)

            .addOnCompleteListener(this) {
                progressDialog!!.show()


                if (it.isSuccessful){
                    storage = FirebaseStorage.getInstance().getReference(/*"image/"+*/firebaseAuth.uid.toString()+".jpg")
                    // storage = FirebaseStorage.getInstance().getReference("image1")

                    storage.putFile(imageUri!!).addOnSuccessListener(this) {
                        val result = it.metadata!!.reference!!.downloadUrl;

                        result.addOnSuccessListener {
                            val imageLink = it.toString()
                            url = it.toString()
                            Log.d("Image",imageLink.toString())
                            val signupData = SignupData(imageLink.toString(), name, userEmil, userPassword,rollnumber)
                            db.collection(firebaseAuth.currentUser!!.uid).add(signupData).
                            addOnSuccessListener {
                                progressDialog!!.dismiss()
                                Toast.makeText(
                                    this,
                                    "Register successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                                startActivity(intent)
                                finish()

                            }

                                .addOnFailureListener {
                                    progressDialog!!.dismiss()

                                    Toast.makeText(
                                        this,
                                        "Error try again",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                    val intent = Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()

                                }
                        }.addOnFailureListener {
                            progressDialog!!.dismiss()
                            val intent = Intent(
                                this@RegisterActivity,
                                LoginActivity::class.java
                            )
                            startActivity(intent)
                            finish()

                        }


                    }

                }





            }.addOnFailureListener {

                progressDialog!!.dismiss()

                Toast.makeText(this,"error ", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }


    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode ==REQUEST_IMAGE_CAPTURE ) {
//            imageUri = data?.data
//
//            binding.imageview.setImageURI(imageUri)
//
//        }


        if (resultCode == RESULT_OK) {

            //Image Uri will not be null for RESULT_OK
            imageUri = data?.data!!
            // Use Uri object instead of File to avoid storage permissions
            binding.imageview.setImageURI(imageUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }


        }

}












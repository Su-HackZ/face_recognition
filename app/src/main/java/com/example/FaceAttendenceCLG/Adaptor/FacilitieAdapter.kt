package com.example.FaceAttendenceCLG.Adaptor

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.FaceAttendenceCLG.DataClass.*
import com.example.FaceAttendenceCLG.R
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.AccessController.getContext

private lateinit var token: String

class FacilitieAdapter(val context: Context, val quizzes: List<AttendenceData>/*, val signup :List<SignupData>*/):
    RecyclerView.Adapter<FacilitieAdapter.FirebaseListViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseListViewholder {
        val inflater =  LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.attendencelist,parent,false)
        val view = inflater.inflate(R.layout.facilitieattendenceitems,parent,false)
        return FirebaseListViewholder(view)
    }

    override fun onBindViewHolder(holder: FirebaseListViewholder, position: Int) {

        //  holder.titles.text = quizzes[position].title
//        holder.itemView.setOnClickListener {
//            val intent= Intent(context, ExamActivity::class.java)
//            intent.putExtra("Title",quizzes[position].title)
//            context.startActivities(arrayOf(intent))
//        }
        val mypreference = MyPreference(context)

//        for (quizss in quizzes){
//           token = quizss.Token
//        }




        holder.date.text = quizzes[position].Date
        holder.attendence.text = quizzes[position].attendence
        //holder.name.text= mypreference.getusername().toString()
        holder.name.text= quizzes[position].studentName
        holder.Rollnumber.text = mypreference.getRollNumber().toString()
        // holder.imageview.text = mypreference.getRollNumber().toString()
        holder.subjectname.text = quizzes[position].subjectName
        holder.time.text = quizzes[position].time



        //  holder.name.text = signup[position].Username
        Glide
            .with(context)
            .load(quizzes[position].studentImage)
            .centerCrop()
            .circleCrop()
            .into(holder.imageview);



/*
        holder.itemView.setOnClickListener {
            Log.d("TOKEN", token)

            PushNotifactionData(
                NotifactionData("Clg Attendence ","why are you not  present"),
                token
            ).also {
                it
            }
        }
*/




    }

    override fun getItemCount(): Int {
        return quizzes.size
    }



    fun sendNotifaction(notifaction: PushNotifactionData) = CoroutineScope(Dispatchers.IO)
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



    class FirebaseListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.findViewById<TextView>(R.id.date)
        val name = itemView.findViewById<TextView>(R.id.name)
        val attendence = itemView.findViewById<TextView>(R.id.attendence)
        val Rollnumber = itemView.findViewById<TextView>(R.id.Rollnumber)
        val imageview = itemView.findViewById<ImageView>(R.id.imageview)
        val subjectname = itemView.findViewById<TextView>(R.id.subjectname)
        val time = itemView.findViewById<TextView>(R.id.time)




    }

}
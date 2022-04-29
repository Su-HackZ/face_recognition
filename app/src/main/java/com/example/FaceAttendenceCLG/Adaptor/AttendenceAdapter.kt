package com.example.FaceAttendenceCLG.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.FaceAttendenceCLG.DataClass.AttendenceData
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.R

class AttendenceAdapter(val context: Context, val quizzes: List<AttendenceData>/*, val signup :List<SignupData>*/):
    RecyclerView.Adapter<AttendenceAdapter.FirebaseListViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseListViewholder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.attendencelist,parent,false)
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



          holder.date.text = quizzes[position].Date
          holder.attendence.text = quizzes[position].attendence
        holder.name.text= mypreference.getusername().toString()
        holder.Rollnumber.text = mypreference.getRollNumber().toString()
      // holder.imageview.text = mypreference.getRollNumber().toString()
       holder.subjectname.text = quizzes[position].subjectName
       holder.time.text = quizzes[position].time



      //  holder.name.text = signup[position].Username
        Glide
            .with(context)
            .load(mypreference.getUrl())
            .centerCrop()
            .circleCrop()
            // .placeholder(R.drawable.imageView)
            .into(holder.imageview);


    }

    override fun getItemCount(): Int {
        return quizzes.size
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
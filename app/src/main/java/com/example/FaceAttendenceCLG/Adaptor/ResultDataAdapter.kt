package com.example.FaceAttendenceCLG.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FaceAttendenceCLG.DataClass.ResultData
import com.example.FaceAttendenceCLG.R

class ResultDataAdapter(val context: Context, val quizzes: List<ResultData>/*, val signup :List<SignupData>*/):
    RecyclerView.Adapter<ResultDataAdapter.FirebaseListViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseListViewholder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.facilityresultitem,parent,false)
        return FirebaseListViewholder(view)
    }

    override fun onBindViewHolder(holder: FirebaseListViewholder, position: Int) {

        holder.name.text = quizzes[position].studentName
     holder.Rollnumber.text = quizzes[position].studentrollnumber
        holder.marks.text = quizzes[position].studentScore+"%"

    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    class FirebaseListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name)
        val Rollnumber = itemView.findViewById<TextView>(R.id.RollNumber)
        val marks = itemView.findViewById<TextView>(R.id.marks)

    }




}

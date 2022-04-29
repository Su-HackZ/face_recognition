package com.example.FaceAttendenceCLG.Adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FaceAttendenceCLG.DataClass.ExamList
import com.example.FaceAttendenceCLG.ExamActivity
import com.example.FaceAttendenceCLG.R

class ExamListAdaptor(val context: Context, val quizzes:List<ExamList>):
    RecyclerView.Adapter<ExamListAdaptor.FirebaseListViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseListViewholder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.exam_list_item,parent,false)
        return FirebaseListViewholder(view)
    }

    override fun onBindViewHolder(holder: FirebaseListViewholder, position: Int) {

        holder.titles.text = quizzes[position].title
        holder.itemView.setOnClickListener {
            val intent= Intent(context,ExamActivity::class.java)
            intent.putExtra("Title",quizzes[position].title)
            context.startActivities(arrayOf(intent))
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    class FirebaseListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titles = itemView.findViewById<TextView>(R.id.titlee)

    }
}
package com.example.FaceAttendenceCLG.Adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FaceAttendenceCLG.DataClass.ClassName
import com.example.FaceAttendenceCLG.DataClass.MyPreference
import com.example.FaceAttendenceCLG.R
import com.example.FaceAttendenceCLG.mlkit.AttendenceMlkit

class subjectlistadapter(val context: Context, val classname: MutableList<ClassName>):
    RecyclerView.Adapter<subjectlistadapter.FirebaseListViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseListViewholder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.subjectlistitems,parent,false)
        return FirebaseListViewholder(view)
    }

    override fun onBindViewHolder(holder: FirebaseListViewholder, position: Int) {

        val mypreference = MyPreference(context)

        //holder.time.text = classname[position].subjecttime
        holder.name.text = classname[position].subjectname



        holder.itemView.setOnClickListener {
            val intent= Intent(context, AttendenceMlkit::class.java)
            intent.putExtra("subjectname",classname[position].subjectname)
            context.startActivities(arrayOf(intent))

        }


    }

    override fun getItemCount(): Int {
        return classname.size
    }

    class FirebaseListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name)
        val time = itemView.findViewById<TextView>(R.id.time)

    }
    }


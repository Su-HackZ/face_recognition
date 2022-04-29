package com.example.FaceAttendenceCLG.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FaceAttendenceCLG.DataClass.Questions
import com.example.FaceAttendenceCLG.R

class firebaseOptionAdapter(val context: Context, val question: Questions):
    RecyclerView.Adapter<firebaseOptionAdapter.optionVireholder>() {


    private var options:List<String> = listOf(question.option1,question
        .option2,question.option3,question.option4)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): optionVireholder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.options_items,parent,false)
        return optionVireholder(view)
    }

    override fun onBindViewHolder(holder: optionVireholder, position: Int) {

        holder.options.text = options[position]

        holder.itemView.setOnClickListener {
            question.useranswer = options[position]
            notifyDataSetChanged()

        }

        if (question.useranswer == options[position]){
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg)
        }else{
            holder.itemView.setBackgroundResource(R.drawable.options_item_bg)

        }



    }

    override fun getItemCount(): Int {

        return options.size
    }

    class optionVireholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var options= itemView.findViewById<TextView>(R.id.option)
    }
}
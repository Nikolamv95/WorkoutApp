package com.example.workoutapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ItemExerciseStatusBinding
import org.w3c.dom.Text

class ExerciseStatusAdapter (val items:ArrayList<ExerciseModel>, val context: Context)
    : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvItem = view.findViewById<TextView>(R.id.tvItem)!!
    }

    // Will be called once the view holder is created (once the screen is displayed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise_status, parent, false))
    }

    // Binds each element from the arrayList in to a view
    // the holder is our ViewHolder class which at the end is our tvItem
    // Here we adjust the view of every single element of the list of items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvItem.text = model.id.toString()

        if (model.isSelected){
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_thin_color_accent_border)

            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        } else if (model.isCompleted){
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_accent_background)

            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_grey_background)

            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
    }

    // Show us the amount of items in our RecycleView (arrayList()
    override fun getItemCount(): Int {
      return items.size
    }
}
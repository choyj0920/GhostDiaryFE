package com.example.ghostdiary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemSpinnerEmotionBinding


class EmotionSpinnerAdapter(private val context: Context,val emotionarray: ArrayList<Int>):BaseAdapter(){

    override fun getCount(): Int {
        return 6

    }
    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, p2: ViewGroup?): View {
        var binding = ItemSpinnerEmotionBinding.inflate(LayoutInflater.from(context), p2, false)
        val rootview: View = binding.root

        if (emotionarray.get(position) == -1) {
            binding.ivSpinnerItem.visibility = View.GONE
            binding.tvSpinnerItem.visibility = View.VISIBLE
            binding.tvSpinnerItem.text = "분류"
        } else {
            binding.ivSpinnerItem.visibility = View.VISIBLE
            binding.tvSpinnerItem.visibility = View.GONE
            binding.ivSpinnerItem.setImageResource(
                when (emotionarray.get(position)) {
                    0 -> R.drawable.ghost_verygood
                    1 -> R.drawable.ghost_good
                    2 -> R.drawable.ghost_normal
                    3 -> R.drawable.ghost_bad
                    4 -> R.drawable.ghost_verybad

                    else -> {
                        R.drawable.ic_ghost
                    }
                } as Int
            )

        }

        return rootview

    }

}



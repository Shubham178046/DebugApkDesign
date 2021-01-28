package com.example.debugapkdesign.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.debugapkdesign.R
import com.example.debugapkdesign.model.FolderViewModel
import java.util.*
import kotlin.collections.ArrayList

class FolderViewAdapter(context: Context, var list: ArrayList<Object> , var onClick: OnClick) :
    RecyclerView.Adapter<FolderViewAdapter.ViewHolder?>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderName: TextView = itemView.findViewById(R.id.txtFolderTitle)
        var folderpath: TextView = itemView.findViewById(R.id.txtFolderPath)
        var mainLayout: LinearLayout = itemView.findViewById(R.id.LLmain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_folder_test, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var folderViewModel = list.get(position) as FolderViewModel
        holder.folderName.setText(folderViewModel.str)
        holder.folderpath.setText(folderViewModel.i.toString() + " Files")
        holder.itemView.setOnClickListener{
            onClick.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClick{
        fun onClick(position : Int)
    }
}
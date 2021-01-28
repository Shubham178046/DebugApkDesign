package com.example.debugapkdesign.adapter

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.debugapkdesign.FileUtils
import com.example.debugapkdesign.PrefUtil
import com.example.debugapkdesign.R
import com.example.debugapkdesign.model.FileInfoModel
import java.io.File
import java.util.*


class FileViewAdapter(var context: Context, var list: ArrayList<Object> , var onClick: OnClick) :
    RecyclerView.Adapter<FileViewAdapter.ViewHolder>() {
    private val sparseBooleanArray = SparseBooleanArray()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtFileName = itemView.findViewById<TextView>(R.id.txtFileName)
        var tvPath = itemView.findViewById<TextView>(R.id.tvPath)
        var txtFileSize = itemView.findViewById<TextView>(R.id.txtFileSize)
        var imgFile = itemView.findViewById<ImageView>(R.id.imgFile)
        var ll1 = itemView.findViewById<LinearLayout>(R.id.ll1)
        var ll3 = itemView.findViewById<LinearLayout>(R.id.ll3)
        var ivMore = itemView.findViewById<ImageView>(R.id.ivMore)
        var lin_doc = itemView.findViewById<LinearLayout>(R.id.lin_doc)
        var imgcheck = itemView.findViewById<ImageView>(R.id.imgcheck)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file_test, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var fileInfoModel: FileInfoModel = list.get(position) as FileInfoModel
        Log.d("TAG", "onBindViewHolder: '"+fileInfoModel.title +" " + list.size )
        for (i in 0 until list!!.size) {
            val ef2: FileInfoModel = list!!.get(i) as FileInfoModel
            Log.d("FileViewAdapter", "setData: " + ef2.title)
        }
        holder.txtFileName.setText(fileInfoModel.title)
        var str: String = extention(fileInfoModel.displayName!!)!!
        setImage(holder.imgFile, str)
        holder.tvPath.setText(fileInfoModel.path)
        holder.ivMore.visibility = if (fileInfoModel.showmenu!!) View.INVISIBLE else View.VISIBLE
        val date = Date(File(fileInfoModel.path).lastModified())
        holder.txtFileSize.setText(
            FileUtils.getSize(fileInfoModel.size!!) + " " + FileUtils.getDate(
                date.toString()
            )
        )
        /*if (PrefUtil(context).getBoolean("setting_filepath", false)) {
            holder.tvPath.visibility = View.GONE
        } else {
            holder.tvPath.visibility = View.VISIBLE
        }*/
        holder.itemView.setOnClickListener {
            onClick.onClick(position)
        }
        holder.ll1.setPadding(0, 0, 0, 0)
    }

    fun setImage(img: ImageView, str: String) {
        var path = str.toLowerCase()

        when (path) {
            "csv" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "doc" -> {
                img.setImageResource(R.mipmap.doc)
            }
            "odp" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "ods" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "odt" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "pdf" -> {
                img.setImageResource(R.mipmap.pdf)
            }
            "pps" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "ppt" -> {
                img.setImageResource(R.mipmap.ppt)
            }
            "rar" -> {
                img.setImageResource(R.mipmap.rar)
            }
            "raw" -> {
                img.setImageResource(R.mipmap.raw)
            }
            "rtf" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "txt" -> {
                img.setImageResource(R.mipmap.txt)
            }
            "xlr" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "xls" -> {
                img.setImageResource(R.mipmap.xls)
            }
            "xps" -> {
                img.setImageResource(R.mipmap.csv)
            }
            "zip" -> {
                img.setImageResource(R.mipmap.zip)
            }
            "docx" -> {
                img.setImageResource(R.mipmap.doc)
            }
            "ppsx" -> {
                img.setImageResource(R.mipmap.raw)
            }
            "pptx" -> {
                img.setImageResource(R.mipmap.ppt)
            }
            "xlsx" -> {
                img.setImageResource(R.mipmap.xls)
            }

        }

    }

    fun extention(str: String): String? {
        return try {
            str.substring(str.lastIndexOf(".") + 1, str.length)
        } catch (unused: Exception) {
            "NF"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClick
    {
        fun onClick(position: Int)
    }
}
package com.example.debugapkdesign

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debugapkdesign.adapter.FolderViewAdapter
import com.example.debugapkdesign.model.FileInfoModel
import com.example.debugapkdesign.model.FolderListModel
import com.example.debugapkdesign.model.FolderViewModel
import kotlinx.android.synthetic.main.fragment_folder.*
import java.io.File
import java.lang.ClassCastException


class FolderFragment : Fragment() {
    var WRITE_EXTERNAL_STORAGE = 101
    var READ_EXTERNAL_STORAGE = 102
    var folderViewAdapter: FolderViewAdapter? = null
    var folderListModel: FolderListModel = FolderListModel()
    var extentionType = arrayOf(
        ".PDF",
        ".TXT",
        ".DOC",
        ".DOCX",
        ".ODT",
        ".RTF",
        ".XPS",
        ".XLS",
        ".XLSX",
        ".CSV",
        ".ODS",
        ".XLR",
        ".PPT",
        ".PPTX",
        ".PPSX",
        ".PPS",
        ".ODP",
        ".ZIP",
        ".RAR"
    )
    var arrayObject: ArrayList<Object>? = null
    var arrayString: ArrayList<String> = ArrayList()
    var sendData: SendData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendData = activity as SendData
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FolderFragment().apply {

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayObject = ArrayList()
        arrayString = ArrayList()
        getPermission()
        Log.d("TAG", "onViewCreated: " + arrayObject!!.size)
        folderViewAdapter =
            FolderViewAdapter(activity!!, arrayObject!!, object : FolderViewAdapter.OnClick {
                override fun onClick(position: Int) {
                    var folderViewModel = arrayObject!!.get(position) as FolderViewModel
                    val path = File(folderListModel.fileInfoModel.get(position).path)
                    Log.d("TAG", "onClick: "+path.absolutePath + " " +path.parent + " " + path.parentFile)
                    sendData!!.setData(folderViewModel.str2)
                }

            })
        recyclerview_list.layoutManager = LinearLayoutManager(activity)
        recyclerview_list.adapter = folderViewAdapter
    }

    fun getPermission() {
        Log.d("FileFragment", "getPermission: " + "getPermission")
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE
            )
            return
        } else if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE
            )
            return
        } else if (folderListModel.fileInfoModel.size == 0) {
            Log.d("FileFragment", "getPermission: " + "FetchList")
            fetchList(this).execute()
        } else {
            Log.d("FileFragment", "getPermission: " + "SetData")
            Log.d("FileFragment", "Size: " + (folderListModel.fileInfoModel.size))
            setData(folderListModel.fileInfoModel)
        }
    }

    inner class fetchList() : AsyncTask<String, Void, String>() {
        private fun C5387b() {}

        constructor(fileFragment: FolderFragment) : this() {
        }

        override fun doInBackground(vararg p0: String?): String {
            fetchdata()
            return "Executed"
        }

        override fun onPreExecute() {
            folderListModel.fileInfoModel.clear()
            arrayObject?.clear()
            arrayString?.clear()
            folderViewAdapter?.notifyDataSetChanged()
        }

        override fun onPostExecute(result: String?) {
            Log.d("TAG", "onPostExecute: " + folderListModel.fileInfoModel.size)
            setData(folderListModel.fileInfoModel)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != 101) {
            if (requestCode != 102) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            } else if (grantResults.size > 0 || grantResults[0] != 0) {
                Toast.makeText(activity, "Read Permission Denied", Toast.LENGTH_LONG).show()
            } else {
                getPermission()
            }
        } else if (grantResults.size > 0 || grantResults[0] != 0) {
            Toast.makeText(activity, "Write Permission Denied", Toast.LENGTH_LONG).show()
        } else {
            getPermission()
        }
    }

    fun setData(fileInfoModel: ArrayList<FileInfoModel>) {
        Log.d("setData", "setData: " + "Call SetDAta")
        arrayObject?.add(FolderViewModel("All Documents", "KING007", fileInfoModel.size) as Object)
        arrayString?.add("KING007")
        if (fileInfoModel != null && fileInfoModel.size > 0) {
            Log.d("TAG", "setData: " + "Call")
            for (i in 0 until fileInfoModel.size) {
                try {
                    val file: File = File(fileInfoModel.get(i).path)
                    val substring =
                        file.parent.substring(file.parent.lastIndexOf(File.separator) + 1)
                    Log.d("Data", "setData: " + substring)
                    if (!arrayString.contains(file.parent)) {
                        val ef = FolderViewModel(substring, file.parent, 1)
                        if (file.parent == "/storage/emulated/0") {
                            ef.str = "Internal Storage"
                        }
                        Log.d("StrData", "setData: " + ef.str)
                        ef.str = (fileName(ef.str)!!)
                        Log.d("StrDataAfter", "setData: " + ef.str)
                        arrayObject!!.add(ef as Object)
                        arrayString.add(file.parent)
                    } else {
                        val indexOf: Int = arrayString.indexOf(file.parent)
                        val ef2: FolderViewModel = arrayObject!!.get(indexOf) as FolderViewModel
                        ef2.i = (ef2.i + 1)
                        arrayObject!!.set(indexOf, ef2 as Object)
                    }
                } catch (unused: java.lang.Exception) {
                }
            }
        }
        folderViewAdapter?.notifyDataSetChanged()
    }

    fun fileName(str: String?): String? {
        Log.d("TAG", "fileName: "+str)
        return if (str == null || str.length == 0) {
            str
        } else str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    fun fetchdata() {
        var sb: StringBuilder
        var str = ""
        var i = 0
        while (i < extentionType.size) {
            try {
                if (i == 0) {
                    sb = java.lang.StringBuilder()
                    sb.append("UPPER(substr(_data,LENGTH(_data) - ")
                    sb.append(extentionType.get(i).length - 1)
                    sb.append(",LENGTH(_data))) = ?")
                } else {
                    sb = java.lang.StringBuilder()
                    sb.append(str)
                    sb.append(" OR UPPER(substr(_data,LENGTH(_data) - ")
                    sb.append(extentionType.get(i).length - 1)
                    sb.append(",LENGTH(_data))) = ?")
                }
                str = sb.toString()
                i++
            } catch (unused: Exception) {
                return
            }
        }
        val query: Cursor? = activity?.getContentResolver()?.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf("_id", "_data", "date_added", "media_type", "mime_type", "title", "_size"),
            str,
            extentionType,
            "date_added DESC"
        )
        if (query != null && query.getCount() >= 1) {
            val columnIndex: Int = query.getColumnIndex("_id")
            val columnIndex2: Int = query.getColumnIndex("_data")
            val columnIndex3: Int = query.getColumnIndex("date_added")
            val columnIndex4: Int = query.getColumnIndex("media_type")
            val columnIndex5: Int = query.getColumnIndex("mime_type")
            query.getColumnIndex("title")
            val columnIndex6: Int = query.getColumnIndex("_size")
            while (query.moveToNext()) {
                val i2: Int = query.getInt(columnIndex)
                val string: String = query.getString(columnIndex2)
                val j: Long = query.getLong(columnIndex3)
                query.getString(columnIndex4)
                val string2: String = query.getString(columnIndex5)
                val j2: Long = query.getLong(columnIndex6)
                val file = File(string)
                if (file.exists()) {
                    val df = FileInfoModel()
                    df.id = i2
                    df.title = (file.getName())
                    df.displayName = (file.getName())
                    df.path = (string)
                    df.extension = (string2)
                    df.size = (j2)
                    df.dateAdded = (j)
                    Log.d(
                        "TAG",
                        "fetchdata: " + df.title + " " + df.displayName + " " + df.path + " " + df.extension
                    )
                    folderListModel.fileInfoModel.add(df)
                } else {
                    MediaScannerConnection.scanFile(
                        activity,
                        arrayOf(file.toString()),
                        null as Array<String?>?,
                        null as OnScanCompletedListener?
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1) {
            if (requestCode == 101) {
                getPermission()
            }
            if (requestCode == 102) {
                getPermission()
            }
        }
    }

    interface SendData {
        fun setData(path: String)
    }
}
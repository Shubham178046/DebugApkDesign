package com.example.debugapkdesign

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.URI_ANDROID_APP_SCHEME
import android.content.Intent.URI_INTENT_SCHEME
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debugapkdesign.adapter.FileViewAdapter
import com.example.debugapkdesign.model.FileInfoModel
import com.example.debugapkdesign.model.FolderListModel
import kotlinx.android.synthetic.main.fragment_file.*
import java.io.File


class FileFragment : Fragment() {
    var WRITE_EXTERNAL_STORAGE = 101
    var READ_EXTERNAL_STORAGE = 102
    var arrayObject: ArrayList<Object>? = null
    var arrayString: ArrayList<String> = ArrayList()
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
    var folderListModel: FolderListModel = FolderListModel()
    var fileViewAdapter: FileViewAdapter? = null
    var temp: Int = -1
    var filePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(isFrom: Int) =
            FileFragment().apply {
                temp = isFrom
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayObject = ArrayList()
        arrayString = ArrayList()
        if (temp == 1) {
            val b = arguments
            filePath = b!!.getString("path")
        }
        getPermission()
        Log.d("TAG", "onViewCreated: '" + arrayObject!!.size)
        fileViewAdapter =
            FileViewAdapter(activity!!, arrayObject!!, object : FileViewAdapter.OnClick {
                override fun onClick(position: Int) {
                    try
                    {
                        openFile(folderListModel.fileInfoModel.get(position).path!!)
                    }
                    catch (e : Exception)
                    {
                        e.printStackTrace()
                    }
                   /* val intent = Intent(activity, PDFViewActivity::class.java)
                    intent.putExtra("path", folderListModel.fileInfoModel.get(position).path)
                    activity?.startActivity(intent)*/
                }

            })
        recyclerview_list.layoutManager = LinearLayoutManager(activity)
        recyclerview_list.adapter = fileViewAdapter
    }

    inner class fetchData() :
        AsyncTask<String, Void, String>() {
        constructor(fileFragment: FileFragment) : this() {
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg p0: String?): String {
            if (temp == 1) {
                fetchFileFromFolder()
            } else {
                fetchlist()
            }
            return "Excuted"
        }

        override fun onPreExecute() {
            folderListModel.fileInfoModel.clear()
            arrayObject?.clear()
            arrayString?.clear()
            fileViewAdapter?.notifyDataSetChanged()
        }

        override fun onPostExecute(result: String?) {
            Log.d("Data", "onPostExecute: '" + folderListModel.fileInfoModel.size)
            setData(folderListModel.fileInfoModel)
            Log.d("TAG", "onPostExecute: '" + arrayObject!!.size)
        }
    }

    @SuppressLint("WrongConstant")
    fun openFile(path: String) {
        var intent : Intent = Intent()
        val lowerCase: String = path.toLowerCase()
        val intent2 = Intent("android.intent.action.VIEW")
        intent2.addFlags(URI_INTENT_SCHEME)
        intent2.addFlags(URI_ANDROID_APP_SCHEME)
        if(lowerCase.endsWith(".csv"))
        {
            Toast.makeText(activity, "Bad Content", Toast.LENGTH_LONG).show()
        }
        else if(lowerCase.endsWith(".zip"))
        {
            intent2.setDataAndType(
                FileProvider.getUriForFile(
                    activity!!, "com.example.debugapkdesign.provider", File(
                        path
                    )
                ), "application/zip"
            )
            intent2.setFlags(3);
            try {
                startActivity(intent2)
                return
            }
            catch (e: ActivityNotFoundException)
            {
               Toast.makeText(activity, "No Application Found", Toast.LENGTH_LONG).show()
            }
        }
        else if(lowerCase.endsWith(".rar"))
        {
            intent2.setDataAndType(
                FileProvider.getUriForFile(
                    activity!!, "com.example.debugapkdesign.provider", File(
                        path
                    )
                ), "application/x-rar-compressed"
            )
            intent2.setFlags(3);
            try {
                startActivity(intent2)
                return
            }
            catch (e: ActivityNotFoundException)
            {
                Toast.makeText(activity, "No Application Found", Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            if (lowerCase.endsWith(".doc") || lowerCase.endsWith(".docx")) {
                //intent = Intent(activity, DocViewActivity::class.java)
            }
            else if (lowerCase.endsWith(".pdf")) {
                intent = Intent(activity, PDFViewActivity::class.java)
            }
            else if (lowerCase.endsWith(".ppt") || lowerCase.endsWith(".pptx")) {
                intent = Intent(activity, PDFViewActivity::class.java)
            }
            else if (lowerCase.endsWith(".txt")) {
                intent = Intent(activity, PDFViewActivity::class.java)
            }
            else if (lowerCase.endsWith(".xls") || lowerCase.endsWith(".xlsx")) {
                intent = Intent(activity, PDFViewActivity::class.java)
            }
            else
            {
                Toast.makeText(activity, "Bad Content", Toast.LENGTH_LONG).show()
            }
            intent.putExtra("filename", File(path).name);
            intent.putExtra("path", path);
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchFileFromFolder() {
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
        var query: Cursor? = null
        if (filePath == "KING007") {
            query = activity?.getContentResolver()?.query(
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
                            null as MediaScannerConnection.OnScanCompletedListener?
                        )
                    }
                }
            }
        } else {
            Log.d("FilePath", "fetchFileFromFolder: " + filePath)
            val path: String =
                Environment.getExternalStorageDirectory().toString() + "/" + filePath
            Log.d("Files", "Path: $path")
            val directory = File(filePath)
            val files = directory.listFiles()
            Log.d("Files", "Size: " + files.size)
            for (i in files.indices) {
                Log.d("Files", "FileName:" + files[i].name)
                Log.d("Files", "FileExtention:" + files[i].extension)
                Log.d("Files", "FilePath:" + files[i].path)
                Log.d("Files", "FileSize:" + files[i].length())
                Log.d("Files", "Filedate:" + files[i].lastModified())
                if (files[i].exists()) {
                    if (files[i].isFile) {
                        val df = FileInfoModel()
                        df.id = i
                        df.title = (files[i].name)
                        df.displayName = (files[i].name)
                        df.path = (files[i].path)
                        df.extension = (files[i].extension)
                        df.size = (files[i].length())
                        df.dateAdded = (files[i].lastModified())
                        folderListModel.fileInfoModel.add(df)
                    }
                }
            }
        }

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
            fetchData(this).execute()
        } else {
            Log.d("TAG", "getPermission: " + "Call")
            setData(folderListModel.fileInfoModel)
        }
    }

    private fun setData(fileInfoModel: ArrayList<FileInfoModel>) {
        Log.d("SetData", "setData: " + "Call")
        if (fileInfoModel != null && fileInfoModel.size > 0) {
            for (i in 0 until fileInfoModel.size) {
                var filemodel = FileInfoModel()
                filemodel.title = fileInfoModel.get(i).title
                filemodel.path = fileInfoModel.get(i).path
                filemodel.displayName = fileInfoModel.get(i).displayName
                filemodel.extension = fileInfoModel.get(i).extension
                filemodel.size = fileInfoModel.get(i).size
                filemodel.dateAdded = fileInfoModel.get(i).dateAdded
                filemodel.dateView = fileInfoModel.get(i).dateView
                filemodel.showmenu = fileInfoModel.get(i).showmenu
                filemodel.id = fileInfoModel.get(i).id
                arrayObject!!.add(filemodel as Object)
            }
        }
        fileViewAdapter?.notifyDataSetChanged()
        for (i in 0 until arrayObject!!.size) {
            val ef2: FileInfoModel = arrayObject!!.get(i) as FileInfoModel
            Log.d("FileInfoModel", "setData: " + ef2.title)
        }
    }

    fun fetchlist() {
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
        var query = activity?.getContentResolver()?.query(
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
                        null as MediaScannerConnection.OnScanCompletedListener?
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
}
package com.example.debugapkdesign


import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfDocument.Bookmark
import kotlinx.android.synthetic.main.content_pdfview.*
import java.io.File


class PDFViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    var check = false
    private var str: String? = null
    private var str1: String? = null
    private val REQUEST_CODE = 42
    val PERMISSION_CODE = 42042
    var pageNumber = 0

    var pdfFileName: String? = null
    val SAMPLE_FILE = "sample.pdf"
    val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val i = Build.VERSION.SDK_INT
        if (i >= 26) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window.navigationBarColor = resources.getColor(R.color.bottom_navigation)
            }
            toolbar.systemUiVisibility = 16
        } else if (i >= 21) {
            val window: Window = window
            window.addFlags(Int.MIN_VALUE)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
            }
        }
        if (getIntent().getExtras() == null || !getIntent().getExtras()!!.containsKey("filepath")) {
            this.check = true;
        } else {
            this.str = getIntent().getAction();
            this.str1 = getIntent().getExtras()!!.getString("filepath");
        }
        var actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        if (!this.check) {
            /* this.str2 = C1039k.m6544b(this.f5125y)
             L.mo76c(this.f5126z as CharSequence?)*/
        }
        loading.visibility = View.GONE
        progress_bar.visibility = View.GONE
        val path = intent.getStringExtra("path").toString()
        getPdfView("https://gailgaspng.in/Upload/PDFs/GAILGasInvoice-3000123456-7033290.pdf")
    }

    fun getPdfView(path: String) {
        pdfFileName = File(path).name
        pdfView
            .fromUri(Uri.parse(path))
            .defaultPage(0)
            .password(null)
            .enableDoubletap(true)
            .enableAnnotationRendering(true)
            .onLoad(this) // called after document is loaded and starts to be rendered
            .onPageChange(this)
            .spacing(10)
            .load();
    }

    override fun loadComplete(nbPages: Int) {
        val meta = pdfView.documentMeta
        Log.e("TAG", "title = " + meta.title)
        Log.e("TAG", "author = " + meta.author)
        Log.e("TAG", "subject = " + meta.subject)
        Log.e("TAG", "keywords = " + meta.keywords)
        Log.e("TAG", "creator = " + meta.creator)
        Log.e("TAG", "producer = " + meta.producer)
        Log.e("TAG", "creationDate = " + meta.creationDate)
        Log.e("TAG", "modDate = " + meta.modDate)

        printBookmarksTree(pdfView.tableOfContents, "-")
    }

    fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b: PdfDocument.Bookmark in tree) {
            Log.e("TAG", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                val a: List<Bookmark> = b.children
                printBookmarksTree(a, sep + "-");
            }
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    fun getFileName(uri: Uri): String {
        var result: String? = null;
        if (uri.getScheme().equals("content")) {
            var cursor: Cursor = getContentResolver().query(uri, null, null, null, null)!!;
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return result!!
    }

    fun pickFile() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
            return
        }
        launchPicker()
    }

    fun launchPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        try {
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            //alert user that file manager not working
            // Toast.makeText(this, R.string.toast_pick_file_error, Toast.LENGTH_SHORT).show()
        }
    }

}
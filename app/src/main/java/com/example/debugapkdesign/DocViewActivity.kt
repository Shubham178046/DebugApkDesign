package com.example.debugapkdesign


/*
class DocViewActivity : AppCompatActivity(), IMainFrame {
    var filePath: String? = null
    var extention: String? = null
    var actionBar: ActionBar? = null
    private var bottomBar: SheetBar? = null
    private var toolsbar: AToolsbar? = null
    private var appFrame: AppFrame? = null
    private val applicationType = -1
    private val bg: Any = -7829368
    private var calloutBar: CalloutToolsbar? = null
    private var dbService: DBService? = null
    private var eraserButton: AImageCheckButton? = null
    var frame: FrameLayout? = null
    private var fullscreen = false
    private val gapView: View? = null
    private var isDispose = false
    private val isThumbnail = false
    private var marked = false
    private var pageDown: AImageButton? = null
    private var pageUp: AImageButton? = null
    private var penButton: AImageCheckButton? = null
    private var searchBar: FindToolBar? = null
    private var settingsButton: AImageButton? = null
    private val tempFilePath: String? = null
    private var toast: Toast? = null
    private var wm: WindowManager? = null
    private var wmParams: WindowManager.LayoutParams? = null
    private val writeLog = true
    var control: MainControl? = null
    var flag: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_view)
        this.frame = findViewById(R.id.container) as FrameLayout
        control = MainControl(this)
        val appFrame2 = AppFrame(this)
        this.appFrame = appFrame2;
        appFrame2.post {object : Runnable{
            override fun run() {
                this@DocViewActivity
            }

        } }

    }
    fun init() {
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_LONG)
        val intent: Intent = intent
        dbService = DBService(applicationContext)
        if (intent.extras == null || !intent.extras!!.containsKey("path")) {
            Toast.makeText(this, "Unable To Open Activity", Toast.LENGTH_LONG).show()
        } else {
            filePath = intent.extras!!.getString("path")
        }
        if (filePath == null) {
            filePath = intent.getDataString()
            val indexOf: Int = getfilePath()!!.indexOf(" ")
            if (indexOf > 0) {
                filePath = filePath!!.substring(indexOf + 3)
            }
            filePath = Uri.decode(filePath)
        }
        val lastIndexOf = filePath!!.lastIndexOf(File.separator)
        title = if (lastIndexOf > 0) {
            filePath!!.substring(lastIndexOf + 1)
        } else {
            filePath
        }
        if (FileKit.instance().isSupport(filePath)) {
            dbService!!.insertRecentFiles(
                MainConstant.TABLE_RECENT,
                filePath
            )
        }
        getdata()
        control!!.openFile(filePath)
        initMarked()
    }
    fun initMarked() {}
    fun getfilePath() : String? {
        return filePath
    }
    @SuppressLint("RestrictedApi")
    fun getdata() {
        extention = File(filePath).extension
        extention = extention!!.replace(".xls.xls", ".xls")
        extention = extention!!.replace(".xlsx.xlsx", ".xlsx")
        extention = extention!!.replace(".doc.doc", ".doc")
        extention = extention!!.replace(".docx.docx", ".docx")
        extention = extention!!.replace(".ppt.ppt", ".ppt")
        extention = extention!!.replace(".pptx.pptx", ".pptx")
        extention = extention!!.replace(".txt.txt", ".txt")
        extention = extention!!.replace(".pdf.pdf", ".pdf")
        actionBar!!.setWindowTitle(extention)

        setData()
    }

    private fun setData() {
        val lowerCase = filePath!!.toLowerCase()
        if (lowerCase.endsWith("doc") || lowerCase.endsWith("docx") || lowerCase.endsWith("txt") || lowerCase.endsWith(
                "dot"
            ) || lowerCase.endsWith("dotx") || lowerCase.endsWith("dotm")
        ) {
            flag = 0
            toolsbar = WPToolsbar(applicationContext, this.control)
        } else if (lowerCase.endsWith("xls") || lowerCase.endsWith("xlsx") || lowerCase.endsWith("xlt") || lowerCase.endsWith(
                "xltx"
            ) || lowerCase.endsWith("xltm") || lowerCase.endsWith("xlsm")
        ) {
            flag = 1
            toolsbar = SSToolsbar(applicationContext, control as IControl?)
        } else if (lowerCase.endsWith("ppt") || lowerCase.endsWith("pptx") || lowerCase.endsWith("pot") || lowerCase.endsWith(
                "pptm"
            ) || lowerCase.endsWith("potx") || lowerCase.endsWith("potm")
        ) {
            flag = 2
            toolsbar = PGToolsbar(applicationContext, control as IControl?)
        } else {
            flag = 0
            toolsbar = WPToolsbar(applicationContext, control)
        }
        this.appFrame!!.addView(this.toolsbar);
    }

    override fun changePage() {

    }

    override fun changeZoom() {

    }

    override fun completeLayout() {

    }

    override fun dispose() {
        isDispose = true
        val mainControl = control
        if (mainControl != null) {
            mainControl.dispose()
            control = null
        }
        toolsbar = null
        searchBar = null
        bottomBar = null
        val dBService = dbService
        if (dBService != null) {
            dBService.dispose()
            dbService = null
        }
        val appFrame2 = appFrame
        if (appFrame2 != null) {
            val childCount = appFrame2.childCount
            for (i in 0 until childCount) {
                val childAt = appFrame!!.getChildAt(i)
                if (childAt is AToolsbar) {
                    childAt.dispose()
                }
            }
            appFrame = null
        }
        if (this.wm != null) {
            this.wm = null
            wmParams = null
            pageUp!!.dispose()
            pageDown!!.dispose()
            penButton!!.dispose()
            eraserButton!!.dispose()
            settingsButton!!.dispose()
            pageUp = null
            pageDown = null
            penButton = null
            eraserButton = null
            settingsButton = null
        }
    }

    private fun markFile() {
        marked = !marked
    }

    fun showSearchBar(z: Boolean) {
        if (z) {
            if (searchBar == null) {
                val findToolBar = FindToolBar(this, control)
                searchBar = findToolBar
                appFrame!!.addView(findToolBar, 0)
            }
            searchBar!!.visibility = View.VISIBLE
            toolsbar!!.visibility = View.GONE
            return
        }
        val findToolBar2 = searchBar
        if (findToolBar2 != null) {
            findToolBar2.visibility = View.GONE
        }
        toolsbar!!.visibility = View.VISIBLE
    }

    fun showCalloutToolsBar(z: Boolean) {
        if (z) {
            if (calloutBar == null) {
                val calloutToolsbar = CalloutToolsbar(applicationContext, control as IControl?)
                calloutBar = calloutToolsbar
                appFrame!!.addView(calloutToolsbar, 0)
            }
            calloutBar?.setCheckState(EventConstant.APP_PEN_ID, 1)
            calloutBar?.setCheckState(EventConstant.APP_ERASER_ID, 2)
            calloutBar?.visibility = View.VISIBLE
            toolsbar!!.visibility = View.INVISIBLE
            return
        }
        val calloutToolsbar2 = calloutBar
        if (calloutToolsbar2 != null) {
            calloutToolsbar2.visibility = View.INVISIBLE
        }
        toolsbar!!.visibility = View.VISIBLE
    }

    fun setPenUnChecked() {
        if (fullscreen) {
            penButton!!.state = 2
            penButton!!.postInvalidate()
            return
        }
        calloutBar!!.setCheckState(EventConstant.APP_PEN_ID, 2)
        calloutBar!!.postInvalidate()
    }

    fun setEraserUnChecked() {
        if (fullscreen) {
            eraserButton!!.state = 2
            eraserButton!!.postInvalidate()
            return
        }
        calloutBar!!.setCheckState(EventConstant.APP_ERASER_ID, 2)
        calloutBar!!.postInvalidate()
    }

    override fun doActionEvent(i: Int, obj: Any?): Boolean {
        if (i === 0) {
            onBackPressed()
        } else if (i === 20) {
            updateToolsbarStatus()
        } else if (i === 268435464) {
            markFile()
        } else if (i === 1073741828) {
            bottomBar!!.setFocusSheetButton((obj as Int).toInt())
        } else if (i === 536870912) {
            showSearchBar(true)
        } else if (i !== 536870913) {
            when (i) {
                EventConstant.APP_DRAW_ID -> {
                    showCalloutToolsBar(true)
                    control!!.getSysKit().calloutManager.drawingMode = 1
                    appFrame!!.post {
                        this@DocViewActivity.control!!.actionEvent(
                            EventConstant.APP_INIT_CALLOUTVIEW_ID,
                            null as Any?
                        )
                    }
                }
                EventConstant.APP_BACK_ID -> {
                    showCalloutToolsBar(false)
                    control!!.getSysKit().calloutManager.drawingMode = 0
                }
                EventConstant.APP_PEN_ID -> if (!(obj as Boolean)) {
                    control!!.getSysKit().calloutManager.drawingMode = 0
                } else {
                    control!!.getSysKit().calloutManager.drawingMode = 1
                    setEraserUnChecked()
                    appFrame!!.post {
                        this.control!!.actionEvent(
                            EventConstant.APP_INIT_CALLOUTVIEW_ID,
                            null as Any?
                        )
                    }
                }
                EventConstant.APP_ERASER_ID -> if (!(obj as Boolean)) {
                    control!!.getSysKit().calloutManager.drawingMode = 0

                } else {
                    control!!.getSysKit().calloutManager.drawingMode = 2
                    setPenUnChecked()

                }
                EventConstant.APP_COLOR_ID -> {
                    val colorPickerDialog = ColorPickerDialog(this, control)
                    colorPickerDialog.show()
                    colorPickerDialog.setOnDismissListener { this.setButtonEnabled(true) }
                    setButtonEnabled(false)
                }
                else ->
                    when (i) {
                        EventConstant.APP_FINDING -> {
                            val trim = (obj as String).trim { it <= ' ' }
                            if (trim.length > 0 && control!!.find.find(trim)) {
                                setFindBackForwardState(true)

                            } else {
                                setFindBackForwardState(false)
                                toast!!.setText(getLocalString("DIALOG_FIND_NOT_FOUND"))
                                toast!!.show()

                            }
                        }
                        EventConstant.APP_FIND_BACKWARD -> {
                            if (control!!.find.findBackward()) {
                                searchBar!!.setEnabled(EventConstant.APP_FIND_FORWARD, true)

                            } else {
                                searchBar!!.setEnabled(EventConstant.APP_FIND_BACKWARD, false)
                                toast!!.setText(getLocalString("DIALOG_FIND_TO_BEGIN"))
                                toast!!.show()
                            }
                        }
                        EventConstant.APP_FIND_FORWARD -> {
                            try {
                                if (control!!.find.findForward()) {
                                    searchBar!!.setEnabled(EventConstant.APP_FIND_BACKWARD, true)
                                } else {
                                    searchBar!!.setEnabled(EventConstant.APP_FIND_FORWARD, false)
                                    toast!!.setText(getLocalString("DIALOG_FIND_TO_END"))
                                    toast!!.show()
                                }
                            } catch (e: Exception) {
                                control!!.getSysKit().errorKit.writerLog(e)
                            }
                            false
                        }
                    else -> false
                }
            }
        } else {
            fileShare()
        }
        return true
    }

    private fun fileShare() {

    }

    override fun error(i: Int) {

    }

    @SuppressLint("ResourceType")
    override fun fullScreen(z: Boolean) {
        fullscreen = z
        if (z) {
            val windowManager: WindowManager = this.wm!!
            wmParams!!.gravity = 53
            wmParams!!.x = 5
            this.wm!!.addView(penButton, wmParams)
            wmParams!!.gravity = 53
            wmParams!!.x = 5
            val layoutParams = wmParams
            layoutParams!!.y = layoutParams.height
            this.wm!!.addView(eraserButton, wmParams)
            wmParams!!.gravity = 53
            wmParams!!.x = 5
            val layoutParams2 = wmParams
            layoutParams2!!.y = layoutParams2.height * 2
            this.wm!!.addView(settingsButton, wmParams)
            wmParams!!.gravity = 19
            wmParams!!.x = 5
            wmParams!!.y = 0
            this.wm!!.addView(pageUp, wmParams)
            wmParams!!.gravity = 21
            this.wm!!.addView(pageDown, wmParams)
            (window.findViewById<View>(16908310).parent as View).visibility =
                View.GONE
            toolsbar!!.visibility =   View.GONE
            gapView!!.visibility =   View.GONE
            penButton!!.state = 2
            eraserButton!!.state = 2
            val attributes = window.attributes
            attributes.flags = attributes.flags or 1024
            window.attributes = attributes
            window.addFlags(512)
            return
        }
        this.wm!!.removeView(pageUp)
        this.wm!!.removeView(pageDown)
        this.wm!!.removeView(penButton)
        this.wm!!.removeView(eraserButton)
        this.wm!!.removeView(settingsButton)
        (window.findViewById<View>(16908310).parent as View).visibility =
            View.VISIBLE
        toolsbar!!.visibility = View.VISIBLE
        gapView!!.visibility = View.VISIBLE
        val attributes2 = window.attributes
        attributes2.flags = attributes2.flags and -1025
        window.attributes = attributes2
        window.clearFlags(512)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun getAppName(): String {
        return getString(R.string.app_name);
    }

    fun setButtonEnabled(z: Boolean) {
        if (fullscreen) {
            pageUp!!.isEnabled = z
            pageDown!!.isEnabled = z
            penButton!!.isEnabled = z
            eraserButton!!.isEnabled = z
            settingsButton!!.isEnabled = z
        }
    }

    override fun getBottomBarHeight(): Int {
        val sheetBar: SheetBar = bottomBar!!
        return sheetBar?.sheetbarHeight ?: 0
    }

    override fun getLocalString(str: String?): String {
        return ResKit.instance().getLocalString(str);
    }

    override fun getPageListViewMovingPosition(): Byte {
        return 0
    }

    override fun getTXTDefaultEncode(): String {
        return "GBK";
    }

    override fun getTemporaryDirectory(): File {
        val externalFilesDir = getExternalFilesDir(null as String?)
        return externalFilesDir ?: filesDir
    }

    override fun getTopBarHeight(): Int {
        return 0
    }

    override fun getViewBackground(): Any {
        return 0
    }

    override fun getWordDefaultView(): Byte {
        return 0
    }

    override fun isChangePage(): Boolean {
        return false
    }

    override fun isDrawPageNumber(): Boolean {
        return false
    }

    override fun isIgnoreOriginalSize(): Boolean {
        return false
    }

    override fun isPopUpErrorDlg(): Boolean {
        return false
    }

    override fun isShowFindDlg(): Boolean {
        return false
    }

    override fun isShowPasswordDlg(): Boolean {
        return false
    }

    override fun isShowProgressBar(): Boolean {
        return false
    }

    override fun isShowTXTEncodeDlg(): Boolean {
        return false
    }

    override fun isShowZoomingMsg(): Boolean {
        return false
    }

    override fun isThumbnail(): Boolean {
        return false
    }

    override fun isTouchZoom(): Boolean {
        return false
    }

    override fun isWriteLog(): Boolean {
        return false
    }

    override fun isZoomAfterLayoutForWord(): Boolean {
        return false
    }

    override fun onEventMethod(
        view: View?,
        motionEvent: MotionEvent?,
        motionEvent2: MotionEvent?,
        f: Float,
        f2: Float,
        b: Byte
    ): Boolean {
return false
    }

    override fun openFileFinish() {

    }

    override fun setFindBackForwardState(z: Boolean) {

    }

    override fun setIgnoreOriginalSize(z: Boolean) {

    }

    override fun setThumbnail(z: Boolean) {

    }

    override fun setWriteLog(z: Boolean) {

    }

    override fun showProgressBar(z: Boolean) {

    }

    override fun updateToolsbarStatus() {

    }

    override fun updateViewImages(list: MutableList<Int>?) {

    }
}*/

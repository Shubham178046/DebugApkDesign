package com.example.debugapkdesign.model

open class FileInfoModel {
    var id : Int? = 0
    var title : String? = ""
    var displayName : String? = ""
    var path : String? = ""
    var extension : String? = ""
    var size : Long? = 0
    var dateAdded : Long? = 0
    var dateView : Long? = 0
    var showmenu : Boolean?= false

    override fun toString(): String {
        return "ModelFile{id=" + this.id.toString() + ", title='" + this.title + '\''.toString() + ", display_name='" + this.displayName + '\''.toString() + ", path='" + this.path + '\''.toString() + ", extension='" + this.extension + '\''.toString() + ", size=" + this.size.toString() + ", date_added=" + this.dateAdded.toString() + ", date_view=" + this.dateView.toString() + ", showmenu=" + this.showmenu + '}'
    }
}
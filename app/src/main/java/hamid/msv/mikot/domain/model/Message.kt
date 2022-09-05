package hamid.msv.mikot.domain.model

data class Message(
    val id : String? = null ,
    var text : String? = null ,
    val time : String? = null ,
    val senderId : String? = null ,
    val receiverId : String? = null ,
    var isEdited : Boolean = false ,
    var editTime : String? = null ,
    var isReply : Boolean = false ,
    var repliedMessageId : String? = null ,
//    var is_text : Boolean = true ,
//    var is_image : Boolean = false ,
//    var is_video : Boolean = false ,
//    var is_music : Boolean = false ,
//    var is_location : Boolean = false ,
//    var _image : String? = null ,
//    var _video : String? = null ,
//    var _music : String? = null ,
//    var _location : String? = null
)

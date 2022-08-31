package hamid.msv.mikot.domain.model

data class Message(
    val id : String ,
    var text : String? = null ,
    val time : String ,
    val senderId : String ,
    val receiverId : String ,
    var isEdited : Boolean = false ,
    var editTime : String? = null ,
    var isReply : Boolean = false ,
    var repliedMessageId : String? = null ,
    var isText : Boolean = true ,
    var isImage : Boolean = false ,
    var isVideo : Boolean = false ,
    var isFile : Boolean = false ,
    var isMusic : Boolean = false ,
    var isLocation : Boolean = false ,
    var image : String? = null ,
    var video : String? = null ,
    var music : String? = null ,
    var file : String? = null ,
    var location : String? = null
)

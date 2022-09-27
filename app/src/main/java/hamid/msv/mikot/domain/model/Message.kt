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
    var repliedMessageId : String? = null
)

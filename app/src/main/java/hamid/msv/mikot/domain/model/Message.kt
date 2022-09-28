package hamid.msv.mikot.domain.model

data class Message(
    var id : String? = null ,
    var text : String? = null ,
    var time : String? = null ,
    val senderId : String? = null ,
    val receiverId : String? = null ,
    var isEdited : Boolean = false ,
    var editTime : String? = null ,
    var isReply : Boolean = false ,
    var repliedMessageId : String? = null
)

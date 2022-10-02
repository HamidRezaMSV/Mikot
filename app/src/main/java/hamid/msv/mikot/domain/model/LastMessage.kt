package hamid.msv.mikot.domain.model

data class LastMessage(
    val id: String? = null,
    val text: String? = null,
    var time: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val senderUsername : String? = null,
    val receiverUsername : String? = null,
    var key: String? = null
){

    fun mapToRoomLastMessage(): RoomLastMessage =
        RoomLastMessage(
            id = id!!,
            text = text!!,
            time = time!!,
            senderId = senderId!!,
            receiverId = receiverId!!,
            senderUsername = senderUsername!!,
            receiverUsername = receiverUsername!!,
            key = key!!
        )

}

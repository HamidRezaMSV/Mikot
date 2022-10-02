package hamid.msv.mikot.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class RoomUser(
    @PrimaryKey
    val id : String ,
    @ColumnInfo(name = "full_name")
    var fullName : String ,
    @ColumnInfo(name = "user_name")
    var userName : String ,
    var password : String ,
    var email : String ,
    var bio : String? = null ,
    @ColumnInfo(name = "profile_image")
    var profileImage : String? = null,
    @ColumnInfo(name = "create_acc_time")
    val createAccountTime : String ,
    @ColumnInfo(name = "phone_number")
    val phoneNumber : String
){

    fun mapToMikotUser() =
        MikotUser(
            id,
            fullName,
            userName,
            password,
            email,
            bio,
            profileImage,
            createAccountTime,
            phoneNumber
        )

}

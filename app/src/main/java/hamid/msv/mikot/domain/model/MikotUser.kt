package hamid.msv.mikot.domain.model

import hamid.msv.mikot.util.NO_BIO
import hamid.msv.mikot.util.NO_PROFILE_IMAGE

data class MikotUser(
    val id : String ,
    var fullName : String ,
    var userName : String ,
    var password : String ,
    val email : String ,
    var bio : String = NO_BIO ,
    var profileImage : String = NO_PROFILE_IMAGE,
    val createAccountTime : String ,
    val phoneNumber : String
)

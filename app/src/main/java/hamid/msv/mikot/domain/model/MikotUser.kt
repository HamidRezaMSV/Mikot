package hamid.msv.mikot.domain.model

import hamid.msv.mikot.util.NO_BIO
import hamid.msv.mikot.util.NO_PROFILE_IMAGE

data class MikotUser(
    val id : String? = null ,
    var fullName : String? = null ,
    var userName : String? = null ,
    var password : String? = null ,
    val email : String? = null ,
    var bio : String = NO_BIO ,
    var profileImage : String = NO_PROFILE_IMAGE,
    val createAccountTime : String? = null ,
    val phoneNumber : String? = null
)

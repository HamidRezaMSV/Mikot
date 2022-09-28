package hamid.msv.mikot.domain.model

data class MikotUser(
    var id : String? = null ,
    var fullName : String? = null ,
    var userName : String? = null ,
    var password : String? = null ,
    var email : String? = null ,
    var bio : String? = null ,
    var profileImage : String? = null,
    var createAccountTime : String? = null ,
    var phoneNumber : String? = null
)

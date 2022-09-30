package hamid.msv.mikot.domain.model

sealed class FirebaseResource<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : FirebaseResource<T>(data = data)
    class Error<T>(error: String, data: T? = null) : FirebaseResource<T>(data = data, error = error)
}

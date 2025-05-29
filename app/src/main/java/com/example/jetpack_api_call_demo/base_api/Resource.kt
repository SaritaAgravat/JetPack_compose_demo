package  com.jetpack_demo.base_api

sealed class Resource<out R> {

    data class Success<out T>(val data: T): Resource<T>()

    data class Error(val message: String,val statusCode: Int = 0): Resource<Nothing>()

    object Loading: Resource<Nothing>()
}

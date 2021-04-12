package pl.edu.wat.androidarchitecture.data.repository

import pl.edu.wat.androidarchitecture.model.internal.Resource
import retrofit2.Response
import timber.log.Timber

interface Repository {
    suspend  infix fun <T> resource(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.success(it)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            Timber.e(e)
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network call has failed for a following reason: $message")
    }

}

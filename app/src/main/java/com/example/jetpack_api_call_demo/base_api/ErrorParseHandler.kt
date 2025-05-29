package  com.jetpack_demo.base_api

import org.json.JSONObject

object ErrorParseHandler {
    fun parseErrorMessage(errorBody: String?): String? {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("message")
        } catch (e: Exception) {
            null
        }
    }
}

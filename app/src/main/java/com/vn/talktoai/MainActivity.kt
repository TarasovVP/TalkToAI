package com.vn.talktoai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vn.talktoai.ui.theme.TalkToAITheme
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkToAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
        val api = Client().getRetrofit().create(Api::class.java)
        runBlocking {
            val call: Call<ApiResponse?> =
                api.getCompletions(ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                    Message(role = "user", content = "Who are you?")
                )))
            call.enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>,
                ) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiResponse? = response.body()
                        Log.e("apiTAG", "MainActivity onResponse isSuccessful apiResponse $apiResponse")
                    } else {
                        Log.e("apiTAG", "MainActivity onResponse isSuccessful.not() apiResponse.message ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    Log.e("apiTAG", "MainActivity onFailure Throwable ${t.localizedMessage}")
                }

            })



            /*call?.enqueue(object : Callback<ApiResponse?> {

                fun onResponse(call: Call<ApiResponse?>?, response: Response<ApiResponse?>) {
                    if (response.isSuccessful()) {
                        val data: ApiResponse? = response.body()
                        // Обработайте данные из ответа
                    } else {
                        // Обработайте ошибку запроса
                    }
                }

                fun onFailure(call: Call<ApiResponse?>?, t: Throwable?) {
                    // Обработайте ошибку сети или другие ошибки
                }
            })*/
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TalkToAITheme {
        Greeting("Android")
    }
}
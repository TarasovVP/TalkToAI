package com.vn.talktoai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.vn.talktoai.ui.theme.TalkToAITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var apiService: ApiService

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

        runBlocking {
            val apiRequest = ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                Message(role = "user", content = "Who are you?")
            ))

            apiService.getCompletions(apiRequest).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: Response<ApiResponse>,
                ) {
                    if (response.isSuccessful) {
                        val apiResponse: ApiResponse? = response.body()
                        Log.e("apiTAG", "MainActivity onResponse isSuccessful apiResponse $apiResponse")
                        setContent {
                            CustomTextView(Gson().toJson(apiResponse))
                        }
                    } else {
                        Log.e("apiTAG", "MainActivity onResponse isSuccessful.not() apiResponse.message ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("apiTAG", "MainActivity onFailure Throwable ${t.localizedMessage}")
                }

            })
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

@Composable
fun CustomTextView(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
}
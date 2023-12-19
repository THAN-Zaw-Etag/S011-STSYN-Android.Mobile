package com.etag.stsyn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.etag.stsyn.ui.theme.STSYNTheme
import com.tzh.retrofit_module.ApiClient
import com.tzh.retrofit_module.RetrofitConstant
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseApiInterface = ApiClient.createApiInterface(RetrofitConstant.BASE_URL)


        lifecycleScope.launch {
            try {
                val response = baseApiInterface.getHealthCheck()
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }

        setContent {
            STSYNTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    STSYNTheme {
        Greeting("Android")
    }
}
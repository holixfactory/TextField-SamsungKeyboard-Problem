package com.example.sampleedittext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sampleedittext.ui.theme.SampleEditTextTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleEditTextTheme {
                Surface(color = Color.White) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(
                                text = "기존 Compose Text",
                            )
                            Spacer(modifier = Modifier.requiredHeight(8.dp))
                            DefaultComposeTextField()
                            Spacer(modifier = Modifier.requiredHeight(16.dp))
                            Text(text = "새로운 Compose Text")
                            Spacer(modifier = Modifier.requiredHeight(8.dp))
                            NewCustomTextField()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultComposeTextField() {
    var text by remember {
        mutableStateOf("")
    }

    TextField(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray),
        value = text,
        onValueChange = { text = it }
    )
}

@Composable
fun NewCustomTextField() {
    var text by remember {
        mutableStateOf("")
    }

    CustomTextField(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 60.dp)
            .background(Color.LightGray)
            .padding(16.dp),
        value = text,
        onValueChange = { text = it },
    )
}

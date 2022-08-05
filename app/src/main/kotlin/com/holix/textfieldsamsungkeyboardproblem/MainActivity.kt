package com.holix.textfieldsamsungkeyboardproblem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import com.holix.textfieldsamsungkeyboardproblem.sample.TextFieldContent
import com.holix.textfieldsamsungkeyboardproblem.ui.theme.TextFieldSamsungKeyboardProblemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextFieldSamsungKeyboardProblemTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar {
                            Text(text = "TextField + 삼성키보드 문제 둘러보기")
                        }
                    }
                ) { innerPadding ->
                    TextFieldContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

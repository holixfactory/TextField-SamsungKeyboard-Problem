package com.holix.textfieldsamsungkeyboardproblem.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.TextField as Material2TextField
import androidx.compose.material3.TextField as Material3TextField

@Composable
fun TextFieldContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .scrollable(rememberScrollState(), Orientation.Vertical)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "${Typography.middleDot} compose(1.3.0-alpha02)", style = MaterialTheme.typography.body1)
        Text(text = "${Typography.middleDot} material3(1.0.0-alpha15)", style = MaterialTheme.typography.body1)
        Text(text = "BasicTextField", style = MaterialTheme.typography.subtitle1)
        LatestReleaseBasicTextField()
        Text(text = "Material 2 TextField", style = MaterialTheme.typography.subtitle1)
        LatestReleaseMaterial2TextField()
        Text(text = "Material 3 TextField", style = MaterialTheme.typography.subtitle1)
        LatestReleaseMaterial3TextField()
    }
}

@Composable
private fun LatestReleaseBasicTextField() {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    BasicTextField(
        text,
        setText,
        modifier = Modifier.background(Color.LightGray)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            )
    )
}

@Composable
private fun LatestReleaseMaterial2TextField() {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    Material2TextField(text, setText)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LatestReleaseMaterial3TextField() {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    Material3TextField(text, setText)
}
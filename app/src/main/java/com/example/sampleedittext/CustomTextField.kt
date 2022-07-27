package com.example.sampleedittext

import android.annotation.SuppressLint
import android.os.Build
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    cursorColor: Color? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        TextFieldImpl(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            cursorColor = cursorColor
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun TextFieldImpl(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    cursorColor: Color? = null,
    placeholderText: String? = null,
) {
    val context = LocalContext.current
    if (cursorColor != null) {
        val cursor = ContextCompat.getDrawable(context, R.drawable.cursor)
        cursor?.setTint(cursorColor.toArgb())
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val editText = LayoutInflater.from(it).inflate(
                R.layout.edit_text_field, null
            ) as EditText

            val keyboardActionRunner = KeyboardActionRunner()
            keyboardActionRunner.keyboardActions = keyboardActions

            editText.apply {
                // Text 관련
                setText(value)
                doOnTextChanged { text, _, _, _ -> onValueChange(text.toString()) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setTextCursorDrawable(R.drawable.cursor)
                }
                if (textStyle != null) {
                    with(textStyle) {
                        if (color.isSpecified) {
                            setTextColor(color.toArgb())
                        }
                        if (fontSize.isSpecified) {
                            textSize = fontSize.value
                        }
                        this@apply.fontFeatureSettings = fontFeatureSettings
                        if (letterSpacing.isSpecified) {
                            this@apply.letterSpacing = letterSpacing.value
                        }
                        if (!lineHeight.isSpecified) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                this@apply.lineHeight = lineHeight.value.toInt()
                            }
                        }

                    }
                }
                setMaxLines(maxLines)
                setOnEditorActionListener { _, actionId, _ ->
                    val imeAction = when (actionId) {
                        1 -> ImeAction.None
                        2 -> ImeAction.Go
                        3 -> ImeAction.Search
                        4 -> ImeAction.Send
                        5 -> ImeAction.Next
                        6 -> ImeAction.Done
                        7 -> ImeAction.Previous
                        else -> null
                    }
                    if (imeAction != null) {
                        keyboardActionRunner.runAction(imeAction)
                    }
                    imeAction != null
                }
                // ime setting 및 multiLine 여부
                when (keyboardOptions.keyboardType) {
                    KeyboardType.Text -> {
                        transformationMethod = null
                        inputType = EditorInfo.TYPE_CLASS_TEXT
                    }
                    KeyboardType.Email -> {
                        transformationMethod = null
                        inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    KeyboardType.Password -> {
                        transformationMethod = PasswordTransformationMethod.getInstance()
                        inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    KeyboardType.Uri -> {
                        transformationMethod = null
                        inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_URI
                    }
                    else -> InputType.TYPE_NULL
                }
                imeOptions = when (keyboardOptions.imeAction) {
                    ImeAction.Default -> {
                        if (singleLine) {
                            EditorInfo.IME_ACTION_DONE
                        } else {
                            EditorInfo.IME_ACTION_UNSPECIFIED
                        }
                    }
                    ImeAction.None -> EditorInfo.IME_ACTION_NONE
                    ImeAction.Go -> EditorInfo.IME_ACTION_GO
                    ImeAction.Next -> EditorInfo.IME_ACTION_NEXT
                    ImeAction.Previous -> EditorInfo.IME_ACTION_PREVIOUS
                    ImeAction.Search -> EditorInfo.IME_ACTION_SEARCH
                    ImeAction.Send -> EditorInfo.IME_ACTION_SEND
                    ImeAction.Done -> EditorInfo.IME_ACTION_DONE
                    else -> EditorInfo.IME_NULL
                }

                if (!singleLine) {
                    inputType = inputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                }
                hint = placeholderText
            }
            editText
        },
        update = { editText ->
            // clear text
            if (value.isBlank()) {
                editText.setText("")
            }

            val lastSelectionPoint = editText.selectionEnd

            if (value != editText.text.toString()) {
                val beforeTextLength = editText.text?.length ?: 0
                editText.setText(value)
                editText.setSelection(lastSelectionPoint + (value.length - beforeTextLength))
            }

            when (keyboardOptions.keyboardType) {
                KeyboardType.Text -> {
                    if (editText.transformationMethod != null) {
                        editText.transformationMethod = null
                        editText.setSelection(lastSelectionPoint)
                    }
                }
                KeyboardType.Password -> {
                    if (editText.transformationMethod != PasswordTransformationMethod.getInstance()) {
                        editText.transformationMethod = PasswordTransformationMethod.getInstance()
                        editText.setSelection(lastSelectionPoint)
                    }
                }
            }
        }
    )
}

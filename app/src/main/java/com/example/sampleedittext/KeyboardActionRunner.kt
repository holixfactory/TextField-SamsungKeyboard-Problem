package com.example.sampleedittext

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Default
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Go
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.ImeAction.Companion.None
import androidx.compose.ui.text.input.ImeAction.Companion.Previous
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.text.input.ImeAction.Companion.Send

/**
 * This class can be used to run keyboard actions when the user triggers an IME action.
 */
internal class KeyboardActionRunner : KeyboardActionScope {

    /**
     * The developer specified [KeyboardActions].
     */
    lateinit var keyboardActions: KeyboardActions

    /**
     * Run the keyboard action corresponding to the specified imeAction. If a keyboard action is
     * not specified, use the default implementation provided by [defaultKeyboardAction].
     */
    fun runAction(imeAction: ImeAction) {
        val keyboardAction = when (imeAction) {
            Done -> keyboardActions.onDone
            Go -> keyboardActions.onGo
            Next -> keyboardActions.onNext
            Previous -> keyboardActions.onPrevious
            Search -> keyboardActions.onSearch
            Send -> keyboardActions.onSend
            Default, None -> null
            else -> error("invalid ImeAction")
        }
        keyboardAction?.invoke(this) ?: defaultKeyboardAction(imeAction)
    }

    /**
     * Default implementations for [KeyboardActions].
     */
    override fun defaultKeyboardAction(imeAction: ImeAction) {
        when (imeAction) {
            // Note: Don't replace this with an else. These are specified explicitly so that we
            // don't forget to update this when statement when new imeActions are added.

            // In Compose textField's KeyboardActionRunner, Next and Previous have
            // default action that is moveFocus. But, now focus and focusManager is this
            // project's limitation so we just do nothing.
            Next, Previous, Done, Go, Search, Send, Default, None -> Unit // Do Nothing.
        }
    }
}

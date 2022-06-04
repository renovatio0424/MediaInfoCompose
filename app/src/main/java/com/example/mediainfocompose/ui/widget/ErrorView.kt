package com.example.mediainfocompose.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorPreview() {
    MediaInfoComposeTheme {
        ErrorView("oh my god!! error is occurred")
    }
}

@Composable
fun ErrorView(errorMessage: String?) {
    errorMessage ?: return
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = errorMessage)
    }
}

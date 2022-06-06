package com.example.mediainfocompose.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediainfocompose.R.string
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionScreen(shouldShowRationale: Boolean, onClickRequestPermission: () -> Unit) {
    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (shouldShowRationale) {
            stringResource(string.storage_permission_rationale)
        } else {
            stringResource(string.storage_permission_didnt_show_again)
        }
        Text(textToShow)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onClickRequestPermission) {
            Text(stringResource(string.button_request_storage_permission))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PermissionScreenPreview() {
    MediaInfoComposeTheme {
        RequestPermissionScreen(true) { /* do nothing */ }
    }
}
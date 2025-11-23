package com.example.avitotest.feature.upload.presentation.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.avitotest.R
import com.example.avitotest.feature.profile.presentation.mvi.ProfileEvents
import com.example.avitotest.feature.upload.presentation.mvi.UploadEvents
import com.example.avitotest.feature.upload.presentation.mvi.UploadIntent
import com.example.avitotest.feature.upload.presentation.mvi.UploadState
import com.example.avitotest.feature.upload.presentation.mvi.UploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    uploadViewModel: UploadViewModel = hiltViewModel(),
    showSnackBar: (String, (() -> Unit)?) -> Unit = { _, _ -> }
) {
    val state by uploadViewModel.state.collectAsStateWithLifecycle()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { uploadViewModel.handleIntent(UploadIntent.FileSelected(it)) }
    }

    LaunchedEffect(Unit) {
        uploadViewModel.event.collect { event ->
            when(event){
                is UploadEvents.ShowMessage -> showSnackBar(event.message, event.onRetryAction)
                is UploadEvents.OnChooseFileClicked -> {
                    val mimeTypes = arrayOf("application/pdf", "application/epub+zip", "text/plain")
                    filePickerLauncher.launch(mimeTypes)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.upload_screen_title)) })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = {
                uploadViewModel.handleIntent(UploadIntent.SelectFile)
            }) {
                Text(stringResource(id = R.string.upload_select_file_button))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.selectedFileName,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.title,
                onValueChange = { uploadViewModel.handleIntent(UploadIntent.TitleChanged(it)) },
                label = { Text(stringResource(id = R.string.upload_title_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.author,
                onValueChange = { uploadViewModel.handleIntent(UploadIntent.AuthorChanged(it)) },
                label = { Text(stringResource(id = R.string.upload_author_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (val uploadState = state.uploadState) {
                is UploadState.Idle -> {
                    Button(
                        onClick = { uploadViewModel.handleIntent(UploadIntent.UploadClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.selectedFileUri != null && state.title.isNotBlank() && state.author.isNotBlank()
                    ) {
                        Text(stringResource(id = R.string.upload_upload_button))
                    }
                }

                is UploadState.Uploading -> {
                    val animatedProgress by animateFloatAsState(
                        targetValue = uploadState.progress,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${stringResource(id = R.string.upload_progress)} ${((state.uploadState as UploadState.Uploading).progress) * 100}%")
                }

                else -> {}
            }
        }
    }
}

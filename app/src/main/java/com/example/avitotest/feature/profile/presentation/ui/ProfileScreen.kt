package com.example.avitotest.feature.profile.presentation.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.avitotest.feature.profile.presentation.mvi.ProfileEvents
import com.example.avitotest.feature.profile.presentation.mvi.ProfileIntents
import com.example.avitotest.feature.profile.presentation.mvi.ProfileState
import com.example.avitotest.feature.profile.presentation.mvi.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    showSnackBar: (String, () -> Unit) -> Unit = { _, _ -> }
) {
    val state by profileViewModel.state.collectAsStateWithLifecycle()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                profileViewModel.handleIntent(ProfileIntents.OnImageSelected(uri))
            }
        }

    LaunchedEffect(Unit) {
        profileViewModel.event.collect { event ->
            when (event) {
                is ProfileEvents.ShowMessage -> showSnackBar(event.message, event.onRetryAction)
                is ProfileEvents.OnLogout -> onLogout()
                is ProfileEvents.OnImageSelected -> {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            }
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                actions = {
                    TextButton(
                        onClick = {
                            if (!state.isEditMode) {
                                profileViewModel.handleIntent(ProfileIntents.OnEnterEditMode)
                            } else {
                                profileViewModel.handleIntent(ProfileIntents.OnDisplayNameSave)
                            }
                        },
                        enabled = state.profileState is ProfileState.Idle
                    ) {
                        Text(
                            if (state.isEditMode) "Сохранить" else "Изменить"
                        )
                    }
                }
            )
        }) { innerPadding ->
        when (state.profileState) {
            is ProfileState.Idle -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.isPhotoUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(30.dp)
                                    .align(Alignment.Center)
                            )
                        } else {
                            AsyncImage(
                                model = state.profileData?.photoUri,
                                contentDescription = "Аватар пользователя",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        profileViewModel.handleIntent(ProfileIntents.OnAvatarClick)
                                    },
                                error = rememberVectorPainter(Icons.Default.AccountCircle),
                                placeholder = rememberVectorPainter(Icons.Default.Downloading)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (!state.isEditMode) {
                            if (state.isDisplayNameUploading) {
                                CircularProgressIndicator()
                            } else {
                                ProfileInfoItem(
                                    label = "Имя",
                                    value = state.profileData?.displayName ?: ""
                                )
                            }
                        } else {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.profileData?.displayName ?: "",
                                onValueChange = { newName ->
                                    profileViewModel.handleIntent(
                                        ProfileIntents.OnDisplayNameChanged(
                                            newName
                                        )
                                    )
                                },
                                label = { Text("Имя") },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        profileViewModel.handleIntent(
                                            ProfileIntents.OnDisplayNameSave
                                        )
                                    }
                                )
                            )
                        }
                        if (!state.profileData?.email.isNullOrEmpty()) {
                            ProfileInfoItem(label = "Email", value = state.profileData?.email ?: "")
                        }
                        if (!state.profileData?.phoneNumber.isNullOrEmpty()) {
                            ProfileInfoItem(
                                label = "Телефон",
                                value = state.profileData?.phoneNumber ?: ""
                            )
                        }
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            profileViewModel.handleIntent(ProfileIntents.OnLogout)
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Выйти из аккаунта")
                    }
                }
            }

            is ProfileState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ProfileState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state.profileState as ProfileState.Error).message)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = (state.profileState as ProfileState.Error).retryAction
                    ) {
                        Text("Повторить")
                    }
                }

            }
        }
    }
}

@Composable
private fun ProfileInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
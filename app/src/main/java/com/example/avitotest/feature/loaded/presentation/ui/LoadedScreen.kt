package com.example.avitotest.feature.loaded.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.avitotest.core.theme.AvitoTestTheme
import com.example.avitotest.feature.loaded.presentation.mvi.LoadedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadedScreen(
    loadedViewModel: LoadedViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("123")})
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .padding(16.dp)
                .background(Color.Red)
        ) {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "123"
            )

            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    loadedViewModel.logout()
                }
            ){
                Text( text = "logout")
            }

            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "123"
            )
        }
    }
}

@Preview
@Composable
fun LoadedScreenPreview(){
    AvitoTestTheme {
        LoadedScreen()
    }
}
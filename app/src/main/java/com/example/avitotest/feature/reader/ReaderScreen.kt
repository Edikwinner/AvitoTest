package com.example.avitotest.feature.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen() {
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
                modifier = Modifier.align(Alignment.Center),
                text = "123"
            )

            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "123"
            )

            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "123"
            )
        }
    }
}
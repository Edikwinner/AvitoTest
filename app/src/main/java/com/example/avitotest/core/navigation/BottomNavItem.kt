package com.example.avitotest.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upload
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    companion object {
        fun bottomNavItems(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    title = "Loaded",
                    icon = Icons.Default.Download,
                    route = Screen.LoadedScreen.route
                ),
                BottomNavItem(
                    title = "Upload",
                    icon = Icons.Default.Upload,
                    route = Screen.UploadScreen.route
                ),
                BottomNavItem(
                    title = "Profile",
                    icon = Icons.Default.AccountCircle,
                    route = Screen.ProfileScreen.route
                )
            )
        }
    }
}
package com.learn.splashlearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learn.splashlearn.ui.theme.SplashLearnTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashLearnTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home_screen") {
                    composable("home_screen") { Splash(navController) }
                    composable("Main_screen") { MainContent() }
                }
            }
        }
    }
}

@Composable
fun Sidebar() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(Color.LightGray)
    ) {
        Text("Sidebar Item 1")
        Text("Sidebar Item 2")
        Text("Sidebar Item 3")
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    SplashLearnTheme {
    }
}
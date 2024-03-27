package com.learn.splashlearn


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learn.splashlearn.ui.theme.SplashLearnTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation.navController = rememberNavController()
            SplashLearnTheme {
                NavHost(Navigation.navController as NavHostController, startDestination = "home_screen") {
                    composable("home_screen") { Splash() }
                    composable("dashboard/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        MainContent(name)
                    }
                    composable("Login_screen") { LoginScreen() }
                    composable("Register_screen") { RegistrationScreen() }
                    composable("Reset_screen") { ResetPasswordScreen() }
                    composable("confirm_screen") { ConfirmCodeScreen() }
                    composable("newPass_screen") { NewPasswordScreen() }
                    composable("clientReg_screen") { ClientRegScreen() }
                    composable("regAs_screen") { RegAsWho() }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    SplashLearnTheme {
    }
}
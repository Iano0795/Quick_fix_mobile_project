package com.learn.splashlearn


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learn.splashlearn.login.ArtisanLogin
import com.learn.splashlearn.login.ClientLogin
import com.learn.splashlearn.login.LoginAsWho
import com.learn.splashlearn.mainContent.MainContent
import com.learn.splashlearn.mainContent.ProfileScreen
import com.learn.splashlearn.register.ClientRegScreen
import com.learn.splashlearn.register.RegAsWho
import com.learn.splashlearn.register.RegistrationScreen
import com.learn.splashlearn.reset.ConfirmCodeScreen
import com.learn.splashlearn.reset.NewPasswordScreen
import com.learn.splashlearn.reset.ResetPasswordScreen
import com.learn.splashlearn.ui.theme.SplashLearnTheme
import com.google.firebase.FirebaseApp
import com.learn.splashlearn.mainContent.CardReviewScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.learn.splashlearn.getArtisanDetailsFromFirestore
import androidx.compose.runtime.*
import com.learn.splashlearn.mainContent.AssignedJobs
import com.learn.splashlearn.mainContent.JobsGiven
import com.learn.splashlearn.mainContent.ReviewJobs
import com.learn.splashlearn.mainContent.Reviews
import com.learn.splashlearn.mainContent.artisanProfileScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            Navigation.navController = rememberNavController()
            SplashLearnTheme {
                NavHost(Navigation.navController as NavHostController, startDestination = "home_screen") {
                    composable("home_screen") { Splash() }
                    composable("dashboard/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        MainContent(user)
                    }

                    composable("loginAs_screen") { LoginAsWho() }
                    composable("artisanLogin_screen") { ArtisanLogin() }
                    composable("clientlogin_screen") { ClientLogin() }
                    composable("Register_screen") { RegistrationScreen() }
                    composable("Reset_screen") { ResetPasswordScreen() }
                    composable("confirm_screen") { ConfirmCodeScreen() }
                    composable("newPass_screen") { NewPasswordScreen() }
                    composable("clientReg_screen") { ClientRegScreen() }
                    composable("regAs_screen") { RegAsWho() }
                    composable("assigned_screen/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        AssignedJobs(user)
                    }
                    composable("jobsgiven_screen/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        JobsGiven(user)
                    }
                    composable("reviewjobs_screen/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        ReviewJobs(user)
                    }
                    composable("yourreviews_screen/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        Reviews(user)
                    }
                    composable("profile/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        ProfileScreen(user)
                    }
                    composable("artisanProfile/{name}") { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val user = User(name ?: "")
                        artisanProfileScreen(user)
                    }
                    composable("review_screen/{artisanName}") { backStackEntry ->
                        val artisanName = backStackEntry.arguments?.getString("artisanName")
                        val user = User(artisanName ?: "")
                        var artisan by remember { mutableStateOf<Artisan?>(null) }

                        LaunchedEffect(artisanName) {
                            val retrievedArtisan = getArtisanDetailsFromFirestore(artisanName)
                            artisan = retrievedArtisan
                        }

                        artisan?.let { CardReviewScreen(artisan = it, user) }
                    }


                }
            }
        }
    }
}
















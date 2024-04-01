package com.learn.splashlearn.login

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.Navigation.navController
import com.learn.splashlearn.R
import com.learn.splashlearn.User

@Composable
fun ArtisanLogin() {
    val navController = Navigation.navController
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Karibu",
            fontSize = 40.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable { navController.navigate("home_Screen") }
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                if (emailErrorState.value) {
                    emailErrorState.value = false
                }
                email.value = it
            },

            modifier = Modifier.fillMaxWidth(),
            isError = emailErrorState.value,
            label = {
                Text(text = "Email*")
            },
            textStyle = TextStyle(color = Color.Black)
        )
        Spacer(modifier = Modifier.height(16.dp))
        val passwordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                if (passwordErrorState.value) {
                    passwordErrorState.value = false
                }
                password.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password*")
            },
            isError = passwordErrorState.value,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    Icon(
                        painter = if (passwordVisibility.value) painterResource(id = R.drawable.visibility_off) else painterResource(
                            id = R.drawable.visibility
                        ),
                        contentDescription = "visibility",
                        tint = Color.Black
                    )
                }
            },
            visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(color = Color.Black)
        )
        if (passwordErrorState.value) {
            val msg = if (password.value.text.isEmpty()) {
                "Required"
            } else if (password.value.text.length < 6) {
                "Password must be at least 6 characters long"
            } else {
                ""
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Forgot Password?",
            color = Color.Gray,
            modifier = Modifier.clickable { navController.navigate("reset_Screen") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(lisInternetConnected(context)){
                    when{
                        email.value.text.isEmpty() -> {
                            emailErrorState.value = true
                        }

                        password.value.text.isEmpty() -> {
                            passwordErrorState.value = true
                        }
                        else -> {
                            loading = true
                            login(email.value.text, password.value.text){
                                    success, errorMessage, User ->
                                loading = false
                                if(success){
                                    Log.d("Debug", "User: $User")
                                    Log.d("Debug", "User name: ${User?.name}")

                                    if (!User?.name.isNullOrBlank()) {
                                        User?.let {
                                            Toast.makeText(
                                                context,
                                                "Login successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("dashboard/${it.name}")
                                        }
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "You are not an artisan!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else{
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                }else{
                    Toast.makeText(
                        context,
                        "Please check your internet connection and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White) // Show CircularProgressIndicator when loading
            } else {
                Text(text = "Login", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Black
            )
            Text(
                text = "Register",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigate("regAs_Screen") }
            )
        }
    }
}

fun login(email: String, password: String, onComplete: (Boolean, String?, User?) -> Unit) {
    val firebaseauth = FirebaseAuth.getInstance()

    firebaseauth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful, trigger retrieval of additional user data
                val uid = task.result?.user?.uid
                retrieveUserData(uid) { success, errorMessage, name ->
                    if (success) {
                        onComplete(true, null, User(name ?: ""))
                    } else {
                        onComplete(false, errorMessage, null)
                    }
                }
            } else {
                onComplete(false, task.exception?.message, null)
            }
        }
}
fun retrieveUserData(uid: String?, onComplete: (Boolean, String?, String?) -> Unit) {
    if (uid == null) {
        onComplete(false, "User ID is null", null)
        return
    }

    // Retrieve additional user data (e.g., name) from Firestore or other data source
    FirebaseFirestore.getInstance().collection("artisans").document(uid)
        .get()
        .addOnSuccessListener { document ->
            val name = document?.getString("name")
            onComplete(true, null, name)
        }
        .addOnFailureListener { exception ->
            onComplete(false, exception.message, null)
        }
}
fun lisInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen(navController)
//}

package com.learn.splashlearn.register

import android.content.Context
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R
import com.learn.splashlearn.User

@Composable
fun RegistrationScreen() {
    val context = LocalContext.current
    val name = remember {
        mutableStateOf(TextFieldValue())
    }

    val email = remember { mutableStateOf(TextFieldValue()) }
    val countryCode = remember { mutableStateOf(TextFieldValue()) }
    val mobileNo = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue()) }
    val navController = Navigation.navController
    var loading by remember { mutableStateOf(false) }
    var selectedWork by remember { mutableStateOf(" ") }
    val itemList = listOf<String>("Plumber", "Carpenter", "Tailor")
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var buttonModifier = Modifier
        .width(300.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(10.dp))
    val nameErrorState = remember { mutableStateOf(false) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Box(modifier = Modifier
        .height(300.dp)
        .fillMaxWidth()){
        Image(
            painter = painterResource(id = R.drawable.img_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {
        CustomClickableText(navController, "Artisan", "Registration")
        Spacer(Modifier.size(16.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                if (nameErrorState.value) {
                    nameErrorState.value = false
                }
                name.value = it
            },

            modifier = Modifier.fillMaxWidth(),
            isError = nameErrorState.value,
            label = {
                Text(text = "Name*")
            },
            textStyle = TextStyle(color = Color.White)
        )
        if (nameErrorState.value) {
            Text(text = "Required", color = Color.Red)
        }
        Spacer(Modifier.size(16.dp))

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
            textStyle = TextStyle(color = Color.White)
        )
        if (emailErrorState.value) {
            Text(text = "Required", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row {
            OutlinedTextField(
                value = countryCode.value,
                onValueChange = {
                    countryCode.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(0.3f),
                label = {
                    Text(text = "Code")
                },
                textStyle = TextStyle(color = Color.Black)

            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = mobileNo.value,
                onValueChange = {

                    mobileNo.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    autoCorrect = false
                ),
                label = {
                    Text(text = "Mobile No")
                },
                textStyle = TextStyle(color = Color.Black)
            )
        }

        Spacer(Modifier.size(16.dp))
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
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(color = Color.Black)
        )
        if (passwordErrorState.value) {
            val msg = if (confirmPassword.value.text.isEmpty()) {
                "Required"
            } else if (password.value.text.length < 6) {
                "Password must be at least 6 characters long"
            } else {
                ""
            }
        }

        Spacer(Modifier.size(16.dp))
        val cPasswordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = {
                if (confirmPasswordErrorState.value) {
                    confirmPasswordErrorState.value = false
                }
                confirmPassword.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordErrorState.value,
            label = {
                Text(text = "Confirm Password*")
            },
            trailingIcon = {
                IconButton(onClick = {
                    cPasswordVisibility.value = !cPasswordVisibility.value
                }) {
                    Icon(
                        painter = if (passwordVisibility.value) painterResource(id = R.drawable.visibility_off) else painterResource(
                            id = R.drawable.visibility
                        ),
                        contentDescription = "visibility",
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (cPasswordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(color = Color.Black)
        )
        if (confirmPasswordErrorState.value) {
            val msg = if (confirmPassword.value.text.isEmpty()) {
                "Required"
            } else if (confirmPassword.value.text != password.value.text) {
                "Password not matching"
            } else if (password.value.text.length < 6) {
                "Password must be at least 6 characters long"
            } else {
                ""
            }
            Text(text = msg, color = Color.Red)
        }
        Spacer(Modifier.size(16.dp))
        DropdownList(itemList = itemList, selectedIndex = selectedIndex, modifier = buttonModifier, onItemSelected = { work ->
            selectedWork = work
        })
        Spacer(Modifier.size(16.dp))
        val fullMobileNo = "+${countryCode.value.text}${mobileNo.value.text}"
        Button(
            onClick = {
                if(isInternetConnected(context)){
                    when {
                        name.value.text.isEmpty() -> {
                            nameErrorState.value = true
                        }

                        email.value.text.isEmpty() -> {
                            emailErrorState.value = true
                        }

                        password.value.text.isEmpty() -> {
                            passwordErrorState.value = true
                        }

                        confirmPassword.value.text.isEmpty() -> {
                            confirmPasswordErrorState.value = true
                        }

                        confirmPassword.value.text != password.value.text -> {
                            confirmPasswordErrorState.value = true
                        }

                        else -> {
                            loading = true
                            createUserWithEmail(email.value.text.toString(), password.value.text.toString(), name.value.text.toString(),
                                fullMobileNo, selectedWork) { success, errorMessage, user ->
                                loading = false
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Registered successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    user?.let { navController.navigate("dashboard/${it.name}") }
                                } else {
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }


                        }
                    }
                }else {
                    // No internet connection, show error message
                    Toast.makeText(
                        context,
                        "Please check your internet connection and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            },
            modifier = Modifier
                .fillMaxWidth()
        ){
            if (loading) {
                CircularProgressIndicator(color = Color.White) // Show CircularProgressIndicator when loading
            } else {
                Text(text = "Register", color = Color.White)
            }
        }
        Spacer(Modifier.size(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = {
                navController.navigate("loginAs_screen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(text = "Login", color = Color.Blue)
            }
        }
    }
}

@Composable
fun CustomClickableText(navController: NavController, firstText: String, secondText: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append(firstText)
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append(secondText)
            }
        },
        fontSize = 30.sp,
        modifier = Modifier.clickable { navController.navigate("home_Screen") },
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )
}
fun createUserWithEmail(
    email: String,
    password: String,
    name: String,
    phoneNumber: String,
    selectedWork: String,
    onComplete: (Boolean, String?, User?) -> Unit
) {
    FirebaseAuth.getInstance()
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // User creation successful, now add additional data to Firestore
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userMap = hashMapOf(
                        "name" to name,
                        "mobileNumber" to phoneNumber,
                        "Work" to selectedWork
                    )

                    FirebaseFirestore.getInstance().collection("artisans")
                        .document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            // Additional data added successfully, return the User object
                            onComplete(true, null, User(name))
                        }
                        .addOnFailureListener { exception ->
                            // Error adding additional data
                            onComplete(false, exception.message, null)
                        }
                } else {
                    onComplete(false, "User ID is null", null)
                }
            } else {
                // User creation failed
                onComplete(false, task.exception?.message, null)
            }
        }
}


fun isInternetConnected(context: Context): Boolean {
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



@Composable
fun DropdownList(itemList: List<String>, selectedIndex: Int, modifier: Modifier, onItemSelected: (String) -> Unit ) {

    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedItemIndex by remember { mutableStateOf(selectedIndex) }


    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Box(
            modifier = modifier
                .background(Color.White)
                .clickable { showDropdown = true },
            contentAlignment = Alignment.Center
        ) {
            Text(text = itemList[selectedItemIndex], modifier = Modifier.padding(3.dp))
        }

        Box() {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    onDismissRequest = { showDropdown = false } // Remove this line
                ) {
                    Column(
                        modifier = modifier
                            .heightIn(max = 90.dp)
                            .verticalScroll(state = scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        itemList.forEachIndexed { index, item ->
                            if (index != 0) {
                                Divider(thickness = 1.dp, color = Color.LightGray)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedItemIndex = index
                                        onItemSelected(itemList[index])
                                        showDropdown = false
                                        // Remove the following line
                                        // showDropdown = !showDropdown
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item,)
                            }
                        }
                    }
                }
            }
        }

    }

}

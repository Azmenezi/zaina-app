package com.nbk.zaina.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nbk.rise.R
import com.nbk.rise.data.requests.RegisterRequest
import com.nbk.rise.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val uiState by authViewModel.uiState

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.blurry_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Overlay for subtle darkening effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Join RISE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text("Position (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = myTextFieldColors()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Passwords do not match!")
                            }
                        } else {
                            val request = RegisterRequest(
                                name = name,
                                email = email,
                                password = password,
                                company = if (company.isNotBlank()) company else null,
                                position = if (position.isNotBlank()) position else null,
                                bio = if (bio.isNotBlank()) bio else null
                            )
                            authViewModel.register(request, onRegisterSuccess)
                        }
                    },
                    enabled = name.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank() &&
                            !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Register", fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back", color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
            authViewModel.clearError()
        }
    }
}

@Composable
fun myTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
        cursorColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White.copy(alpha = 0.8f),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )
}


//package com.nbk.zaina.ui.screens.auth
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.nbk.rise.R
//import com.nbk.rise.data.requests.RegisterRequest
//import com.nbk.rise.viewmodels.AuthViewModel
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegisterScreen(
//    authViewModel: AuthViewModel,
//    onRegisterSuccess: () -> Unit,
//    onNavigateBack: () -> Unit
//) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var company by remember { mutableStateOf("") }
//    var position by remember { mutableStateOf("") }
//    var bio by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//    val uiState by authViewModel.uiState
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { innerPadding ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            // Background Image
//            Image(
//                painter = painterResource(id = R.drawable.blurry_background),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//
//            // Optional dark overlay for better contrast
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f))
//            )
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(24.dp)
//                    .clip(RoundedCornerShape(24.dp))
//                    .background(Color.White.copy(alpha = 0.1f))
//                    .padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    "Join RISE",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//
//                Spacer(Modifier.height(24.dp))
//
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Name") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Password") },
//                    singleLine = true,
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                            Icon(
//                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
//                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = confirmPassword,
//                    onValueChange = { confirmPassword = it },
//                    label = { Text("Confirm Password") },
//                    singleLine = true,
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = company,
//                    onValueChange = { company = it },
//                    label = { Text("Company (Optional)") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = position,
//                    onValueChange = { position = it },
//                    label = { Text("Position (Optional)") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = bio,
//                    onValueChange = { bio = it },
//                    label = { Text("Bio (Optional)") },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = myTextFieldColors()
//                )
//
//                Spacer(Modifier.height(24.dp))
//
//                Button(
//                    onClick = {
//                        if (password != confirmPassword) {
//                            coroutineScope.launch {
//                                snackbarHostState.showSnackbar("Passwords do not match!")
//                            }
//                        } else {
//                            val request = RegisterRequest(
//                                name = name,
//                                email = email,
//                                password = password,
//                                company = if (company.isNotBlank()) company else null,
//                                position = if (position.isNotBlank()) position else null,
//                                bio = if (bio.isNotBlank()) bio else null
//                            )
//                            authViewModel.register(request, onRegisterSuccess)
//                        }
//                    },
//                    enabled = name.isNotBlank() &&
//                            email.isNotBlank() &&
//                            password.isNotBlank() &&
//                            confirmPassword.isNotBlank() &&
//                            !uiState.isLoading,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Black,
//                        contentColor = Color.White
//                    )
//                ) {
//                    if (uiState.isLoading) {
//                        CircularProgressIndicator(
//                            color = Color.White,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    } else {
//                        Text("Register", fontWeight = FontWeight.Bold)
//                    }
//                }
//
//                TextButton(
//                    onClick = onNavigateBack,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Back", color = Color.White.copy(alpha = 0.7f))
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(uiState.error) {
//        uiState.error?.let { errorMessage ->
//            snackbarHostState.showSnackbar(
//                message = errorMessage,
//                withDismissAction = true,
//                duration = SnackbarDuration.Long
//            )
//            authViewModel.clearError()
//        }
//    }
//}
//
//@Composable
//fun myTextFieldColors(): TextFieldColors {
//    return TextFieldDefaults.colors(
//        focusedIndicatorColor = Color.White,
//        unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
//        focusedLabelColor = Color.White,
//        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
//        cursorColor = Color.White,
//        focusedTextColor = Color.White,
//        unfocusedTextColor = Color.White.copy(alpha = 0.8f),
//        focusedContainerColor = Color.Transparent,
//        unfocusedContainerColor = Color.Transparent
//    )
//}

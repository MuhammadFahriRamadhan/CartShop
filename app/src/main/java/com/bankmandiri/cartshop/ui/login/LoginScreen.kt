package com.bankmandiri.cartshop.ui.login

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import com.bankmandiri.cartshop.core.exception.Failure
import com.bankmandiri.cartshop.core.util.ShowSnackBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController : NavController) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: LoginViewModel = koinViewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val loginState = viewModel.isLoginComplete.asFlow().collectAsState(null).value
    val isLoading = viewModel.isLoadingLiveData.asFlow().collectAsState(false)
    val failureState = viewModel.failureLiveData.asFlow().collectAsState(null).value
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(), // Light greenish background
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cart Shop",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2AB3A6),
            modifier = Modifier.clickable {
              backDispatcher?.onBackPressed()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Masuk ke Akun",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Masukkan username dan password Anda",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Masukkan username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2AB3A6),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedContainerColor = Color.White, // Background color when focused
                        unfocusedContainerColor = Color.White // Light greenish background
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Masukkan password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2AB3A6),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedContainerColor = Color.White, // Background color when focused
                        unfocusedContainerColor = Color.White // Light greenish background
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lupa password?",
                    fontSize = 14.sp,
                    color = Color(0xFF2AB3A6),
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.login(username, password)
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF2AB3A6)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isLoading.value) "Loading..." else "Masuk", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Belum punya akun? Daftar sekarang",
                    fontSize = 14.sp,
                    color = Color(0xFF2AB3A6)
                )
            }
        }
    }
    LaunchedEffect(loginState) {
        if (loginState == true) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    LaunchedEffect(failureState) {
        when (failureState) {
            is Failure.ServerError -> {
                val message =  failureState.message
                if (message.contains("401")){
                    viewModel.findUser(username, password)
                }else{
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }

    }


}
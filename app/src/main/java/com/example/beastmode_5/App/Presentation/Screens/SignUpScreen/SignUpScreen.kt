package com.example.beastmode_5.App.Presentation.Screens.SignUpScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.beastmode_5.App.Presentation.Navigation.BeastMode5Routes
import com.example.beastmode_5.App.Presentation.Viewmodels.AuthenticationViewModel
import com.example.beastmode_5.R
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.SecondaryText

@Composable
fun SignUpScreen(viewModel: AuthenticationViewModel,navController: NavController) {
    val context = LocalContext.current
    val primaryColor = Color(0xFF172B4D)
    val backgroundColor = Color(0xFFF7F8FA)
    Box(modifier = Modifier.fillMaxSize()
        .background(backgroundColor)){

        when{
            viewModel.isloading->{
                Box(modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        color = primaryColor
                    )
                }
            }
            else->{
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    Image(
                        painter = painterResource(R.drawable.icon2),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sign up to get started",
                        fontSize = 18.sp,
                        color = HomeSecondaryText,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.updateemail(it)
                        },
                        label = {
                            Text("Email",
                                color= HomePrimaryText
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = primaryColor,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = {
                            viewModel.updatepassword(it)
                        },
                        label = {
                            Text("Password",
                                color= HomePrimaryText)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.togglepasswordvisibility()
                                }
                            ) {
                                Icon(
                                    imageVector =
                                        if (viewModel.ispasswordvisible)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                    contentDescription = "Password Toggle",
                                    tint = HomePrimaryText
                                )
                            }
                        },
                        visualTransformation =
                            if (viewModel.ispasswordvisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = primaryColor,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password
                    OutlinedTextField(
                        value = viewModel.conformpassword,
                        onValueChange = {
                            viewModel.updateconfirmpassword(it)
                        },
                        label = {
                            Text("Confirm Password",
                                color= HomePrimaryText)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.toggleconformpasswordvisibility()
                                }
                            ) {
                                Icon(
                                    imageVector =
                                        if (viewModel.isconfirmpasswordvisible)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                    contentDescription = "Password Toggle",
                                    tint = HomePrimaryText
                                )
                            }
                        },
                        visualTransformation =
                            if (viewModel.isconfirmpasswordvisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = primaryColor,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign Up button
                    Button(
                        onClick = {

                            if (!viewModel.CheckPassword()){

                                Toast.makeText(
                                    context,
                                    "Passwords do not match",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                viewModel.SignUpUser(
                                    viewModel.email,
                                    viewModel.password
                                ) { value, errorMessage ->

                                    if (value) {

                                        Toast.makeText(
                                            context,
                                            "Account created successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        //Navigate
                                        navController.navigate(BeastMode5Routes.HomeScreen){
                                            popUpTo(BeastMode5Routes.SignupScreen){
                                                inclusive=true
                                            }
                                        }
                                        viewModel.resetemail()
                                        viewModel.resetpassword()
                                        viewModel.resetconformpassword()

                                    } else {
                                        Toast.makeText(
                                            context,
                                            errorMessage ?: "Signup failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                            disabledContentColor = Color.Black.copy(0.5f)
                        ),
                        enabled =
                            viewModel.email.isNotBlank() &&
                                    viewModel.password.isNotBlank() &&
                                    viewModel.conformpassword.isNotBlank()
                    ) {

                        Text(
                            text = "Sign Up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login navigation
                    TextButton(
                        onClick = {
                            viewModel.resetpassword()
                            viewModel.resetemail()
                            viewModel.resetconformpassword()
                            navController.popBackStack()
                            // Navigate to Login
                        }
                    ) {

                        Text(
                            text = "Already have an account? Login",
                            color = primaryColor,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
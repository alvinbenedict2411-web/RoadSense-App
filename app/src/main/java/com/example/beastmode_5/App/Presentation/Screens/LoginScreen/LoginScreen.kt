package com.example.beastmode_5.App.Presentation.Screens.LoginScreen

import android.app.Activity
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.beastmode_5.ui.theme.BackgroundColor
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText

@Composable
fun LoginScreen(viewModel: AuthenticationViewModel,navController: NavController) {
    val context = LocalContext.current
    val activity =context as Activity
    val primaryColor = Color(0xFF172B4D)
    val backgroundColor = Color(0xFFF7F8FA)
    LaunchedEffect(viewModel.userloggedin) {
        if (viewModel.userloggedin) {
            // Navigate to another screen or perform other actions
            navController.navigate(BeastMode5Routes.HomeScreen){
                popUpTo(BeastMode5Routes.LoginScreen){
                    inclusive=true
                }
            }
            viewModel.resetuserloggedIn()
        }
    }
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
                    Text(text = "Welcome Back",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color= Color(0xFF1F2937))
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )
                    Text(
                        text = "Sign in to continue",
                        fontSize = 18.sp,
                        color = HomeSecondaryText,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(value = viewModel.email,
                        onValueChange = {viewModel.updateemail(it)},
                        label = {
                            Text(text = "Email",
                                color = HomePrimaryText
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = primaryColor,
                            unfocusedLabelColor = Color.Gray
                        ))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = viewModel.password,
                        onValueChange = {viewModel.updatepassword(it)},
                        label = {
                            Text(text = "Password",
                                color = HomePrimaryText)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.togglepasswordvisibility()
                            }) {
                                Icon(imageVector = if(viewModel.ispasswordvisible)Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Password Toggle",
                                    tint=HomePrimaryText)
                            }
                        },
                        visualTransformation = if(viewModel.ispasswordvisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = primaryColor,
                            unfocusedLabelColor = Color.Gray
                        ))
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        viewModel.LoginUser(viewModel.email,viewModel.password, onResult = {value,errormsg->
                            if(value){
                                // navigate
                                navController.navigate(BeastMode5Routes.HomeScreen){
                                    popUpTo(BeastMode5Routes.LoginScreen){
                                        inclusive=true
                                    }
                                }
                                viewModel.resetemail()
                                viewModel.resetpassword()
                            }
                            else{
                                // show error
                                Toast.makeText(context,errormsg,Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                        modifier = Modifier.fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                            disabledContentColor = Color.Black.copy(0.5f)
                        ),
                        enabled = viewModel.email.isNotBlank() && viewModel.password.isNotBlank()) {
                        Text(text = "Login",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.fillMaxWidth()
                        .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = Color.Black)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)){

                        if(viewModel.googleisloadingIndicator==true) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = primaryColor)
                                }
                            }
                        }
                        else{
                            Button(onClick = {
                                viewModel.GoogleSingnIn(activity, Result = {value,errormsg->
                                    if(value){
                                        // navigate
                                        navController.navigate(BeastMode5Routes.HomeScreen){
                                            popUpTo(BeastMode5Routes.LoginScreen){
                                                inclusive=true
                                            }
                                        }
                                        viewModel.resetemail()
                                        viewModel.resetpassword()
                                    }
                                    else{
                                        Toast.makeText(context,errormsg,Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }, modifier = Modifier.fillMaxWidth(),
                                enabled = viewModel.googleisloadingIndicator==false,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )){

                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically) {

                                    Image(painter = painterResource(R.drawable.googleicon6),
                                        contentDescription = null,
                                        modifier = Modifier.size(33.dp))

                                    Spacer(modifier = Modifier.width(7.dp))

                                    Text(text = "Sign-in with Google",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = HomePrimaryText)
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = {
                        // Navigate to the registration screen or perform other actions
                        navController.navigate(BeastMode5Routes.SignupScreen)
                        viewModel.resetpassword()
                        viewModel.resetemail()
                    },
                        ) {
                        Text("Don't have an account? Sign Up",
                            color = primaryColor,
                            fontSize = 14.sp,)
                    }
                }
            }
        }

    }
}
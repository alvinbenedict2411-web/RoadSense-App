package com.example.beastmode_5.App.Presentation.Viewmodels

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.play.core.aipacks.internal.ap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.maps.android.compose.ComposeUiViewRenderer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthenticationViewModel(val application: Application): ViewModel() {

    private val auth= FirebaseAuth.getInstance()

    var userloggedin by mutableStateOf(false)
        private set

    var googleisloadingIndicator by mutableStateOf(false)
        private set

    var isconfirmpasswordvisible by mutableStateOf(false)
        private set

    var isloading by mutableStateOf(false)
        private set

    var ispasswordvisible by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var conformpassword by mutableStateOf("")
        private set

    fun updateemail(newemail: String){
        email=newemail
    }

    fun updatepassword(newpassword: String){
        password=newpassword
    }

    fun updateconfirmpassword(newconformpassword: String){
        conformpassword=newconformpassword
    }

    fun resetconformpassword(){
        conformpassword=""
    }

    fun togglepasswordvisibility(){
        ispasswordvisible=!ispasswordvisible
    }

    fun resetemail(){
        email=""
    }

    fun resetpassword(){
        password=""
    }

    fun GoogleSingnIn(activity: Activity,Result:(Boolean, String?)-> Unit){
        googleisloadingIndicator=true
        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(activity)

                val googleidoption= GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false)
                    .setServerClientId(activity.getString(R.string.default_web_client_id))
                    .build()

                val request= GetCredentialRequest.Builder()
                    .addCredentialOption(googleidoption)
                    .build()


                val result=credentialManager.getCredential(
                    context = activity,
                    request=request
                )
                isloading=true
                val credential=result.credential

                if(credential is CustomCredential && credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    val googleidtoken= GoogleIdTokenCredential.createFrom(credential.data)
                    val idtoken=googleidtoken.idToken
                    val firebasecrendial= GoogleAuthProvider.getCredential(idtoken,null)
                   val authuser= auth.signInWithCredential(firebasecrendial).await()
                    if(authuser.user!=null){
                        isloading=false
                        Result(true,null)
                    }
                    else{
                        isloading=false
                        googleisloadingIndicator=false
                        Result(false,"Firebase authentication failed")
                    }
                }
                else{
                    isloading=false
                    googleisloadingIndicator=false
                    Toast.makeText(application,"An error occurred try again", Toast.LENGTH_SHORT).show()
                }

            }
            catch (e: CancellationException){
                throw e
            }
            catch (e: Exception){
                Toast.makeText(application,e.message, Toast.LENGTH_SHORT).show()
            }
            finally {
                isloading=false
                googleisloadingIndicator=false
            }
        }

    }

    fun LoginUser(email: String,password: String,onResult:(Boolean,String?)->Unit){
        isloading=true
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task->
            val errormsg=task.exception?.message
            if(task.isSuccessful){
                onResult(true,errormsg)
                isloading=false
            }
            else{
                onResult(false,errormsg)
                isloading=false
            }
        }
    }

    fun SignUpUser(email: String,password: String,onResult:(Boolean,String?)->Unit){
        isloading=true
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            val errormsg=task.exception?.message
            if(task.isSuccessful){
                onResult(true,errormsg)
                isloading=false
            }
            else{
                onResult(false,errormsg)
                isloading=false
            }
        }
    }

    fun CheckUserLoggedIn(){
        if(auth.currentUser!=null){
            // navigate
            userloggedin=true
        }
        else{
            userloggedin=false
        }
    }

    init {
        CheckUserLoggedIn()
    }

    fun CheckPassword(): Boolean{
        return password==conformpassword
    }

    fun toggleconformpasswordvisibility(){
        isconfirmpasswordvisible=!isconfirmpasswordvisible
    }

    fun resetuserloggedIn(){
        userloggedin=false
    }
}
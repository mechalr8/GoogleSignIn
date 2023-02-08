package com.example.googleoauth20signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.security.KeyStore.Builder


class MainActivity : AppCompatActivity() {
    private var signInButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInButton = findViewById(R.id.sign_in_button)
//        signInButton?.setSize(SignInButton.SIZE_STANDARD)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            }
        }

        signInButton?.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            getResult.launch(signInIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val id = account.idToken
            Log.e("idToken: ", id.toString())

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("exp handleSignIn", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account == null) {
            Toast.makeText(this,"Not signed in", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this,"SIGNED IN",Toast.LENGTH_SHORT).show()
            Log.e("msg signInSuccess", "SignIn Success")

            Log.d("Account Info", account.toString())
        }
    }
}
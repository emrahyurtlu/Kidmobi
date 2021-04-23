package com.kidmobi.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kidmobi.R
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.goto
import com.kidmobi.databinding.ActivityLoginBinding
import com.kidmobi.mvvm.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    private val mobileDeviceViewModel: MobileDeviceViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callbackManager = CallbackManager.Factory.create()
        Timber.d("onCreate: ")

        //saveDevice(sharedPrefsUtil.getDeviceId())

        checkIfUserLoggedIn()
    }

    /*private fun saveDevice(uniqueDeviceId: String) {
        GlobalScope.launch()
        {
            try {
                Timber.d("Saving device")
                runOnUiThread {
                    mobileDeviceViewModel.saveDeviceInitially(uniqueDeviceId)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/

    private fun checkIfUserLoggedIn() {
        auth.currentUser?.let { user ->
            if (!user.isAnonymous) {
                this.goto(DashboardActivity::class.java)
                finish()
            }
        }
    }

    fun loginWithFacebook(view: View) {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.d("facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Timber.d("facebook:onCancel")
                    Toast.makeText(
                        baseContext, "Facebook ile giriş iptal edildi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException) {
                    Timber.d(error)
                    Toast.makeText(
                        baseContext, "Facebook ile giriş sırasında hata oluştu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken: $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("handleFacebookAccessToken: successful")
                    this.goto(DashboardActivity::class.java)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.d("handleFacebookAccessToken: ${task.exception}")
                    Toast.makeText(
                        baseContext, getString(R.string.login_auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun loginWithGoogle(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    fun loginAsAnonymously(view: View) {
        val result = auth.signInAnonymously()
        result.addOnCompleteListener(this) {
            if (it.isSuccessful) {
                this.goto(DeviceIdActivity::class.java)
            } else {
                Toast.makeText(
                    baseContext, getString(R.string.login_auth_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(
                    this,
                    getString(R.string.login_google_basarisiz_giris),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (resultCode != RC_GOOGLE_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    this.goto(DashboardActivity::class.java)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.login_kullanici_bilgileri_alinamadi),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 1
    }
}
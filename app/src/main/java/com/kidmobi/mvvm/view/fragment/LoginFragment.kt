package com.kidmobi.mvvm.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.kidmobi.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        //binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        checkIfUserLoggedIn()

        binding.btnLoginFacebook.setOnClickListener { loginWithFacebook() }

        binding.btnLoginGoogle.setOnClickListener { loginWithGoogle() }

        binding.btnLoginAsGuest.setOnClickListener { loginAsAnonymously() }
    }

    private fun checkIfUserLoggedIn() {
        auth.currentUser?.let {
            if (!it.isAnonymous)
                findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }
    }

    private fun loginWithFacebook() {
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
                        context, "Facebook ile giriş iptal edildi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException) {
                    Timber.d(error)
                    Toast.makeText(
                        context, "Facebook ile giriş sırasında hata oluştu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken: $token")
        activity?.let {
            val credential = FacebookAuthProvider.getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("handleFacebookAccessToken: successful")
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.d("handleFacebookAccessToken: ${task.exception}")
                        Toast.makeText(
                            context, getString(R.string.login_auth_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    private fun loginWithGoogle() {
        activity?.let {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(it, gso)

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

    }

    private fun loginAsAnonymously() {
        val result = auth.signInAnonymously()
        activity?.let { activity ->
            result.addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFragment_to_showDeviceIdFragment)
                } else {
                    Toast.makeText(
                        context, getString(R.string.login_auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                    context,
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
        activity?.let { activity ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.login_kullanici_bilgileri_alinamadi),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 1
    }
}
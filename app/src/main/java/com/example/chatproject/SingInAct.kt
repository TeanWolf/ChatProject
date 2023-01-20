//осуществление входа в аккаунт
package com.example.chatproject

import android.content.Intent
import android.content.pm.LauncherActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.chatproject.databinding.ActivitySingInBinding
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SingInAct : AppCompatActivity() {

    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySingInBinding

//подключение аккаунта
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
            //получение информации об аккаунте и подключение аккаунта к firebase
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            //проверка на ошибки
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null)
                {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException)
            {
                Log.d("MyLog", "Api exception")
            }
        }
    //обработка нажатия кнопки входа
        binding.buttonSignIn.setOnClickListener{
            signInWithGoogle()

        }
    }

    private fun getClient(): GoogleSignInClient
    {
        //создаем вход в аккаунт создает токен для регистрации
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    //работа кнопки дя входа
    private fun signInWithGoogle()
    {
        val signInClient = getClient()
        //получение информации о пользователе
        launcher.launch(signInClient.signInIntent)
    }

    //проверяет успешность регистрации
    private fun firebaseAuthWithGoogle(idToken: String)
    {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Log.d("MyLog", "Google signIn done")
            }
            else
            {
                Log.d("MyLog", "Google signIn error")
            }
        }
    }

}
package com.example.chatproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //подключение к бд
        val database = Firebase.database
        //создание пробной записи в БД
        val myRef = database.getReference("message")
        myRef.setValue("Hello!")
    }
}
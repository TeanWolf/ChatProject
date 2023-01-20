package com.example.chatproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatproject.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //активация binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //подключение к бд
        val database = Firebase.database
        //создание пробной записи в БД
        val myRef = database.getReference("message")
        //отправка сообщения по кнопке
        binding.buttonSend.setOnClickListener {
            myRef.setValue("Hello!")
        }
    }

    //добавление сообщений на рабочий стол rview
    private fun onChangeListener(dRef: DatabaseReference)
    {
        //потсоянно проверяет обновления
        dRef.addValueEventListener(object : ValueEventListener {
            //запись изменений
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }
}
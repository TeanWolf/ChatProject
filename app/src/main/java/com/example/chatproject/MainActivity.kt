package com.example.chatproject

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //активация binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setUpActionBar()
        //подключение к бд
        val database = Firebase.database
        //создание пробной записи в БД
        val myRef = database.getReference("message")
        //отправка сообщения по кнопке
        binding.buttonSend.setOnClickListener {
            myRef.child(myRef.push().key ?: "bla").setValue(User(auth.currentUser?.displayName, binding.edMessage.text.toString()))
            //Выключение клавиатуры и фокус на списке
            val view: View? = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }

            binding.rcView.smoothScrollToPosition(binding.rcView.getAdapter()?.itemCount!!.toInt() - 1)
        }
        onChangeListener(myRef)
        initRcView()
    }

    //инициализация списка и заполнение
    private fun initRcView() = with(binding)
    {
        adapter = UserAdapter()
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        rcView.adapter = adapter
    }

    //создание выхода из приложения
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //создание выхода из приложения
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out)
        {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //добавление сообщений на рабочий стол rview
    private fun onChangeListener(dRef: DatabaseReference)
    {
        //потсоянно проверяет обновления
        dRef.addValueEventListener(object : ValueEventListener{
            //запись изменений
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for(s in snapshot.children)
                {
                    val user = s.getValue(User::class.java)
                    if(user != null)list.add(user)
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //добавление иконки и имени
    private fun setUpActionBar()
    {
        val ab = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)
            runOnUiThread{
                ab?.setDisplayHomeAsUpEnabled(true)
                ab?.setHomeAsUpIndicator(dIcon)
                ab?.title = auth.currentUser?.displayName
            }
        }.start()

    }

}
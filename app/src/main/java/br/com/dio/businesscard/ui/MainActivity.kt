package br.com.dio.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.businesscard.App
import br.com.dio.businesscard.R
import br.com.dio.businesscard.databinding.ActivityMainBinding
import br.com.dio.businesscard.util.Image

class MainActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val mainViewModel:MainViewModel by viewModels { MainViewModelFactory((application as App).repository) }
    private val adapter by lazy { BusinessCardAdapter(arrayListOf(),this, mainViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUi()
        getAllBusinessCard()
        insertListener()
    }

    private fun setupUi(){
        binding.rvCards.layoutManager = LinearLayoutManager(this)
        binding.rvCards.adapter = adapter
    }

    private fun insertListener() {
        binding.fab.setOnClickListener{
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllBusinessCard(){
        mainViewModel.getAll().observe(this,{businessCard ->
            adapter.apply {
                addCards(businessCard)
                notifyDataSetChanged()
            }
        } )
    }
}

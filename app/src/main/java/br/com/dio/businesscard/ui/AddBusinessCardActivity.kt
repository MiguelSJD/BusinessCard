package br.com.dio.businesscard.ui

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import br.com.dio.businesscard.App
import br.com.dio.businesscard.R
import br.com.dio.businesscard.data.BusinessCard
import br.com.dio.businesscard.databinding.ActivityAddBusinessCardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddBusinessCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddBusinessCardBinding.inflate(layoutInflater)}

    private val mainViewModel:MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        insertListener()
        changeColor(this)
    }

    private fun changeColor(context: Context){
        binding.tvColorBox.setOnClickListener{
            val singleItems = arrayOf("Preto", "Vermelho", "Verde" ,"Azul", "Magenta")
            val checkedItem = 0

            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.label_pick_the_color))
                .setNeutralButton(resources.getString(R.string.label_cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.label_confirm)) { dialog, which ->
                    when((dialog as AlertDialog).listView.checkedItemPosition){
                        0 -> {
                            binding.tvColorBox.setBackgroundColor(Color.parseColor("#000000"))
                            binding.tvColorBox.setText("#000000")
                        }
                        1 -> {
                            binding.tvColorBox.setBackgroundColor(Color.parseColor("#FF0000"))
                            binding.tvColorBox.setText("#FF0000")
                        }
                        2 -> {
                            binding.tvColorBox.setBackgroundColor(Color.parseColor("#00FF00"))
                            binding.tvColorBox.setText("#00FF00")
                        }
                        3 -> {
                            binding.tvColorBox.setBackgroundColor(Color.parseColor("#0000FF"))
                            binding.tvColorBox.setText("#0000FF")
                        }
                        4 -> {
                            binding.tvColorBox.setBackgroundColor(Color.parseColor("#FF00FF"))
                            binding.tvColorBox.setText("#FF00FF")
                        }
                    }
                }
                .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                    // Respond to item chosen
                }
                .show()
        }
    }

    private fun insertListener() {
        binding.btnCancel.setOnClickListener{
            finish()
        }
        binding.btnSave.setOnClickListener{
            val businessCard = BusinessCard(
                name = binding.tilName.editText?.text.toString(),
                phone = binding.tilPhone.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                company = binding.tilCompany.editText?.text.toString(),
                customBackground = binding.tvColorBox.text.toString()
            )
            mainViewModel.insert(businessCard)
            Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

package br.com.dio.businesscard.ui

import android.app.Activity
import android.graphics.Color
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.businesscard.R
import br.com.dio.businesscard.data.BusinessCard
import br.com.dio.businesscard.databinding.ItemBusinessCardBinding
import br.com.dio.businesscard.util.Image

class BusinessCardAdapter(private val items: ArrayList<BusinessCard>, private val activity: Activity, private val viewModel:MainViewModel): RecyclerView.Adapter<BusinessCardAdapter.ViewHolder>() {

    val views:ArrayList<View> = arrayListOf()
    var isEnabled = false
    val selectedList:ArrayList<BusinessCard> = ArrayList()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBusinessCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.cardItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(view: View?): Boolean {
                if(!isEnabled){
                    val callback: ActionMode.Callback = object : ActionMode.Callback {
                        override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                            val menuInflater = actionMode.menuInflater
                            menuInflater.inflate(R.menu.select_items_menu, menu)
                            return true
                        }

                        override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                            isEnabled = true
                            if (view != null) {
                                clickItem(holder, view)
                            }
                            viewModel.mutableLiveData.observe(activity as LifecycleOwner,
                                { s -> actionMode.title = String.format("$s Selected") })
                            return true
                        }

                        override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
                            when(menuItem.itemId){
                                R.id.menu_delete -> {
                                    selectedList.forEach { s ->
                                        viewModel.deleteCard(s)
                                        notifyDataSetChanged()
                                    }
                                    actionMode.finish()
                                }
                                R.id.menu_share -> {
                                    holder.ivCheckBox.visibility = View.GONE
                                    views.forEach { share(it) }
                                    actionMode.finish()
                                }
                            }
                            return true
                        }

                        override fun onDestroyActionMode(actionMode: ActionMode) {
                            isEnabled = false
                            selectedList.clear()
                            notifyDataSetChanged()
                        }
                    }
                    ((view!!.context as AppCompatActivity).startActionMode(callback))
                }else{
                    view?.let { clickItem(holder, it) }
                }
                return true
            }
        })
        holder.cardItem.setOnClickListener {
            if (isEnabled) clickItem(holder, it)
        }
        if(isEnabled){
            holder.ivCheckBox.visibility = View.VISIBLE
            holder.wholeItem.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.ivCheckBox.visibility = View.GONE
            holder.wholeItem.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    fun share(view: View){
        Image.share(activity, view)
    }

    private fun clickItem(holder: ViewHolder, view: View) {
        val s = items[holder.adapterPosition]
        if (holder.ivCheckBox.visibility == View.GONE) {
            holder.ivCheckBox.visibility = View.VISIBLE
            holder.wholeItem.setBackgroundColor(Color.LTGRAY)
            views.add(view)
            selectedList.add(s)
        } else {
            holder.ivCheckBox.visibility = View.GONE
            holder.wholeItem.setBackgroundColor(Color.TRANSPARENT)
            views.remove(view)
            selectedList.remove(s)
        }
        viewModel.mutableLiveData.value = selectedList.size.toString()
    }

    inner class ViewHolder(
        private val binding: ItemBusinessCardBinding
    ): RecyclerView.ViewHolder(binding.root){
        val ivCheckBox = binding.ivCheckBox
        val cardItem = binding.mcvContent
        val wholeItem = binding.allContent
        fun bind(item:BusinessCard){
            binding.tvName.text = item.name
            binding.tvPhone.text = item.phone
            binding.tvEmail.text = item.email
            binding.tvCompanyName.text = item.company
            binding.mcvContent.setCardBackgroundColor(Color.parseColor(item.customBackground))
        }
    }

    fun addCards(repositories:List<BusinessCard>){
        this.items.apply {
            clear()
            addAll(repositories)
        }
    }

}

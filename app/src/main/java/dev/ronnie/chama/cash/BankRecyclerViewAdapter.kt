package dev.ronnie.chama.cash

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.admin.AdminActivity
import dev.ronnie.chama.admin.TransactionActivity
import dev.ronnie.chama.databinding.BankListBinding
import dev.ronnie.chama.models.Bank

class BankRecyclerViewAdapter(var context: Context, var bankList: MutableList<Bank>) :
    RecyclerView.Adapter<BankRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: BankListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bank_list,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bank = bankList[position]

        holder.setData(bank)
    }

    inner class MyViewHolder(var binding: BankListBinding) : RecyclerView.ViewHolder(binding.root) {

        private var bankSend: Bank? = null
        fun setData(bank: Bank) {

            binding.bank = bank


            bankSend = bank
        }

        init {
            binding.root.setOnClickListener {
                if (AdminActivity.isAdminActivityRunning) {
                    val intent = Intent(context, TransactionActivity::class.java)
                    intent.putExtra("group", AdminActivity.group)
                    intent.putExtra("bank", bankSend)
                    context.startActivity(intent)
                }
            }
        }
    }
}
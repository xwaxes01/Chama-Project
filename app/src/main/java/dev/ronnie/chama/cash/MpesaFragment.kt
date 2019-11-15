package dev.ronnie.chama.cash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.FragmentMpesaBinding
import dev.ronnie.chama.models.Groups


class MpesaFragment : Fragment(), MpesaListener {

    lateinit var viewModel: MpesaViewModel
    lateinit var binding: FragmentMpesaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mpesa, container, false)
        viewModel = ViewModelProviders.of(this)[MpesaViewModel::class.java]
        viewModel.listener = this

        val bundle = arguments
        if (bundle != null) {
            val group = bundle.getParcelable("group") as Groups
            init(group)
            Log.d("Mpesa", "Mpesa Found ${group.group_name}")
        } else {
            Log.d("Mpesa", "Mpesa Group Null")
        }
        return binding.root
    }

    private fun init(group: Groups) {
        viewModel.getMpesa(group).observe(this, Observer {
            val adapter = MpesaRecyclerViewAdapter(context!!, it)
            binding.mpesaRecyclerView.layoutManager =
                LinearLayoutManager(context)
            binding.mpesaRecyclerView.adapter = adapter
        })
    }
}

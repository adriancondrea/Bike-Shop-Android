package com.ilazar.myapp2.bikeStore.bikes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ilazar.myapp2.R
import com.ilazar.myapp2.auth.data.AuthRepository
import com.ilazar.myapp2.core.TAG
import com.ilazar.myapp2.databinding.FragmentBikeListBinding

class BikeListFragment : Fragment() {
    private var _binding: FragmentBikeListBinding? = null
    private lateinit var bikeListAdapter: BikeListAdapter
    private lateinit var bikesModel: BikeListViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        _binding = FragmentBikeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.FragmentLogin)
            return
        }
        setupBikeList()
        binding.fab.setOnClickListener {
            Log.v(TAG, "add new bike")
            findNavController().navigate(R.id.action_BikeListFragment_to_BikeEditFragment)
        }
    }

    private fun setupBikeList() {
        bikeListAdapter = BikeListAdapter(this)
        binding.bikeList.adapter = bikeListAdapter
        bikesModel = ViewModelProvider(this).get(BikeListViewModel::class.java)
        bikesModel.bikes.observe(viewLifecycleOwner, { value ->
            Log.i(TAG, "update bikes")
            bikeListAdapter.bikes = value
        })
        bikesModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        bikesModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        bikesModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}
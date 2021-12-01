package com.ilazar.myapp2.todo.bike

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ilazar.myapp2.core.TAG
import com.ilazar.myapp2.databinding.FragmentBikeEditBinding
import com.ilazar.myapp2.todo.data.Bike

class BikeEditFragment : Fragment() {
    companion object {
        const val BIKE_ID = "BIKE_ID"
    }

    private lateinit var viewModel: BikeEditViewModel
    private var bikeId: String? = null
    private var bike: Bike? = null

    private var _binding: FragmentBikeEditBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        arguments?.let {
            if (it.containsKey(BIKE_ID)) {
                bikeId = it.getString(BIKE_ID).toString()
            }
        }
        _binding = FragmentBikeEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupViewModel()
        binding.fab.setOnClickListener {
            Log.v(TAG, "save bike")
            val i = bike
            if (i != null) {
                i.name = binding.bikeName.text.toString()
                i.condition = binding.bikeCondition.text.toString()
                i.price = binding.bikePrice.text.toString().toInt()
                i.warranty = binding.bikeWarranty.isChecked
                viewModel.saveOrUpdateBike(i)
            }
        }
        //todo: what is this?
//        binding.bikeName.setText(bikeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG, "onDestroyView")
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(BikeEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            binding.progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = bikeId
        if (id == null) {
            bike = Bike("", "", "", false, 0)
        } else {
            viewModel.getBikeById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update bikes")
                if (it != null) {
                    bike = it
                    binding.bikeName.setText(it.name)
                    binding.bikeWarranty.isChecked = it.warranty
                    binding.bikePrice.setText(it.price.toString())
                    binding.bikeCondition.setText(it.condition)
                }
            })
        }
    }
}
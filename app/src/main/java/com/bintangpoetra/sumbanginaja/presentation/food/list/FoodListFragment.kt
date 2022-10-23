package com.bintangpoetra.sumbanginaja.presentation.food.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintangpoetra.sumbanginaja.data.lib.ApiResponse
import com.bintangpoetra.sumbanginaja.databinding.FragmentFoodListBinding
import com.bintangpoetra.sumbanginaja.presentation.home.adapter.FoodAdapter
import com.bintangpoetra.sumbanginaja.utils.ext.hideShimmerLoading
import com.bintangpoetra.sumbanginaja.utils.ext.showShimmerLoading
import org.koin.android.ext.android.inject

class FoodListFragment: Fragment() {

    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodListViewModel by inject()

    private val adapter: FoodAdapter by lazy { FoodAdapter { toDetail(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initProcess()
        initObservers()
    }

    private fun initUI() {
        binding.rvHome.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHome.adapter = this.adapter
    }

    private fun initProcess() {
        viewModel.getFoods()
    }

    private fun initObservers() {
        viewModel.foodResult.observe(viewLifecycleOwner) { response ->
            when(response) {
                is ApiResponse.Loading -> {
                    binding.let {
                        showShimmerLoading(it.foodListShimmeringLoading, it.llShimmeringContainer)
                    }
                }
                is ApiResponse.Success -> {
                    adapter.setData(response.data)
                    binding.let {
                        hideShimmerLoading(it.foodListShimmeringLoading, it.llShimmeringContainer)
                    }
                }
                is ApiResponse.Error -> {
                    binding.let {
                        hideShimmerLoading(it.foodListShimmeringLoading, it.llShimmeringContainer)
                    }
                }
                else -> {
                    binding.let {
                        hideShimmerLoading(it.foodListShimmeringLoading, it.llShimmeringContainer)
                    }
                }
            }
        }
    }

    private fun toDetail(foodId: Int) {
        findNavController().navigate(FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(foodId))
    }

}
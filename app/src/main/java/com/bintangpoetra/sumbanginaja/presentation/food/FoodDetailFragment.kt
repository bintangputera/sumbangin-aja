package com.bintangpoetra.sumbanginaja.presentation.food

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bintangpoetra.sumbanginaja.R
import com.bintangpoetra.sumbanginaja.base.ui.BaseFragment
import com.bintangpoetra.sumbanginaja.data.lib.ApiResponse
import com.bintangpoetra.sumbanginaja.databinding.FragmentFoodDetailBinding
import com.bintangpoetra.sumbanginaja.domain.food.model.Food
import com.bintangpoetra.sumbanginaja.utils.PreferenceManager
import com.bintangpoetra.sumbanginaja.utils.ext.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.android.ext.android.inject
import timber.log.Timber

class FoodDetailFragment : BaseFragment<FragmentFoodDetailBinding>() {

    private val foodDetailViewModel: FoodDetailViewModel by inject()
    private val pref: PreferenceManager by lazy { getPrefManager() }

    private lateinit var mMap: GoogleMap
    private var foodId = 0
    private var isOwnedFood = false
    private var mFood: Food? = null

    override fun initUI() {
        binding.lottieLoading.initLottie()
        binding.toolbarAccount.apply {
            title = context.getString(R.string.title_food_detail)
            setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFoodDetailBinding {
       return FragmentFoodDetailBinding.inflate(inflater, container, false)
    }

    override fun initIntent() {
        val safeArgs = arguments?.let { FoodDetailFragmentArgs.fromBundle(it) }
        foodId = safeArgs?.id ?: 0
    }

    override fun initProcess() {
        foodDetailViewModel.getFoodDetail(foodId)
    }

    override fun initAction() {
        binding.apply {
            btnDetailAction.click {
                if (isOwnedFood) {
                    showBarcodeDialog(mFood?.foodGenerateCode.toString())
                } else {
                    mFood?.let {
                        val message = it.generateMessageToFoodRanger()
                        val phone = it.user?.phoneNumber
                        openWhatsApp(phone?.toWhatsAppNumberFormat().toString(), message)
                    }
                }
            }

            btnOpenMap.click {
                val action = FoodDetailFragmentDirections.actionFoodDetailFragmentToMapFragment(
                    name = mFood?.name ?: "",
                    latitude = (mFood?.latitude ?: 0.0).toFloat(),
                    longitude = (mFood?.longitude ?: 0.0).toFloat(),
                    tag = mFood?.id.toString(),
                )
                findNavController().navigate(action)

            }

        }
    }

    override fun initObservers() {
        foodDetailViewModel.foodDetailResult.observe(viewLifecycleOwner) { response ->
            Timber.d("Response is $response")
            when (response) {
                is ApiResponse.Loading -> {
                    showLoading(binding.loadingContainer)
                }
                is ApiResponse.Success -> {
                    val food = response.data
                    mFood = food
                    binding.apply {
                        imvFood.setImageUrl(food.images.toSumbanginAjaImageUrl())
                        imvFoodOwner.setImageUrl(food.user?.profileUsers.toString())

                        tvFoodName.text = food.name
                        tvFoodOwner.text = food.user?.name
                        tvFoodDescription.text = food.descriptions
                        tvAddress.text = food.address

                        if (food.user?.id.toString().isTheFoodOwner(pref.getUserId.toString())) {
                            isOwnedFood = true
                            btnDetailAction.text = getString(R.string.label_give_food)
                        } else {
                            isOwnedFood = false
                            btnDetailAction.text = getString(R.string.label_call_food_ranger)
                        }

                        val mapFragment =
                            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

                        mapFragment?.getMapAsync { googleMap ->
                            mMap = googleMap
                            val foodLocation = LatLng(food.latitude, food.longitude)
                            mMap.addSingleMarker(
                                location = foodLocation,
                                markerName = food.name,
                                tag = food.id.toString()
                            )
                            mMap.animateCameraToSingleMarker(foodLocation)
                        }
                    }
                    hideLoading(binding.loadingContainer)
                }
                is ApiResponse.Error -> {
                    hideLoading(binding.loadingContainer)

                }
                else -> {
                    hideLoading(binding.loadingContainer)
                }
            }
        }
    }

    private fun openWhatsApp(phone: String, message: String) {
        val url = "https://api.whatsapp.com/send?phone=$phone&text=$message"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}
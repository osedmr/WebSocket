package com.inohom.test.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.inohom.test.R
import com.inohom.test.data.model.Lighting
import com.inohom.test.databinding.FragmentAydinlatmaBinding
import com.inohom.test.ui.adapters.LightingAdapter
import com.inohom.test.ui.viewmodels.AydınlatmaViewModel
import com.inohom.test.util.GridItemDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AydınlatmaFragment : Fragment() {

    private var _binding: FragmentAydinlatmaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AydınlatmaViewModel by viewModels()
    private lateinit var adapter: LightingAdapter

    // Manuel örnek aydınlatma verileri
    val lightingList = mutableListOf<Lighting>(
        Lighting("1", "Yönetim Hariç Spot", true, 1),
        Lighting("2", "Serkan Led", true, 0),
        Lighting("3", "Yönetim Spot", true, 1),
        Lighting("4", "Abaçur", true, 0),
        Lighting("5", "Pano Aydınlatma 2", true, 1),
        Lighting("6", "Teknik Aydınlatma", true, 0),
        Lighting("7", "Arçe WC", true, 1),
        Lighting("8", "Toplantı Aydınlatma", true, 0),
        Lighting("9", "Giriş Aydınlatma", true, 1),
        Lighting("10", "Mutfak Aydınlatma", true, 0),
        Lighting("11", "Bodrum Depo Ayd 1", true, 1),
        Lighting("12", "Sahne Arza Ayd 1", true, 0)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAydinlatmaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        
        // Fragment açıldığında GetControlList isteği gönder
        viewModel.getControlList()
    }
    
    private fun setupRecyclerView() {
        adapter = LightingAdapter()
        binding.recyclerView.adapter = adapter
        adapter.differ.submitList(lightingList)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        
        // Grid çizgileri ekle
        val gridDecoration = GridItemDecoration(
            spanCount = 2,
            spacing = 16,
            includeEdge = true,
            lineColor = ContextCompat.getColor(requireContext(), android.R.color.white),
            lineWidth = 2
        )
        binding.recyclerView.addItemDecoration(gridDecoration)
        
        // Item click listener ekle
        adapter.setOnItemClickListener { lighting ->
            Log.d("AydınlatmaFragment", "Aydınlatma tıklandı: ${lighting.name}")
            
            // Sadece ilk LED (position 0) için UpdateControlValue isteği gönder
            val position = lightingList.indexOfFirst { it.id == lighting.id }
            if (position == 0) {
                Log.d("AydınlatmaFragment", "İlk LED - UpdateControlValue isteği gönderiliyor")
                // Gerçek ID'yi kullan (örnek: "a2830d60-ddff-4dad-8f3d-dfca0ded2462")
                viewModel.updateControlValue("a2830d60-ddff-4dad-8f3d-dfca0ded2462", lighting.currentValue)
            } else {
                Log.d("AydınlatmaFragment", "Diğer LED - GetControlList isteği gönderiliyor")
                viewModel.getControlList()
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.controlListResult.observe(viewLifecycleOwner) { response ->
            Log.d("AydınlatmaFragment", "GetControlList Response alındı: $response")
            
            // Gelen control listesini işle ve listeyi güncelle
            val updatedList = mutableListOf<Lighting>()
            
            response.params.firstOrNull()?.data?.forEach { control ->
                Log.d("AydınlatmaFragment", "Control: ${control.name}, Value: ${control.currentValue}, Type: ${control.typeId}")
                
                // Type ID 1 ise lighting kontrolü
                if (control.typeId == "1") {
                    updatedList.add(
                        Lighting(
                            id = control.id,
                            name = control.name,
                            isActive = control.isActive,
                            currentValue = control.currentValue
                        )
                    )
                }
            }
            
            // Eğer WebSocket'ten veri geldiyse listeyi güncelle
            if (updatedList.isNotEmpty()) {
                adapter.differ.submitList(updatedList)
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("AydınlatmaFragment", "Loading state: $isLoading")
        }
        
        viewModel.entityUpdatedResult.observe(viewLifecycleOwner) { response ->
            Log.d("AydınlatmaFragment", "EntityUpdated Response alındı: $response")
            
            // Güncellenen entity'yi listede bul ve güncelle
            response.params.firstOrNull()?.entity?.let { updatedControl ->
                val updatedList = lightingList.toMutableList()
                val index = updatedList.indexOfFirst { it.id == updatedControl.id }
                if (index != -1) {
                    updatedList[index] = Lighting(
                        id = updatedControl.id,
                        name = updatedControl.name,
                        isActive = updatedControl.isActive,
                        currentValue = updatedControl.currentValue
                    )
                    adapter.differ.submitList(updatedList)
                    Log.d("AydınlatmaFragment", "LED güncellendi: ${updatedControl.name} = ${updatedControl.currentValue}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

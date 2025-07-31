package com.inohom.test.ui.fragments

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inohom.test.R
import com.inohom.test.data.model.Services
import com.inohom.test.databinding.FragmentInohomBinding
import com.inohom.test.ui.adapters.ServicesAdapter
import com.inohom.test.ui.viewmodels.AuthViewModel
import com.inohom.test.util.GridItemDecoration


class InohomFragment : Fragment() {



    private var _binding: FragmentInohomBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var adapter: ServicesAdapter


    val servisList= mutableListOf<Services>(
        Services(1,R.drawable.star,"Favoriler"),
        Services(2,R.drawable.led,"Aydınlatma"),
        Services(3,R.drawable.cctv,"Kamera"),
        Services(4,R.drawable.cctv,"Kamera"),
        Services(5,R.drawable.bell,"Alarm"),
        Services(6,R.drawable.star,"Favoriler"),
        Services(1,R.drawable.star,"Favoriler"),
        Services(2,R.drawable.bell,"Alarm"),
        Services(3,R.drawable.cctv,"Kamera"),
        Services(4,R.drawable.cctv,"Kamera"),
        Services(5,R.drawable.bell,"Alarm"),
        Services(6,R.drawable.star,"Favoriler"),
        Services(1,R.drawable.star,"Favoriler"),
        Services(2,R.drawable.bell,"Alarm"),
        Services(3,R.drawable.cctv,"Kamera"),
        Services(1,R.drawable.star,"Favoriler"),
        Services(2,R.drawable.bell,"Alarm"),
        Services(3,R.drawable.cctv,"Kamera")


    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInohomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ServicesAdapter()
        binding.recyclerView.adapter = adapter
        adapter.differ.submitList(servisList)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        
        // Grid çizgileri ekle
        val gridDecoration = GridItemDecoration(
            spanCount = 3,
            spacing = 16,
            includeEdge = true,
            lineColor = ContextCompat.getColor(requireContext(), android.R.color.white),
            lineWidth = 2
        )
        binding.recyclerView.addItemDecoration(gridDecoration)
        
        // Item click listener ekle
        adapter.setOnItemClickListener { service ->
            when (service.id) {
                2 -> {
                    // ID'si 2 olan "Aydınlatma" item'ine tıklandığında
                    findNavController().navigate(R.id.action_inohomFragment_to_aydınlatmaFragment)
                }
                // Diğer item'lar için gelecekte eklenebilir
            }
        }
    }


}

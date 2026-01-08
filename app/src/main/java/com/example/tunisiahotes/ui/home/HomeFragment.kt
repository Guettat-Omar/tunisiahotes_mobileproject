package com.tunisiahotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunisia.hotes.R
import com.tunisia.hotes.databinding.FragmentHomeBinding
import com.tunisiahotes.adapter.MaisonAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireActivity().application)
    }

    private lateinit var adapter: MaisonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = MaisonAdapter(
            onDetailClick = { maison ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailFragment(maison.id)
                findNavController().navigate(action)
            },
            onDecouvrirClick = { maison ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailFragment(maison.id)
                findNavController().navigate(action)
            },
            onLongPress = { maison ->
                // Ouvrir le dialogue pour ajouter un avis
                showAddAvisDialog(maison.id)
            }
        )

        binding.rvMaisons.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allMaisons.collect { maisons ->
                if (maisons.isEmpty()) {
                    binding.tvEmptyMessage.visibility = View.VISIBLE
                    binding.rvMaisons.visibility = View.GONE
                } else {
                    binding.tvEmptyMessage.visibility = View.GONE
                    binding.rvMaisons.visibility = View.VISIBLE
                    adapter.submitList(maisons)
                }
            }
        }
    }

    private fun showAddAvisDialog(maisonId: Int) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToAddAvisFragment(maisonId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
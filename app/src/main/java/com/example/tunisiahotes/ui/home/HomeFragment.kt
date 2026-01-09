package com.example.tunisiahotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tunisiahotes.R
import com.example.tunisiahotes.adapter.MaisonAdapter
import com.example.tunisiahotes.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import com.example.tunisiahotes.data.entity.MaisonHoteEntity

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

        setupTopButtons()
        setupRecyclerView()
        observeData()
    }

    private fun setupTopButtons() {
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_filterFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = MaisonAdapter(
            onDetailClick = { maison ->
                goToDetail(maison.id)
            },
            onReserveClick = { maison ->
                goToReservation(maison.id)
            },
            onEditClick = { maison ->
                goToEdit(maison.id)
            },
            onDeleteClick = { maison ->
                confirmDelete(maison)
            },
            onLongPress = { maison ->
                goToAddAvis(maison.id)
            }
        )

        binding.rvMaisons.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvMaisons.adapter = adapter
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

    private fun goToDetail(maisonId: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_detailFragment,
            bundleOf("maisonId" to maisonId)
        )
    }

    private fun goToAddAvis(maisonId: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_addAvisFragment,
            bundleOf("maisonId" to maisonId)
        )
    }
    private fun goToReservation(maisonId: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_reservationFragment,
            bundleOf("maisonId" to maisonId)
        )
    }
    private fun goToEdit(maisonId: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_addMaisonFragment,
            bundleOf("maisonId" to maisonId)
        )
    }

    private fun confirmDelete(maison: MaisonHoteEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.maison_delete_title)
            .setMessage(R.string.maison_delete_message)
            .setNegativeButton(R.string.btn_annuler, null)
            .setPositiveButton(R.string.maison_delete) { _, _ ->
                viewModel.deleteMaison(maison)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

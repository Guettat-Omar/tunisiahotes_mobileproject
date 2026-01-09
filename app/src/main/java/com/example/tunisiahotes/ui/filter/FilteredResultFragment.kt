package com.example.tunisiahotes.ui.filter

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tunisiahotes.R
import com.example.tunisiahotes.adapter.MaisonAdapter
import com.example.tunisiahotes.data.database.AppDatabase
import com.example.tunisiahotes.data.database.MaisonRepository
import com.example.tunisiahotes.data.entity.MaisonHoteEntity
import com.example.tunisiahotes.databinding.FragmentFilteredResultBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FilteredResultFragment : Fragment() {

    private var _binding: FragmentFilteredResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MaisonRepository
    private lateinit var adapter: MaisonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteredResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = MaisonRepository(AppDatabase.getDatabase(requireContext()).maisonHoteDao())

        setupRecyclerView()
        loadFilteredResults()
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


        binding.rvMaisons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMaisons.adapter = adapter
    }

    private fun loadFilteredResults() {
        val filterType = arguments?.getString("filterType").orEmpty()
        val filterValue = arguments?.getString("filterValue").orEmpty()

        binding.tvFilterTitle.text = if (filterValue.isNotBlank()) {
            "$filterType : $filterValue"
        } else {
            "Résultats du filtre"
        }

        val flow = getFilteredFlow(filterType, filterValue)
        viewLifecycleOwner.lifecycleScope.launch {
            flow.collect { maisons ->
                updateList(maisons)
            }
        }
    }

    private fun getFilteredFlow(filterType: String, filterValue: String): Flow<List<MaisonHoteEntity>> {
        return when (filterType.lowercase()) {
            "region" -> repository.filterByRegion(filterValue)
            "ville" -> repository.filterByVille(filterValue)
            "saison" -> repository.filterBySaison(filterValue)
            "prix" -> repository.sortByPrix()
            "avis" -> repository.sortByAvis()
            else -> repository.getAllMaisons()
        }
    }

    private fun updateList(maisons: List<MaisonHoteEntity>) {
        if (maisons.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvMaisons.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvMaisons.visibility = View.VISIBLE
            adapter.submitList(maisons)
        }
    }

    private fun goToDetail(maisonId: Int) {
        findNavController().navigate(
            R.id.action_filteredResultFragment_to_detailFragment,
            bundleOf("maisonId" to maisonId)
        )
    }

    private fun goToAddAvis(maisonId: Int) {
        findNavController().navigate(
            R.id.addAvisFragment,
            bundleOf("maisonId" to maisonId)
        )
    }
    private fun goToReservation(maisonId: Int) {
        findNavController().navigate(
            R.id.action_filteredResultFragment_to_reservationFragment,
            bundleOf("maisonId" to maisonId)
        )
    }
    private fun goToEdit(maisonId: Int) {
        findNavController().navigate(
            R.id.action_filteredResultFragment_to_addMaisonFragment,
            bundleOf("maisonId" to maisonId)
        )
    }

    private fun confirmDelete(maison: MaisonHoteEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.maison_delete_title)
            .setMessage(R.string.maison_delete_message)
            .setNegativeButton(R.string.btn_annuler, null)
            .setPositiveButton(R.string.maison_delete) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteMaison(maison)
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
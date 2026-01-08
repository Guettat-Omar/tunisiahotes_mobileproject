package com.tunisiahotes.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tunisiahotes.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val regions = listOf("SudEst", "SudOuest", "Djerba", "Sahel", "CapBon", "Tunis")
    private val villes = listOf(
        "Tataouine", "Gabès", "Tamezret", "Douz", "Tozeur",
        "Houmt Souk", "Mahdia", "Sousse",
        "Rafraf", "Hammamet", "Tunis", "Gammarth"
    )
    private val saisons = listOf("hiver", "été", "printemps", "automne")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            // Bouton Retour
            btnRetour.setOnClickListener {
                findNavController().navigateUp()
            }

            // Filtrer par Région
            btnRegion.setOnClickListener {
                showRegionDialog()
            }

            // Filtrer par Ville
            btnVille.setOnClickListener {
                showVilleDialog()
            }

            // Filtrer par Saison
            btnSaison.setOnClickListener {
                showSaisonDialog()
            }

            // Trier par Prix (croissant)
            btnPrix.setOnClickListener {
                navigateToFilteredResult("Prix", "Croissant")
            }

            // Trier par Avis (décroissant)
            btnAvis.setOnClickListener {
                navigateToFilteredResult("Avis", "Décroissant")
            }
        }
    }

    private fun showRegionDialog() {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Choisir une région")
            .setItems(regions.toTypedArray()) { _, which ->
                navigateToFilteredResult("Region", regions[which])
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    private fun showVilleDialog() {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Choisir une ville")
            .setItems(villes.toTypedArray()) { _, which ->
                navigateToFilteredResult("Ville", villes[which])
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    private fun showSaisonDialog() {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Choisir une saison")
            .setItems(saisons.toTypedArray()) { _, which ->
                navigateToFilteredResult("Saison", saisons[which])
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    private fun navigateToFilteredResult(filterType: String, filterValue: String) {
        val action = FilterFragmentDirections
            .actionFilterFragmentToFilteredResultFragment(filterType, filterValue)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
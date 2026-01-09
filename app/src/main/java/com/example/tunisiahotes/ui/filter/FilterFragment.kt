package com.example.tunisiahotes.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tunisiahotes.R
import com.example.tunisiahotes.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val regions = listOf("Sud Est", "Sud Ouest", "Djerba", "Sahel", "Cap Bon", "Tunis")
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

    private fun setupUI() = with(binding) {

        btnRetour.setOnClickListener {
            findNavController().navigateUp()
        }

        btnRegion.setOnClickListener {
            showDialog("Choisir une région", regions, "Region")
        }

        btnVille.setOnClickListener {
            showDialog("Choisir une ville", villes, "Ville")
        }

        btnSaison.setOnClickListener {
            showDialog("Choisir une saison", saisons, "Saison")
        }

        btnPrix.setOnClickListener {
            navigateToFilteredResult("Prix", "Croissant")
        }

        btnAvis.setOnClickListener {
            navigateToFilteredResult("Avis", "Décroissant")
        }
    }

    private fun showDialog(title: String, list: List<String>, filterType: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(list.toTypedArray()) { _, which ->
                navigateToFilteredResult(filterType, list[which])
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun navigateToFilteredResult(filterType: String, filterValue: String) {
        findNavController().navigate(
            R.id.action_filterFragment_to_filteredResultFragment,
            bundleOf(
                "filterType" to filterType,
                "filterValue" to filterValue
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

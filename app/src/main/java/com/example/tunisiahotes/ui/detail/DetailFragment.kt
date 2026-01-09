package com.example.tunisiahotes.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.tunisiahotes.R
import com.example.tunisiahotes.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        loadMaisonDetails()
    }

    // -------------------------
    // UI & Navigation
    // -------------------------
    private fun setupUI() {

        binding.btnRetour.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReserver.setOnClickListener {
            val maisonId = arguments?.getInt("maisonId") ?: return@setOnClickListener
            findNavController().navigate(
                R.id.action_detailFragment_to_reservationFragment,
                bundleOf("maisonId" to maisonId)
            )
        }

        // Long press → Ajouter avis
        binding.root.setOnLongClickListener {
            val maisonId = arguments?.getInt("maisonId") ?: return@setOnLongClickListener true
            findNavController().navigate(
                R.id.action_detailFragment_to_addAvisFragment,
                bundleOf("maisonId" to maisonId)
            )
            true
        }
    }

    // -------------------------
    // Chargement des données
    // -------------------------
    private fun loadMaisonDetails() {
        viewLifecycleOwner.lifecycleScope.launch {

            val maisonId = arguments?.getInt("maisonId")
                ?: return@launch

            val maison = viewModel.getMaisonById(maisonId)

            if (maison != null) {
                binding.apply {

                    tvNomMaison.text = maison.nom
                    tvDescription.text = maison.description
                    tvRegion.text = maison.region
                    tvVille.text = maison.ville
                    tvSaison.text = maison.saison
                    tvPrix.text = "${maison.prixNuite.toInt()} TND"
                    tvVueMer.text = if (maison.vueMer) "Oui" else "Non"
                    tvProcheMer.text = maison.procheMer
                    tvAvis.text =
                        "★ ${String.format("%.1f", maison.avis)}/10 (${maison.nbAvis} avis)"

                    val imageUrl = convertDriveLink(maison.photoLink)
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .centerCrop()
                        .into(ivMaisonImage)
                }
            } else {
                findNavController().navigateUp()
            }
        }
    }

    // -------------------------
    // Google Drive image helper
    // -------------------------
    private fun convertDriveLink(link: String): String {
        val regex = "/d/([^/]+)/".toRegex()
        val match = regex.find(link)
        return if (match != null) {
            val fileId = match.groupValues[1]
            "https://drive.google.com/uc?export=view&id=$fileId"
        } else {
            link
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

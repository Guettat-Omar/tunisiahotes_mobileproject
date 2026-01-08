package com.tunisiahotes.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tunisiahotes.R
import com.tunisiahotes.databinding.FragmentDetailBinding

import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

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

    private fun setupUI() {
        binding.btnRetour.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReserver.setOnClickListener {
            val action = DetailFragmentDirections
                .actionDetailFragmentToReservationFragment(args.maisonId)
            findNavController().navigate(action)
        }

        // Long press pour ajouter un avis
        binding.root.setOnLongClickListener {
            val action = DetailFragmentDirections
                .actionDetailFragmentToAddAvisFragment(args.maisonId)
            findNavController().navigate(action)
            true
        }
    }

    private fun loadMaisonDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            val maison = viewModel.getMaisonById(args.maisonId)

            if (maison != null) {
                binding.apply {
                    // Nom
                    tvNomMaison.text = maison.nom

                    // Description
                    tvDescription.text = maison.description

                    // Région
                    tvRegion.text = maison.region

                    // Ville
                    tvVille.text = maison.ville

                    // Saison
                    tvSaison.text = maison.saison

                    // Prix
                    tvPrix.text = "${maison.prixNuite.toInt()} TND"

                    // Vue mer
                    tvVueMer.text = if (maison.vueMer) "Oui" else "Non"

                    // Proche mer
                    tvProcheMer.text = maison.procheMer

                    // Avis
                    tvAvis.text = "★ ${String.format("%.1f", maison.avis)}/10 (${maison.nbAvis} avis)"

                    // Image
                    val imageUrl = convertDriveLink(maison.photoLink)
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .centerCrop()
                        .into(ivMaisonImage)
                }
            } else {
                // Maison non trouvée
                findNavController().navigateUp()
            }
        }
    }

    private fun convertDriveLink(link: String): String {
        val fileIdRegex = "/d/([^/]+)/".toRegex()
        val matchResult = fileIdRegex.find(link)
        return if (matchResult != null) {
            val fileId = matchResult.groupValues[1]
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
package com.example.tunisiahotes.ui.developer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tunisiahotes.R
import com.example.tunisiahotes.data.database.AppDatabase
import com.example.tunisiahotes.data.database.MaisonRepository
import com.example.tunisiahotes.data.entity.MaisonHoteEntity
import com.example.tunisiahotes.databinding.FragmentAddMaisonBinding
import kotlinx.coroutines.launch

class AddMaisonFragment : Fragment() {

    private var _binding: FragmentAddMaisonBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: MaisonRepository
    private var currentMaison: MaisonHoteEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMaisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = MaisonRepository(AppDatabase.getDatabase(requireContext()).maisonHoteDao())

        val maisonId = arguments?.getInt("maisonId", -1) ?: -1
        if (maisonId != -1) {
            binding.tvMaisonTitle.setText(R.string.edit_maison_title)
            loadMaison(maisonId)
        } else {
            binding.tvMaisonTitle.setText(R.string.add_maison_title)
        }

        binding.btnSaveMaison.setOnClickListener {
            saveMaison()
        }
    }

    private fun loadMaison(maisonId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            currentMaison = repository.getMaisonById(maisonId)
            currentMaison?.let { maison ->
                binding.inputMaisonNom.setText(maison.nom)
                binding.inputMaisonDescription.setText(maison.description)
                binding.inputMaisonRegion.setText(maison.region)
                binding.inputMaisonVille.setText(maison.ville)
                binding.inputMaisonSaison.setText(maison.saison)
                binding.inputMaisonPrix.setText(maison.prixNuite.toString())
                binding.switchMaisonVueMer.isChecked = maison.vueMer
                binding.inputMaisonProcheMer.setText(maison.procheMer)
                binding.inputMaisonPhoto.setText(maison.photoLink)
            }
        }
    }

    private fun saveMaison() {
        val nom = binding.inputMaisonNom.text?.toString()?.trim().orEmpty()
        val description = binding.inputMaisonDescription.text?.toString()?.trim().orEmpty()
        val region = binding.inputMaisonRegion.text?.toString()?.trim().orEmpty()
        val ville = binding.inputMaisonVille.text?.toString()?.trim().orEmpty()
        val saison = binding.inputMaisonSaison.text?.toString()?.trim().orEmpty()
        val prixText = binding.inputMaisonPrix.text?.toString()?.trim().orEmpty()
        val procheMer = binding.inputMaisonProcheMer.text?.toString()?.trim().orEmpty()
        val photoLink = binding.inputMaisonPhoto.text?.toString()?.trim().orEmpty()

        if (nom.isBlank() || description.isBlank() || region.isBlank() || ville.isBlank()
            || saison.isBlank() || prixText.isBlank() || procheMer.isBlank() || photoLink.isBlank()
        ) {
            Toast.makeText(requireContext(), R.string.error_champ_vide, Toast.LENGTH_SHORT).show()
            return
        }

        val prix = prixText.toDoubleOrNull()
        if (prix == null) {
            Toast.makeText(requireContext(), R.string.error_prix_invalide, Toast.LENGTH_SHORT).show()
            return
        }

        val existing = currentMaison
        val maison = MaisonHoteEntity(
            id = existing?.id ?: 0,
            nom = nom,
            description = description,
            region = region,
            ville = ville,
            saison = saison,
            prixNuite = prix,
            vueMer = binding.switchMaisonVueMer.isChecked,
            procheMer = procheMer,
            avis = existing?.avis ?: 0f,
            nbAvis = existing?.nbAvis ?: 0,
            photoLink = photoLink
        )

        viewLifecycleOwner.lifecycleScope.launch {
            if (existing == null) {
                repository.insertMaison(maison)
            } else {
                repository.updateMaison(maison)
            }
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.tunisiahotes.adapter

import MaisonHoteEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunisia.hotes.R
import com.tunisia.hotes.databinding.ItemMaisonHoteBinding



class MaisonAdapter(
    private val onDetailClick: (MaisonHoteEntity) -> Unit,
    private val onDecouvrirClick: (MaisonHoteEntity) -> Unit,
    private val onLongPress: ((MaisonHoteEntity) -> Unit)? = null
) : ListAdapter<MaisonHoteEntity, MaisonAdapter.MaisonViewHolder>(MaisonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaisonViewHolder {
        val binding = ItemMaisonHoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaisonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaisonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MaisonViewHolder(
        private val binding: ItemMaisonHoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(maison: MaisonHoteEntity) {
            binding.apply {
                // Nom de la maison
                tvNomMaison.text = maison.nom

                // Région et ville
                tvRegionVille.text = "${maison.region} - ${maison.ville}"

                // Avis
                tvAvis.text = "★ ${String.format("%.1f", maison.avis)}/10"

                // Prix
                tvPrix.text = "${maison.prixNuite.toInt()} TND/nuit (2 pers.)"

                // Charger l'image
                val imageUrl = convertDriveLink(maison.photoLink)
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .centerCrop()
                    .into(ivMaisonImage)

                // Boutons
                btnPlusDetails.setOnClickListener {
                    onDetailClick(maison)
                }

                btnDecouvrirPlus.setOnClickListener {
                    onDecouvrirClick(maison)
                }

                // Long press pour ajouter un avis
                itemView.setOnLongClickListener {
                    onLongPress?.invoke(maison)
                    true
                }
            }
        }

        // Convertir le lien Google Drive en URL directe
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
    }

    class MaisonDiffCallback : DiffUtil.ItemCallback<MaisonHoteEntity>() {
        override fun areItemsTheSame(
            oldItem: MaisonHoteEntity,
            newItem: MaisonHoteEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MaisonHoteEntity,
            newItem: MaisonHoteEntity
        ): Boolean = oldItem == newItem
    }
}
package com.example.spirala1.Adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spirala1.Models.Biljka
import com.example.spirala1.DAO.BiljkaDAO
import com.example.spirala1.Objects.BiljkaList.defaultImage
import com.example.spirala1.Models.Enums.MedicinskaKorist
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicinskiListaAdapter(
    private var biljke: List<Biljka>,
    private val onClickListener: (Biljka) -> Unit,
    private val trefleDAO: TrefleDAO,
    private val biljkaDao: BiljkaDAO
) : RecyclerView.Adapter<MedicinskiListaAdapter.BiljkaViewHolder>() {

    var isFiltered = false;
    lateinit var filteredList: ArrayList<Biljka>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.medicinski_layout, parent, false)
        return BiljkaViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {

        val biljka = biljke[position]

        holder.nazivItem.text = biljka.naziv
        holder.upozorenjeItem.text = biljke[position].medicinskoUpozorenje

        val brojKoristi: Int = biljka.medicinskeKoristi.size

        var counter = 0
        if (counter < brojKoristi) {
            val text = biljka.medicinskeKoristi[0].name
            holder.korist1Item.text = MedicinskaKorist.valueOf(text).opis
            counter++
        } else {
            holder.korist1Item.text = ""
        }
        if (counter < brojKoristi) {
            val text = biljka.medicinskeKoristi[1].name
            holder.korist2Item.text = MedicinskaKorist.valueOf(text).opis
            counter++
        } else {
            holder.korist2Item.text = ""
        }
        if (counter < brojKoristi) {
            val text = biljka.medicinskeKoristi[2].name
            holder.korist3Item.text = MedicinskaKorist.valueOf(text).opis
        } else {
            holder.korist3Item.text = ""
        }

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = withContext(Dispatchers.IO) {
                val biljkaId = biljkaDao.getAllBiljkas().find { it.naziv == biljka.naziv }?.id
                if (biljkaId != null) {
                    val biljkaBitmap = biljkaDao.getBitmapById(biljkaId)
                    if (biljkaBitmap != null) {
                        biljkaBitmap.bitmap
                    } else {
                        val fetchedBitmap = trefleDAO.getImage(biljka)
                        if (!defaultImage) {
                            fetchedBitmap?.let {
                                val resizedBitmap = resizeBitmap(fetchedBitmap, 300, 300)
                                biljkaDao.addImage(idBiljke = biljkaId, resizedBitmap)
                                resizedBitmap
                            }
                        } else {
                            null
                        }
                    }
                } else {
                    null
                }
            }

            if (defaultImage) {
                val placeholderImageResourceId = R.drawable.plant
                holder.slikaItem.setImageResource(placeholderImageResourceId)
            } else if (bitmap != null) {
                holder.slikaItem.setImageBitmap(bitmap)
            }
        }

        holder.favouriteItem.isChecked = biljka.favourite

        holder.favouriteItem.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val biljkaId = biljkaDao.getAllBiljkas().find { it.naziv == biljka.naziv }?.id
                biljkaId?.let {
                    biljka.favourite = !biljka.favourite
                    biljkaDao.updateBiljka(biljka)
                    notifyItemChanged(position)
                }
            }
        }

        holder.itemView.setOnClickListener {
            val data = biljka
            onClickListener(data)
            filterOnClick(data)
        }
    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    fun filterOnClick(clickedItem: Biljka) {
        val toMatch = clickedItem.medicinskeKoristi
        filteredList = biljke.filter { biljka ->
            biljka.medicinskeKoristi.any { korist -> korist in toMatch }
        } as ArrayList<Biljka>
        isFiltered = true
        updateBiljke(filteredList)
    }

    inner class BiljkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)
        val favouriteItem: CheckBox = itemView.findViewById(R.id.favouriteButton)
    }

    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


}


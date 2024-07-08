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
import com.example.spirala1.Objects.BiljkaList
import com.example.spirala1.Models.Enums.ProfilOkusaBiljke
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KuharskiListaAdapter(
    private var biljke: ArrayList<Biljka>,
    private val onClickListener: (Biljka) -> Unit,
    private val trefleDAO: TrefleDAO,
    private val biljkaDao: BiljkaDAO
) : RecyclerView.Adapter<KuharskiListaAdapter.BiljkaViewHolder>() {

    var isFiltered = false;
    lateinit var filteredList: ArrayList<Biljka>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.kuharski_layout, parent, false)
        return BiljkaViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {

        val biljka = biljke[position]

        holder.nazivItem.text = biljka.naziv

        val text = biljka.profilOkusa?.name
        holder.profilOkusaItem.text = text?.let { ProfilOkusaBiljke.valueOf(it).opis }

        val brojJela: Int = biljka.jela.size
        var counter = 0

        if (counter < brojJela) {
            holder.jelo1Item.text = biljka.jela[0]
            counter++
        } else {
            holder.jelo1Item.text = ""
        }
        if (counter < brojJela) {
            holder.jelo2Item.text = biljka.jela[1]
            counter++
        } else {
            holder.jelo2Item.text = ""
        }
        if (counter < brojJela) {
            holder.jelo3Item.text = biljka.jela[2]
        } else {
            holder.jelo3Item.text = ""
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
                        if (!BiljkaList.defaultImage) {
                            fetchedBitmap?.let {
                                val resizedBitmap = resizeBitmap(it, 300, 300)
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
            if (BiljkaList.defaultImage) {
                val placeholderImageResourceId = R.drawable.plant
                holder.slikaItem.setImageResource(placeholderImageResourceId)
            } else if (bitmap != null) {
                holder.slikaItem.setImageBitmap(bitmap)
            }
        }
        holder.itemView.setOnClickListener {
            val data = biljka
            onClickListener(data)
            filterOnClick(data)
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
    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke as ArrayList<Biljka>
        notifyDataSetChanged()
    }

    fun filterOnClick(clickedItem: Biljka) {
        val toMatchOkus = clickedItem.profilOkusa
        val toMatchJelo = clickedItem.jela
        filteredList = biljke.filter { biljka ->
            (biljka.profilOkusa == toMatchOkus) || (toMatchJelo.any { jelo -> jelo in biljka.jela })
        } as ArrayList<Biljka>
        updateBiljke(filteredList)
        isFiltered = true;
    }

    inner class BiljkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
        val favouriteItem: CheckBox = itemView.findViewById(R.id.favouriteButton)
    }

    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}


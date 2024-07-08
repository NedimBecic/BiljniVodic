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
import com.example.spirala1.Objects.BiljkaList.filterOn
import com.example.spirala1.Models.Enums.KlimatskiTip
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import com.example.spirala1.Models.Enums.Zemljiste
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BotanickiListaAdapter(
    private var biljke: List<Biljka>,
    private val onClickListener: (Biljka) -> Unit,
    private val trefleDAO: TrefleDAO,
    private val biljkaDao: BiljkaDAO
) : RecyclerView.Adapter<BotanickiListaAdapter.BiljkaViewHolder>() {

    var isFiltered = false;
    lateinit var filteredList: ArrayList<Biljka>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.botanicki_layout, parent, false)
        return BiljkaViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {

        val biljka = biljke[position]

        holder.nazivItem.text = biljka.naziv
        holder.porodicaItem.text = biljka.porodica

        if (biljka.klimatskiTipovi.size > 0) {
            var text = biljka.klimatskiTipovi[0].name
            holder.klimatskiTipItem.text = KlimatskiTip.valueOf(text).opis
        } else {
            holder.klimatskiTipItem.text = ""
        }

        if (biljka.zemljisniTipovi.size > 0) {
            var text = biljka.zemljisniTipovi[0].name
            holder.zemljisniTipItem.text = Zemljiste.valueOf(text).naziv
        } else {
            holder.zemljisniTipItem.text = ""
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
                    TrefleDAO().getImage(biljka)
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
        this.biljke = biljke
        notifyDataSetChanged()
    }

    fun filterOnClick(clickedItem: Biljka) {
        if (!filterOn) return
        val toMatchKlimatski = clickedItem.klimatskiTipovi
        val toMatchZemljisni = clickedItem.zemljisniTipovi
        filteredList = biljke.filter { biljka ->
            (biljka.klimatskiTipovi.any { klimatskiTip -> klimatskiTip in toMatchKlimatski }) &&
                    (biljka.zemljisniTipovi.any { zemljisniTip -> zemljisniTip in toMatchZemljisni })
        } as ArrayList<Biljka>
        updateBiljke(filteredList)
        isFiltered = true;
    }

    inner class BiljkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem: TextView = itemView.findViewById(R.id.porodicaItem)
        val klimatskiTipItem: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        val zemljisniTipItem: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        val favouriteItem: CheckBox = itemView.findViewById(R.id.favouriteButton)
    }

    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
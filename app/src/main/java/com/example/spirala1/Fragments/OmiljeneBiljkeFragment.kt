package com.example.spirala1.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spirala1.Adapters.BotanickiListaAdapter
import com.example.spirala1.Adapters.CustomSpinnerAdapter
import com.example.spirala1.Adapters.KuharskiListaAdapter
import com.example.spirala1.Adapters.MedicinskiListaAdapter
import com.example.spirala1.DAO.BiljkaDAO
import com.example.spirala1.DAO.BiljkaDatabase
import com.example.spirala1.Objects.BiljkaList
import com.example.spirala1.Data.BiljkeStaticData
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import kotlinx.coroutines.launch

class OmiljeneBiljkeFragment : Fragment() {
    private lateinit var biljkeLista: RecyclerView
    private lateinit var medicinskiListaAdapter: MedicinskiListaAdapter
    private lateinit var botanickiListaAdapter: BotanickiListaAdapter
    private lateinit var kuharskiListaAdapter: KuharskiListaAdapter

    private var isFiltered = false
    private lateinit var trefleDAO: TrefleDAO
    private lateinit var biljkaDAO: BiljkaDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.omiljene_biljke, container, false)

        trefleDAO = TrefleDAO()
        trefleDAO.setContext(requireContext())

        biljkaDAO = BiljkaDatabase.getInstance(requireContext()).biljkaDao()

        biljkeLista = view.findViewById(R.id.listaOmiljenih)
        biljkeLista.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val izbor = resources.getStringArray(R.array.Izbor)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        var mod = ""
        var prethodniMod = ""

        val boje = resources.getStringArray(R.array.boje)
        val bojeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, boje)
        bojeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        lifecycleScope.launch {
            initializeDatabase()
            var biljke = biljkaDAO.getFavouriteBiljkas()

            if (spinner != null) {
                val adapter = CustomSpinnerAdapter(requireContext(), izbor)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent != null) {
                            when (parent.getItemAtPosition(position) as String) {
                                "Medicinski" -> {
                                    lifecycleScope.launch {
                                        biljke = biljkaDAO.getFavouriteBiljkas()

                                        mod = "Medicinski"
                                        medicinskiListaAdapter = MedicinskiListaAdapter(
                                            arrayListOf(),
                                            { clickedBiljka ->
                                                medicinskiListaAdapter.filterOnClick(
                                                    clickedBiljka
                                                )
                                            },
                                            trefleDAO,
                                            biljkaDAO
                                        )
                                        biljkeLista.adapter = medicinskiListaAdapter

                                        if (prethodniMod == "Kuharski") {
                                            isFiltered = kuharskiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                kuharskiListaAdapter.filteredList
                                        } else if (prethodniMod == "Botanički") {
                                            isFiltered = botanickiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                botanickiListaAdapter.filteredList
                                        }

                                        medicinskiListaAdapter.updateBiljke(biljke)
                                        prethodniMod = "Medicinski"
                                        //sakrijDodatniFilter()
                                    }
                                }

                                "Kuharski" -> {
                                    lifecycleScope.launch {
                                        biljke = biljkaDAO.getFavouriteBiljkas()

                                        mod = "Kuharski"
                                        kuharskiListaAdapter = KuharskiListaAdapter(
                                            arrayListOf(),
                                            { clickedBiljka ->
                                                kuharskiListaAdapter.filterOnClick(
                                                    clickedBiljka
                                                )
                                            },
                                            trefleDAO,
                                            biljkaDAO
                                        )
                                        biljkeLista.adapter = kuharskiListaAdapter

                                        if (prethodniMod == "Medicinski") {
                                            isFiltered = medicinskiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                medicinskiListaAdapter.filteredList
                                        } else if (prethodniMod == "Botanički") {
                                            isFiltered = botanickiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                botanickiListaAdapter.filteredList
                                        }

                                        kuharskiListaAdapter.updateBiljke(biljke)
                                        prethodniMod = "Kuharski"
                                    }
                                }

                                "Botanički" -> {
                                    lifecycleScope.launch {
                                        biljke = biljkaDAO.getFavouriteBiljkas()

                                        mod = "Botanički"
                                        botanickiListaAdapter = BotanickiListaAdapter(
                                            arrayListOf(),
                                            { clickedBiljka ->
                                                botanickiListaAdapter.filterOnClick(
                                                    clickedBiljka
                                                )
                                            },
                                            trefleDAO,
                                            biljkaDAO
                                        )
                                        biljkeLista.adapter = botanickiListaAdapter

                                        if (prethodniMod == "Medicinski") {
                                            isFiltered = medicinskiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                medicinskiListaAdapter.filteredList
                                        } else if (prethodniMod == "Kuharski") {
                                            isFiltered = kuharskiListaAdapter.isFiltered
                                            if (isFiltered) biljke =
                                                kuharskiListaAdapter.filteredList
                                        }

                                        botanickiListaAdapter.updateBiljke(biljke)
                                        prethodniMod = "Botanički"
                                    }
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        medicinskiListaAdapter = MedicinskiListaAdapter(
                            arrayListOf(),
                            { clickedBiljka -> medicinskiListaAdapter.filterOnClick(clickedBiljka) },
                            trefleDAO,
                            biljkaDAO
                        )
                        biljkeLista.adapter = medicinskiListaAdapter
                        medicinskiListaAdapter.updateBiljke(biljke)
                    }
                }
                val buttonClick = view.findViewById<Button>(R.id.reset)
                buttonClick.setOnClickListener {
                    lifecycleScope.launch {
                        val biljke = biljkaDAO.getFavouriteBiljkas()
                        BiljkaList.filterOn = true
                        when (mod) {
                            "Medicinski" -> {
                                medicinskiListaAdapter.updateBiljke(biljke)
                                medicinskiListaAdapter.isFiltered = false
                            }

                            "Kuharski" -> {
                                kuharskiListaAdapter.updateBiljke(biljke)
                                kuharskiListaAdapter.isFiltered = false
                            }

                            "Botanički" -> {
                                botanickiListaAdapter.updateBiljke(biljke)
                                botanickiListaAdapter.isFiltered = false
                            }
                        }
                        isFiltered = false
                    }
                }

            }
        }
        return view
    }

    private suspend fun initializeDatabase() {
        val biljkeInDb = biljkaDAO.getAllBiljkas()
        if (biljkeInDb.isEmpty()) {
            BiljkeStaticData.biljke.forEach { biljka ->
                biljkaDAO.saveBiljka(biljka)
            }
        }
    }
}
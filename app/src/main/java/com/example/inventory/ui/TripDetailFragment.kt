package com.example.inventory.ui

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.inventory.R
import com.example.inventory.data.Trip
import com.example.inventory.databinding.FragmentTripDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.text.SimpleDateFormat
import java.util.*

class TripDetailFragment : Fragment() {

    private lateinit var binding: FragmentTripDetailBinding
    private lateinit var trip: Trip
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = requireArguments().getSerializable("trip") as Trip
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.detailTitle.text = trip.title
        binding.detailDescription.text = trip.description
        val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            .format(Date(trip.timestamp))
        binding.detailDate.text = formattedDate

        setupMap()

        // 👇 Modifier en premier
        binding.btnEditTrip.setOnClickListener {
            showEditDialog()
        }

        // 👇 Supprimer ensuite
        binding.btnDeleteTrip.setOnClickListener {
            deleteTrip()
        }
    }

    private fun showEditDialog() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 30, 40, 10)
        }

        val inputTitle = EditText(context).apply {
            hint = getString(R.string.edit_trip_title) // 🔁 Texte multilingue
            setText(trip.title)
        }

        val inputDesc = EditText(context).apply {
            hint = getString(R.string.edit_trip_description) // 🔁 Texte multilingue
            setText(trip.description)
        }

        layout.addView(inputTitle)
        layout.addView(inputDesc)

        builder.setTitle(getString(R.string.edit_trip)) // 🔁 Texte multilingue
            .setView(layout)
            .setPositiveButton(getString(R.string.save_changes)) { _, _ ->
                val newTitle = inputTitle.text.toString()
                val newDesc = inputDesc.text.toString()

                firestore.collection("Trips")
                    .whereEqualTo("userId", trip.userId)
                    .whereEqualTo("timestamp", trip.timestamp)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            firestore.collection("Trips").document(doc.id)
                                .update(
                                    mapOf(
                                        "title" to newTitle,
                                        "description" to newDesc
                                    )
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(context, getString(R.string.trip_saved), Toast.LENGTH_SHORT).show()
                                    trip.title = newTitle
                                    trip.description = newDesc
                                    binding.detailTitle.text = newTitle
                                    binding.detailDescription.text = newDesc
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erreur : ${it.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null) // 🔁 Texte multilingue
            .show()
    }


    private fun deleteTrip() {
        firestore.collection("Trips")
            .whereEqualTo("userId", trip.userId)
            .whereEqualTo("timestamp", trip.timestamp)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "Aucun document trouvé", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (doc in documents) {
                    firestore.collection("Trips").document(doc.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Trajet supprimé", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Échec de la suppression", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erreur lors de la recherche", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupMap() {
        val map = binding.detailMap
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(15.0)

        if (trip.positions.isNotEmpty()) {
            val start = trip.positions.first()
            val geoStart = GeoPoint(start.lat, start.lon)
            map.controller.setCenter(geoStart)

            val polyline = Polyline().apply {
                setPoints(trip.positions.map { GeoPoint(it.lat, it.lon) })
                width = 5f
            }
            map.overlays.add(polyline)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.detailMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.detailMap.onPause()
    }
}


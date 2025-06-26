package com.example.inventory.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inventory.R
import com.example.inventory.data.GeoPosition
import com.example.inventory.data.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.File

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var draggableMarker: Marker
    private val positions = mutableListOf<GeoPosition>()
    private val geoPoints = mutableListOf<GeoPoint>()
    private lateinit var polyline: Polyline
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        val osmConf = Configuration.getInstance()
        osmConf.userAgentValue = "com.example.inventory"
        osmConf.osmdroidBasePath = File(requireContext().filesDir, "osmdroid")
        osmConf.osmdroidTileCache = File(osmConf.osmdroidBasePath, "tile")

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.osm_map)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        val montreal = GeoPoint(45.5017, -73.5673)
        mapView.controller.setCenter(montreal)

        // Marqueur draggable
        draggableMarker = Marker(mapView).apply {
            position = montreal
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Position simulée"
            isDraggable = true
            setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker?) {}
                override fun onMarkerDrag(marker: Marker?) {}
                override fun onMarkerDragEnd(marker: Marker?) {
                    marker?.let {
                        val pos = it.position
                        draggableMarker.position = pos
                        mapView.invalidate()
                        Toast.makeText(requireContext(), "Position mise à jour : ${pos.latitude}, ${pos.longitude}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        mapView.overlays.add(draggableMarker)

        // Tracé initial
        polyline = Polyline().apply {
            setOnClickListener { _, _, _ -> true }
            title = "Trajet"
        }
        mapView.overlays.add(polyline)

        mapView.invalidate()

        val btnAdd = view.findViewById<View>(R.id.btn_add_position)
        val btnSave = view.findViewById<View>(R.id.btn_save_trip)

        btnAdd.setOnClickListener {
            val pos = draggableMarker.position
            val geoPos = GeoPosition(pos.latitude, pos.longitude, System.currentTimeMillis())
            positions.add(geoPos)
            geoPoints.add(pos)

            // Met à jour le tracé
            polyline.setPoints(geoPoints)
            mapView.invalidate()

            Toast.makeText(requireContext(), "Position ajoutée : ${pos.latitude}, ${pos.longitude}", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            promptAndSaveTrip()
        }
    }

    private fun promptAndSaveTrip() {
        if (positions.isEmpty()) {
            Toast.makeText(requireContext(), "Aucune position à enregistrer", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(requireContext())
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 30, 40, 10)
        }

        val inputTitle = EditText(requireContext()).apply { hint = "Titre du trajet" }
        val inputDesc = EditText(requireContext()).apply { hint = "Description" }

        layout.addView(inputTitle)
        layout.addView(inputDesc)

        builder.setTitle("Enregistrer le trajet")
            .setView(layout)
            .setPositiveButton("Enregistrer") { _, _ ->
                val title = inputTitle.text.toString()
                val description = inputDesc.text.toString()
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Toast.makeText(requireContext(), "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val trip = Trip(
                    userId = userId,
                    title = title,
                    description = description,
                    timestamp = System.currentTimeMillis(),
                    positions = positions.toList()
                )

                firestore.collection("Trips")
                    .add(trip)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Trajet sauvegardé", Toast.LENGTH_SHORT).show()
                        positions.clear()
                        geoPoints.clear()
                        polyline.setPoints(emptyList())
                        mapView.invalidate()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Erreur : ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        draggableMarker.isDraggable = true
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}





package com.example.inventory.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.R
import com.example.inventory.data.Trip
import com.example.inventory.databinding.FragmentTripsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var tripList = mutableListOf<Trip>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
            return
        }

        binding.tripsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        db.collection("Trips")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                tripList = result.documents.mapNotNull { it.toObject(Trip::class.java) }.toMutableList()

                binding.tripsRecyclerView.adapter = TripAdapter(tripList) { trip ->
                    val bundle = Bundle().apply {
                        putSerializable("trip", trip)
                    }

                    findNavController().navigate(R.id.tripDetailFragment, bundle)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erreur : ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}

package com.glucode.about_you.engineers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData

class EngineersFragment : Fragment() {
    private lateinit var binding: FragmentEngineersBinding
    private var engineers: List<Engineer> = MockData.engineers
    private lateinit var adapter: EngineersRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEngineersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        // Initialize the adapter with the engineers list
        adapter = EngineersRecyclerViewAdapter(engineers) {
            goToAbout(it)
        }
        binding.list.adapter = adapter

        // Set up the RecyclerView
        setUpEngineersList()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_engineers, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_years -> {
                sortEngineersByAttribute("years")
                true
            }
            R.id.action_coffees -> {
                sortEngineersByAttribute("coffees")
                true
            }
            R.id.action_bugs -> {
                sortEngineersByAttribute("bugs")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpEngineersList() {
        // Add any additional setup for the RecyclerView here
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(dividerItemDecoration)
    }

    private fun sortEngineersByAttribute(attribute: String) {
        engineers = when (attribute) {
            "years" -> engineers.sortedBy { it.quickStats.years }
            "coffees" -> engineers.sortedBy { it.quickStats.coffees }
            "bugs" -> engineers.sortedBy { it.quickStats.bugs }
            else -> engineers
        }
        adapter.updateEngineers(engineers) // Update the adapter with the sorted list
    }

    private fun goToAbout(engineer: Engineer) {
        val bundle = Bundle().apply {
            putString("name", engineer.name)
        }
        findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
    }
}
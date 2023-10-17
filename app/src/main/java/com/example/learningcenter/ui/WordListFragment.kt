package com.example.learningcenter.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.R
import com.example.learningcenter.databinding.FragmentWordListBinding
import com.example.learningcenter.ui.util.onQueryTextChanged
import com.example.learningcenter.ui.viewmodel.WordViewModel
import com.example.learningcenter.ui.viewmodel.WordViewModelFactory


class WordListFragment : Fragment() {

    private var _binding: FragmentWordListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: WordViewModel by activityViewModels {
        WordViewModelFactory(
            (activity?.application as BaseApplication).database.wordDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWordListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = WordListAdapter{
            val action = WordListFragmentDirections.actionWordListFragmentToWordDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            val action = WordListFragmentDirections.actionWordListFragmentToAddWordFragment(
                "Add new word"
            )
            this.findNavController().navigate(action)
        }



        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.word_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView


        if (viewModel.searchQuery.value.isNotEmpty())
        {
            viewModel.searchQuery.value = ""
        }
        if (viewModel.searchQuery.value.isEmpty()){
            searchView.onQueryTextChanged {
                viewModel.searchQuery.value = it
            }
        }

    }
}

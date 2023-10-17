package com.example.learningcenter.ui

import android.content.ClipData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.R
import com.example.learningcenter.databinding.FragmentWordDetailBinding
import com.example.learningcenter.model.Word
import com.example.learningcenter.ui.viewmodel.WordViewModel
import com.example.learningcenter.ui.viewmodel.WordViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class WordDetailFragment : Fragment() {

    private val navigationArgs: WordDetailFragmentArgs by navArgs()

    private var _binding: FragmentWordDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var word: Word


    private val viewModel: WordViewModel by activityViewModels {
        WordViewModelFactory(
            (activity?.application as BaseApplication).database.wordDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.wordId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            word = selectedItem
            bind(word)
        }
    }

    private fun bind(word: Word) {
        binding.apply {
            wordName.text = word.wordTitle
            wordTranscript.text = word.wordTranscript
            wordDefinition.text = word.wordDefinition
            deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener { editItem() }
        }
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteWord()
            }
            .show()
    }
    private fun deleteWord() {
        viewModel.deleteWord(word)
        findNavController().navigateUp()
    }
    private fun editItem() {
        val action = WordDetailFragmentDirections.actionWordDetailFragmentToAddWordFragment(
            getString(R.string.edit_fragment_title),
            word.id
        )
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
package com.example.learningcenter.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.databinding.FragmentAddWordBinding
import com.example.learningcenter.model.Word
import com.example.learningcenter.ui.viewmodel.WordViewModel
import com.example.learningcenter.ui.viewmodel.WordViewModelFactory


class AddWordFragment : Fragment() {

    private val navigationArgs: WordDetailFragmentArgs by navArgs()

    private var _binding: FragmentAddWordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordViewModel by activityViewModels {
        WordViewModelFactory(
            (activity?.application as BaseApplication).database
                .wordDao()
        )
    }
    lateinit var words: Word

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.wordName.text.toString(),
            binding.wordTranscript.text.toString(),
            binding.wordDefinition.text.toString(),
            binding.wordFound.text.toString()
        )
    }
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewWord(
                binding.wordName.text.toString(),
                binding.wordTranscript.text.toString(),
                binding.wordDefinition.text.toString(),
                binding.wordFound.text.toString().toInt(),
                binding.isDoneNewWord.isChecked
            )
            val action = AddWordFragmentDirections.actionAddWordFragmentToWordListFragment()
            findNavController().navigate(action)
        } else if (binding.wordName.text?.isBlank() == true) {
            binding.wordName.setError("Enter a word")
        } else if (binding.wordTranscript.text?.isBlank() == true){
            binding.wordTranscript.setError("Enter transcript of the word")
        } else if (binding.wordDefinition.text?.isBlank() == true){
            binding.wordDefinition.setError("Enter definition of the word")
        }else if (binding.wordFound.text?.isBlank() == true){
            binding.wordFound.setText("0")
        } else if (binding.wordFound.text.toString().toInt() > 20) {
            binding.wordFound.setError("Word found cannot be higher than 20")
            //Toast.makeText(this.context, "Word found cannot be higher than 20", Toast.LENGTH_SHORT).show()
        }

    }
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.wordId,
                this.binding.wordName.text.toString(),
                this.binding.wordTranscript.text.toString(),
                this.binding.wordDefinition.text.toString(),
                this.binding.wordFound.text.toString().toInt(),
                this.binding.isDoneNewWord.isChecked
            )
            val action = AddWordFragmentDirections.actionAddWordFragmentToWordListFragment()
            findNavController().navigate(action)
        } else if (binding.wordName.text?.isBlank() == true) {
            binding.wordName.setError("Enter a word")
        } else if (binding.wordTranscript.text?.isBlank() == true){
            binding.wordTranscript.setError("Enter transcript of the word")
        } else if (binding.wordDefinition.text?.isBlank() == true){
            binding.wordDefinition.setError("Enter definition of the word")
        }else if (binding.wordFound.text?.isBlank() == true){
            binding.wordFound.setText("0")
        } else if (binding.wordFound.text.toString().toInt() > 20) {
            binding.wordFound.setError("Word found cannot be higher than 20")
            //Toast.makeText(this.context, "Word found cannot be higher than 20", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.wordId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                words = selectedItem
                bind(words)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
    private fun bind(words: Word){
        binding.apply {
            wordName.setText(words.wordTitle, TextView.BufferType.SPANNABLE)
            wordTranscript.setText(words.wordTranscript, TextView.BufferType.SPANNABLE)
            wordDefinition.setText(words.wordDefinition, TextView.BufferType.SPANNABLE)
            wordFound.setText(words.wordFound.toString(), TextView.BufferType.SPANNABLE)
            if(words.isDone){
                isDoneNewWord.isChecked = true
            }
            saveAction.setOnClickListener { updateItem() }


        }

    }

}
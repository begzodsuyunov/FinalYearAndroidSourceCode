package com.example.learningcenter.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.R
import com.example.learningcenter.data.WordDao
import com.example.learningcenter.databinding.FragmentUnscrambledAppBinding
import com.example.learningcenter.databinding.FragmentWordDetailBinding
import com.example.learningcenter.model.Word
import com.example.learningcenter.ui.viewmodel.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class UnscrambledAppFragment : Fragment() {
    private var _binding: FragmentUnscrambledAppBinding? = null
    private val binding get() = _binding!!
    lateinit var words: Word

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
        _binding = FragmentUnscrambledAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
        binding.lifecycleOwner = viewLifecycleOwner
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        if (viewModel.isEnoughWords() || viewModel.isEnoughWordIsDone()){
            binding.skip.isEnabled = false
            binding.submit.isEnabled = false
        } else {
            binding.skip.isEnabled = true
            binding.submit.isEnabled = true
        }

        binding.floatingActionButton.setOnClickListener {
            val action =
                UnscrambledAppFragmentDirections.actionUnscrambledAppFragmentToUnscrambleAppStatisticsFragment()
            findNavController().navigate(action)
        }
        if(viewModel.isEnoughWordIsDone()){
            Toast.makeText(this.context,"You need more than 20 new words to unlock the game", Toast.LENGTH_SHORT).show()
        }

    }
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            viewModel.correctWord()
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                val action =
                    UnscrambledAppFragmentDirections.actionUnscrambledAppFragmentToHomePageFragment()
                findNavController().navigate(action)
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }



    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

}
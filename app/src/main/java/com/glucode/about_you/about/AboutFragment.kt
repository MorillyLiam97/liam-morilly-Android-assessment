package com.glucode.about_you.about

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.glucode.about_you.R
import com.glucode.about_you.about.views.QuestionCardView
import com.glucode.about_you.databinding.FragmentAboutBinding
import com.glucode.about_you.mockdata.MockData

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    // Register the launcher for picking an image
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { updateProfileImage(it) }
    }

    // Register the launcher for requesting permission
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        Log.d("Permission", "isGranted: $isGranted")
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            // Handle permission denial if needed
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProfile(uri = null)
        setUpQuestions()
    }

    private fun setUpProfile(uri: Uri?) {
        val engineerName = arguments?.getString("name")
        val engineer = MockData.engineers.first { it.name == engineerName }

        // Initialize and bind profile data
        binding.profileView.profileImage.setImageURI(uri ?: engineer.defaultImageName)
        binding.profileView.name.text = engineer.name
        binding.profileView.role.text = engineer.role

        // Handle image click
        binding.profileView.profileImage.setOnClickListener {
            checkPermissionsAndPickImage()
        }
    }

    private fun checkPermissionsAndPickImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun setUpQuestions() {
        val engineerName = arguments?.getString("name")
        val engineer = MockData.engineers.first { it.name == engineerName }

        engineer.questions.forEach { question ->
            val questionView = QuestionCardView(requireContext())
            questionView.title = question.questionText
            questionView.answers = question.answerOptions
            questionView.selection = question.answer.index

            binding.container.addView(questionView)
        }
    }

    private fun updateProfileImage(uri: Uri) {
        binding.profileView.profileImage.setImageURI(uri)
        val engineerName = arguments?.getString("name")
        MockData.engineers.find { it.name == engineerName }?.defaultImageName = uri
    }
}

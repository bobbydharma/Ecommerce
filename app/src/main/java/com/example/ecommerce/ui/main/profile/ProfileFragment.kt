package com.example.ecommerce.ui.main.profile

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.utils.Result
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var uri: Uri
    private var validName: Boolean = false

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                displayCapturedPhoto()
            } else {
                Toast.makeText(context, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }

    private val imageGaleri =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { useGaleri(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uri = createTempImageUri()

        if (viewModel.imageUri != null) {
            binding.ivProfile.setImageURI(viewModel.imageUri)
            binding.imageView2.visibility = View.GONE
        }

        validationButton()
        spanText()
        checking()

        binding.btnSelesai.setOnClickListener {
            val userName = MultipartBody.Part.createFormData(
                "userName",
                binding.layoutEtName.editText?.text.toString()
            )

            if (viewModel.imageUri != null) {
                val file = uriToFile(viewModel.imageUri!!, requireContext())
                val part = MultipartBody.Part.createFormData(
                    "userImage", file.name, file.asRequestBody(
                        "image/*".toMediaType()
                    )
                )
                val data = ProfileRequest(userName, part)
                viewModel.postProfile(data)
            } else {
                val data =
                    ProfileRequest(userName, null)
                viewModel.postProfile(data)
            }

            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Profile_Done")
            }
        }

        viewModel.profileData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBarProfile.isVisible = false
                    binding.btnSelesai.isVisible = true
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(), result.exception.message, Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarProfile.isVisible = false
                    binding.btnSelesai.isVisible = true
                }

                is Result.Loading -> {
                    binding.btnSelesai.isVisible = false
                    binding.progressBarProfile.isVisible = true
                }

                else -> {}
            }
        }

        binding.ivProfile.setOnClickListener {
            val item = arrayOf(getString(R.string.kamera), getString(R.string.galeri))
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Profile_Image")
            }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.pilih_gambar))
                .setItems(item) { _, which ->
                    when (which) {
                        0 -> {

                            takePictureLauncher.launch(uri)
                        }

                        1 -> {
                            imageGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    }
                }.show()
        }
    }

    private fun checking() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            binding.layoutEtName.isErrorEnabled = false
            validName = true
            validationButton()
        }
    }

    private fun validationButton() {
        val uriCek = viewModel.imageUri.toString()
        binding.btnSelesai.isEnabled =
            validName && !binding.layoutEtName.editText?.text.isNullOrEmpty() && !uriCek.isNullOrEmpty()
    }

    private fun displayCapturedPhoto() {
        if (uri != null) {
            viewModel.imageUri = uri
            binding.ivProfile.setImageURI(viewModel.imageUri)
            binding.imageView2.visibility = View.GONE
        } else {
        }
    }

    private fun createTempImageUri(): Uri {
        val photosDir = File(requireActivity().cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${requireActivity().packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(requireActivity(), authority, photoFile)

    }

    private fun generateFilename() = "selfie-${System.currentTimeMillis()}.jpg"

    fun uriToFile(uri: Uri, context: Context): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun useGaleri(uri: Uri) {
        viewModel.imageUri = uri
        binding.ivProfile.setImageURI(viewModel.imageUri)
        binding.imageView2.visibility = View.GONE
    }

    private fun spanText() {
        val textDaftarFirst = getString(R.string.spannable_text_daftar_first)
        val textDaftar = getString(R.string.spannable_text_daftar)
        val combinedText = "$textDaftarFirst$textDaftar"

        val spannableString = SpannableString(combinedText)

        val startIndex = combinedText.indexOf(getString(R.string.syarat_dan_ketentuan))
        val endIndex = startIndex + getString(R.string.syarat_dan_ketentuan).length

        val kebijakanStartIndex = combinedText.indexOf(getString(R.string.kebijakan_privasi))
        val kebijakanEndIndex = kebijakanStartIndex + getString(R.string.kebijakan_privasi).length

        spannableString.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ), kebijakanStartIndex, kebijakanEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.tvSyarat.text = spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
        private val REQUEST_WRITE_EXTERNAL_STORAGE = 1
        private const val PHOTOS_DIR = "photos"
        private const val FILE_PROVIDER = "fileprovider"
    }
}
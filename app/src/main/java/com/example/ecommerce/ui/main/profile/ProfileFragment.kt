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
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.model.user.ProfileRequest
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
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
    private var validName: Boolean = false
    val timestamp:String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                displayCapturedPhoto()
            } else {
                Toast.makeText(context, "isTaken", Toast.LENGTH_SHORT).show()
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

        if (viewModel.imageUri != null){
            displayCapturedPhoto()
        }

        validationButton()
        spanText()
        checking()

        binding.btnSelesai.setOnClickListener {
            val userName = MultipartBody.Part.createFormData("userName", binding.layoutEtName.editText?.text.toString())
            if (viewModel.imageUri != null){
                val file = uriToFile(viewModel.imageUri!!, requireContext())
                val part = MultipartBody.Part.createFormData("userImage", file.name, file.asRequestBody(
                    "image/*".toMediaType()
                ))
                val data = ProfileRequest(userName, part)
                viewModel.postProfile(data)
            }else{
                Toast.makeText(context, "Gagal Upload Image", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.profileData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBarProfile.isVisible = false
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(), result.exception.message, Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarProfile.isVisible = false
                }

                is Result.Loading -> {
                    binding.progressBarProfile.isVisible = true
                }

                else -> {}
            }
        }

        binding.ivProfile.setOnClickListener {
            val item = arrayOf(getString(R.string.kamera), getString(R.string.galeri))
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.pilih_gambar))
                .setItems(item) { _, which ->
                    when (which) {
                        0 -> {
                            checkCameraPermissionAndTakePicture()
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
        val uri = viewModel.imageUri.toString()
        binding.btnSelesai.isEnabled = validName && !binding.layoutEtName.editText?.text.isNullOrEmpty() && !uri.isNullOrEmpty()
    }

    private fun displayCapturedPhoto() {
        if (viewModel.imageUri != null) {
            binding.ivProfile.setImageURI(viewModel.imageUri)
            binding.imageView2.visibility = View.GONE
        } else {
        }
    }

    private fun checkCameraPermissionAndTakePicture() {
            viewModel.imageUri = createTempImageUri()
            takePictureLauncher.launch(viewModel.imageUri)

    }

    private fun createTempImageUri(): Uri {
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "${timestamp}.jpg")
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

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

        spannableString.setSpan(ForegroundColorSpan(MaterialColors.getColor(requireView(), android.R.attr.colorPrimary)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(MaterialColors.getColor(requireView(), android.R.attr.colorPrimary)), kebijakanStartIndex, kebijakanEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        binding.tvSyarat.text = spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

    }
}
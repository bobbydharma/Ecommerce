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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()
    private var tempImageUri: Uri? = null

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            useKamera(it)
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

        spanText()

        binding.btnSelesai.setOnClickListener {
            val userName = MultipartBody.Part.createFormData("userName", binding.etNama.editText?.text.toString())
            if (tempImageUri != null){
                val file = uriToFile(tempImageUri!!, requireContext())
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
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(), result.exception.message, Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {

                }
            }
        }

        binding.ivProfile.setOnClickListener {
            val item = arrayOf("Kamera", "Galeri")
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih Gambar")
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

    private fun displayCapturedPhoto() {
        if (tempImageUri != null) {
            binding.ivProfile.setImageURI(viewModel.imageUri)
            binding.imageView2.visibility = View.GONE
        } else {
            // Gagal mendapatkan gambar
        }
    }

    private fun checkCameraPermissionAndTakePicture() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            tempImageUri = createTempImageUri()
            takePictureLauncher.launch(tempImageUri)
        } else {
            // Jika izin belum diberikan, tampilkan permintaan izin
            requestPermissions(arrayOf(permission), CAMERA_PERMISSION_REQUEST)
        }
    }

    private fun createTempImageUri(): Uri {
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "temp_image.jpg")
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tempImageUri = createTempImageUri()
                takePictureLauncher.launch(tempImageUri)
            } else {

            }
        }
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

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 123
    }

    private fun useKamera(isTaken: Boolean) {
        if (isTaken) {
            displayCapturedPhoto()
        } else {

        }
    }

    private fun useGaleri(uri: Uri) {
        tempImageUri = uri
        binding.ivProfile.setImageURI(uri)
        binding.imageView2.visibility = View.GONE
    }

    private fun spanText() {
        val spannable =
            SpannableString("Dengan daftar disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon.")
        spannable.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ),
            37, // start
            57, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ),
            63, // start
            79, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvSyarat.text = spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
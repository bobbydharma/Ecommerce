package com.example.ecommerce.ui.main.profile

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.ui.prelogin.register.RegisterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<ProfileViewModel>()
    private var tempImageUri: Uri? = null


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
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spanText()

        binding.ivProfile.setOnClickListener {
            val item = arrayOf("Kamera", "Galeri")

            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Pilih Gambar")
                    .setItems(item) { dialog, which ->
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
    }

    private fun displayCapturedPhoto() {
        if (tempImageUri != null) {
            viewModel.imageUri.observe(viewLifecycleOwner){uri ->
                binding.ivProfile.setImageURI(uri)
                binding.imageView2.visibility = View.GONE
            }
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
            viewModel.setImageUri(tempImageUri!!)
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
                viewModel.setImageUri(tempImageUri!!)
            } else {

            }
        }
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
        viewModel.setImageUri(uri)
        if (uri != null) {
            viewModel.imageUri.observe(viewLifecycleOwner){uri ->
                binding.ivProfile.setImageURI(uri)
                binding.imageView2.visibility = View.GONE
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun spanText() {
        val spannable =
            SpannableString("Dengan daftar disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon.")
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            37, // start
            57, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
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
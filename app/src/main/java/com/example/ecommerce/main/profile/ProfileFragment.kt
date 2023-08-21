package com.example.ecommerce.main.profile

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
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val imageGaleri =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { useGaleri(it) }
        }

    private val imageCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
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
                                buildNewUri().let { uri ->
                                        imageCamera.launch(uri)
                                        binding.ivProfile.setImageURI(uri)
                                }
                            }

                            1 -> {
                                imageGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        }
                    }
                    .show()
            }
        }
    }

    private fun useGaleri(uri: Uri) {
        if (uri != null) {
            binding.ivProfile.setImageURI(uri)
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

    fun buildNewUri(): Uri {
        val photosDir = File(requireActivity().cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${requireActivity().packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(requireActivity(), authority, photoFile)
    }


    private fun generateFilename() = "selfie-${System.currentTimeMillis()}.jpg"

    companion object {
        private const val PHOTOS_DIR = "photos"
        private const val FILE_PROVIDER = "fileprovider"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
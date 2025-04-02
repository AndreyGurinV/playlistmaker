package com.example.playlistmaker.media.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.media.domain.models.NewPlaylistsFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylist : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var textWatcher: TextWatcher
    private val viewModel by viewModel<NewPlaylistsFragmentViewModel>()

    private var imageUri: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imageUri = uri
            if (uri != null) {
                Glide.with(binding.ivPlaylistImage)
                    .load(uri)
                    .placeholder(R.drawable.default_album_icon)
                    .fitCenter()
                    .transform(RoundedCorners(8))
                    .into(binding.ivPlaylistImage)


            } else {
                println("PhotoPicker - No media selected")
            }
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.newPlaylistFragment) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tbBackFromNewPlaylist.setNavigationOnClickListener {
            if(haveUnsavedData()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Завершить создание плейлиста?")
                    .setMessage("Все несохраненные данные будут потеряны")
                    .setNeutralButton("Отмена") { _, _ ->
                    }
                    .setPositiveButton("Завершить") { _, _ ->
                        findNavController().popBackStack()
                    }
                    .show()
            } else {
                findNavController().popBackStack()
            }
        }

        binding.btnCreatePlaylist.isEnabled = false

        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.createNewPlaylist(
                binding.editTextName.text.toString(),
                binding.editTextDescription.text.toString(),
                imageUri?.let { saveImageToPrivateStorage(it, binding.editTextName.text.toString()) }?: ""
            )
            onNewPlaylistCreated(binding.etPlaylistName.editText?.text.toString())
            findNavController().popBackStack()
        }

        binding.ivPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnCreatePlaylist.isEnabled = s.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.etPlaylistName.editText?.addTextChangedListener(textWatcher)
        binding.etPlaylistName.setOnFocusChangeListener { view, hasFocus ->
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(haveUnsavedData()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Завершить создание плейлиста?")
                        .setMessage("Все несохраненные данные будут потеряны")
                        .setNeutralButton("Отмена") { _, _ ->
                        }
                        .setPositiveButton("Завершить") { _, _ ->
                            findNavController().popBackStack()
                        }
                        .show()
                } else {
                    findNavController().popBackStack()
                }
            }
        })
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val contentResolver = requireActivity().applicationContext.contentResolver
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), playlistName)
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, "cover.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.path
    }

    private fun onNewPlaylistCreated(name: String) {
        (requireActivity() as CallBackInterface).showMessage("Плейлист $name создан!")
    }

    private fun haveUnsavedData(): Boolean {
        return binding.editTextName.text.toString().isNotEmpty() ||
            binding.editTextDescription.text.toString().isNotEmpty() ||
            imageUri != null
    }

}
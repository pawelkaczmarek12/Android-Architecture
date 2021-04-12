package pl.edu.wat.androidarchitecture.ui.sheet.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.data.config.autoCleared
import pl.edu.wat.androidarchitecture.databinding.CreateSheetFragmentBinding
import pl.edu.wat.androidarchitecture.model.entity.Sheet
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.ui.custom.Show
import pl.edu.wat.androidarchitecture.ui.custom.nonEmptyList


@AndroidEntryPoint
class CreateSheetFragment : Fragment() {

    private var binding: CreateSheetFragmentBinding by autoCleared()
    private val viewModel: CreateSheetViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CreateSheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveNewSheetBtn.setOnClickListener {
            val isValid = nonEmptyList(binding.titleLayout, binding.descriptionLayout) { view, msg ->
                view.error = getString(R.string.field_is_mandatory)
            }
            if (isValid) {
                val sheet = Sheet(
                    title = binding.title.text.toString(),
                    description = binding.description.text.toString(),
                )
                viewModel
                    .create(sheet)
                    .observe(viewLifecycleOwner, {
                        when (it.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                it.data?.let { sheet ->
                                    if (sheet.isSynced()) {
                                        Show.info(requireActivity().main_content, "Sheet synced with API")
                                    } else {
                                        Show.info(requireActivity().main_content, "Saved in database, sync in progress...")
                                    }
                                }

                            }
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                Show.error(requireActivity().main_content, it.message)
                            }
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }
                    })
            }
        }
    }
}

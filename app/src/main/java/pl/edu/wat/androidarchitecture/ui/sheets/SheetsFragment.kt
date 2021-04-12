package pl.edu.wat.androidarchitecture.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.data.config.autoCleared
import pl.edu.wat.androidarchitecture.databinding.SheetsFragmentBinding
import pl.edu.wat.androidarchitecture.model.entity.SheetFull
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.ui.custom.Confirm
import pl.edu.wat.androidarchitecture.ui.custom.Show

@AndroidEntryPoint
class SheetsFragment : Fragment() {

    private var binding: SheetsFragmentBinding by autoCleared()
    private val viewModel: SheetsViewModel by viewModels()
    private lateinit var adapter: SheetsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SheetsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()

        binding.addSheetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_sheetsFragment_to_createSheetFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.sheetsRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SheetsAdapter()
        binding.sheetsRv.adapter = adapter

        adapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as SheetFull
            findNavController().navigate(R.id.action_sheetsFragment_to_sheetDetailsFragment, bundleOf("sheetId" to item.sheet.id, "sheetRemoteId" to item.sheet.remoteId))
        }

        adapter.setOnItemChildClickListener { adapter, _, position ->
            Confirm.delete(requireActivity()) {
                val sheetFull = adapter.getItem(position) as SheetFull
                viewModel.delete(sheetFull).observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { status ->
                                if (status) {
                                    Show.info(requireActivity().main_content, "Item has been removed from remote source successfully!")
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

    private fun setupObservers() {
        viewModel.sheets.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let {
                        adapter.setNewInstance(it.toMutableList())
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

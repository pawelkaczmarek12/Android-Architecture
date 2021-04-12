package pl.edu.wat.androidarchitecture.ui.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.data.config.autoCleared
import pl.edu.wat.androidarchitecture.databinding.SheetFragmentBinding
import pl.edu.wat.androidarchitecture.model.entity.FinancialElement
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.ui.custom.Confirm
import pl.edu.wat.androidarchitecture.ui.custom.Show


@AndroidEntryPoint
class SheetFragment : Fragment() {

    private var binding: SheetFragmentBinding by autoCleared()
    private val viewModel: SheetViewModel by viewModels()
    private lateinit var financialElementAdapter: FinancialElementAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetId = requireArguments().getLong("sheetId")
        val sheetRemoteId = requireArguments().getLong("sheetRemoteId")
        sheetId.let { viewModel.initWithId(it) }
        setupObservers()
        setupRecyclerViews()

        binding.addIncomeBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_sheetsFragment_to_createFinancialElementFragment,
                bundleOf( "sheetId" to sheetId,"sheetRemoteId" to sheetRemoteId, "isIncome" to true)
            )
        }
        binding.addExpenseBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_sheetsFragment_to_createFinancialElementFragment,
                bundleOf("sheetId" to sheetId,"sheetRemoteId" to sheetRemoteId, "isIncome" to false)
            )
        }

    }

    private fun setupRecyclerViews() {
        financialElementAdapter = FinancialElementAdapter()
        binding.financialElementsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.financialElementsRv.adapter = financialElementAdapter

        financialElementAdapter.setOnItemChildClickListener { adapter, _, position ->
            Confirm.delete(requireActivity()) {
                viewModel.delete(adapter.getItem(position) as FinancialElement).observe(viewLifecycleOwner, {
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
        viewModel.sheetFull.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.title.setText(it.data!!.sheet.title)
                    binding.description.setText(it.data.sheet.description)
                    val financialElements = (it.data.incomes + it.data.expenses)
                    financialElementAdapter.setNewInstance(financialElements.toMutableList())
                    binding.progressBar.visibility = View.GONE
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

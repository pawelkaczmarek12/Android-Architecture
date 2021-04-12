package pl.edu.wat.androidarchitecture.ui.financial

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
import pl.edu.wat.androidarchitecture.databinding.CreateFinancialElementFragmentBinding
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.ui.custom.Show
import pl.edu.wat.androidarchitecture.ui.custom.nonEmptyList

@AndroidEntryPoint
class CreateFinancialElementFragment : Fragment() {

    private var binding: CreateFinancialElementFragmentBinding by autoCleared()
    private val viewModel: CreateFinancialElementViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CreateFinancialElementFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetId = requireArguments().getLong("sheetId")
        val sheetRemoteId = requireArguments().getLong("sheetRemoteId")
        val isIncome = requireArguments().getBoolean("isIncome")

        val imageResource = if (isIncome) R.drawable.ic_income_24 else R.drawable.ic_expense_24
        binding.image.setImageResource(imageResource)

        requireActivity().toolbar.run {
            title = if (isIncome) "Create income" else "Create expense"
        }

        binding.addNewFinancialElementBtn.setOnClickListener {
            val isValid = nonEmptyList(binding.titleLayout, binding.descriptionLayout, binding.costsLayout) { view, msg ->
                view.error = getString(R.string.field_is_mandatory)
            }
            if (isValid) {

                if (isIncome) {
                    val income = flushIncome(sheetId, sheetRemoteId)
                    viewModel.add(income, sheetRemoteId).observe(viewLifecycleOwner, {
                        when (it.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                it.data?.isSynced()?.let { sync ->
                                    if (sync) {
                                        Show.info(requireActivity().main_content, "Income synced with API")
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
                } else {
                    val expense = flushExpense(sheetId, sheetRemoteId)
                    viewModel.add(expense, sheetRemoteId).observe(viewLifecycleOwner, {
                        when (it.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                it.data?.isSynced()?.let { sync ->
                                    if (sync) {
                                        Show.info(requireActivity().main_content, "Expense synced with API")
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

    private fun flushExpense(sheetId: Long, sheetRemoteId: Long): Expense {
        val expense = Expense(
            sheetId = sheetId,
            sheetRemoteId = sheetRemoteId,
            title = binding.title.text.toString(),
            description = binding.description.text.toString(),
            costs = binding.costs.text.toString().toDouble()
        )
        binding.title.text?.clear()
        binding.description.text?.clear()
        binding.costs.text?.clear()

        return expense
    }

    private fun flushIncome(sheetId: Long, sheetRemoteId: Long): Income {
        val income = Income(
            sheetId = sheetId,
            sheetRemoteId = sheetRemoteId,
            title = binding.title.text.toString(),
            description = binding.description.text.toString(),
            costs = binding.costs.text.toString().toDouble()
        )
        binding.title.text?.clear()
        binding.description.text?.clear()
        binding.costs.text?.clear()

        return income
    }

}

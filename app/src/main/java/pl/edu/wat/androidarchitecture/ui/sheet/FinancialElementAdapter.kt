package pl.edu.wat.androidarchitecture.ui.sheet

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.FinancialElement

class FinancialElementAdapter : BaseQuickAdapter<FinancialElement, BaseViewHolder>(R.layout.item_financial_element) {

    init {
        addChildClickViewIds(R.id.delete_btn)
    }

    override fun convert(holder: BaseViewHolder, item: FinancialElement) {
        holder.setText(R.id.title, item.title)
        holder.setText(R.id.description, item.description)
        holder.setText(R.id.costs, "${item.costs}")

        if (item is Expense) {
            holder.setImageResource(R.id.image_type, R.drawable.ic_expense_24)
        } else {
            holder.setImageResource(R.id.image_type, R.drawable.ic_income_24)
        }

        if (item.isSynced()) {
            holder.setImageResource(R.id.image_sync, R.drawable.ic_sync_green_24)
        } else {
            holder.setImageResource(R.id.image_sync, R.drawable.ic_no_sync_24)
        }
    }

}

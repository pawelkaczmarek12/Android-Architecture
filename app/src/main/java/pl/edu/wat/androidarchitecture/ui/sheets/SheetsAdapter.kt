package pl.edu.wat.androidarchitecture.ui.sheets

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.model.entity.SheetFull

class SheetsAdapter : BaseQuickAdapter<SheetFull, BaseViewHolder>(R.layout.item_sheet) {

    init {
        addChildClickViewIds(R.id.delete_btn)
    }

    override fun convert(holder: BaseViewHolder, item: SheetFull) {
        holder.setText(R.id.title, item.sheet.title)
        holder.setText(R.id.description, item.sheet.description)
        holder.setText(R.id.expense_and_incomes_count, "${item.expenses.size} expenses, ${item.incomes.size} incomes")

        if (item.sheet.isSynced()) {
            holder.setImageResource(R.id.image, R.drawable.ic_sync_green_24)
        } else {
            holder.setImageResource(R.id.image, R.drawable.ic_no_sync_24)
        }
    }

}

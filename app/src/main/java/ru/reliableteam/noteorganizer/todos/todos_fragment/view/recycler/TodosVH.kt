package ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler

import android.graphics.Color
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import ru.reliableteam.noteorganizer.R
import ru.reliableteam.noteorganizer.todos.model.Todo
import ru.reliableteam.noteorganizer.todos.todos_fragment.presenter.ITodoPresenter
import ru.reliableteam.noteorganizer.utils.DateUtils

internal class TodosVH(itemView: View, presenter: ITodoPresenter) : RecyclerView.ViewHolder(itemView), IViewHolder {
    private val CLASS_TAG = "MyViewHolder"

    private var root: LinearLayoutCompat = itemView.findViewById(R.id.todo_root)
    private var title: TextView = itemView.findViewById(R.id.todo_item_title)
    private var dateEnd: TextView = itemView.findViewById(R.id.todo_date_end)
    private var checkBoxDone: MaterialCheckBox = itemView.findViewById(R.id.todo_item_checkbox)

    init {
        root.setOnClickListener { presenter.clicked(pos) }
        root.setOnLongClickListener {
            presenter.longClicked(pos)
            true
        }
        checkBoxDone.setOnCheckedChangeListener { _, _ ->
            presenter.makeTodoDone(pos, checkBoxDone.isChecked) }
    }

    override fun setTodo(todo: Todo) {
        setDate(todo.endDate)
        checkBoxDone.isChecked = todo.isDone
        setTitle(todo.title, todo.isDone)
        setBackground(todo)
    }

    override fun getPos(): Int {
        return layoutPosition
    }

    private fun setDate(endDate: Long) {
        if (DateUtils.isDateConfigured(endDate)) {
            dateEnd.visibility = View.VISIBLE
            dateEnd.text = DateUtils.dateToString(endDate)
        } else {
            dateEnd.visibility = View.GONE
        }
    }

    private fun setTitle(title_: String, isDone: Boolean) {
        val e: Editable = SpannableStringBuilder(title_)
        if (isDone) {
            e.setSpan(StrikethroughSpan(), 0, title_.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            e.removeSpan(StrikethroughSpan())
        }
        title.text = e
    }

    private fun setBackground(todo: Todo) {
        if (todo.isDone) {
            itemView.setBackgroundColor(Color.argb(30, 0, 133, 119))
            return
        }
        if (todo.endDate > System.currentTimeMillis()) {
            itemView.setBackgroundColor(0)
            return
        }
        if (todo.endDate < System.currentTimeMillis()) {
            if (DateUtils.isDateConfigured(todo.endDate)) itemView.setBackgroundColor(Color.argb(30, 207, 102, 121)) else itemView.setBackgroundColor(0)
        }
    }
}
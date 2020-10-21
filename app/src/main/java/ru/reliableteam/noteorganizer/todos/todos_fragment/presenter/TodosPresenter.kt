package ru.reliableteam.noteorganizer.todos.todos_fragment.presenter

import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager
import ru.reliableteam.noteorganizer.todos.todos_fragment.TodoRequestCodes
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.ITodosFragment
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.IViewHolder
import ru.reliableteam.noteorganizer.utils.DateUtils

class TodosPresenter(private val view: ITodosFragment) : ITodoPresenter, TodoRequestCodes {
    private val todoDao: TodoDaoImpl = TodoDaoImpl()
    private val appSettings: SharedPreferencesManager = todoDao.appSettings


    override fun notifyDatasetChanged(messageId: Int) {
        println("DATA CHANGED")
        view.notifyDataChanged()
    }

    override fun getTodos() {
        todoDao.getTodosByState(this)
    }

    override fun bindView(viewHolder: IViewHolder) {
        val position = viewHolder.pos
        viewHolder.setTodo(todoDao.todoList[position])
    }

    override fun getItemCount(): Int {
        return todoDao.size()
    }

    override fun saveTodo(title: String, description: String, dateTime: Long, timeChosen: Boolean) {
        var dateTime: Long? = dateTime
        dateTime = if (timeChosen) dateTime else 0
        println(DateUtils.dateToString(dateTime))
        todoDao.saveTodo(title, description, dateTime, this)
    }

    override fun editTodo(title: String, description: String, dateTime: Long, timeChosen: Boolean, action: Int) {
        when (action) {
            TodoRequestCodes.ACTION_UPDATE -> todoDao.update(title, description, dateTime, this)
            TodoRequestCodes.ACTION_DELETE -> deleteTodo()
        }
    }

    override fun longClicked(position: Int) {
        todoDao.setTodo(position)
        view.showConfirmationDialog()
    }

    override fun deleteTodo() {
        todoDao.delete(this)
    }

    override fun clicked(position: Int) {
        todoDao.setTodo(position)
        appSettings.clickedTodoId = todoDao.getTodo().id
        view.viewTodo()
    }

    fun newTodo() {
        appSettings.clickedTodoId = NEW_TODO.toLong()
    }

    override fun makeTodoDone(position: Int, isDone: Boolean) {
        todoDao.setTodo(position)
        val todo = todoDao.getTodo()
        todo.isDone = isDone
        todoDao.update(todo, this)
    }

    fun showAllTodos() {
        todoDao.showAll(this)
    }

    fun showDoneTodos() {
        todoDao.showDone(this)
    }

    fun showCurrentTodos() {
        todoDao.showCurrent(this)
    }

    fun showMissedTodos() {
        todoDao.showMissed(this)
    }

    companion object {
        private const val NEW_TODO = -1
    }
}
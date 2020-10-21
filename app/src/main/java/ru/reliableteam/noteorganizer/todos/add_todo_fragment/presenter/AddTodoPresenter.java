package ru.reliableteam.noteorganizer.todos.add_todo_fragment.presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.IAddTodoFragment;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.utils.DateUtils;

public class AddTodoPresenter implements BasePresenter {

    private SharedPreferencesManager appSettings;
    private TodoDaoImpl todoDao;

    private IAddTodoFragment view;

    public AddTodoPresenter(IAddTodoFragment fragment) {
        view = fragment;
        todoDao = new TodoDaoImpl();
        appSettings = todoDao.getAppSettings();
    }

    public boolean isNewTodo() {
        return appSettings.getClickedTodoId() == -1;
    }

    private void setUIData() {
        Todo todo = todoDao.todo;
        String[] dateTime = DateUtils.dateToString(todo.endDate).split(" ");
        String date = dateTime[0];
        String time = dateTime[1];
        if (DateUtils.isDateConfigured(date)) {
            view.setDate(date);
            view.setTime(time);
        }
        view.setTitle(todo.title);
        view.setDescription(todo.description);
    }

    public void getUIData() {
        long id = appSettings.getClickedTodoId();
        todoDao.getTodo(id, this);
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        setUIData();
    }
}

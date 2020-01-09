package ru.reliableteam.noteorganizer.todos.add_todo_fragment.presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.NoteDaoImpl;
import ru.reliableteam.noteorganizer.entity.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.IAddTodoFragment;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.utils.DateUtils;

@InjectViewState
public class AddTodoPresenter extends MvpPresenter<IAddTodoFragment> implements BasePresenter {

    private SharedPreferencesManager appSettings;
    private TodoDaoImpl todoDao;

    public AddTodoPresenter() {
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
            getViewState().setDate(date);
            getViewState().setTime(time);
        }
        getViewState().setTitle(todo.title);
        getViewState().setDescription(todo.description);
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

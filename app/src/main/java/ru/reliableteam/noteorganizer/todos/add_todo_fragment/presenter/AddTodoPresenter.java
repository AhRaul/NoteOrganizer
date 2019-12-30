package ru.reliableteam.noteorganizer.todos.add_todo_fragment.presenter;

import android.content.Intent;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.utils.DateUtils;

public class AddTodoPresenter extends TodoDaoImpl implements BasePresenter {
    private final int NEW_TODO = -1;
    private final int ACTION_SAVE = 1;
    private final int ACTION_DELETE = -1;

    private AddTodoBottomFragment view;
    private SharedPreferencesManager appSettings;

    public AddTodoPresenter(AddTodoBottomFragment fragment) {
        view = fragment;
        appSettings = getAppSettings();
    }

    public boolean isNewTodo() {
        return appSettings.getClickedTodoId() == -1;
    }

    public void setUIData() {
        // todo with UTILS
        String[] dateTime = DateUtils.dateToString(todo.endDate).split(" ");
        String date = dateTime[0];
        String time = dateTime[1];
        System.out.println("date = " + date);
        if (DateUtils.isDateConfigured(date)) {
            view.setDate(date);
            view.setTime(time);
        }
        view.setTitle(todo.title);
        view.setDescription(todo.description);
    }

    public void getUIData() {
        long id = appSettings.getClickedTodoId();
        getTodo(id, this);
    }

    public void updateTodo(Intent intent) {
        todo.title = intent.getStringExtra("title");
        todo.description = intent.getStringExtra("description");
        todo.endDate = intent.getLongExtra("endDate", 0L);
        boolean timeChosen = intent.getBooleanExtra("timeChosen", false);
        int action = intent.getIntExtra("action", ACTION_SAVE);

        todo.endDate = timeChosen ? todo.endDate : 0L;
        if (action == ACTION_SAVE)
            update(todo, this);
        else
            delete(todo, this);
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        setUIData();
    }
}
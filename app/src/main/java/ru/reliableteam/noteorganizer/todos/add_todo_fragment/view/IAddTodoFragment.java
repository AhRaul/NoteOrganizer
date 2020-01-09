package ru.reliableteam.noteorganizer.todos.add_todo_fragment.view;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface IAddTodoFragment extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void setDate(String date);
    @StateStrategyType(SingleStateStrategy.class)
    void setTime(String time);
    @StateStrategyType(SingleStateStrategy.class)
    void setTitle(String title);
    @StateStrategyType(SingleStateStrategy.class)
    void setDescription(String description);
}

package ru.reliableteam.noteorganizer.todos.todos_fragment.view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ITodosFragment extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void notifyDataChanged();
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showConfirmationDialog();
    @StateStrategyType(SingleStateStrategy.class)
    void viewTodo();
}

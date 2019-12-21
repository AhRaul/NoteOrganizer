package ru.reliableteam.noteorganizer.entity.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.reliableteam.noteorganizer.entity.NoteDaoImpl;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter.CalcPresenter;


@Singleton
@Component (modules = AppModule.class)
public interface AppComponent {

    void inject(CalcPresenter calcPresenter);

}

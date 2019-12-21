package ru.reliableteam.noteorganizer.entity.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.reliableteam.noteorganizer.entity.data_base.PersistenceManager;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.model.CalculatorModel;

@Module
public class AppModule {

    private final Application application;

    public AppModule (Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    PersistenceManager providePersistenceManager(){
        return new PersistenceManager();
    }

    @Singleton
    @Provides
    CalculatorModel provideCalculatorModel(){
        return new CalculatorModel();
    }


}

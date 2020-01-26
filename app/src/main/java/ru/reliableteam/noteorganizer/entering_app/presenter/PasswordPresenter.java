package ru.reliableteam.noteorganizer.entering_app.presenter;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entering_app.PasswordActivity;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;

public class PasswordPresenter {
    private SharedPreferencesManager appSettings;
    private PasswordActivity view;

    private String oldPassword;

    public PasswordPresenter (PasswordActivity activity) {
        this.view = activity;
        appSettings = AppConfig.getInstance().getAppSettings();
        oldPassword = appSettings.getLocalPassword();
    }

    public void checkPassword() {
        if (isAppFirstEnter()) {
            appFirstEnter();
        } else {
            comparePasswords();
        }
    }

    private void appFirstEnter() {
        String password = view.getPassword();
        if (!isConvenient(password)) {
            view.showConfirmation(R.string.wrong_password_hint, null, null, R.string.understand, -1);
        } else {
            view.showConfirmation(R.string.remember_or_reenter_password,
                    () -> {
                        setPassword(password);
                        view.allowEnterApp();
                    }, null,
                    R.string.confirm, R.string.reenter);
        }
    }

    private void comparePasswords() {
        String password = view.getPassword();
        if (isConvenient(password) && oldPassword.equals(password))
            view.allowEnterApp();
        else {
            view.showConfirmation(R.string.wrong_password_hint, null, null, R.string.understand, -1);
        }
    }

    private void setPassword(String password) {
        appSettings.setLocalPassword(password);
        oldPassword = appSettings.getLocalPassword();
    }

    private boolean isAppFirstEnter() {
        return oldPassword.equals("");
    }


    private boolean isConvenient(String password) {
        if (password.length() < 4)
            return false;

        String oldPassword = appSettings.getLocalPassword();
        return oldPassword.equals("") || oldPassword.equals(password);
    }

}

package ru.reliableteam.noteorganizer;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;

/**
 * Do not edit this class.
 *
 * Only for setting navigation to BottomNavBar.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAppTheme();
        setContentView(R.layout.activity_main);
        setAppNavigation();
        hideKeyBoard();
    }

    private void setAppTheme() {
        SharedPreferencesManager appSettings = new SharedPreferencesManager(this);
        setTheme(appSettings.getAppTheme());
    }

    private void setAppNavigation(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
}

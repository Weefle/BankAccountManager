package fr.weefle.myapplication.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.weefle.myapplication.Fragment.MapFragment;
import fr.weefle.myapplication.Fragment.PositionFragment;
import fr.weefle.myapplication.Fragment.TempFragment;
import fr.weefle.myapplication.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_temp:
                    showFragment(new TempFragment());
                    return true;
                case R.id.navigation_map:
                    showFragment(new PositionFragment());
                    return true;
                case R.id.navigation_maps:
                    showFragment(new MapFragment());
                    return true;

        }
            return false;
    }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_map);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new PositionFragment());


    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }

}

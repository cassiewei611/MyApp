package dk.itu.shopping;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    //Shopping V8 using Network Server

    private FragmentManager fm;
    Fragment fragmentUI, fragmentList;
    MainViewModel list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingmain);
        list = new ViewModelProvider(this).get(MainViewModel.class);

        fm = getSupportFragmentManager();

        //This line will pause the Activity until the list of items has been initialized
        list.awaitInit();

        setUpFragments();
    }

    private void setUpFragments() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentUI = fm.findFragmentById(R.id.container_ui_landscape);
            fragmentList = fm.findFragmentById(R.id.container_list);
            if ((fragmentUI == null) && (fragmentList == null)) {
                Fragment fragmentUI = new UIFragment();
                Fragment fragmentList = new ListFragment();
                Fragment fragmentCamera = new ListFragment();
                fm.beginTransaction()
                        .add(R.id.container_ui_landscape, fragmentUI)
                        .add(R.id.container_list, fragmentList)
                        .commit();
            }
        } else {
            //Orientation portrait
            if (fragmentUI == null) {
                fragmentUI = new UIFragment();
                fm.beginTransaction()
                        .add(R.id.main_fragment, fragmentUI)
                        .commit();
            }
        }
    }
}
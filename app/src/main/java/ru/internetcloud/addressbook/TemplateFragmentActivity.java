package ru.internetcloud.addressbook;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//********************************************
// это вспомогательная "шаблонная" Активити, чтобы использовать Фрагменты
//********************************************

public abstract class TemplateFragmentActivity extends AppCompatActivity {

    // протектед - чтобы только наследники
    protected abstract Fragment createFragment(); // каждый класс, который унаследуется от данного класса, будет "использовать" свой фрагмент

    protected int getLayoutResId() {
        return R.layout.activity_fragment; // для планшета будет переопределен
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment(); // каждый экземпляр класса-потомка здесь свой Фрагмент "предъявит"
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}

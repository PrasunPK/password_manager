package me.opens.password_manager.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.opens.password_manager.App;
import me.opens.password_manager.R;
import me.opens.password_manager.adapters.PersonAdapter;
import me.opens.password_manager.entity.Person;

public class DisplayMessageActivity extends AppCompatActivity {

    private RecyclerView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Person> all = App.get().getDB().productDao().getAll();
                if (all.isEmpty()) {
                    retrieveAll();
                } else {
                    populateAll(all);
                }
            }
        }).start();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"This is a toast message",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveAll() {
        ArrayList<Person> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setName(String.valueOf("Item -" + i));
            person.setAge(String.valueOf("Sub Item - ." + i));
            list.add(person);
        }

        App.get().getDB().productDao().insertAll(list);

        populateAll(list);
    }

    private void populateAll(final List<Person> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recycleView.setAdapter(new PersonAdapter(list));
            }
        });
    }
}

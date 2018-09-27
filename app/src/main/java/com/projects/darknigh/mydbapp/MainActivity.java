package com.projects.darknigh.mydbapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    String TAG = "myLog";
    ListView listView;
    private DatabaseHandler dbHandler;
    List<Film> films;
    FilmAdapter filmAdapter;
    private GetDataService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        listView = findViewById(R.id.listFilms);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        dbHandler = new DatabaseHandler(this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        films = dbHandler.getAllFilms();
        filmAdapter = new FilmAdapter(this, films);
        listView.setAdapter(filmAdapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilmDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Long click for update or delete item", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Film dialogFilm = (Film) filmAdapter.getItem(position);
                openDialog(dialogFilm);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDefault:
                Call<List<Film>> call = service.getAllFilms();
                call.enqueue(new Callback<List<Film>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Film>> call, @NonNull Response<List<Film>> response) {
                        dbHandler.deleteAll();
                        List<Film> responseList = response.body();
                        for (Film film : responseList) {
                            dbHandler.addFilm(film);
                        }
                        refreshListView(responseList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Film>> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.menuClear:
                dbHandler.deleteAll();
                refreshListView(dbHandler.getAllFilms());
                break;
        }
        films.addAll(dbHandler.getAllFilms());
        filmAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public void openDialog(Film film) {
        final Film filmDialog = film;
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_film);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText filmName = dialog.findViewById(R.id.dialogFilmName);
        final EditText filmDirector = dialog.findViewById(R.id.dialogDirectorName);
        final EditText filmBudget = dialog.findViewById(R.id.dialogFilmBudget);
        final EditText filmDate = dialog.findViewById(R.id.dialogFilmDate);
        filmName.setText(filmDialog.getName());
        filmDirector.setText(filmDialog.getDirector());
        filmBudget.setText(String.valueOf(filmDialog.getBudget()));
        filmDate.setText(filmDialog.getDate());
        Button btnDialogSave = dialog.findViewById(R.id.dialogBtnSave);
        btnDialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCheck(filmName, filmDirector, filmBudget, filmDate)) {
                    dbHandler.updateFilm(new Film(filmDialog.getID(), filmName.getText().toString(), filmDirector.getText().toString(), Integer.valueOf(filmBudget.getText().toString()), filmDate.getText().toString()));
                    refreshListView(dbHandler.getAllFilms());
                    dialog.dismiss();
                }
            }
        });
        Button btnDialogDelete = dialog.findViewById(R.id.dialogBtnDelete);
        btnDialogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteFilm(filmDialog);
                refreshListView(dbHandler.getAllFilms());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void addFilmDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_film);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnDialogAdd = dialog.findViewById(R.id.dialogBtnAdd);
        btnDialogAdd.setVisibility(View.VISIBLE);
        Button btnDialogDelete = dialog.findViewById(R.id.dialogBtnDelete);
        btnDialogDelete.setVisibility(View.GONE);
        Button btnDialogSave = dialog.findViewById(R.id.dialogBtnSave);
        btnDialogSave.setVisibility(View.GONE);
        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText filmName = dialog.findViewById(R.id.dialogFilmName);
                EditText filmDirector = dialog.findViewById(R.id.dialogDirectorName);
                EditText filmBudget = dialog.findViewById(R.id.dialogFilmBudget);
                EditText filmDate = dialog.findViewById(R.id.dialogFilmDate);
                if (editTextCheck(filmName, filmDirector, filmBudget, filmDate)) {
                    Film film = new Film(filmName.getText().toString(), filmDirector.getText().toString(), Integer.valueOf(filmBudget.getText().toString()), filmDate.getText().toString());
                    dbHandler.addFilm(film);
                    refreshListView(dbHandler.getAllFilms());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    void refreshListView(List<Film> list) {
        films.clear();
        films.addAll(list);
        filmAdapter.notifyDataSetChanged();
    }

    boolean editTextCheck(EditText name, EditText director, EditText budget, EditText data) {
        boolean check = true;
        String errorMsg = "Input required!";
        EditText[] fields = {name, director, budget, data};
        for (EditText field : fields) {
            if (field.length() == 0) {
                field.setError(errorMsg);
                check = false;
            }
        }
        return check;
    }
}

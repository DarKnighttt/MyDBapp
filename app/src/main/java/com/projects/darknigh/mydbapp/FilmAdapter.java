package com.projects.darknigh.mydbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Film> films;

    FilmAdapter(Context context, List<Film> list){
        films = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_item, parent,false);
        }
        Film film = films.get(position);
        ((TextView) view.findViewById(R.id.filmName)).setText(film.getName());
        ((TextView) view.findViewById(R.id.directorName)).setText(film.getDirector());
        String text = film.getBudget() + " million $";
        ((TextView) view.findViewById(R.id.filmBudget)).setText(text);
        ((TextView) view.findViewById(R.id.filmDate)).setText(film.getDate());
        return view;
    }

}

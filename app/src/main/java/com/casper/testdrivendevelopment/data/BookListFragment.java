package com.casper.testdrivendevelopment.data;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.casper.testdrivendevelopment.BookListMainActivity;
import com.casper.testdrivendevelopment.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class BookListFragment extends Fragment {

    private BookListMainActivity.booksAdapter bookAdapter;
    @SuppressLint("ValidFragment")
    public BookListFragment(BookListMainActivity.booksAdapter bookAdapter) {
        // Required empty public constructor
        this.bookAdapter=bookAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView ListViewBooks=view.findViewById(R.id.list_view_books);
        ListViewBooks.setAdapter(bookAdapter);
        this.registerForContextMenu(ListViewBooks);
        return view;
    }

}

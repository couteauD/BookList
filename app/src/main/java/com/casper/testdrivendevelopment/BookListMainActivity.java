package com.casper.testdrivendevelopment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    private ListView bookListView;
    private List<Book> list_books=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        init();
        bookListView= (ListView) this.findViewById(R.id.list_view_books);

        booksAdapter theAdapter=new booksAdapter(this,R.layout.book_item,list_books);
        bookListView.setAdapter(theAdapter);
    }

    private void init() {
        list_books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        list_books.add(new Book("创新工程实践", R.drawable.book_no_name));
        list_books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }

    List<Book> getListBooks(){
        return list_books;
    }

    class booksAdapter extends ArrayAdapter<Book>{
        private int resourceId;
        public booksAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource, objects);
            this.resourceId=resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater=LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);
            ImageView img = (ImageView)item.findViewById(R.id.image_view_book_cover);
            TextView title = (TextView)item.findViewById(R.id.text_view_book_title);
            Book book_item=this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            title.setText(book_item.getTitle());
            return item;
        }
    }
}

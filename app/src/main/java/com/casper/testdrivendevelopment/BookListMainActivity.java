package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    private ListView bookListView;
    private ArrayList<Book> list_books;
    private booksAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        init();

        bookListView= (ListView) this.findViewById(R.id.list_view_books);
        theAdapter=new booksAdapter(this,R.layout.book_item,list_books);
        bookListView.setAdapter(theAdapter);

        this.registerForContextMenu(bookListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        if(v==bookListView){
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(list_books.get(itemPosition).getTitle());
            menu.add(0,1,0,"新建");
            menu.add(0,2,0,"删除");
            menu.add(0,3,0,"关于...");
        }
        super.onCreateContextMenu(menu,v,menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 1:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                list_books.add(menuInfo.position,new Book("Android Studio应用程序设计(第2版)－微课版",R.drawable.book_3));
                theAdapter.notifyDataSetChanged();

                Toast.makeText(this,"新建成功",Toast.LENGTH_SHORT).show();

                break;
            }
            case 2:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list_books.remove(itemPosition);
                                theAdapter.notifyDataSetChanged();

                                Toast.makeText(BookListMainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(BookListMainActivity.this,"取消删除！",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

                break;
            }
            case 3:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                Toast.makeText(this,"这是第"+(itemPosition+1)+"本书",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void init() {
        list_books=new ArrayList<>();
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

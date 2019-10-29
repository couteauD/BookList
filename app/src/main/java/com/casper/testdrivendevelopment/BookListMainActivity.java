package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.casper.testdrivendevelopment.data.BookFragmentAdapter;
import com.casper.testdrivendevelopment.data.BookListFragment;
import com.casper.testdrivendevelopment.data.BookSaver;
import com.casper.testdrivendevelopment.data.NewsFragment;
import com.casper.testdrivendevelopment.data.VendorFragment;
import com.casper.testdrivendevelopment.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_NEW_BOOK = 1;
    public static final int CONTEXT_DELETE_BOOK = CONTEXT_NEW_BOOK+1;
    public static final int CONTEXT_UPDATE_BOOK = CONTEXT_DELETE_BOOK+1;
    public static final int CONTEXT_ABOUT_BOOK = CONTEXT_UPDATE_BOOK+1;
    public static final int REQUEST_CODE_NEW_BOOK=901;
    public static final int REQUEST_CODE_UPDATE_BOOK=902;
    private ArrayList<Book> list_books=new ArrayList<>();
    private booksAdapter theAdapter;
    private BookSaver bookSaver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookSaver=new BookSaver(this);
        list_books=bookSaver.load();
        if(list_books.size()==0)
            init();
        theAdapter=new booksAdapter(BookListMainActivity.this,R.layout.book_item,list_books);

        ArrayList<String> titleDatas=new ArrayList<>();
        titleDatas.add("图书");
        titleDatas.add("新闻");
        titleDatas.add("卖家");

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new BookListFragment(theAdapter));
        fragmentList.add(new NewsFragment());
        fragmentList.add(new VendorFragment());

        BookFragmentAdapter bookFragmentAdapter = new BookFragmentAdapter(getSupportFragmentManager(), titleDatas, fragmentList);

        TabLayout tCoupon=findViewById(R.id.t_coupon);
        ViewPager vpCoupon=findViewById(R.id.vp_coupon);
        vpCoupon.setAdapter(bookFragmentAdapter);
        tCoupon.setupWithViewPager(vpCoupon);
        tCoupon.setTabsFromPagerAdapter(bookFragmentAdapter);
//
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        if(v==findViewById(R.id.list_view_books)){
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(list_books.get(itemPosition).getTitle());
            menu.add(0,1,0,"新建");
            menu.add(0,2,0,"删除");
            menu.add(0,3,0,"修改");
            menu.add(0,4,0,"关于...");
        }
        super.onCreateContextMenu(menu,v,menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case CONTEXT_NEW_BOOK:{
                Intent intent = new Intent(this, EditBookActivity.class);
                intent.putExtra("title", "无名书籍");
                intent.putExtra("price","0.0");
                intent.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_NEW_BOOK);
                break;
            }
            case CONTEXT_DELETE_BOOK:{
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
            case CONTEXT_UPDATE_BOOK:{
                int position=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Intent intent = new Intent(this, EditBookActivity.class);
                intent.putExtra("title", list_books.get(position).getTitle());
                intent.putExtra("price",list_books.get(position).getPrice());
                intent.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_BOOK);
                break;
            }
            case CONTEXT_ABOUT_BOOK:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                Toast.makeText(this,"这是第"+(itemPosition+1)+"本书",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_BOOK:
                if(resultCode==RESULT_OK){
                    String title=data.getStringExtra("title");
                    double price=data.getDoubleExtra("price",0);
                    int insertPosition = data.getIntExtra("insert_position", 0);
                    getListBooks().add(insertPosition,new Book(title,price,R.drawable.book_no_name));
                    theAdapter.notifyDataSetChanged();
                    Toast.makeText(this,"新建成功",Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_UPDATE_BOOK:
                if (resultCode == RESULT_OK) {
                    int insertPosition = data.getIntExtra("insert_position", 0);
                    Book bookAtAdapter=getListBooks().get(insertPosition);
                    bookAtAdapter.setTitle(data.getStringExtra("title"));
                    bookAtAdapter.setPrice(data.getDoubleExtra("price",0));
                    theAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    private void init() {
        list_books.add(new Book("软件项目管理案例教程（第4版）",10.0, R.drawable.book_2));
        list_books.add(new Book("创新工程实践",20.0, R.drawable.book_no_name));
        list_books.add(new Book("信息安全数学基础（第2版）",30.0, R.drawable.book_1));
    }

    List<Book> getListBooks(){
        return list_books;
    }

    public class booksAdapter extends ArrayAdapter<Book>{
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
            TextView price=(TextView)item.findViewById(R.id.text_view_price);
            Book book_item=this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            title.setText(book_item.getTitle());
            price.setText(book_item.getPrice()+"");
            return item;
        }
    }
}

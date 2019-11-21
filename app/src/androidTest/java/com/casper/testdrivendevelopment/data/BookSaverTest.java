package com.casper.testdrivendevelopment.data;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.casper.testdrivendevelopment.data.model.Book;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookSaverTest{
    private BookSaver bookKeeper;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context= ApplicationProvider.getApplicationContext();
        bookKeeper=new BookSaver(context);
        bookKeeper.load();
    }

    @After
    public void tearDown() throws Exception {
        bookKeeper.save();
    }

    @Test
    public void getBooks() {
        assertNotNull(bookKeeper.getBooks());
        BookSaver bookSaver=new BookSaver(context);
        assertNotNull(bookSaver.getBooks());
        assertEquals(0,bookSaver.getBooks().size());
    }

    @Test
    public void saveThenLoad() {
        BookSaver bookTester=new BookSaver(context);
        assertNotNull(bookTester.getBooks().size());
        Book book=new Book("testbook",1.23,123);
        bookTester.getBooks().add(book);
        book=new Book("testbook2",1.24,124);
        bookTester.getBooks().add(book);
        bookTester.save();

        BookSaver bookSaverLoader=new BookSaver(context);
        bookSaverLoader.load();
        assertEquals(bookTester.getBooks().size(),bookSaverLoader.getBooks().size());
        for(int i=0;i<bookTester.getBooks().size();i++){
            Book bookThis=bookTester.getBooks().get(i);
            Book bookThat=bookSaverLoader.getBooks().get(i);
            Assert.assertEquals(bookThat.getCoverResourceId(),bookThis.getCoverResourceId());
            Assert.assertEquals(bookThat.getTitle(),bookThis.getTitle());
            assertEquals(bookThat.getPrice(),bookThis.getPrice(),1e-4);
        }
    }

    @Test
    public void saveEmptyThenLoad(){
        BookSaver bookSaverTest=new BookSaver(context);
        Assert.assertEquals(0,bookSaverTest.getBooks().size());
        bookSaverTest.save();

        BookSaver bookSaverLoader=new BookSaver(context);
        bookSaverLoader.load();
        Assert.assertEquals(bookSaverTest.getBooks().size(),bookSaverLoader.getBooks().size());
    }
}
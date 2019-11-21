package com.casper.testdrivendevelopment.data;

import com.casper.testdrivendevelopment.data.model.Shop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShopLoaderTest {

    private ShopLoader shopLoader;
    @Before
    public void setUp() throws Exception {
        shopLoader=new ShopLoader();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getShops() {
        assertNotNull(shopLoader.getShops());
        assertEquals(0,shopLoader.getShops().size());
    }

    @Test
    public void download() {
        String content=shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        assertTrue(content.length()>=300);
        assertTrue(content.contains("\"longitude\": \"113.526421\","));
    }

    @Test
    public void parseJson() {
        String content="{"
                +"\"shops\": [{"
                +"\"name\": \"暨南大学珠海校区\","
                +"\"latitude\": \"22.255925\","
                +"\"longitude\": \"113.541112\","
                +"\"memo\": \"暨南大学珠海校区\""
        +"},"
        +"{"
            +"\"name\": \"沃尔玛(前山店)\","
            +"\"latitude\": \"22.261365\","
            +"\"longitude\": \"113.532989\","
            +"\"memo\": \"沃尔玛(前山店)\""
        +"},"
        +"{"
            +"\"name\": \"明珠商业广场\","
            +"\"latitude\": \"22.251953\","
            +"\"longitude\": \"113.526421\","
            +"\"memo\": \"珠海二城广场\""
        +"}"
  +"]"
+"}";
        shopLoader.parseJson(content);
        assertEquals(3,shopLoader.getShops().size());
        Shop shop=shopLoader.getShops().get(2);
        assertEquals("明珠商业广场",shop.getName());
        assertEquals("珠海二城广场",shop.getMemo());
        assertEquals(22.251953,shop.getLatitude(),1e-6);
        assertEquals(113.526421,shop.getLongitude(),1e-6);


    }
}
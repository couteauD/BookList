package com.casper.testdrivendevelopment.data;

import android.util.Log;

import com.casper.testdrivendevelopment.data.model.Shop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.Handler;

public class ShopLoader {
    private ArrayList<Shop> shops=new ArrayList<>();

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public String download(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接属性
            conn.setConnectTimeout(5000);
            // 设置超时
            conn.setUseCaches(false);

            conn.connect();
            //获取数据
            InputStream inputStream = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(input);
            if (conn.getResponseCode() == 200) {//200表示返回的是"OK"
                String inputLine;
                StringBuffer resultData = new StringBuffer();
                while ((inputLine = buffer.readLine()) != null) {
                    resultData.append(inputLine);
                }
                String text = resultData.toString();
                Log.v("out---------------->", text);
                return (text);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public void parseJson(String text){
        try {
            //这里的text就是上边获取到的数据，一个String.
            JSONObject jsonObject = new JSONObject(text);
            JSONArray jsonDatas = jsonObject.getJSONArray("shops");
            int length = jsonDatas.length();
            String test;
            for (int i = 0; i < length; i++) {
                JSONObject shopJson = jsonDatas.getJSONObject(i);
                Shop shop = new Shop();
                shop.setName(shopJson.getString("name"));
                shop.setLatitude(shopJson.getDouble("latitude"));
                shop.setLongitude(shopJson.getDouble("longitude"));
                shop.setMemo(shopJson.getString("memo"));
                shops.add(shop);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(final Handler handler, final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = download(url);
                parseJson(content);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}

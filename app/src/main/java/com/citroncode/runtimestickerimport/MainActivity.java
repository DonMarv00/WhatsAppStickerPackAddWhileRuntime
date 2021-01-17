package com.citroncode.runtimestickerimport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.util.Util;
import com.citroncode.runtimestickerimport.adapter.StickerAdapter;
import com.citroncode.runtimestickerimport.model.StickerModel;
import com.citroncode.runtimestickerimport.task.GetStickers;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetStickers.Callbacks {

    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKERPACK = "stickerpack";
    private static final String TAG = MainActivity.class.getSimpleName();
    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String path;
    ArrayList<String> strings;
    File jsonFile;
    StickerAdapter adapter;
    ArrayList<StickerPack> stickerPacks = new ArrayList<>();
    List<Sticker> mStickers;
    Toolbar toolbar;
    ArrayList<StickerModel> stickerModels = new ArrayList<>();
    RecyclerView recyclerView;
    List<String> mEmojis,mDownloadFiles;
    TextView tv_log;
    String android_play_store_link;
    Button ad_json,add_sticker, add_icon;
    int counter;
    public static int image_data_code = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stickerPacks = new ArrayList<>();
        path = getFilesDir() + "/" + "stickers_asset";
        mStickers = new ArrayList<>();
        stickerModels = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mEmojis.add("");
        adapter = new StickerAdapter(this, stickerPacks);
        getPermissions();
        setContentView(R.layout.activity_main);

        tv_log = findViewById(R.id.tv_log);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        createFolders();

        //new GetStickers(MainActivity.this, MainActivity.this, jsonBuilder()).execute();

        add_icon = findViewById(R.id.btn_icon);
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_log.append("\nadded icon: icon.png");

                int w = 96, h = 96;

                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap bmp = Bitmap.createBitmap(w, h, conf);
                bmp.eraseColor(Color.BLACK);
                SaveImage(bmp,"icon.png","1");
            }
        });
        add_sticker = findViewById(R.id.btn_sticker);
        add_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_log.append("\nadded sticker: " + counter + ".webp");


                int w = 512, h = 512;

                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap bmp = Bitmap.createBitmap(w, h, conf);
                bmp.eraseColor(Color.RED);
                SaveImage(bmp, counter + ".webp","1");
                counter++;
            }
        });
        ad_json = findViewById(R.id.btn_json);
        ad_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerPacks = new ArrayList<>();
                path = getFilesDir() + "/" + "stickers_asset";
                mStickers = new ArrayList<>();
                stickerModels = new ArrayList<>();
                mEmojis = new ArrayList<>();
                mDownloadFiles = new ArrayList<>();
                mEmojis.add("");
                adapter = new StickerAdapter(MainActivity.this, stickerPacks);

                image_data_code++;
                new GetStickers(MainActivity.this, MainActivity.this, jsonBuilder()).execute();
            }
        });

    }

    private void createFolders(){
        File folder  = new File(path + "/1");
        if(!folder.exists()){
            folder.mkdirs();
        }
    }
    public static void SaveImage(Bitmap finalBitmap, String name, String identifier) {

        String root = path + "/" + identifier;
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = name;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            if(name == "icon"){
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            }else{
                finalBitmap.compress(Bitmap.CompressFormat.WEBP, 90, out);

            }

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updatePack(View v){
        stickerPacks = new ArrayList<>();
        path = getFilesDir() + "/" + "stickers_asset";
        mStickers = new ArrayList<>();
        stickerModels = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mEmojis.add("");
        adapter = new StickerAdapter(this, stickerPacks);

        image_data_code++;
       // new GetStickers(MainActivity.this, MainActivity.this, jsonBuilder()).execute();
        jsonBuilder();
    }
    public static void SaveTryImage(Bitmap finalBitmap, String name, String identifier) {

        String root = path + "/" + identifier;
        File myDir = new File(root + "/" + "try");
        myDir.mkdirs();
        String fname = name.replace(".png","").replace(" ","_") + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermissions() {
        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    1
            );
        }
    }
    public void listFiles(View v){
       File files = new File(path + "/contents.json");
        /*File[] liste = files.listFiles();
        String filesS = "";
        for(int i = 0; i < liste.length;i++){
            filesS = filesS + "\n" + liste[i].getName()+ "\n";
        }*/

        Toast.makeText(v.getContext(), files.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onListLoaded(String jsonResult, boolean jsonSwitch) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    android_play_store_link = jsonResponse.getString("android_play_store_link");
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("sticker_packs");
                    Log.d(TAG, "onListLoaded: " + jsonMainNode.length());
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Log.d(TAG, "onListLoaded: " + jsonChildNode.getString("name"));
                        stickerPacks.add(new StickerPack(
                                jsonChildNode.getString("identifier"),
                                jsonChildNode.getString("name"),
                                jsonChildNode.getString("publisher"),
                                jsonChildNode.getString("tray_image_file"),
                                jsonChildNode.getString("publisher_email"),
                                jsonChildNode.getString("publisher_website"),
                                jsonChildNode.getString("privacy_policy_website"),
                                jsonChildNode.getString("license_agreement_website"),
                                String.valueOf(image_data_code)
                        ));
                        JSONArray stickers = jsonChildNode.getJSONArray("stickers");
                        Log.d(TAG, "onListLoaded: " + stickers.length());
                        for (int j = 0; j < stickers.length(); j++) {
                            JSONObject jsonStickersChildNode = stickers.getJSONObject(j);
                            mStickers.add(new Sticker(
                                    jsonStickersChildNode.getString("image_file"),
                                    mEmojis
                            ));
                            mDownloadFiles.add(jsonStickersChildNode.getString("image_file"));
                        }
                        Log.d(TAG, "onListLoaded: " + mStickers.size());
                        Hawk.put(jsonChildNode.getString("identifier"), mStickers);
                        stickerPacks.get(i).setAndroidPlayStoreLink(android_play_store_link);
                        stickerPacks.get(i).setStickers(Hawk.get(jsonChildNode.getString("identifier"),new ArrayList<Sticker>()));
                        /*stickerModels.add(new StickerModel(
                                jsonChildNode.getString("name"),
                                mStickers.get(0).imageFileName,
                                mStickers.get(1).imageFileName,
                                mStickers.get(2).imageFileName,
                                mStickers.get(2).imageFileName,
                                mDownloadFiles
                        ));*/
                        mStickers.clear();
                    }
                    Hawk.put("sticker_packs", stickerPacks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new StickerAdapter(this, stickerPacks);
                try{
                    recyclerView.setAdapter(adapter);

                }catch (Exception e){
                    tv_log.append("\n" + e.getMessage().toString());
                }

                Log.d(TAG, "onListLoaded: Adatper set!");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onListLoaded: " + stickerPacks.size());
    }


    private String jsonBuilder()  {
        ArrayList<String> webps = new ArrayList<>();
        String startTags = "{\n" +
                "  \"android_play_store_link\": \"\",\n" +
                "  \"ios_app_store_link\": \"\",\n" +
                "  \"sticker_packs\": [\n" +
                "    {\n" +
                "      \"identifier\": \"" + 1 + "\",\n" +
                "      \"name\": \"" + "Test Paket" + "\",\n" +
                "      \"publisher\": \"" + "Marvin" + "\",\n" +
                "      \"tray_image_file\": \"icon.png\",\n" +
                "      \"image_data_version\":\"" + image_data_code + "\",\n" +
                "      \"avoid_cache\":true,\n" +
                "      \"publisher_email\":\"\",\n" +
                "      \"publisher_website\": \"\",\n" +
                "      \"privacy_policy_website\": \"\",\n" +
                "      \"license_agreement_website\": \"\",\n";
        String stickers = "";
        String stickersStr = "";
        File stickerFiles = new File(path + "/1/");
        File[] listFiles = stickerFiles.listFiles();

        JSONObject rootObject = new JSONObject();
        JSONArray stickerArr = new JSONArray();
        for(int i = 0; i<listFiles.length; i++){
            if(!listFiles[i].getName().equals("icon.png")){


               // tv_log.append("\n" + "I = " + i + " / " + String.valueOf(listFiles.length) + "   Name: " + listFiles[i].getName());

                try {
                    JSONObject object = new JSONObject();
                    object.put("image_file", listFiles[i].getName());
                    object.put("emojis" , "[\"\uD83D\uDCBB\",\"\uD83D\uDE29\"]");
                    stickerArr.put(object);
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
               // tv_log.append("\n Skipped" + listFiles[i].getName());
            }

        }
        try {
            rootObject.put("stickers", stickerArr);
            stickersStr = rootObject.toString();
            stickersStr = removeLastChar(stickersStr);
            stickersStr = removeFirstChar(stickersStr);


            tv_log.append("\n" + stickersStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String endTag = "    }\n" +
                "  ]\n" +
                "}";

        return startTags + "\n" + stickersStr + "\n" + endTag;
    }
    public String removeLastChar(String s) {
        s = s.substring(0, s.length()-1);
        return s;
    }
    public String removeFirstChar(String s){
        return s.substring(1);
    }
}
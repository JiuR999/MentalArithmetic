package com.swust.mentalarithmetic.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author ASUS
 */
public class FileUtil {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String CSV = ".csv";
    public static final String TXT = ".txt";
    private final String TAG = "FileUtil";
    private Context context;

    public FileUtil(Context context) {
        this.context = context;
    }

    public void saveFile(String filename, String type, String content){
        String PATH = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {

            PATH = Environment.getExternalStorageDirectory().getPath();
        }
        Log.d(TAG, Objects.requireNonNull(context.getExternalFilesDir(null)).getPath());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PATH+"/"+filename);
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            Log.d(TAG,"写入成功"+content);
        } catch (FileNotFoundException e) {
            Log.d(TAG,"文件没找到");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.d(TAG,"写入失败");
            throw new RuntimeException(e);
        }
    }

    void readFile(String filename){
        try {
            FileInputStream inputStream = new FileInputStream(filename);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            byte[] buffer = new byte[inputStream.available()];
            int len = 0;
            inputStream.read(buffer);
            String s = new String(buffer);
            Log.d(TAG,s);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

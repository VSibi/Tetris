package com.sibich.tetris;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Sibic_000 on 10.10.2016.
 */
public class JSONSerializer {

    private Context mContext;
    private String mFilename;

    public JSONSerializer(Context c, String f ) {
        mContext = c;
        mFilename = f;
    }

    public void saveGameField(int [][] gameField) throws JSONException,IOException {

        // Построение массива в JSON
        JSONArray array = new JSONArray();
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                array.put(gameField[i][j]);
            }
        }

        // Запись файла на диск
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    public int [][] loadGameField() throws IOException, JSONException {
        int [][] gameField = new int[20][10];

        BufferedReader reader = null;
        try {
            // Открытие и чтение файла в StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Разбор JSON с использованием JSONTokener
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                        .nextValue();
            // Построение массива  по данным JSON
            int k = 0;
            for (int i = 0; i < gameField.length; i++) {
                for (int j = 0; j<gameField[i].length; j++){
                    gameField[i][j] = (int)array.get(k);
                    k++;
                }
            }
        } catch (FileNotFoundException e) {
            // Происходит при начале "с нуля"; не обращайте внимания
        } finally {
            if (reader != null)
                reader.close();
        }
        return gameField;
    }

    public void deleteSavedGame() {
        try {
            String path = mContext.getApplicationInfo().dataDir + "/files/" + mFilename;
            File savedGame = new File(path);
            savedGame.delete();
            Toast.makeText(mContext, "DELETE file. OK!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(mContext, "Error DELETE file", Toast.LENGTH_SHORT).show();
        }
    }


}

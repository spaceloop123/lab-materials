package by.bsu.up.chat.storage.listener;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class JSONListener implements Listener {

    private static final Logger logger = Log.create(JSONListener.class);

    private Type itemsListType;
    private Gson gson;

    public JSONListener() {
        this.itemsListType = new TypeToken<List<Message>>() {
        }.getType();
        gson = new Gson();
    }

    @Override
    public void read(String fileName, List<Message> list) {
        try (Reader inputStreamReader = new InputStreamReader(new FileInputStream(fileName))) {
            List<Message> listItems = Collections.synchronizedList(gson.fromJson(new JsonReader(inputStreamReader), itemsListType));
            list.addAll(listItems);
        } catch (FileNotFoundException e) {
            logger.error("File " + fileName + " not found", e);
        } catch (IOException e) {
            logger.error("IOException while reading " + fileName, e);
        }
    }

    @Override
    public void write(String fileName, List<Message> list) {
        try (Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName))) {
            List<Message> listItems = Collections.synchronizedList(list);
            gson.toJson(listItems, outputStreamWriter);
        } catch (IOException e) {
            logger.error("Can't write chat history to " + fileName, e);
        }
    }
}

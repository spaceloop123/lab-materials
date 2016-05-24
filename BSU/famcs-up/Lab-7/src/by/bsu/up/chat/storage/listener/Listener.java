package by.bsu.up.chat.storage.listener;

import by.bsu.up.chat.common.models.Message;

import java.util.List;

public interface Listener {
    void read(String fileName, List<Message> list);

    void write(String fileName, List<Message> list);
}

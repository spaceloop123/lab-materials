package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.storage.listener.JSONListener;
import by.bsu.up.chat.storage.listener.Listener;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "files/messages.json";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();
    private Listener listener = new JSONListener();

    public InMemoryMessageStorage() {
        readMessages();
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        listener.write(DEFAULT_PERSISTENCE_FILE, messages);
    }

    @Override
    public boolean updateMessage(Message message) {
        for (Message item : messages) {
            if (message.getId().equals(item.getId())) {
                item.setText(message.getText()); //timestamp should changed either
                item.setEdited(true);

                logger.info("Update message " + item);

                listener.write(DEFAULT_PERSISTENCE_FILE, messages);

                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message item : messages) {
            if (messageId.equals(item.getId())) {
                //messages.remove(item);
                item.setRemoved(true);

                logger.info("Removed message " + item);

                listener.write(DEFAULT_PERSISTENCE_FILE, messages);

                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    private synchronized void readMessages() {
        listener.read(DEFAULT_PERSISTENCE_FILE, messages);
    }
}

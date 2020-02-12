package no.ntnu;


import java.util.ArrayList;

public class MessageQueue implements Channel {

    private final ArrayList<Object> queue;
    private final int size;

    public MessageQueue(int size) {
        if (size < 1) {
            size = 1; // Do not allow weird size
        }
        
        this.size = size;
        queue = new ArrayList<>(size);
    }

    @Override
    public synchronized void send(Object item) throws InterruptedException {
        while (this.queue.size() == this.size) {
            wait();
        }
        this.queue.add(item);
        if (this.queue.size() == 1) {
            notifyAll();
        }
        //IF the amount of object's is bigger than the size, block
    }

    // implements a nonblocking receive
    @Override
    public synchronized Object receive() throws InterruptedException{
        while (queue.isEmpty()) {
            wait();
        }
        if (this.queue.size() == this.size) {
            notifyAll();
        }

        return this.queue.remove(0);
    }

    @Override
    public int getNumQueuedItems() {
        return queue.size();
    }

    /**
     * Return comma-separated objects
     * @return 
     */
    @Override
    public String getQueueItemList() {
        String res = "";
        for (Object item : queue) {
            res += item + ",";
        }
        return res;
    }
}

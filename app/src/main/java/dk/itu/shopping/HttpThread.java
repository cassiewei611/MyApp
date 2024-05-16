package dk.itu.shopping;

import java.util.List;
import java.util.concurrent.Semaphore;

public class HttpThread implements Runnable {
    // to pass parameters to thread
    String url;
    List<Item> values;
    Semaphore init;

    public HttpThread(String url, List<Item> values, Semaphore init) {
        this.values = values;
        this.url = url;
        this.init = init;
    }

    // doInBackGround
    public void run() {
        new NetworkFetcher().fetchItems(url, values);
        init.release(); //Signals that initialization of values is finished
    }
}


package ee.locawork.util;

import com.billy.android.preloader.interfaces.DataLoader;

public class BillyPreloader implements DataLoader<String> {
    @Override
    public String loadData() {
        //this method runs in thread pool
        // load data in this method synchronously
        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {
        }
        return "data from network server";
    }
}

package ee.locawork.util;

import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {

    public static void startCountout(TextView timeView){

        Timer timer = new Timer();        // A thread of execution is instantiated
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int timeFrame = 45;
                    timeView.setText(--timeFrame);

                    if(timeFrame == 0){
                        timer.cancel(); // timer is cancelled when time reaches 0
                    }
                }
            },0,1000);
            // 0 is the time in second from when this code is to be executed
            // 1000 is time in millisecond after which it has to repeat
        }
}

package ee.locawork.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScannerView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ee.locawork.services.ServiceReachedJob;

public class TimerUtils {

    public static void startCount(Activity activity, TextView timeView,
                                  CodeScannerView codeScannerView, long expectedEndTime,
                                  TextView expectedSalary){
        long timer = 0;
        long currentDate= new Date().getTime();
        if(expectedEndTime > currentDate){
            timer = expectedEndTime - currentDate;
            new CountDownTimer(timer, 1000) { // adjust the milli seconds here
                @SuppressLint("DefaultLocale")
                public void onTick(long millisUntilFinished) {
                    timeView.setText(""+String.format("%d hour, %d min, %d sec",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 360,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    PreferencesUtil.save(activity,  ServiceReachedJob.KEY_HOURS_TO_WORK, millisUntilFinished * 1000);
                    String salary = PreferencesUtil.readString(activity, ServiceReachedJob.KEY_JOB_SALARY, "");
                    long expectedSalaryLong = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 360) * ((expectedEndTime- currentDate)/(60*60*1000));
                    long roundedUp = Math.round(expectedSalaryLong/10.0) * 10;
                    expectedSalary.setText(roundedUp + "");
                }

                public void onFinish() {
                    timeView.setText("done!");
                    codeScannerView.setVisibility(View.VISIBLE);
                }
            }.start();
        }else{
            timeView.setText("done!");
            codeScannerView.setVisibility(View.VISIBLE);
        }
    }
}

package ee.locawork.ui.myaddedwork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.ramotion.fluidslider.FluidSlider;

import ee.locawork.ActivityWorkInProgress;
import ee.locawork.ControllerEndWork;
import ee.locawork.model.dto.EndTimeDTO;
import ee.locawork.services.ServiceReachedJob;
import ee.locawork.ui.myaddedwork.ControllerGetCurrentlyWorkingPersonData;
import ee.locawork.ui.myaddedwork.ControllerGetSelectedApplyerData;
import ee.locawork.R;
import ee.locawork.alert.AlertAskPermissionBeforeDeleting;
import ee.locawork.model.Job;
import ee.locawork.ui.findwork.alert.AlertEditJob;
import ee.locawork.ui.myaddedwork.AdapterAddedJobs;
import ee.locawork.ui.myaddedwork.ControllerDeleteJob;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.PrefConstants;
import ee.locawork.util.PreferencesUtil;

import java.util.Date;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ee.locawork.util.TimeUtil;

import static android.content.Context.WINDOW_SERVICE;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;


public class AlertAddedJob {


    private static String url = "";

    public static void init(final AdapterAddedJobs adapterAddedJobs, final List<Job> jobList, final int position, final Activity activity, final Context context, String location) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        final Job job = jobList.get(position);
        url =  Integer.toString(job.getApplyerId());
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_added_job_detail, null);
        alertDialog.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(jobList.get(position).getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(job.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_duration)).setText(job.getHoursToWork() + "");
        ((TextView) dialogView.findViewById(R.id.location_tv)).setText(location);
        dialogView.findViewById(R.id.end_user_work).setOnClickListener(view -> {
            String endTime = new Date().getTime() + "";
            EndTimeDTO endTimeDTO = new EndTimeDTO();
            endTimeDTO.setEndTime(endTime);
            endTimeDTO.setApplyerId(jobList.get(position).getApplyerId());
            endTimeDTO.setJobId(jobList.get(position).getId());

            new ControllerEndWork().postData(context, endTimeDTO);
            alertDialog.cancel();
        });
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.added_job));
        if(job.getWorkStartTime() != null){
            ((TextView) dialogView.findViewById(R.id.work_started)).setText(TimeUtil.dayStringFormat(Long.parseLong(job.getWorkStartTime().toString()), context));
        }else{
            ((TextView) dialogView.findViewById(R.id.work_started)).setText(context.getResources().getString(R.string.user_has_not_started_the_work_yet));
        }
        if(job.getWorkEndTime() != null){
            ((TextView) dialogView.findViewById(R.id.work_finished)).setText(TimeUtil.dayStringFormat(Long.parseLong(job.getWorkEndTime().toString()), context));
            ((TextView) dialogView.findViewById(R.id.end_user_work)).setVisibility(View.GONE);
        }else{
            ((TextView) dialogView.findViewById(R.id.work_finished)).setText(context.getResources().getString(R.string.user_has_not_finished_the_work_yet));
            ((TextView) dialogView.findViewById(R.id.end_user_work)).setVisibility(View.VISIBLE);
        }


        ImageView qrCode = dialogView.findViewById(R.id.qr_code);
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.format("%1$,.2f", new Object[]{Double.valueOf(job.getSalary())}));
        ImageButton delete = dialogView.findViewById(R.id.cancel_application);
        int applyerId = job.getApplyerId();

        ControllerGetSelectedApplyerData controllerGetSelectedApplyerData = new ControllerGetSelectedApplyerData();
        controllerGetSelectedApplyerData.getData(context, job.getApplyerId(), dialogView);

        ControllerGetCurrentlyWorkingPersonData controllerGetCurrentlyWorkingPersonData = new ControllerGetCurrentlyWorkingPersonData();
        controllerGetCurrentlyWorkingPersonData.getData(context, job.getApplyerId(), dialogView);

        ImageButton edit = dialogView.findViewById(R.id.edit_job);
        ImageButton back = dialogView.findViewById(R.id.back);
        delete.setOnClickListener(v -> {
            if (PreferencesUtil.readString(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START).equals("1")) {
                AlertAskPermissionBeforeDeleting.init(activity, context, job);
            } else {
                new ControllerDeleteJob().deleteJob(context, job.getId());
                alertDialog.cancel();
                jobList.remove(job);

            }
            adapterAddedJobs.notifyDataSetChanged();
        });
        edit.setOnClickListener(v -> {
            AlertEditJob.init(activity, context, job);
        });
        back.setOnClickListener(v -> alertDialog.cancel());

        generateQrCode(context, qrCode, url);
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }

    private static void generateQrCode(Context context, ImageView qrCode, String url) {
        Bitmap bitmap = null;
        // the windowmanager service.
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        QRGEncoder qrgEncoder = new QRGEncoder(url, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }
}

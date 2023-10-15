package ee.locawork.ui.report;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import ee.locawork.R;


public class FragmentReport extends Fragment {
    public EditText content;
    private ImageButton submit;
    public EditText title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        this.title = root.findViewById(R.id.report_title);
        this.content = root.findViewById(R.id.report_content);
        ImageButton imageButton = (ImageButton) root.findViewById(R.id.report_submit);
        this.submit = imageButton;
        imageButton.setOnClickListener(v -> {
            if (FragmentReport.this.title.getText().equals("") || FragmentReport.this.content.getText().equals("")) {
                Toast.makeText(FragmentReport.this.getContext(), FragmentReport.this.getResources().getString(R.string.please_fill_in_all_data), Toast.LENGTH_LONG).show();
                return;
            }
            FragmentReport.this.sendMail();

            FragmentReport.this.title.setText("");
            FragmentReport.this.content.setText("");
        });
        return root;
    }

    /* access modifiers changed from: private */
    public void sendMail() {
        String emailsend = "plado.andreas@locawork.ee";
        String emailsubject = title.getText().toString();
        String emailbody = content.getText().toString();

        // define Intent object with action attribute as ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);

        // add three fields to intent using putExtra function
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailsend});
        intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailbody);

        // set type of intent
        intent.setType("message/rfc822");

        // startActivity with intent with chooser as Email client using createChooser function
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_an_email_client) + " :"));
    }
}

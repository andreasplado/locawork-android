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
        this.title = (EditText) root.findViewById(R.id.report_title);
        this.content = (EditText) root.findViewById(R.id.report_content);
        ImageButton imageButton = (ImageButton) root.findViewById(R.id.report_submit);
        this.submit = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FragmentReport.this.title.getText().equals("") || FragmentReport.this.content.getText().equals("")) {
                    Toast.makeText(FragmentReport.this.getContext(), FragmentReport.this.getResources().getString(R.string.please_fill_in_all_data), Toast.LENGTH_LONG).show();
                    return;
                }
                FragmentReport.this.sendMail();
                FragmentReport.this.title.setText("");
                FragmentReport.this.content.setText("");
            }
        });
        return root;
    }

    /* access modifiers changed from: private */
    public void sendMail() {
        Intent i = new Intent("android.intent.action.SEND");
        i.setType("message/rfc822");
        i.putExtra("android.intent.extra.EMAIL", new String[]{getResources().getString(R.string.locawork_mail)});
        i.putExtra("android.intent.extra.SUBJECT", this.content.getText() + ": " + this.title.getText());
        i.putExtra("android.intent.extra.TEXT", this.content.getText());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
        }
    }
}

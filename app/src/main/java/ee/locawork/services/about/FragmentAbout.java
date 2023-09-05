package ee.locawork.services.about;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import ee.locawork.R;

public class FragmentAbout extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        TextView licence = (TextView) root.findViewById(R.id.licence);
        licence.setText(Html.fromHtml(getResources().getString(R.string.licence_content)));
        licence.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }
}

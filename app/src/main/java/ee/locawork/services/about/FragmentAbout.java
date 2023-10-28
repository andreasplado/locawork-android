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
import ee.locawork.alert.AlertPrivacyPolicy;
import ee.locawork.alert.AlertWebsite;

public class FragmentAbout extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        TextView licence = (TextView) root.findViewById(R.id.licence);
        TextView privacyPolicy = (TextView) root.findViewById(R.id.privacy_policy);
        TextView website = (TextView) root.findViewById(R.id.website);
        licence.setText(Html.fromHtml(getResources().getString(R.string.licence_content)));
        licence.setMovementMethod(LinkMovementMethod.getInstance());
        website.setText(Html.fromHtml(getResources().getString(R.string.website_link)));
        privacyPolicy.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy_link)));

        website.setOnClickListener(view -> {
            AlertWebsite.init(getActivity(), getContext());
        });
        privacyPolicy.setOnClickListener(view -> {
            AlertPrivacyPolicy.init(getActivity(), getContext());
        });
        licence.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }
}

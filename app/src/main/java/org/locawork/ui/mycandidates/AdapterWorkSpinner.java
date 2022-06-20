package org.locawork.ui.mycandidates;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.locawork.R;
import org.locawork.SignInActivity;
import org.locawork.model.JobApplicationDTO;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AdapterWorkSpinner implements SpinnerAdapter {

    private Context context;
    private List<JobApplicationDTO> listdata;
    private LayoutInflater inflter;
    private ControllerCandiates controllerCandiates = new ControllerCandiates();

    public AdapterWorkSpinner(List<JobApplicationDTO> listData, Context context) {
        this.listdata = listData;
        this.context = context;
        inflter = (LayoutInflater.from(context));
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.item_candidate_work, null);
        TextView candidateWorkName = convertView.findViewById(R.id.default_value);
        LinearLayout dropdown = convertView.findViewById(R.id.dropdown);
        candidateWorkName.setText(listdata.get(position).getTitle());
        dropdown.setOnClickListener(v -> {
            this.controllerCandiates.getDatawithFilter(context, PreferencesUtil.readInt(context, KEY_USER_ID, 0),listdata.get(position).getTitle() );
            dropdown.setVisibility(View.GONE);
        });
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.item_dropdown_default_view, null);
        TextView candidateWorkName = convertView.findViewById(R.id.default_value);
        candidateWorkName.setText(listdata.get(position).getTitle());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return listdata.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}

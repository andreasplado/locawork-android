package org.locawork.ui.addedjob;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class AddedJobsAdapter extends RecyclerView.Adapter<AddedJobsAdapter.ViewHolder>{
    private List<Job> listdata;

    public AddedJobsAdapter(List<Job> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_added_job, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final List<Job> myListData = listdata;
        holder.titleTv.setText(myListData.get(position).getTitle());
        holder.salaryTv.setText(Double.toString(myListData.get(position).getSalary()));
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView salaryTv;
        public TextView titleTv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.salaryTv = itemView.findViewById(R.id.tv_salary);
            this.titleTv = itemView.findViewById(R.id.tv_job_title);
        }
    }
}
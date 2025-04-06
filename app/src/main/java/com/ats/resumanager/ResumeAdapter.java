package com.ats.resumanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ats.resumanager.models.Resume;
import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {
    private List<Resume> resumes;

    public ResumeAdapter(List<Resume> resumes) {
        this.resumes = resumes;
    }

    @NonNull
    @Override
    public ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resume, parent, false);
        return new ResumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeViewHolder holder, int position) {
        Resume resume = resumes.get(position);
        holder.tvFileName.setText(resume.getFileName());
        holder.tvScore.setText(String.format("%.1f/100", resume.getAtsScore()));
        holder.tvFileType.setText(resume.getFileType().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return resumes.size();
    }

    public void updateData(List<Resume> newResumes) {
        resumes = newResumes;
        notifyDataSetChanged();
    }

    static class ResumeViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName, tvScore, tvFileType;

        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvFileType = itemView.findViewById(R.id.tvFileType);
        }
    }
}
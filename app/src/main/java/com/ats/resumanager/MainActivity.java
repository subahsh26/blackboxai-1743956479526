package com.ats.resumanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.ats.resumanager.models.Resume;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int FILE_PICKER_REQUEST = 1;
    private RecyclerView recyclerView;
    private ResumeAdapter adapter;
    private ProgressBar progressBar;
    private List<Resume> resumeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.rvResumes);
        progressBar = findViewById(R.id.progress);
        Button btnUpload = findViewById(R.id.btnUpload);

        // Setup RecyclerView
        adapter = new ResumeAdapter(resumeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set click listener for upload button
        btnUpload.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == FILE_PICKER_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    try {
                        if (data.getClipData() != null) {
                            // Multiple files selected
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                processFile(uri);
                            }
                        } else if (data.getData() != null) {
                            // Single file selected
                            processFile(data.getData());
                        }
                        
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            adapter.updateData(resumeList);
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "Error processing files", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            }
        }
    }

    private void processFile(Uri uri) {
        try {
            String content = ResumeParser.parseDocument(this, uri);
            
            String fileName = getFileName(uri);
            String fileType = getFileType(fileName);
            Resume resume = new Resume(fileName, uri.toString(), fileType);
            
            // Calculate ATS score
            ATSScoreCalculator.calculateScore(resume, content.toString());
            
            resumeList.add(resume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileName(Uri uri) {
        String result = uri.getLastPathSegment();
        if (result == null) {
            return "Unknown";
        }
        return result.substring(result.lastIndexOf('/') + 1);
    }

    private String getFileType(String fileName) {
        if (fileName.endsWith(".pdf")) return "PDF";
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) return "DOC";
        return "OTHER";
    }

    private void showSortDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Sort Resumes")
            .setItems(new String[]{"By Score (High to Low)", "By Score (Low to High)", "By File Type", "By Date"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Collections.sort(resumeList, (r1, r2) -> Double.compare(r2.getAtsScore(), r1.getAtsScore()));
                        break;
                    case 1:
                        Collections.sort(resumeList, (r1, r2) -> Double.compare(r1.getAtsScore(), r2.getAtsScore()));
                        break;
                    case 2:
                        Collections.sort(resumeList, (r1, r2) -> r1.getFileType().compareTo(r2.getFileType()));
                        break;
                    case 3:
                        Collections.sort(resumeList, (r1, r2) -> r2.getUploadDate().compareTo(r1.getUploadDate()));
                        break;
                }
                adapter.updateData(resumeList);
            })
            .show();
    }
}

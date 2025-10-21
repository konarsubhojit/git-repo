package com.cloudsync.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private List<File> folders = new ArrayList<>();
    private File parentFolder = null;
    private OnFolderClickListener listener;

    public interface OnFolderClickListener {
        void onFolderClick(File folder);
    }

    public FolderAdapter(OnFolderClickListener listener) {
        this.listener = listener;
    }

    public void setFolders(List<File> folders, File parentFolder) {
        this.folders = folders;
        this.parentFolder = parentFolder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        File folder = folders.get(position);
        boolean isParent = parentFolder != null && folder.equals(parentFolder);
        
        if (isParent) {
            holder.folderName.setText(".. (Parent Folder)");
            holder.folderIcon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.folderName.setText(folder.getName());
            holder.folderIcon.setImageResource(R.drawable.ic_folder);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFolderClick(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView folderIcon;
        TextView folderName;

        FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderIcon = itemView.findViewById(R.id.folderIcon);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }
}

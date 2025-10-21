package com.cloudsync.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudsync.app.models.CloudFolder;

import java.util.ArrayList;
import java.util.List;

public class CloudFolderAdapter extends RecyclerView.Adapter<CloudFolderAdapter.CloudFolderViewHolder> {
    private List<CloudFolder> folders = new ArrayList<>();
    private boolean hasParent = false;
    private OnFolderClickListener listener;

    public interface OnFolderClickListener {
        void onFolderClick(CloudFolder folder);
        void onParentClick();
    }

    public CloudFolderAdapter(OnFolderClickListener listener) {
        this.listener = listener;
    }

    public void setFolders(List<CloudFolder> folders, boolean hasParent) {
        this.folders = folders;
        this.hasParent = hasParent;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CloudFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);
        return new CloudFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CloudFolderViewHolder holder, int position) {
        if (position == 0 && hasParent) {
            // Parent folder item
            holder.folderName.setText(".. (Parent Folder)");
            holder.folderIcon.setImageResource(R.drawable.ic_folder);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onParentClick();
                }
            });
        } else {
            // Regular folder
            int folderIndex = hasParent ? position - 1 : position;
            CloudFolder folder = folders.get(folderIndex);
            
            holder.folderName.setText(folder.getName());
            holder.folderIcon.setImageResource(R.drawable.ic_cloud);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFolderClick(folder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return folders.size() + (hasParent ? 1 : 0);
    }

    static class CloudFolderViewHolder extends RecyclerView.ViewHolder {
        ImageView folderIcon;
        TextView folderName;

        CloudFolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderIcon = itemView.findViewById(R.id.folderIcon);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }
}

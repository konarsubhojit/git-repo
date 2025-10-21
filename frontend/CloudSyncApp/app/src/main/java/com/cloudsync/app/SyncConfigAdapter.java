package com.cloudsync.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudsync.app.models.SyncConfig;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SyncConfigAdapter extends RecyclerView.Adapter<SyncConfigAdapter.ViewHolder> {
    private List<SyncConfig> configs;
    private OnConfigClickListener listener;

    public interface OnConfigClickListener {
        void onConfigClick(SyncConfig config);
        void onConfigLongClick(SyncConfig config);
    }

    public SyncConfigAdapter(OnConfigClickListener listener) {
        this.configs = new ArrayList<>();
        this.listener = listener;
    }

    public void setConfigs(List<SyncConfig> configs) {
        this.configs = configs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sync_config, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SyncConfig config = configs.get(position);
        holder.bind(config);
    }

    @Override
    public int getItemCount() {
        return configs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private ImageView providerIcon;
        private TextView localFolderText;
        private TextView cloudFolderText;
        private TextView syncModeText;
        private TextView statusText;
        private ImageView statusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            providerIcon = itemView.findViewById(R.id.providerIcon);
            localFolderText = itemView.findViewById(R.id.localFolderText);
            cloudFolderText = itemView.findViewById(R.id.cloudFolderText);
            syncModeText = itemView.findViewById(R.id.syncModeText);
            statusText = itemView.findViewById(R.id.statusText);
            statusIcon = itemView.findViewById(R.id.statusIcon);
        }

        public void bind(SyncConfig config) {
            localFolderText.setText(config.getLocalFolderPath());
            cloudFolderText.setText(config.getCloudFolderPath());
            syncModeText.setText(config.getSyncMode().getDisplayName());
            
            // Set provider icon
            if ("google".equals(config.getProvider())) {
                providerIcon.setImageResource(R.drawable.ic_google);
            } else {
                providerIcon.setImageResource(R.drawable.ic_onedrive);
            }
            
            // Set status
            if (config.isEnabled()) {
                statusText.setText("Active");
                statusIcon.setImageResource(R.drawable.ic_check_circle);
            } else {
                statusText.setText("Disabled");
                statusIcon.setImageResource(R.drawable.ic_error);
            }
            
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConfigClick(config);
                }
            });
            
            cardView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onConfigLongClick(config);
                }
                return true;
            });
        }
    }
}

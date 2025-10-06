package com.cloudsync.app;

import android.accounts.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    
    private List<Account> accounts;
    private OnAccountClickListener listener;
    
    public interface OnAccountClickListener {
        void onAccountClick(Account account);
    }
    
    public AccountAdapter(List<Account> accounts, OnAccountClickListener listener) {
        this.accounts = accounts;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.bind(account, listener);
    }
    
    @Override
    public int getItemCount() {
        return accounts.size();
    }
    
    static class AccountViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private TextView accountEmail;
        
        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.accountName);
            accountEmail = itemView.findViewById(R.id.accountEmail);
        }
        
        public void bind(Account account, OnAccountClickListener listener) {
            accountName.setText(account.name);
            accountEmail.setText(account.type);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAccountClick(account);
                }
            });
        }
    }
}

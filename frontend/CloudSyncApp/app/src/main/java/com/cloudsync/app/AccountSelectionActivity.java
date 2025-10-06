package com.cloudsync.app;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AccountSelectionActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 1;
    
    private RecyclerView recyclerView;
    private TextView noAccountsText;
    private AccountAdapter adapter;
    private List<Account> googleAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_selection);
        
        recyclerView = findViewById(R.id.accountsRecyclerView);
        noAccountsText = findViewById(R.id.noAccountsText);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        googleAccounts = new ArrayList<>();
        
        checkPermissionAndLoadAccounts();
    }
    
    private void checkPermissionAndLoadAccounts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSION_REQUEST_GET_ACCOUNTS);
        } else {
            loadGoogleAccounts();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGoogleAccounts();
            } else {
                showNoAccounts();
            }
        }
    }
    
    private void loadGoogleAccounts() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        
        googleAccounts.clear();
        for (Account account : accounts) {
            googleAccounts.add(account);
        }
        
        if (googleAccounts.isEmpty()) {
            showNoAccounts();
        } else {
            showAccounts();
        }
    }
    
    private void showAccounts() {
        recyclerView.setVisibility(View.VISIBLE);
        noAccountsText.setVisibility(View.GONE);
        
        adapter = new AccountAdapter(googleAccounts, account -> {
            // Account selected
            Intent resultIntent = new Intent();
            resultIntent.putExtra("account_name", account.name);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        
        recyclerView.setAdapter(adapter);
    }
    
    private void showNoAccounts() {
        recyclerView.setVisibility(View.GONE);
        noAccountsText.setVisibility(View.VISIBLE);
    }
}

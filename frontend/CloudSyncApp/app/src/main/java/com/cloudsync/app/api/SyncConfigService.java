package com.cloudsync.app.api;

import com.cloudsync.app.api.responses.SyncConfigListResponse;
import com.cloudsync.app.api.responses.SyncConfigResponse;
import com.cloudsync.app.api.requests.CreateSyncConfigRequest;
import com.cloudsync.app.api.requests.UpdateSyncConfigRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SyncConfigService {
    
    @GET("api/sync-config")
    Call<SyncConfigListResponse> getConfigs();
    
    @GET("api/sync-config/{configId}")
    Call<SyncConfigResponse> getConfig(@Path("configId") String configId);
    
    @POST("api/sync-config")
    Call<SyncConfigResponse> createConfig(@Body CreateSyncConfigRequest request);
    
    @PUT("api/sync-config/{configId}")
    Call<SyncConfigResponse> updateConfig(@Path("configId") String configId, 
                                          @Body UpdateSyncConfigRequest request);
    
    @DELETE("api/sync-config/{configId}")
    Call<SyncConfigResponse> deleteConfig(@Path("configId") String configId);
}

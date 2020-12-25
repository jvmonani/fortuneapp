package com.mydomain.yyjp.myfortuneapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.mydomain.yyjp.myfortuneapp.network.FortuneResponse;
import com.mydomain.yyjp.myfortuneapp.network.NetworkApi;
import com.mydomain.yyjp.myfortuneapp.network.NetworkService;
import com.mydomain.yyjp.myfortuneapp.util.SingleLiveEvent;

import java.util.List;

import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = ViewModel.class.getSimpleName();
    private MutableLiveData<List<String>> itemList = new MutableLiveData<>();
    private SingleLiveEvent<String> errorStatus = new SingleLiveEvent<>();

    public MainActivityViewModel(Context context) {
        Log.d(TAG, "MainActivityViewModel");
        Context applicationContex = context;
        initialize();
    }

    private void initialize() {
        // initialize fortune
        getFortune();
    }

    private void updateList(@NonNull List<String> itemList) {
        Log.d("ViewModel", "Updating List: " + itemList.size());
        this.itemList.postValue(itemList);
    }


    public void getFortune() {
        NetworkApi networkApi = new NetworkService().getApi();
        Call<FortuneResponse> call = networkApi.getFortune();
        call.enqueue(new Callback<FortuneResponse>() {
            @Override
            public void onResponse(Call<FortuneResponse> call, Response<FortuneResponse> response) {
                Log.d(TAG, "Response: " + response);
                if(response != null && response.isSuccessful()) {
                    FortuneResponse fortuneResponse = response.body();
                    if(fortuneResponse.getItems() != null)
                        updateList(fortuneResponse.getItems());
                    else
                        errorStatus.postValue("Empty Server Response");
                } else {
                    Log.d(TAG, "Response Error: " + response.message());
                    errorStatus.postValue(response.message());
                }
            }

            @Override
            public void onFailure(Call<FortuneResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() + ", cause: " + t.getCause());
                errorStatus.postValue(t.getMessage());
            }
        });
    }

    public MutableLiveData<List<String>> getItemList() {
        return itemList;
    }

    public SingleLiveEvent<String> getErrorStatus() {
        return errorStatus;
    }

    @Override
    public void onCleared() {
        Log.d(TAG, "onCleared");
        if(itemList.getValue() != null)
            itemList.getValue().clear();
        itemList.setValue(null);
        errorStatus.setValue(null);
    }
}

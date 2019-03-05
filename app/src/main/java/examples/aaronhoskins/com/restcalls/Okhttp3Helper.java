package examples.aaronhoskins.com.restcalls;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import examples.aaronhoskins.com.restcalls.model.users.UserResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static examples.aaronhoskins.com.restcalls.UrlConstants.ACTUAL_URL;


public class Okhttp3Helper {

    public static String getSyncroniousOkhttpResonse() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ACTUAL_URL)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static void getAsyncOkHttpResponse() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ACTUAL_URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //get json response from the rest call
                String jsonResponse = response.body().string();
                //Create a gson converter for the json string
                Gson gsonConverter = new Gson();
                //post an EventBus post event containing the UserResponse object parsed from the json
                Log.d("TAG_OKHTTP_ASYNC", jsonResponse);
                EventBus.getDefault().post(gsonConverter.fromJson(jsonResponse, UserResponse.class));

            }
        });
    }
}

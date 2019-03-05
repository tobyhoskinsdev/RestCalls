package examples.aaronhoskins.com.restcalls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import examples.aaronhoskins.com.restcalls.model.users.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread restCallThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String json = HttpUrlConnectionHelper.getJsonUsingHttpURLConn();
                        Gson gson = new Gson();
                        //Convert json string to a POJO
                        UserResponse userResponse = gson.fromJson(json,UserResponse.class);
                        Log.d("TAG", json);
                        Log.d("TAG", userResponse.getResults().get(0).getEmail());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        restCallThread.start();

        //Make sync. okhttp request
        AsyncTaskForRestCall asyncTaskForRestCall = new AsyncTaskForRestCall();
        asyncTaskForRestCall.execute();

        //Make async okhttp request
        Okhttp3Helper.getAsyncOkHttpResponse();

        RetrofitHelper retrofitHelper = new RetrofitHelper();
        //Start Retrofit in a async way to get our pojo response
        retrofitHelper.getRandomUsers("3").enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                String email = response.body().getResults().get(0).getEmail();
                Log.d("TAG_RETROFIT", "onResponse: " + email);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Register to EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Unregister to EventBus
        EventBus.getDefault().unregister(this);
    }

    //Subscribe to the posting event on EventBus that is passing a UserResponse
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userResponseEvent(UserResponse userResponse) {
        Log.d("TAG_EVENT_RECEIVED", userResponse.getResults().get(0).getEmail());
    }
}

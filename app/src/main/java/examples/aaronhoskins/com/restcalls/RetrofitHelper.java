package examples.aaronhoskins.com.restcalls;

import examples.aaronhoskins.com.restcalls.model.users.UserResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static examples.aaronhoskins.com.restcalls.UrlConstants.BASE_URL;
import static examples.aaronhoskins.com.restcalls.UrlConstants.PATH;
import static examples.aaronhoskins.com.restcalls.UrlConstants.QUERY_RESULTS;

public class RetrofitHelper {
    //create retrofit client
    private Retrofit getRetrofitClient() {
         return new Retrofit.Builder()
                 .baseUrl(BASE_URL)
                 //Uses gson behind the scenes to make the result object
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
    }
    //build the retrofit service
    private GetRandomUsers getUserResponse() {
        return getRetrofitClient().create(GetRandomUsers.class);
    }
    //get the response
    public Call<UserResponse> getRandomUsers(String numOfResponses) {
        return getUserResponse().getRandomUsers("5");
    }


    public interface GetRandomUsers{
        @GET(PATH)
        Call<UserResponse> getRandomUsers(@Query(QUERY_RESULTS) String numOfResponse);
    }

}

package RetrofitClient;

import java.util.List;

import Models.Athlete;
import Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Okuhle on 6/18/2016.
 */
public interface IUser {

    //Get the user's details based on supplied ID
    @GET("User/GetUser/{id}")
    Call<User> getUser(@Path("id") int userId);

    //Add a user's details to the system
    @POST("User/Add/user")
    Call<User> addUser(@Body User newUser, @Body Athlete newAthlete);


}

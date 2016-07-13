package RetrofitClient;

import Models.Athlete;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Okuhle on 6/12/2016.
 */

//All methods relating to the athlete will be grouped in this interface
public interface IAthlete {

    //Get the athlete's details based on the supplied ID
    @GET("Athlete/Get/{id}")
    Call<Athlete> getAthlete(@Path("id") int athleteId);

    //Add a new athlete to the system
    @POST("Athlete/Add/athlete")
    Call<Athlete> addAthlete(@Body Athlete newAthlete);

    //Update an athlete's details
    @PUT("Athlete/Update/athlete")
    Call<Athlete> updateAthlete(@Body Athlete updatedAthlete);


}

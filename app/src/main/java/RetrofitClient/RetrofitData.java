package RetrofitClient;

import Models.Athlete;
import Models.User;
import retrofit2.Retrofit;

/**
 * Created by Okuhle on 6/26/2016.
 */
public class RetrofitData {

    private Retrofit retrofitClient;

    public RetrofitData(String baseUrl) {
        retrofitClient = new Retrofit.Builder().baseUrl(baseUrl).build();
    }

    //To add a new athlete to our system, we will create a new athlete and user
    public void addAthlete(User newUser, Athlete newAthlete) {
        IUser newUserObject = retrofitClient.create(IUser.class);
        IAthlete athleteObject = retrofitClient.create(IAthlete.class);

        newUserObject.addUser(newUser, newAthlete);
    }
}

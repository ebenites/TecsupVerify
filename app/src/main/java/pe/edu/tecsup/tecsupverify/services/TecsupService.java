package pe.edu.tecsup.tecsupverify.services;

import java.util.List;

import pe.edu.tecsup.tecsupverify.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TecsupService {

    @GET("api/user/{dni}")
    Call<List<User>> searchUsers(@Path("dni") String dni);

}

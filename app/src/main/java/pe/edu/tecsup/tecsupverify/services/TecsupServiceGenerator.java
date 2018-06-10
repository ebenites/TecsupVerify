package pe.edu.tecsup.tecsupverify.services;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.edu.tecsup.tecsupverify.models.APIError;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ebenites on 07/01/2017.
 * Best Practice: TecsupServiceGenerator https://futurestud.io/blog/retrofit-getting-started-and-android-client#servicegenerator
 */
public final class TecsupServiceGenerator {

    public static final String API_BASE_URL = "https://api.tecsup.edu.pe/tecsup-api/";
//    public static final String API_BASE_URL = "http://10.0.2.2:8080/";

    public static final String PHOTO_URL = API_BASE_URL + "/api/user/picture/";

    private static final String TAG = TecsupServiceGenerator.class.getSimpleName();

    private static Retrofit retrofit;

    private TecsupServiceGenerator() {
    }

    public static <S> S createService(final Class<S> serviceClass) {

        if(retrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Retrofit Debug: https://futurestud.io/blog/retrofit-2-log-requests-and-responses
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(TecsupServiceGenerator.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()).build();
        }

        return retrofit.create(serviceClass);
    }

    // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
    public static APIError parseError(retrofit2.Response<?> response) {
        if(response.code() == 404)
            return new APIError("Servicio no disponible");
        Converter<ResponseBody, APIError> converter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
        try {
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError("Error en el servicio");
        }
    }

}

package be.wienert.soundbird.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.wienert.soundbird.data.model.Sound;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class RestApi {
    private static String SERVER_URL = "http://178.128.244.80/";

    private ApiContract service;

    public RestApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiContract.class);
    }

    public LiveData<List<Sound>> getSounds() {
        final MutableLiveData<List<Sound>> data = new MutableLiveData<>();
        service.getSounds().enqueue(new Callback<List<RestApiSound>>() {
            @Override
            public void onResponse(@NonNull Call<List<RestApiSound>> call, @NonNull Response<List<RestApiSound>> response) {
                if (response.isSuccessful()) {
                    List<RestApiSound> restSounds = response.body();
                    List<Sound> sounds = new ArrayList<>();
                    for (RestApiSound sound : restSounds) {
                        sounds.add(new Sound(UUID.fromString(sound.uuid), Uri.parse(sound.uri), sound.name));
                    }
                    data.setValue(sounds);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RestApiSound>> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public void addSound(Sound sound) {
        // TODO
    }

//    public Sound addSound(Sound sound) throws IOException {
//        File file = new File(sound.uri.getPath());
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), sound.name);
//        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", String.valueOf(sound.id) + ".mp3", requestFile);
//
//        Call<Sound> call = service.addSound(multipartBody, name);
//        Response<Sound> response = call.execute();
//        if (!response.isSuccessful()) {
//            throw new IOException(response.errorBody().string());
//        }
//        return response.body();
//    }

    private class RestApiSound {
        String uuid;
        String name;
        String uri;
    }

    private interface ApiContract {
        @GET("sounds")
        Call<List<RestApiSound>> getSounds();

        @Multipart
        @POST("sounds")
        Call<RestApiSound> addSound(@Part MultipartBody.Part file,
                             @Part("name") RequestBody name);
    }
}

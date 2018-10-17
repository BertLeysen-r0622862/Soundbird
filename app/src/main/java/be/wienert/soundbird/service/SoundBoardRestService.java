package be.wienert.soundbird.service;

import java.io.IOException;
import java.util.List;

import be.wienert.soundbird.model.Sound;
import be.wienert.soundbird.model.SoundBoard;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class SoundBoardRestService implements SoundBoardService {

    private SoundsRestServiceInterface service;

    public SoundBoardRestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://178.128.244.80/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SoundsRestServiceInterface.class);
    }

    @Override
    public List<SoundBoard> getBoards() throws IOException {
        Call<List<SoundBoard>> call = service.getBoards();
        Response<List<SoundBoard>> response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody().string());
        }
        return response.body();
    }

    @Override
    public List<Sound> getSounds() throws IOException {
        Call<List<Sound>> call = service.getSounds();
        Response<List<Sound>> response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody().string());
        }
        return response.body();
    }

    private interface SoundsRestServiceInterface {
        @GET("sounds")
        Call<List<Sound>> getSounds();

        @GET("boards")
        Call<List<SoundBoard>> getBoards();
    }
}

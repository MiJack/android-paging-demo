package cn.mijack.paging.api;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import cn.mijack.paging.api.GithubService;
import cn.mijack.paging.api.RepoSearchResponse;
import cn.mijack.paging.model.Repo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Mi&Jack
 */
public class GithubServiceCreator {
    private static final String TAG = "GithubService";
    private static final String IN_QUALIFIER = "in:name,description";


    public static void searchRepos(GithubService service, String query, int page, int itemsPerPage,
                                   final IOnSuccess onSuccess, final IOnError onError) {
        Log.d(TAG, "query: " + query + ", page: " + page + ", itemsPerPage: " + itemsPerPage);

        String apiQuery = query + IN_QUALIFIER;

        service.searchRepos(apiQuery, page, itemsPerPage).enqueue(
                new Callback<RepoSearchResponse>() {

                    @Override
                    public void onResponse(Call<RepoSearchResponse> call,
                                           Response<RepoSearchResponse> response) {
                        Log.d(TAG, "got a response " + response);
                        if (response.isSuccessful()) {
                            List<Repo> repos = Collections.emptyList();
                            if (response.body() != null && response.body().getItems() != null) {
                                repos = response.body().getItems();
                            }
                            onSuccess.onSuccess(repos);
                        } else {
                            String error = "Unknown error";
                            try {
                                if (response.errorBody() != null
                                        && response.errorBody().string() != null) {
                                    error = response.errorBody().string();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            onError.onError(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<RepoSearchResponse> call, Throwable t) {
                        Log.d(TAG, "fail to get data");
                        String errorInfo = TextUtils.isEmpty(t.getMessage()) ?
                                "unknown error" : t.getMessage();
                        onError.onError(errorInfo);


                    }
                });
    }

    static String BASE_URL = "https://api.github.com/";

    public static GithubService create() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService.class);
    }

    public interface IOnSuccess {
        void onSuccess(List<Repo> repos);
    }

    public interface IOnError {
        void onError(String error);
    }
}

package cn.mijack.paging.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import cn.mijack.paging.IUnit;
import cn.mijack.paging.api.GithubServiceCreator;
import cn.mijack.paging.api.GithubService;
import cn.mijack.paging.db.GithubLocalCache;
import cn.mijack.paging.model.Repo;
import cn.mijack.paging.model.RepoSearchResult;

/**
 * @author Mi&Jack
 */

public class GithubRepository {
    private GithubService service;
    private GithubLocalCache cache;

    public GithubRepository(GithubService service, GithubLocalCache cache) {
        this.service = service;
        this.cache = cache;
    }

    // keep the last requested page. When the request is successful, increment the page number.
    private int lastRequestedPage = 1;

    // LiveData of network errors.
    private MutableLiveData<String> networkErrors = new MutableLiveData<String>();

    // avoid triggering multiple requests in the same time
    private boolean isRequestInProgress = false;

    /**
     * Search repositories whose names match the query.
     */
    public RepoSearchResult search(String query) {
        Log.d("GithubRepository", "New query: " + query);
        lastRequestedPage = 1;
        requestAndSaveData(query);

        // Get data from the local cache
        LiveData<List<Repo>> data = cache.reposByName(query);
        return new RepoSearchResult(data, networkErrors);
    }

   public void requestMore(String query) {
        requestAndSaveData(query);
    }

    private void requestAndSaveData(String query) {
        if (isRequestInProgress) {
            return;
        }

        isRequestInProgress = true;
        GithubServiceCreator.searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE,
                new GithubServiceCreator.IOnSuccess() {
                    @Override
                    public void onSuccess(List<Repo> repos) {
                        cache.insert(repos, new IUnit() {
                            @Override
                            public void insertFinished() {
                                lastRequestedPage++;
                                isRequestInProgress = false;
                            }
                        });
                    }
                },
                new GithubServiceCreator.IOnError() {
                    @Override
                    public void onError(String error) {
                        networkErrors.postValue(error);
                        isRequestInProgress = false;
                    }
                });
    }

    private static final int NETWORK_PAGE_SIZE = 50;
}
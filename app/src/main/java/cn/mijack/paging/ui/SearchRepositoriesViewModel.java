package cn.mijack.paging.ui;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import cn.mijack.paging.data.GithubRepository;
import cn.mijack.paging.model.Repo;
import cn.mijack.paging.model.RepoSearchResult;

/**
 * @author Mi&Jack
 */
public class SearchRepositoriesViewModel extends ViewModel {
    private GithubRepository repository;
    private final static int VISIBLE_THRESHOLD = 5;

    public SearchRepositoriesViewModel(GithubRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<String> queryLiveData = new MutableLiveData<String>();
    private LiveData<RepoSearchResult> repoResult =
            Transformations.map(queryLiveData, new Function<String, RepoSearchResult>() {
                @Override
                public RepoSearchResult apply(String input) {
                    return repository.search(input);
                }
            });
    private LiveData<List<Repo>> repos =
            Transformations.switchMap(repoResult, new Function<RepoSearchResult, LiveData<List<Repo>>>() {
                @Override
                public LiveData<List<Repo>> apply(RepoSearchResult input) {
                    return input.getData();
                }
            });
    private LiveData<String> networkErrors =
            Transformations.switchMap(repoResult, new Function<RepoSearchResult, LiveData<String>>() {
                @Override
                public LiveData<String> apply(RepoSearchResult input) {
                    return input.getNetworkErrors();
                }
            });

    /**
     * Search a repository based on a query string.
     */
    public void searchRepo(String queryString) {
        queryLiveData.postValue(queryString);
    }

    public void listScrolled(int visibleItemCount, int lastVisibleItemPosition, int totalItemCount) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            String immutableQuery = lastQueryValue();
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery);

            }
        }
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    public LiveData<List<Repo>> getRepos() {
        return repos;
    }

    /**
     * Get the last query value.
     */
    public String lastQueryValue() {
        return queryLiveData.getValue();
    }
}
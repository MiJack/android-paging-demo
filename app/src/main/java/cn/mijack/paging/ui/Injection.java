package cn.mijack.paging.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import java.util.concurrent.Executors;

import cn.mijack.paging.api.GithubServiceCreator;
import cn.mijack.paging.data.GithubRepository;
import cn.mijack.paging.db.GithubLocalCache;
import cn.mijack.paging.db.RepoDatabase;

/**
 * @author Mi&Jack
 */


public class Injection {

    /**
     * Creates an instance of [GithubLocalCache] based on the database DAO.
     */
    public static GithubLocalCache provideCache(Context context) {
        RepoDatabase database = RepoDatabase.getInstance(context);
        return new GithubLocalCache(database.reposDao(), Executors.newSingleThreadExecutor());
    }

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    public static GithubRepository provideGithubRepository(Context context) {
        return new GithubRepository(GithubServiceCreator.create(), provideCache(context));
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    public static ViewModelProvider.Factory provideViewModelFactory(Context context) {
        return new ViewModelFactory(provideGithubRepository(context));
    }

}
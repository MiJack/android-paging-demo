package cn.mijack.paging.db;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import cn.mijack.paging.IUnit;
import cn.mijack.paging.model.Repo;
import cn.mijack.paging.utils.CollectionUtils;

/**
 * @author Mi&Jack
 */
public class GithubLocalCache {
    private RepoDao repoDao;
    private Executor ioExecutor;

    public GithubLocalCache(RepoDao repoDao, Executor ioExecutor) {
        this.repoDao = repoDao;
        this.ioExecutor = ioExecutor;
    }

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    public void insert(final List<Repo> repos, final IUnit insertFinished) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("GithubLocalCache", "inserting "+ CollectionUtils.size(repos)+"repos");
                repoDao.insert(repos);
                insertFinished.insertFinished();
            }
        });
    }

    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     *
     * @param name repository name
     */
    public LiveData<List<Repo>> reposByName(String name) {
        // appending '%' so we can allow other characters to be before and after the query string
        String query = name.replace(' ', '%');
        return repoDao.reposByName(query);
    }


}

package cn.mijack.paging.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import cn.mijack.paging.data.GithubRepository;

/**
 * @author Mi&Jack
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private GithubRepository repository;

    public ViewModelFactory(GithubRepository repository) {
        this.repository = repository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel.class)) {
            return (T) new SearchRepositoriesViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

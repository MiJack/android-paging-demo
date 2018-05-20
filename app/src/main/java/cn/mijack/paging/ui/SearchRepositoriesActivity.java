package cn.mijack.paging.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.mijack.paging.R;
import cn.mijack.paging.model.Repo;


public class SearchRepositoriesActivity extends AppCompatActivity {
    public static final String LAST_SEARCH_QUERY = "last_search_query";
    public static final String DEFAULT_QUERY = "Android";
    private SearchRepositoriesViewModel viewModel;
    private ReposAdapter adapter = new ReposAdapter();
    private RecyclerView list;
    private TextView emptyList;
    private TextView search_repo;
    private String query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        emptyList = findViewById(R.id.emptyList);
        search_repo = findViewById(R.id.search_repo);
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(SearchRepositoriesViewModel.class);

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list.addItemDecoration(decoration);
        setupScrollListener();
        initAdapter();
        String query = getQuery(savedInstanceState);
        viewModel.searchRepo(query);
        initSearch(query);
    }

    public String getQuery(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String query = savedInstanceState.getString(LAST_SEARCH_QUERY);
            if (!TextUtils.isEmpty(query)) {
                return query;
            }
        }
        return DEFAULT_QUERY;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue());
    }

    //
    private void initAdapter() {
        list.setAdapter(adapter);
        viewModel.getRepos().observe(this, new Observer<List<Repo>>() {
            @Override
            public void onChanged(@Nullable List<Repo> repos) {
                Log.d("Activity", "list: " + (repos == null ? 0 : repos.size()));
                showEmptyList(repos == null || repos.isEmpty());
                adapter.submitList(repos);
            }
        });
        viewModel.getNetworkErrors().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(SearchRepositoriesActivity.this,
                        "\uD83D\uDE28 Wooops " + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    private void initSearch(String query) {
        search_repo.setText(query);

        search_repo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateRepoListFromInput();
                    return true;
                } else {
                    return false;
                }
            }
        });
        search_repo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateRepoListFromInput();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    //
    private void updateRepoListFromInput() {
        if (!TextUtils.isEmpty(search_repo.getText())) {
            list.scrollToPosition(0);
            viewModel.searchRepo(search_repo.getText().toString());
            adapter.submitList(null);
        }

    }

    private void showEmptyList(Boolean show) {
        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    private void setupScrollListener() {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) list.getLayoutManager();

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount);

            }
        });
    }

}
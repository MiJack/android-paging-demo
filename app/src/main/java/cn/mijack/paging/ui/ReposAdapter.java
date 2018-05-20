package cn.mijack.paging.ui;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import cn.mijack.paging.model.Repo;

/**
 * @author Mi&Jack
 */
public class ReposAdapter extends ListAdapter<Repo, RecyclerView.ViewHolder> {
    protected ReposAdapter() {
        super(new DiffUtil.ItemCallback<Repo>() {
            @Override
            public boolean areItemsTheSame(Repo oldItem, Repo newItem) {
                return oldItem.getFullName() == newItem.getFullName();
            }

            @Override
            public boolean areContentsTheSame(Repo oldItem, Repo newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RepoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Repo repoItem = getItem(position);
        if (repoItem != null) {
            ((RepoViewHolder) holder).bind(repoItem);
        }
    }

}

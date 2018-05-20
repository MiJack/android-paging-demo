package cn.mijack.paging.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.mijack.paging.model.Repo;

/**
 * @author Mi&Jack
 */
public class RepoSearchResponse {
    @SerializedName("items")
    List<Repo> items = new ArrayList<>();
    int nextPage;
    @SerializedName("total_count")
    int total;

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

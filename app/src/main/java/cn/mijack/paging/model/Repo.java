package cn.mijack.paging.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author Mi&Jack
 */
@Entity(tableName = "repos")
public class Repo {
    @PrimaryKey
    @SerializedName("id")
    Long id;
    @SerializedName("name")
    String name;
    @SerializedName("full_name")
    String fullName;//: String,
    @SerializedName("description")
    String description;//: String?,
    @SerializedName("html_url")
    String url;//: String,
    @SerializedName("stargazers_count")
    int stars;//: Int,
    @SerializedName("forks_count")
    int forks;//: Int,
    @SerializedName("language")
    String language;//: String?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repo repo = (Repo) o;

        if (stars != repo.stars) return false;
        if (forks != repo.forks) return false;
        if (id != null ? !id.equals(repo.id) : repo.id != null) return false;
        if (name != null ? !name.equals(repo.name) : repo.name != null) return false;
        if (fullName != null ? !fullName.equals(repo.fullName) : repo.fullName != null)
            return false;
        if (description != null ? !description.equals(repo.description) : repo.description != null)
            return false;
        if (url != null ? !url.equals(repo.url) : repo.url != null) return false;
        return language != null ? language.equals(repo.language) : repo.language == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + stars;
        result = 31 * result + forks;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}

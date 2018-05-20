package cn.mijack.paging.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import cn.mijack.paging.model.Repo;

/**
 * @author Mi&Jack
 */
@Database(
        entities = {Repo.class},
        version = 1,
        exportSchema = false
)
public abstract class RepoDatabase extends RoomDatabase {

    public abstract RepoDao reposDao();

    private static volatile RepoDatabase INSTANCE = null;

    public static RepoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RepoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static RepoDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                RepoDatabase.class, "Github.db").build();
    }
}

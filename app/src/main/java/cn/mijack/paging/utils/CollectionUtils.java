package cn.mijack.paging.utils;

import java.util.Collection;

/**
 * @author Mi&Jack
 */
public class CollectionUtils {
    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }
}

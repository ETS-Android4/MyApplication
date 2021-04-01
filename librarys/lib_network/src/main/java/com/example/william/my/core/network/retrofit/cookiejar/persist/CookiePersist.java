package com.example.william.my.core.network.retrofit.cookiejar.persist;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * A CookiePersist handles the persistent cookie storage.
 */
public interface CookiePersist {

    List<Cookie> loadAll();

    /**
     * Persist all cookies, existing cookies will be overwritten.
     *
     * @param cookies cookies persist
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * Removes indicated cookies from persistence.
     *
     * @param cookies cookies to remove from persistence
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * Clear all cookies from persistence.
     */
    void clear();

}

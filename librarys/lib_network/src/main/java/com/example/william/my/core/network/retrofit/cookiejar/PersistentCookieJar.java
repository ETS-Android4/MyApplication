package com.example.william.my.core.network.retrofit.cookiejar;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.cookiejar.cache.CookieCache;
import com.example.william.my.core.network.retrofit.cookiejar.persist.CookiePersist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements ClearableCookieJar {

    private final CookieCache cache;
    private final CookiePersist persist;

    public PersistentCookieJar(CookieCache cache, CookiePersist persist) {
        this.cache = cache;
        this.persist = persist;

        this.cache.addAll(persist.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
        persist.saveAll(filterPersistentCookies(cookies));
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        List<Cookie> persistentCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                persistentCookies.add(cookie);
            }
        }
        return persistentCookies;
    }

    @NonNull
    @Override
    synchronized public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookiesToRemove = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie);
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        persist.removeAll(cookiesToRemove);

        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    synchronized public void clearSession() {
        cache.clear();
        cache.addAll(persist.loadAll());
    }

    @Override
    synchronized public void clear() {
        cache.clear();
        persist.clear();
    }
}

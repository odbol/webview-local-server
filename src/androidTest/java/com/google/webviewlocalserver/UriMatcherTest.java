package com.google.webviewlocalserver;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.google.webviewlocalserver.third_party.android.UriMatcher;

/*
 * Test cases for UriMatcher.
 */
public class UriMatcherTest extends InstrumentationTestCase {
    private final Object nullObject = new Object();

    public void testFullHttpUrlMatching() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("http", "org.chromium", "/some/path", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/some/path")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/some/path?asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/some/path#asd")));

        assertNull(matcher.match(Uri.parse("https://org.chromium/some/path")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/somepath")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/some/other")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/some/path/other")));

        assertEquals(nullObject, matcher.match(Uri.parse("http:")));
        assertEquals(nullObject, matcher.match(Uri.parse("https:")));
    }

    public void testFullHttpsUrlMatching() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("https", "org.chromium", "/some/path", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("https://org.chromium/some/path")));
        assertEquals(pathMatch, matcher.match(Uri.parse("https://org.chromium/some/path?asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("https://org.chromium/some/path#asd")));

        assertNull(matcher.match(Uri.parse("http://org.chromium/some/path")));
        assertNull(matcher.match(Uri.parse("https://org.chromium/somepath")));
        assertNull(matcher.match(Uri.parse("https://org.chromium/")));
        assertNull(matcher.match(Uri.parse("https://org.chromium/some/other")));
        assertNull(matcher.match(Uri.parse("https://org.chromium/some/path/other")));

        assertEquals(nullObject, matcher.match(Uri.parse("http:")));
        assertEquals(nullObject, matcher.match(Uri.parse("https:")));
    }

    public void testHttpUrlSingleStarMatching() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("http", "org.chromium", "/path/*", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a?asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a#asd")));

        assertNull(matcher.match(Uri.parse("https://org.chromium/pa")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path?asd")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path#asd")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/c")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/other/path")));
    }

    public void testHttpUrlDoubleStarMatching() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("http", "org.chromium", "/path/**", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/c")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a?asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a#asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b?asd")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b#asd")));

        assertNull(matcher.match(Uri.parse("https://org.chromium/pa")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path?asd")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path#asd")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/other/path")));
    }

    public void testRemoveUriSingle() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("http", "org.chromium", "/path/a/**", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/c")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/e")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d/e")));

        matcher.removeURI("http", "org.chromium", "/path/a/**");
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/c")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/e")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d/e")));
    }

    public void testRemoveUri() {
        Object pathMatch = new Object();
        UriMatcher matcher = new UriMatcher(nullObject);
        matcher.addURI("http", "org.chromium", "/path/a/c/**", pathMatch);
        matcher.addURI("http", "org.chromium", "/path/a/b/c/**", pathMatch);
        matcher.addURI("http", "org.chromium", "/path/a/b/d/**", pathMatch);
        matcher.addURI("http", "org.chromium", "/path/a/b/k/**", pathMatch);

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/c/e")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/d/t")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/k/d")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/k/d/e")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d/e")));

        matcher.removeURI("http", "org.chromium", "/path/a/b/k/**");

        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/k/d/e")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/k/d")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/k/d")));

        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/c/e")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/b/d/t")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d")));
        assertEquals(pathMatch, matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d/e")));

        matcher.removeURI("http", "org.chromium", "/path/**");

        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/c/e")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/b/d/t")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d")));
        assertNull(matcher.match(Uri.parse("http://org.chromium/path/a/c/c/d/e")));
    }
}



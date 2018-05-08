package com.github.nhttp.router;


import org.junit.Assert;
import org.junit.Test;

public class RouterTest {

    @Test
    public void router() {
        Router router = Router.router("abc");
        Assert.assertTrue(router.getRouterPath().startsWith("/"));

        router = Router.router("abc/");
        Assert.assertFalse(router.getRouterPath().endsWith("/"));

        router = Router.router("/abc/");
        Assert.assertFalse(router.getRouterPath().endsWith("/"));
        Assert.assertTrue(router.getRouterPath().startsWith("/"));


        router = Router.router("/");
        Assert.assertTrue(router.getRouterPath().equals("/"));

    }
}
package com.library.bootstrap.web;

import io.javalin.http.Context;

public class LandingController {

    public void show(Context ctx) {
        ctx.render("landing");
    }
}

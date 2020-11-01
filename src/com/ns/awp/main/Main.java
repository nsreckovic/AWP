package com.ns.awp.main;

import com.ns.awp.engine.DIEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        Root root = new Root();
        DIEngine.getInstance(root);
        root.test();
    }

}

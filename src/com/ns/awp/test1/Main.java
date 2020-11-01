package com.ns.awp.test1;

import com.ns.awp.DIEngine;
import com.ns.awp.Root;

public class Main {

    public static void main(String[] args) throws Exception {
        Root root = new Root();
        DIEngine.getInstance(root);
        root.test();
    }

}

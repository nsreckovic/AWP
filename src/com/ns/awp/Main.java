package com.ns.awp;

public class Main {

    public static void main(String[] args) throws Exception {
        Root root = new Root();
        DIEngine.getInstance(Main.class.getPackageName(), root);
        root.test();
    }

}

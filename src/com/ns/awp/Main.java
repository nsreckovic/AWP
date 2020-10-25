package com.ns.awp;

public class Main {

    public static void main(String[] args) throws Exception {
        Root root = new Root();
        DIEngine engine = createEngine(root);
        root.test();
    }

    private static DIEngine createEngine(Object root) throws Exception {
        String rootPackageName = Main.class.getPackageName();
        return DIEngine.createEngineForPackage(rootPackageName, root);
    }

    private static void demonstrate() {

    }
}

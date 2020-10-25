package com.ns.awp;

import com.ns.awp.annotations.Autowired;

public class Root {
    @Autowired(verbose = false)
    Service1 nekiMojServis;
    @Autowired(verbose = false)
    Service1 nekiMojServis2;
    @Autowired(verbose = false)
    Service2 nekiMojServis3;

    public void test() {
        nekiMojServis.ispis();
        nekiMojServis2.ispis();
        nekiMojServis3.ispis();
    }
}

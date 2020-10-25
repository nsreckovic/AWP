package com.ns.awp;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Qualifier;

public class Root {
//    @Autowired(verbose = true)
//    Service1 nekiMojServis;
//    @Autowired(verbose = true)
//    Service1 nekiMojServis2;
//    @Autowired(verbose = true)
//    Service2 nekiMojServis3;
//
//    @Autowired(verbose = true)
//    Component1 komponenta1;
//    @Autowired(verbose = true)
//    Component1 komponenta2;
//    @Autowired(verbose = true)
//    Component2 komponenta3;
//    @Autowired(verbose = true)
//    Component3 komponenta4;
//    @Autowired(verbose = true)
//    Component3 komponenta5;

//    @Autowired(verbose = false)
//    NoBeanClass noBeanClass;

    @Autowired(verbose = true)
    @Qualifier(key = "test")
    InterfaceInjection injection;


    public void test() {
//        nekiMojServis.ispis();
//        nekiMojServis2.ispis();
//        nekiMojServis3.ispis();
//
//        System.out.println("\n\n");
//
//        komponenta1.ispis();
//        komponenta2.ispis();
//        komponenta3.ispis();
//        komponenta4.print();
//        komponenta5.print();

        injection.print();

    }
}

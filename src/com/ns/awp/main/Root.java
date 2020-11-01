package com.ns.awp.main;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Qualifier;
import com.ns.awp.components.BeanPrototype;
import com.ns.awp.components.Component1;
import com.ns.awp.components.Component2;
import com.ns.awp.components.Component3;
import com.ns.awp.services.BeanSingleton;
import com.ns.awp.services.interfaces.InterfaceInjection;

public class Root {
//    @Autowired(verbose = true)
//    Service1 testService1;
//    @Autowired(verbose = true)
//    Service1 testService2;
//    @Autowired(verbose = true)
//    Service2 testService3;
//    @Autowired(verbose = true)
//    BeanSingleton testService4;

//    @Autowired(verbose = true)
//    Component1 testComponent1;
//    @Autowired(verbose = true)
//    Component1 testComponent2;
//    @Autowired(verbose = true)
//    Component2 testComponent3;
//    @Autowired(verbose = true)
//    Component3 testComponent4;
//    @Autowired(verbose = true)
//    Component3 testComponent5;
//    @Autowired(verbose = true)
//    BeanPrototype testComponent6;

//    @Autowired(verbose = false)
//    NoBeanClass noBeanClass;

    @Autowired(verbose = true)
    @Qualifier(key = "test")
    InterfaceInjection injection;

    public void test() {
        System.out.println("\n-------------------- Root prints: --------------------\n\n");

//        testService1.print();
//        testService2.print();
//        testService3.print();
//        testService4.print();

//        testComponent1.print();
//        testComponent2.print();
//        testComponent3.print();
//        testComponent4.print();
//        testComponent5.print();
//        testComponent6.print();

//        noBeanClass.toString();

        injection.print();

    }
}

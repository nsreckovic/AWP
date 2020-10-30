package com.ns.awp;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Qualifier;
import com.ns.awp.annotations.Service;

@Qualifier(key = "test")
@Service
public class InterfaceInjectionClass implements InterfaceInjection {
    @Autowired(verbose = true)
    Service1 service1;

    @Override
    public void print() {
        service1.ispis();
        System.out.println("Print from class that implements InterfaceInjection interface");
    }
}

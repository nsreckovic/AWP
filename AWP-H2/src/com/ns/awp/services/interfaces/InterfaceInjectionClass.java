package com.ns.awp.services.interfaces;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Qualifier;
import com.ns.awp.annotations.Service;
import com.ns.awp.services.Service1;

@Qualifier(key = "test")
@Service
public class InterfaceInjectionClass implements InterfaceInjection {
    @Autowired(verbose = true)
    Service1 service1;

    @Override
    public void print() {
        service1.print();
        System.out.println("Print from InterfaceInjectionClass that implements InterfaceInjection interface - Hash: " + this.hashCode());
    }
}

package com.ns.awp.services.interfaces;

import com.ns.awp.annotations.Qualifier;
import com.ns.awp.annotations.Service;

@Qualifier(key = "test2")
@Service
public class InterfaceInjectionClass2 implements InterfaceInjection {
    @Override
    public void print() {
        System.out.println("Print from InterfaceInjectionClass2 that implements InterfaceInjection interface - Hash: " + this.hashCode());
    }
}

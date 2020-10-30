package com.ns.awp;

import com.ns.awp.annotations.Qualifier;
import com.ns.awp.annotations.Service;

@Qualifier(key = "test2")
@Service
public class InterfaceInjectionClass2 implements InterfaceInjection {
    @Override
    public void print() {
        System.out.println("Print from class that implements InterfaceInjection interface 3");
    }
}

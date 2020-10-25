package com.ns.awp;

import com.ns.awp.annotations.Qualifier;
import com.ns.awp.annotations.Service;

@Qualifier(key = "test")
@Service
public class InterfaceInjectionClass implements InterfaceInjection {
    @Override
    public void print() {
        System.out.println("Print from class that implements InterfaceInjection interface");
    }
}

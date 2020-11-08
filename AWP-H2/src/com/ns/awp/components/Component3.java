package com.ns.awp.components;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Component;

@Component
public class Component3 {
    @Autowired(verbose = true)
    Component1 component1InsideComponent3;

    public void print() {
        component1InsideComponent3.print();
        System.out.println("Print from Component3 - Hash: " + this.hashCode());
    }
}

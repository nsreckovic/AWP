package com.ns.awp;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Component;

@Component
public class Component3 {
    @Autowired(verbose = true)
    Component1 component1InsideComponent3;

    public void print() {
        component1InsideComponent3.ispis();
        System.out.println("Print from component 3 - " + hashCode());
    }
}

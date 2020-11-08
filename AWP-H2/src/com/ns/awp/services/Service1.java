package com.ns.awp.services;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Service;
import com.ns.awp.components.Component1;

@Service
public class Service1 {
    @Autowired(verbose = true)
    Component1 componentInsideService1;

    public void print() {
        componentInsideService1.print();
        System.out.println("Print form Service1 - Hash: " + this.hashCode());
    }
}

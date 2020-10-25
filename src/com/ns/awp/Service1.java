package com.ns.awp;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Service;

@Service
public class Service1 {
    @Autowired(verbose = true)
    Component1 componentInsideService1;

    public void ispis() {
        componentInsideService1.ispis();
        System.out.println("neki ispis iz servisa 1 - " + this.hashCode());
    }
}

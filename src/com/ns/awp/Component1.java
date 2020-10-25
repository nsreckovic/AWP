package com.ns.awp;

import com.ns.awp.annotations.Component;

@Component
public class Component1 {
    public void ispis() {
        System.out.println("Ispis iz komponente 1 - " + this.hashCode());
    }
}

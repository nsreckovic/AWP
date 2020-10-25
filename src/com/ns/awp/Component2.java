package com.ns.awp;

import com.ns.awp.annotations.Component;

@Component
public class Component2 {
    public void ispis() {
        System.out.println("Ispis iz komponente 2 - " + this.hashCode());
    }
}

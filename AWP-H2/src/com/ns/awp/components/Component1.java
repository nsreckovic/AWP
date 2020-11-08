package com.ns.awp.components;

import com.ns.awp.annotations.Component;

@Component
public class Component1 {
    public void print() {
        System.out.println("Print from Component1 - Hash: " + this.hashCode());
    }
}

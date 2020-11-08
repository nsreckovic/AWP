package com.ns.awp.components;

import com.ns.awp.annotations.Component;

@Component
public class Component2 {
    public void print() {
        System.out.println("Print from Component2 - Hash: " + this.hashCode());
    }
}

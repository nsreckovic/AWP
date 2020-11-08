package com.ns.awp.services;

import com.ns.awp.annotations.Service;

@Service
public class Service2 {
    public void print() {
        System.out.println("Print form Service2 - Hash: " + this.hashCode());
    }
}

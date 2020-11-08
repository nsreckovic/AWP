package com.ns.awp.services;

import com.ns.awp.annotations.Bean;

@Bean
public class BeanSingleton {
    public void print() {
        System.out.println("Print from BeanSingleton - Hash: " + this.hashCode());
    }
}

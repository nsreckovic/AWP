package com.ns.awp.components;

import com.ns.awp.annotations.Bean;
import com.ns.awp.annotations.Scope;

@Bean(scope = Scope.SCOPE_PROTOTYPE)
public class BeanPrototype {
    public void print() {
        System.out.println("Print from BeanPrototype - Hash: " + this.hashCode());
    }
}

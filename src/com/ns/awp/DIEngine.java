package com.ns.awp;

import com.ns.awp.annotations.Autowired;
import com.ns.awp.annotations.Bean;
import com.ns.awp.annotations.Component;
import com.ns.awp.annotations.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class DIEngine {

    private final Set<Object> serviceInstances = new HashSet<>();
    private static Set<Class<?>> allClassesInPackage;
    private static Set<Class<?>> serviceClasses = new HashSet<>();
    private static Set<Class<?>> componentClasses = new HashSet<>();
    private static Object root;

    public static DIEngine createEngineForPackage(String rootPackageName, Object rootObject) throws Exception {
        root = rootObject;
        allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        for (Class<?> aClass : allClassesInPackage) {
            if (aClass.isAnnotationPresent(Service.class)) serviceClasses.add(aClass);
            else if (aClass.isAnnotationPresent(Component.class)) componentClasses.add(aClass);
            else if (aClass.isAnnotationPresent(Bean.class) && !aClass.isInterface()) {
                switch (aClass.getAnnotation(Bean.class).scope()) {
                    case SCOPE_SINGLETON:
                        serviceClasses.add(aClass);
                        break;
                    case SCOPE_PROTOTYPE:
                        componentClasses.add(aClass);
                        break;
                }
            }
        }
        List<Collection<Class<?>>> sortedClasses = new ArrayList<>();
        sortedClasses.add(serviceClasses);
        sortedClasses.add(componentClasses);
        System.out.println(sortedClasses);
        return new DIEngine(serviceClasses);
    }

    private List<Collection<Class<?>>> checkClasses() {

        return null;
    }

    private List<Collection<Field>> checkFields(Class classToCheck) throws Exception {
        System.out.println("Pre: " + serviceInstances);
        for (Field field : classToCheck.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;
            Class<?> fieldType = field.getType();
            if (!serviceClasses.contains(fieldType)) throw new Exception("Nije anotiran sa service");
            if (getServiceInstance(fieldType) == null) {
                Constructor<?> constructor = fieldType.getConstructor();
                constructor.setAccessible(true);
                Object serviceInstance = constructor.newInstance();
                this.serviceInstances.add(serviceInstance);
            }
            field.setAccessible(true);
            field.set(root, getServiceInstance(fieldType));
        }
        System.out.println("Posle: " + serviceInstances);
        return null;
    }


    public DIEngine(Collection<Class<?>> serviceClasses) throws Exception {
        checkFields(Root.class);
//        // create an instance of each service class
//        for (Class<?> serviceClass : serviceClasses) {
//            Constructor<?> constructor = serviceClass.getConstructor();
//            constructor.setAccessible(true);
//            Object serviceInstance = constructor.newInstance();
//            this.serviceInstances.add(serviceInstance);
//        }
//        // wire them together
//        for (Object serviceInstance : this.serviceInstances) {
//            for (Field field : serviceInstance.getClass().getDeclaredFields()) {
//                if (!field.isAnnotationPresent(Autowired.class)) {
//                    // this field is none of our business
//                    continue;
//                }
//                Class<?> fieldType = field.getType();
//                field.setAccessible(true);
//                // find a suitable matching service instance
//                for (Object matchPartner : this.serviceInstances) {
//                    if (fieldType.isInstance(matchPartner)) {
//                        field.set(serviceInstance, matchPartner);
//                    }
//                }
//            }
//        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getServiceInstance(Class<T> serviceClass) {
        for (Object serviceInstance : this.serviceInstances) {
            if (serviceClass.isInstance(serviceInstance)) {
                return (T) serviceInstance;
            }
        }
        return null;
    }
}

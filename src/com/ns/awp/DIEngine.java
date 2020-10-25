package com.ns.awp;

import com.ns.awp.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DIEngine {

    private static DIEngine instance = null;
    private final Set<Object> serviceInstances = new HashSet<>();
    private static final Set<Class<?>> serviceClasses = new HashSet<>();
    private static final Set<Class<?>> componentClasses = new HashSet<>();
    private static Map<String, Class<?>> dependencySupplier = new HashMap<>();

    private DIEngine(String rootPackageName, Object rootObject) throws Exception {
        checkClasses(rootPackageName);
        checkFields(rootObject, rootObject.getClass());
    }

    public static DIEngine getInstance(String rootPackageName, Object rootObject) throws Exception {
        if (instance == null) instance = new DIEngine(rootPackageName, rootObject);
        return instance;
    }

    // Checks all classes and sorts annotated ones
    private void checkClasses(String rootPackageName) throws Exception {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        for (Class<?> aClass : allClassesInPackage) {
            if (aClass.isInterface() || aClass.isEnum()) {
                continue;
            } else {
                if (aClass.isAnnotationPresent(Service.class)) {
                    serviceClasses.add(aClass);
                } else if (aClass.isAnnotationPresent(Component.class)) {
                    componentClasses.add(aClass);
                } else if (aClass.isAnnotationPresent(Bean.class)) {
                    switch (aClass.getAnnotation(Bean.class).scope()) {
                        case SCOPE_SINGLETON:
                            serviceClasses.add(aClass);
                            break;
                        case SCOPE_PROTOTYPE:
                            componentClasses.add(aClass);
                            break;
                    }
                }
                if (aClass.isAnnotationPresent(Qualifier.class)) {
                    if (!aClass.isAnnotationPresent(Service.class) &&
                            !aClass.isAnnotationPresent(Component.class) &&
                            !aClass.isAnnotationPresent(Bean.class))
                        throw new Error("Class with Qualifier annotation must be a Bean.");
                    else dependencySupplier.put(aClass.getAnnotation(Qualifier.class).key(), aClass);
                }
            }

        }
    }

    // Checks all fields in given class and sorts annotated ones
    private void checkFields(Object fieldObject, Class classToCheck) throws Exception {

        for (Field field : classToCheck.getDeclaredFields()) {
            // If not annotated continue to next one
            if (!field.isAnnotationPresent(Autowired.class)) continue;

            Class<?> fieldType = field.getType();

            // If it's among the services
            if (serviceClasses.contains(fieldType)) {
                if (getServiceInstance(fieldType) == null) {
                    Constructor<?> constructor = fieldType.getConstructor();
                    constructor.setAccessible(true);
                    Object serviceInstance = constructor.newInstance();
                    if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, serviceInstance);
                    this.serviceInstances.add(serviceInstance);
                    if (fieldType.getDeclaredFields().length > 0) checkFields(serviceInstance, fieldType);
                }
                field.setAccessible(true);
                field.set(fieldObject, getServiceInstance(fieldType));

            // If it's among the components
            } else if (componentClasses.contains(fieldType)) {
                Constructor<?> constructor = fieldType.getConstructor();
                constructor.setAccessible(true);
                Object componentInstance = constructor.newInstance();
                if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, componentInstance);
                field.setAccessible(true);
                field.set(fieldObject, componentInstance);
                if (fieldType.getDeclaredFields().length > 0) checkFields(componentInstance, fieldType);

            // If it's annotated with Qualifier
            } else if (field.isAnnotationPresent(Qualifier.class)) {
                // Check in dependency supplier if there is class to inject
                Class<?> classToInject = dependencySupplier.get(field.getAnnotation(Qualifier.class).key());
                if (classToInject != null) {
                    // Check if it's among the services
                    if (serviceClasses.contains(classToInject)) {
                        if (getServiceInstance(classToInject) == null) {
                            Constructor<?> constructor = classToInject.getConstructor();
                            constructor.setAccessible(true);
                            Object serviceInstance = constructor.newInstance();
                            if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, serviceInstance);
                            this.serviceInstances.add(serviceInstance);
                            if (classToInject.getDeclaredFields().length > 0) checkFields(serviceInstance, fieldType);
                        }
                        field.setAccessible(true);
                        field.set(fieldObject, getServiceInstance(classToInject));

                    // Check if it's among the components
                    } else if (componentClasses.contains(classToInject)) {
                        Constructor<?> constructor = classToInject.getConstructor();
                        constructor.setAccessible(true);
                        Object componentInstance = constructor.newInstance();
                        if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, componentInstance);
                        field.setAccessible(true);
                        field.set(fieldObject, componentInstance);
                        if (classToInject.getDeclaredFields().length > 0) checkFields(componentInstance, classToInject);
                    } else {
                        throw new ClassNotFoundException("Class for injection must be annotated with Bean annotation.");
                    }

                } else {
                    throw new ClassNotFoundException("No class found for injection");
                }

            // Else it's annotated with Autowired, but there is no implementation class
            } else {
                throw new ClassNotFoundException("No class found for injection");

            }

        }
    }

    private void printFieldDetails(Field field, Object instance) {
        System.out.println(
                "Type: " + field.getType().getName() + "\n" +
                        "Name: " + field.getName() + "\n" +
                        "Parent Class: " + field.getDeclaringClass().getName() + "\n" +
                        "Init time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss:nnnnnnnnn dd/MM/yyyy")) + "\n" +
                        "Hash code: " + instance.hashCode() + "\n"
        );
    }

    public Object getServiceInstance(Class<?> serviceClass) {
        for (Object serviceInstance : this.serviceInstances) {
            if (serviceClass.isInstance(serviceInstance)) {
                return serviceInstance;
            }
        }
        return null;
    }
}

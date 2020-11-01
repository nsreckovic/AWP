package com.ns.awp.engine;

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
    private static final Map<String, Class<?>> dependencySupplier = new HashMap<>();

    private DIEngine(Object rootObject) throws Exception {
        checkClasses();
        checkFields(rootObject, rootObject.getClass());
    }

    public static DIEngine getInstance(Object rootObject) throws Exception {
        if (instance == null) instance = new DIEngine(rootObject);
        return instance;
    }

    // Check all classes and sort annotated ones
    private void checkClasses() throws Exception {
        // Get all classes
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage();

        // Sort classes
        for (Class<?> aClass : allClassesInPackage) {
            boolean beanClass = false;
            if (!aClass.isInterface() && !aClass.isEnum()) {
                if (aClass.isAnnotationPresent(Service.class)) {
                    serviceClasses.add(aClass);
                    beanClass = true;
                } else if (aClass.isAnnotationPresent(Component.class)) {
                    componentClasses.add(aClass);
                    beanClass = true;
                } else if (aClass.isAnnotationPresent(Bean.class)) {
                    switch (aClass.getAnnotation(Bean.class).scope()) {
                        case SCOPE_SINGLETON:
                            serviceClasses.add(aClass);
                            beanClass = true;
                            break;
                        case SCOPE_PROTOTYPE:
                            componentClasses.add(aClass);
                            beanClass = true;
                            break;
                    }
                }
                if (aClass.isAnnotationPresent(Qualifier.class)) {
                    if (beanClass) {
                        if (dependencySupplier.putIfAbsent(aClass.getAnnotation(Qualifier.class).key(), aClass) != null)
                            throw new RuntimeException("There cannot be multiple Bean classes with the same Qualifier key.");
                    } else {
                        throw new RuntimeException("Class with Qualifier annotation must be a Bean.");
                    }
                }
            }
        }
    }

    // Check all fields in given class and sort annotated ones
    private void checkFields(Object fieldObject, Class<?> classToCheck) throws Exception {
        for (Field field : classToCheck.getDeclaredFields()) {

            // If field is not annotated for injection continue to the next one
            if (!field.isAnnotationPresent(Autowired.class)) {
                if (field.isAnnotationPresent(Qualifier.class)) throw new RuntimeException("Field annotated with Qualifier must have Autowired annotation.");
                continue;
            }

            Class<?> fieldType = field.getType();

            // If field class is among the service classes
            if (serviceClasses.contains(fieldType)) {
                serviceInjection(fieldObject, field, fieldType);

            // If field class is among the component classes
            } else if (componentClasses.contains(fieldType)) {
                componentInjection(fieldObject, field, fieldType);

            // If field is annotated with Qualifier
            } else if (field.isAnnotationPresent(Qualifier.class)) {
                // Check in the dependency supplier if there is class to inject that has the same Qualifier key as the one the field does
                Class<?> classToInject = dependencySupplier.get(field.getAnnotation(Qualifier.class).key());

                // If there is, try to inject it
                if (classToInject != null) {
                    // If class for injection is among the service classes
                    if (serviceClasses.contains(classToInject)) {
                        serviceInjection(fieldObject, field, classToInject);

                    // If field class is among the component classes
                    } else if (componentClasses.contains(classToInject)) {
                        componentInjection(fieldObject, field, classToInject);

                    // Else, it's a class that has Qualifier, but no Bean annotation
                    } else {
                        throw new RuntimeException("Class annotated with Qualifier must have Bean annotation.");
                    }

                // Else, throw an exception
                } else {
                    throw new RuntimeException("No class with Qualifier key \"" + field.getAnnotation(Qualifier.class).key() + "\" found for injection.");
                }

            // Else, it's annotated with Autowired, but there is no implementation class
            } else {
                throw new RuntimeException("No class found for the injection.");

            }

        }
    }

    private void printFieldDetails(Field field, Object instance) {
        System.out.println(
                "Type: " + field.getType().getName() + "\n" +
                "Name: " + field.getName() + "\n" +
                "Parent Class: " + field.getDeclaringClass().getName() + "\n" +
                "Init time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss:nnnnnnnn dd/MM/yyyy")) + "\n" +
                "Hash code: " + instance.hashCode() + "\n"
        );
    }

    private void serviceInjection(Object fieldObject, Field field, Class<?> fieldType) throws Exception {
        // If service hasn't been instantiated before
        if (getServiceInstance(fieldType) == null) {

            // Make new class instance
            Constructor<?> constructor = fieldType.getConstructor();
            constructor.setAccessible(true);
            Object serviceInstance = constructor.newInstance();

            // Check if the service has any fields annotated for injection
            if (fieldType.getDeclaredFields().length > 0) checkFields(serviceInstance, fieldType);

            // Print instantiation details
            if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, serviceInstance);

            // Add service to instantiated services
            this.serviceInstances.add(serviceInstance);
        }

        // Inject instantiated service
        field.setAccessible(true);
        field.set(fieldObject, getServiceInstance(fieldType));
    }

    private void componentInjection(Object fieldObject, Field field, Class<?> fieldType) throws Exception {
        // Make new class instance
        Constructor<?> constructor = fieldType.getConstructor();
        constructor.setAccessible(true);
        Object componentInstance = constructor.newInstance();

        // Check if the component has any fields annotated for injection
        if (fieldType.getDeclaredFields().length > 0) checkFields(componentInstance, fieldType);

        // Print instantiation details
        if (field.getAnnotation(Autowired.class).verbose()) printFieldDetails(field, componentInstance);

        // Inject instantiated component
        field.setAccessible(true);
        field.set(fieldObject, componentInstance);
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

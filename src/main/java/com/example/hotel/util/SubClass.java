package com.example.hotel.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(SubClass.SubClazz.class)

public @interface SubClass {
    Class value();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface SubClazz{
        SubClass[] value();
    }
}



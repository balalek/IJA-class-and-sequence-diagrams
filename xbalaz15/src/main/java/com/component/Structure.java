package com.component;

import com.uml.UMLClass;

public final class Structure {
    private final ClassComponent box;
    private final UMLClass cls;

    public ClassComponent getBox() {
        return box;
    }

    public UMLClass getCls() {
        return cls;
    }

    public Structure(ClassComponent box, UMLClass cls) {
        this.box = box;
        this.cls = cls;
    }

}


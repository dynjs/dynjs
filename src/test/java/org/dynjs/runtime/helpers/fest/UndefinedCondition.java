package org.dynjs.runtime.helpers.fest;

import org.dynjs.runtime.Attribute;
import org.dynjs.runtime.DynAtom;
import org.fest.assertions.Condition;

public class UndefinedCondition extends Condition<Attribute<DynAtom>> {

    @Override
    public boolean matches(Attribute<DynAtom> attribute) {
        return attribute.isUndefined();
    }

    public static UndefinedCondition undefined() {
        return new UndefinedCondition();
    }
}

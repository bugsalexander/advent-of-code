/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;
import java.util.Objects;

public class TypeValuePair {
    private SeedRequirementType type;
    private BigInteger value;

    public TypeValuePair(SeedRequirementType type, BigInteger value) {
        this.type = type;
        this.value = value;
    }

    public SeedRequirementType getType() {
        return type;
    }

    public TypeValuePair setType(SeedRequirementType type) {
        this.type = type;
        return this;
    }

    public BigInteger getValue() {
        return value;
    }

    public TypeValuePair setValue(BigInteger value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeValuePair that = (TypeValuePair) o;
        return getType() == that.getType() && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getValue());
    }
}

package org.example;

@FunctionalInterface
public interface OffsetFunction {
    int getActualOffset(int offset, char ch);
}

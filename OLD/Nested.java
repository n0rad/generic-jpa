package net.awired.ajsl.persistence.entity;

public interface Nested<ENTITY> {

    String KEY = "parent";

    ENTITY getParent();

    void setParent(ENTITY parent);
}

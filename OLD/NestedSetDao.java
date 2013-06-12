package net.awired.ajsl.persistence.dao;

import java.io.Serializable;
import net.awired.ajsl.persistence.entity.IdEntity;

public interface NestedSetDao<ENTITY extends IdEntity<KEY>, KEY extends Serializable> {

    ENTITY findByLeftRight(KEY idThread, Long left, Long right);

    ENTITY findByLeftRight(KEY idThread, Long left, Long right, KEY idToReturn);

    ENTITY findByLeft(KEY idThread, Long left);

    ENTITY findByThread(KEY thread);

    ENTITY findByThread(ENTITY thread);

    ENTITY findByIdWithSub(KEY idNode);

    ENTITY findByIdWithTree(KEY idNode);

}

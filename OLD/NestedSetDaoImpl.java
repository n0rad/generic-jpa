package net.awired.ajsl.persistence.dao.impl;

import java.io.Serializable;
import net.awired.ajsl.persistence.dao.NestedSetDao;
import net.awired.ajsl.persistence.entity.IdEntity;

public abstract class NestedSetDaoImpl<ENTITY extends IdEntity<KEY>, KEY extends Serializable> extends
        AdvancedDaoImpl<ENTITY, KEY> implements NestedSetDao<ENTITY, KEY> {
    //
    //    //    public enum Requests {
    //    //        BY_THREAD()
    //    //
    //    //        ;
    //    //    }
    //    //
    //    //    /** Used to know if the DAO can use prepare request or not. */
    //    //    private boolean prepareRequests = false;
    //
    //    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    //    //    @Override
    //    //    public void attachDirty(ENTITY instance) {
    //    //        // TODO: it
    //    //        throw new RuntimeException("todo");
    //    //    }
    //    //
    //    //    @Override
    //    //    public void attachClean(ENTITY instance) {
    //    //        // TODO: it
    //    //        throw new RuntimeException("todo");
    //    //    }
    //
    //    @Override
    //    public void delete(ENTITY persistentInstance, LockMode lMode) {
    //
    //        ENTITY root = persistentInstance.getNsThread();
    //
    //        // save node
    //        ENTITY toDelete = persistentInstance;
    //        // tell parent to remove it
    //        persistentInstance.getParent().removeChild(persistentInstance);
    //
    //        // delete node
    //        super.delete(toDelete, lMode);
    //
    //        this.rebuildLeftRight(root);
    //        // TODO: change this?
    //        this.getHibernateTemplate().flush();
    //        this.getHibernateTemplate().persist(root);
    //    }
    //
    //    @Override
    //    public ENTITY merge(ENTITY detachedInstance) {
    //        // TODO: it
    //        throw new RuntimeException("todo");
    //    }
    //
    //    //    @Override
    //    //    public List<ENTITY> findByExample(ENTITY instance) {
    //    //        // TODO: it
    //    //        throw new RuntimeException("todo");
    //    //    }
    //
    //    /**
    //     * 
    //     */
    //    @Override
    //    public void delete(ENTITY persistentInstance) {
    //
    //        ENTITY root = persistentInstance.getNsThread();
    //
    //        // tell parent to remove it
    //        if (persistentInstance.getParent() != null) {
    //            persistentInstance.getParent().removeChild(persistentInstance);
    //        }
    //
    //        if (persistentInstance == root) {
    //            // trying to remove thread
    //
    //            // remove thread
    //            this.getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
    //            persistentInstance.setNsThread(null);
    //            this.getSessionFactory().getCurrentSession().flush();
    //
    //            // now delete
    //            super.delete(persistentInstance);
    //        } else {
    //            // delete node
    //            super.delete(persistentInstance);
    //
    //            this.rebuildLeftRight(root);
    //            this.save(root);
    //        }
    //    }
    //
    //    /**
    //     * 
    //     */
    //    @Override
    //    public void persist(ENTITY transientInstance) {
    //        // rebuilding tree before persiste
    //        this.rebuildLeftRight(transientInstance);
    //        super.persist(transientInstance);
    //
    //    }
    //
    //    /**
    //     * 
    //     */
    //    @Override
    //    public void save(ENTITY transientInstance) {
    //        // rebuilding tree before save
    //        this.rebuildLeftRight(transientInstance);
    //        super.save(transientInstance);
    //    }
    //
    //    /**
    //     * 
    //     */
    //    @Override
    //    public List<ENTITY> findAll() {
    //        // get all db result
    //        List<ENTITY> all = super.findAll();
    //        // rebuild trees
    //        List<ENTITY> res = this.rebuildTreeChildrenFromParent(all);
    //        return res;
    //    }
    //
    //    /**
    //     * 
    //     */
    //    @Override
    //    public void update(ENTITY transientInstance) {
    //        this.rebuildLeftRight(transientInstance);
    //        super.update(transientInstance);
    //    }
    //
    //    //////////////////////////////////////////////////////////////////////////////////////////
    //
    //    /**
    //     * get the node but load all the tree
    //     */
    //    @Override
    //    public ENTITY findByIdWithTree(KEY_TYPE idNode) {
    //        // get thread
    //        ENTITY thread = this.getThreadFromNodeId(idNode);
    //        // get all node
    //        ENTITY tree = this.findByLeftRight(thread.getId(), thread.getNsLeft(), thread.getNsRight(), idNode);
    //        return tree;
    //    }
    //
    //    /**
    //     * find a tree part by node id.
    //     * 
    //     * @return a tree part
    //     */
    //    @Override
    //    public ENTITY findByIdWithSub(KEY_TYPE idNode) {
    //        return this.findByThread(idNode);
    //    }
    //
    //    /**
    //     * find a tree part by node id.
    //     * 
    //     * @param thread
    //     * @return a tree part
    //     */
    //    @Override
    //    public ENTITY findByThread(KEY_TYPE thread) {
    //        // you want all the thread tree so thread == id
    //        // and we can find by id to get the root node
    //        ENTITY e = super.findById(thread);
    //        this.logger.debug("find by thread fetch parent node -> " + e.getId());
    //        return this.findByLeftRight(e.getNsThread().getId(), e.getNsLeft(), e.getNsRight());
    //    }
    //
    //    @Override
    //    public ENTITY findByThread(ENTITY thread) {
    //        @SuppressWarnings("unchecked")
    //        List<ENTITY> l = this.getHibernateTemplate().find(
    //                "select r from " + ReflectTool.getClassDeclaredInSuperClassGeneric(this, 0).getName() + " r where r.nsThread = ?",
    //                thread);
    //        return rebuildChildrenFromParent(l);
    //    }
    //
    //    /**
    //     * 
    //     * @param idThread
    //     * @param left
    //     * @return
    //     */
    //    @Override
    //    public ENTITY findByLeft(KEY_TYPE idThread, Long left) {
    //        Object[] val = { idThread, idThread, left };
    //        try {
    //            @SuppressWarnings("unchecked")
    //            List<ENTITY> l = this.getHibernateTemplate().find(
    //                    "select r from " + ReflectTool.getClassDeclaredInSuperClassGeneric(this, 0).getName()
    //                            + " r where (r.nsThread.id = ? or r.id = ?) and r.nsLeft = ?", val);
    //
    //            // get single result (root node)
    //            ENTITY e = l.get(0);
    //            // get all tree
    //            return this.findByLeftRight(idThread, left, e.getNsRight());
    //        } catch (NullPointerException e) {
    //            // no root like this
    //            throw new RuntimeException("not found in db");
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * 
    //     * @param idThread
    //     * @param left
    //     * @param right
    //     * @return
    //     */
    //    @Override
    //    public ENTITY findByLeftRight(KEY_TYPE idThread, Long left, Long right) {
    //        return this.findByLeftRight(idThread, left, right, null);
    //    }
    //
    //    /**
    //     * 
    //     * @param idThread
    //     * @param left
    //     * @param right
    //     * @return
    //     */
    //    @Override
    //    @SuppressWarnings("unchecked")
    //    public ENTITY findByLeftRight(KEY_TYPE idThread, Long left, Long right, KEY_TYPE idToReturn) {
    //
    //        Object[] val = { idThread, idThread, left, right };
    //
    //        try {
    //            List<ENTITY> l = this.getHibernateTemplate().find(
    //                    "select r from " + ReflectTool.getClassDeclaredInSuperClassGeneric(this, 0).getName()
    //                            + " r where (r.nsThread.id = ? or r.id = ?) and r.nsLeft >= ? and r.nsRight <= ?", val);
    //            return this.rebuildChildrenFromParent(l, left, idToReturn);
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    //
    //    // ////////////////////////////////////////////////////////////
    //
    //    /**
    //     * get only the thread from a node id.
    //     * 
    //     * @param idNode
    //     * @return
    //     */
    //    private ENTITY getThreadFromNodeId(KEY_TYPE idNode) {
    //        Object[] val = { idNode };
    //        try {
    //            List<ENTITY> l = this.getHibernateTemplate().find(
    //                    "select r.nsThread from " + ReflectTool.getClassDeclaredInSuperClassGeneric(this, 0).getName()
    //                            + " r where r.id = ?", val);
    //
    //            // get single result (root node)
    //            ENTITY e = l.get(0);
    //            // get all tree
    //            return e;
    //        } catch (NullPointerException e) {
    //            // no root like this
    //            throw new RuntimeException("not found in db");
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    //
    //    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //    /**
    //     * 
    //     * @param transientInstance
    //     */
    //    private void updateRec(ENTITY transientInstance) {
    //        List<ENTITY> s = transientInstance.getChildren();
    //        Iterator<ENTITY> i = s.iterator();
    //
    //        System.out.println(s);
    //
    //        // call to update children
    //        while (i.hasNext()) {
    //            ENTITY type = (ENTITY) i.next();
    //            this.updateRec(type);
    //        }
    //
    //        if (transientInstance.getId() == null) {
    //            // new node need save
    //            super.save(transientInstance);
    //        } else {
    //            // update node
    //            System.out.println("____________________________");
    //            super.save(transientInstance);
    //            System.out.println("____________________________");
    //        }
    //    }
    //
    //    /**
    //     * 
    //     * @param nodeList
    //     * @return
    //     */
    //    protected ENTITY rebuildChildrenFromParent(List<ENTITY> nodeList) {
    //        return this.rebuildChildrenFromParent(nodeList, 1L);
    //    }
    //
    //    /**
    //     * 
    //     * @param nodeList
    //     * @param left
    //     * @return
    //     */
    //    protected ENTITY rebuildChildrenFromParent(List<ENTITY> nodeList, Long left) {
    //        return this.rebuildChildrenFromParent(nodeList, left, null);
    //    }
    //
    //    /**
    //     * reaffecting childrens to a tree using parent.
    //     * 
    //     * @param nodeList
    //     * @param left
    //     * @param idToReturn
    //     *            the node to return. null to get the root
    //     * @return first node (tree)
    //     */
    //    protected ENTITY rebuildChildrenFromParent(List<ENTITY> nodeList, Long left, KEY_TYPE idToReturn) {
    //        ENTITY tree = null;
    //        Iterator<ENTITY> i = nodeList.iterator();
    //        while (i.hasNext()) {
    //            ENTITY type = (ENTITY) i.next();
    //
    //            if ((idToReturn == null && type.getNsLeft().equals(left))
    //                    || (idToReturn != null && type.getId().equals(idToReturn))) {
    //                // save node to return
    //                tree = type;
    //            }
    //
    //            Iterator<ENTITY> i2 = nodeList.iterator();
    //            List<ENTITY> children = new ArrayList<ENTITY>();
    //
    //            while (i2.hasNext()) {
    //                ENTITY type2 = (ENTITY) i2.next();
    //                if (type2.getParent() != null && type.getId().equals(type2.getParent().getId())) {
    //                    children.add(type2);
    //                }
    //            }
    //            type.setChildren(children);
    //
    //        }
    //        return tree;
    //    }
    //
    //    /**
    //     * rebuilt all trees from a list.
    //     * 
    //     * @param nodeList
    //     * @param left
    //     * @return
    //     */
    //    protected List<ENTITY> rebuildTreeChildrenFromParent(List<ENTITY> nodeList) {
    //
    //        List<ENTITY> TreeList = new ArrayList<ENTITY>();
    //
    //        Iterator<ENTITY> i = nodeList.iterator();
    //        while (i.hasNext()) {
    //            ENTITY type = (ENTITY) i.next();
    //
    //            if (type.getParent() == null) {
    //                // save first node
    //                TreeList.add(type);
    //            }
    //            Iterator<ENTITY> i2 = nodeList.iterator();
    //            List<ENTITY> children = new ArrayList<ENTITY>();
    //
    //            while (i2.hasNext()) {
    //                ENTITY type2 = (ENTITY) i2.next();
    //                if (type2.getParent() != null && type.getId().equals(type2.getParent().getId())) {
    //                    children.add(type2);
    //                }
    //            }
    //            type.setChildren(children);
    //
    //        }
    //        return TreeList;
    //    }
    //
    //    /**
    //     * 
    //     * @param l
    //     * @param current
    //     * @return
    //     */
    //    private Long rebuildLeftRight(ENTITY node) {
    //        ENTITY rootNode = node;
    //
    //        // go back to first node
    //        while (rootNode.getParent() != null) {
    //            rootNode = rootNode.getParent();
    //        }
    //
    //        // not usefull because we are in root node
    //
    //        // Long left = root.getNsLeft();
    //        // if (left == null) {
    //        // left = 1L;
    //        // }
    //        return this.rebuildLeftRightRec(rootNode, 1L, rootNode);
    //    }
    //
    //    /**
    //     * 
    //     * @param l
    //     * @param current
    //     * @param rootNode
    //     * @return
    //     */
    //    private Long rebuildLeftRightRec(ENTITY node, long current, ENTITY rootNode) {
    //        long newCurrent = current;
    //        List<ENTITY> s = node.getChildren();
    //        Iterator<ENTITY> i = s.iterator();
    //
    //        node.setNsThread(rootNode);
    //        node.setNsLeft(newCurrent++);
    //
    //        while (i.hasNext()) {
    //            ENTITY type = (ENTITY) i.next();
    //            newCurrent = rebuildLeftRightRec(type, newCurrent, rootNode);
    //        }
    //        node.setNsRight(newCurrent++);
    //        return newCurrent;
    //    }

}

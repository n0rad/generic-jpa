package net.awired.ajsl.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public abstract class NestedSetEntityImpl<ENTITY extends NestedSet, KEY_TYPE extends Serializable> implements
        NestedSet<ENTITY, KEY_TYPE> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private KEY_TYPE id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT")
    private ENTITY parent;

    // @Column(name = "NS_THREAD", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
    @JoinColumn(name = "NS_THREAD", nullable = true)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private ENTITY nsThread;

    @Column(name = "NS_LEFT", nullable = false)
    private Long nsLeft = 1L;

    @Column(name = "NS_RIGHT", nullable = false)
    private Long nsRight = 1L;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ENTITY> children = new ArrayList<ENTITY>();

    // //////////////////////////////////////////////////////////

    @Override
    public void addChild(ENTITY child) {
        if (child.getParent() != null) {
            child.getParent().getChildren().remove(child);
        }
        getChildren().add(child);
        child.setParent(this);
    }

    @Override
    public ENTITY removeChild(ENTITY child) {
        getChildren().remove(child);
        child.setParent(null);
        return child;
    }

    @Override
    public int directChildCount() {
        return getChildren().size();
    }

    @Override
    public int totalChildCount() {
        return (int) Math.floor((getNsRight() - getNsLeft()) / 2);
    }

    public String treeSuperclassEntityName() {
        return getClass().getSimpleName();
    }

    // /////////////////////////////////////////////////

    @Override
    public KEY_TYPE getId() {
        return this.id;
    }

    @Override
    public String toString() {
        String res = "Node: (" + getId() + ") ";
        if (getNsThread() != null) {
            res += "THREAD: " + getNsThread().getId();
        }
        res += " " + getNsLeft() + "|" + getNsRight();
        return res;
    }

    // /////////////////////////////////////////////////

    @Override
    public void setId(KEY_TYPE id) {
        this.id = id;
    }

    @Override
    public ENTITY getNsThread() {
        return nsThread;
    }

    @Override
    public void setNsThread(ENTITY nsThread) {
        this.nsThread = nsThread;
    }

    @Override
    public Long getNsLeft() {
        return nsLeft;
    }

    @Override
    public void setNsLeft(Long nsLeft) {
        this.nsLeft = nsLeft;
    }

    @Override
    public Long getNsRight() {
        return nsRight;
    }

    @Override
    public void setNsRight(Long nsRight) {
        this.nsRight = nsRight;
    }

    @Override
    public ENTITY getParent() {
        return this.parent;
    }

    @Override
    public void setParent(ENTITY parent) {
        this.parent = parent;
    }

    @Override
    public List<ENTITY> getChildren() {
        return this.children;
    }

    public void setChildren(List<ENTITY> children) {
        this.children = children;
    }

}

package com.github.zhangyanwei.sct.model.entity;

import com.github.zhangyanwei.sct.common.utils.IDGenerator;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@DynamicUpdate
public abstract class Base implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Long id = IDGenerator.next();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;
        Base base = (Base) o;
        return Objects.equals(getId(), base.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
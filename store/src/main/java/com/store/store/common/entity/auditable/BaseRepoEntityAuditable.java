package com.store.store.common.entity.auditable;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseRepoEntityAuditable<U, PK extends Serializable> extends BaseRepoEntity<PK> 
{
    private static final long serialVersionUID = 141481953116476081L;
    @CreatedBy
    @Nullable
    @Column(updatable = false)
    private U createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    @Nullable
    private U lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    @LastModifiedDate
    private Date lastModifiedDate;

    public BaseRepoEntityAuditable() {
    }
}
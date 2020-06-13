package com.forgqi.resourcebaseserver.entity;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionListener;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@RevisionEntity(AuditingRevisionListener.class)
@Entity(name = "revinfo")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "rev")),
        @AttributeOverride(name = "timestamp", column = @Column(name = "revtstmp"))
})
@Getter
@Setter
public class AuditedRevisionEntity extends DefaultRevisionEntity {

    private String user;

}

class AuditingRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditedRevisionEntity auditedRevisionEntity = (AuditedRevisionEntity) revisionEntity;
        String userName = UserHelper.getUserBySecurityContext().map(User::getUsername).orElse("anonymousUser");
        auditedRevisionEntity.setUser(userName);
    }
}

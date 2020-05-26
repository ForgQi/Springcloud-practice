package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.AbstractAuditingEntity;
import com.forgqi.resourcebaseserver.entity.AuditedRevisionEntity;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import com.forgqi.resourcebaseserver.service.client.YjsxgService;
import com.forgqi.resourcebaseserver.service.client.ZhxgService;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.CountProjection;
import org.hibernate.criterion.Order;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.order.AuditOrder;
import org.hibernate.envers.query.order.internal.PropertyAuditOrder;
import org.hibernate.envers.query.projection.AuditProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.util.CastUtils;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MISController {
    private final JwkService jwkService;
    private final GmsService gmsService;
    private final ZhxgService zhxgService;
    private final YjsxgService yjsxgService;
    private final SysRoleRepository sysRoleRepository;
    private final UserRepository userRepository;
    private final Map<String, RevisionRepository<?, Long, Integer>> revisionRepositoryMap;
    private final EntityManager entityManager;

    @GetMapping(value = "/course")
    List<CourseDTO> getCourse(Integer user) throws IOException {
        User user1 = UserHelper.getUserBySecurityContext().orElseThrow();
        List<CourseDTO> courseDTOList;
        if (user1.getType() == User.Type.STUDENT) {
            courseDTOList = jwkService.getCourse();
        } else {
            courseDTOList = gmsService.getCourse();
        }
        courseDTOList.forEach(courseDTO -> courseDTO.setUser(user));
        return courseDTOList;
    }

    @GetMapping(value = "/cookie")
    public Map<String, List<HttpCookie>> getCookie() {
        HashMap<String, List<HttpCookie>> map = new HashMap<>();
        User user = UserHelper.getUserBySecurityContext().orElseThrow();
        if (user.getType() == User.Type.STUDENT) {
            map.put("jwk", jwkService.getCookie());
            map.put("zhxg", zhxgService.getCookie());
            return map;
        }
        map.put("gms", gmsService.getCookie());
        map.put("yjsxg", yjsxgService.getCookie());
        return map;
    }

    @GetMapping(value = "/roles")
    public Page<SysRole> getRoles(@PageableDefault Pageable pageable) {
        return sysRoleRepository.findAll(pageable);
    }

    @GetMapping(value = "/roles/users")
    public Page<User> getUsersByRole(@PageableDefault Pageable pageable,
                                     @RequestParam(required = false) List<String> sysRoles) {
        if (sysRoles != null) {
            return userRepository.findDistinctByRolesIn(sysRoles.stream().map(sysRoleRepository::findFirstByRole).collect(Collectors.toList()), pageable);
        }
        return userRepository.findDistinctByRolesIn(sysRoleRepository.findAll(), pageable);
    }

    @GetMapping(value = "/audit/{repository}/{id}")
    public List<?> getAuditedLog(@PathVariable String repository, @PathVariable Long id) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.forgqi.resourcebaseserver.entity.forum." + ParseUtil.upperFirstCase(repository));
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> resultList = CastUtils.cast(auditReader.createQuery()
                //  Creates a query, which selects the revisions, at which the given entity was modified.
                .forRevisionsOfEntity(aClass, false, true)
                // false for Entities only, true for selectDeletedEntities
                .add(AuditEntity.id().eq(id)).getResultList());
        return resultList.stream()
                .map(objects -> Map.of("entity", objects[0], "metadata", objects[2], "user", userRepository.findById(Long.valueOf(Optional.ofNullable(((AbstractAuditingEntity)objects[0]).getLastModifiedBy()).orElse("0")))))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/audit/users/{id}")
    public PageImpl<?> getUserAuditedLog(@PathVariable String id, String category, @RequestParam(required = false)String operation, @PageableDefault Pageable pageable) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.forgqi.resourcebaseserver.entity.forum." + ParseUtil.upperFirstCase(category));
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery auditQuery = auditReader.createQuery()
                //  Creates a query, which selects the revisions, at which the given entity was modified.
                .forRevisionsOfEntity(aClass, false, true)
                // false for Entities only, true for selectDeletedEntities
                .add(AuditEntity.revisionProperty("user").eq(id));
        if (operation != null) {
            auditQuery = auditQuery.add(AuditEntity.revisionType().eq(RevisionType.valueOf(operation.toUpperCase())));
        }
        List<Object[]> resultList = CastUtils.cast(auditQuery
                .addOrder(AuditEntity.revisionNumber().desc())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList());
        return new PageImpl<>(resultList.stream()
                .map(objects -> {
                    AuditedRevisionEntity defaultRevisionEntity = CastUtils.cast(objects[1]);
                    return Map.of("entity", objects[0], "metadata", Map.of("RevisionType", objects[2], "RevisionEntity", Map.of("id", defaultRevisionEntity.getId(), "revisionDate", defaultRevisionEntity.getRevisionDate(), "user", defaultRevisionEntity.getUser())));
                })
                .collect(Collectors.toList()), pageable, (long)auditReader.createQuery().forRevisionsOfEntity(aClass, true).add(AuditEntity.property("lastModifiedBy").eq(id)).addProjection(AuditEntity.revisionNumber().count()).getSingleResult());
    }
}

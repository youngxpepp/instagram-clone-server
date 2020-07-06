package com.youngxpepp.instagramcloneserver.domain.follow.model;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.AbstractBaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"from_member_id", "to_member_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follow extends AbstractBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public Follow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}

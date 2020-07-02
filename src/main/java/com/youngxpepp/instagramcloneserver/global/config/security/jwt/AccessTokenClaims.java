package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class AccessTokenClaims {

    private String email;
    private List<MemberRole> roles;

    public List<String> getRolesByString() {
        return this.roles
                .stream()
                    .map(role -> role.getName())
                        .collect(Collectors.toList());
    }

    public Map<String, Object> getClaimsByMap() {
        Map<String, Object> claimsByMap = new HashMap<>();
        claimsByMap.put("email", this.email);
        claimsByMap.put("roles", this.getRolesByString());
        return claimsByMap;
    }
}

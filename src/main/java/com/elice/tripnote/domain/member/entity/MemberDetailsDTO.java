package com.elice.tripnote.domain.member.entity;

import com.elice.tripnote.domain.admin.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class MemberDetailsDTO implements UserDetails {

    private final Member member;
    private final Admin admin;
    private final Status status; // 회원 상태 필드

    public MemberDetailsDTO(Member member) {
        this.member = member;
        this.admin = null;
        this.status = member.getStatus();
    }

    public MemberDetailsDTO(Admin admin) {
        this.member = null;
        this.admin = admin;
        this.status = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                if (member != null) {
                    return "ROLE_MEMBER";
                } else if (admin != null) {
                    return "ROLE_ADMIN";
                }
                throw new IllegalStateException("Member 또는 Admin 객체가 존재하지 않습니다.");
            }
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        if (admin != null) {
            return admin.getPassword();
        } else if (member != null) {
            return member.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (admin != null) {
            return admin.getLoginId();
        } else if (member != null) {
            return member.getEmail();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (member != null) {
            return member.getStatus() == Status.ACTIVE;
        }
        return true;
    }

    public Status getStatus() {
        return status;
    }
}
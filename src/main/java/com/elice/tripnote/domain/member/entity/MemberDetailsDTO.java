package com.elice.tripnote.domain.member.entity;

import com.elice.tripnote.domain.admin.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class MemberDetailsDTO implements UserDetails {

    private final Member member;
    private final Admin admin;

    public MemberDetailsDTO(Member member) {
        this.member = member;
        this.admin = null;
    }

    public MemberDetailsDTO(Admin admin) {
        this.member = null;
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                if (member != null) {
                    return "MEMBER";
                } else if (admin != null) {
                    return "ADMIN";
                }
                throw new IllegalStateException("Member 또는 Admin 객체가 존재하지 않습니다.");
            }
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        if (admin != null) {
//            return admin.getPassword();  //admin getter 추가시 주석해제
            return "12345678"; // 임시 비밀번호
        } else if (member != null) {
            return member.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (admin != null) {
//            return admin.getLogin_id();  //admin getter 추가시 주석해제
            return "admin"; // 임시 아이디
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
}
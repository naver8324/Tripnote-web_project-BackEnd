package com.elice.tripnote.domain.member.entity;

public enum Status {
    ACTIVE,      // 사용 중 (삭제되지 않음)
    DELETED_BY_USER,  // 사용자에 의해 삭제됨
    DELETED_BY_ADMIN  // 관리자에 의해 삭제됨
}
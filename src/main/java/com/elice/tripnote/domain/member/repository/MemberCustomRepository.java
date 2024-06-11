package com.elice.tripnote.domain.member.repository;

import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface MemberCustomRepository {

    Page<MemberResponseDTO> customFindAll(PageRequestDTO pageRequestDTO);
}

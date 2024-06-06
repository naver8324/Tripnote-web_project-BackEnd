package com.elice.tripnote.domain.hashtag.repository;

import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HashtagCustomRepository {

    List<HashtagResponseDTO> findByIsCityAndIsDelete(boolean isCity, boolean isDelete);
    Page<HashtagDTO> customFindAll(Pageable pageable);
}

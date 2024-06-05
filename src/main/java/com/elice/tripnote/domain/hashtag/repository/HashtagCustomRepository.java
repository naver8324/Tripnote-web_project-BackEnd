package com.elice.tripnote.domain.hashtag.repository;

import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;

import java.util.List;

public interface HashtagCustomRepository {

    List<HashtagResponseDTO> findByIsCityAndIsDelete(boolean isCity, boolean isDelete);
    List<HashtagDTO> customFindAll();
}

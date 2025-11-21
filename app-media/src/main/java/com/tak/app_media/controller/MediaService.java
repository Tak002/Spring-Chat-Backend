package com.tak.app_media.controller;

import com.tak.app_media.entity.Media;
import com.tak.app_media.repository.MediaRepository;
import com.tak.common.api.ApiResponseBody;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;

    public String generatePresignedURL(String type, Long userId){
        //media id 생성
        Long lastId = mediaRepository.findTopByOrderByIdDesc().getId();
        String key = type + "_" + (lastId + 1);
        Media media = Media.builder()
                .key(key)
                .purpose(type)
                .owner(AppUser.builder().id(userId).build())
                .build();
        //media db에 저장
        Media save = mediaRepository.save(media);
        //presigned url 생성
        
        // image id, presigned url 반환
        return null;
    }
}

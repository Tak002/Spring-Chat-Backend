package com.tak.app_media.controller;

import com.tak.app_media.dto.PresignedUrlResponse;
import com.tak.app_media.entity.Media;
import com.tak.app_media.repository.MediaRepository;
import com.tak.common.api.ApiResponseBody;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final S3Presigner s3Presigner;
    
    @Value("${app.s3.bucket}")
    private String bucket;

    public PresignedUrlResponse generatePresignedURL(String type, Long userId){
        //media id 생성
        Media media = Media.builder()
                .purpose(type)
                .owner(AppUser.builder().id(userId).build())
                .build();
        //media db에 저장
        Media save = mediaRepository.save(media);
        Long mediaId =  save.getId();
        //presigned url 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(String.valueOf(mediaId))
                .build();
//        if(type.equals("profile")){
//            프로필에 프로필 이미지 등록
//        }
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(10)) // URL 유효시간
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);
        
        String uploadUrl = presigned.url().toString();

        // image id, presigned url 반환
        return new PresignedUrlResponse(save.getId(),uploadUrl);
    }

    // todo get media url
    public String getMediaUrl(Long mediaId){
        return null;
    }
}

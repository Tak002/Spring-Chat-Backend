package com.tak.app_media.controller;

import com.tak.app_media.dto.PresignedUrlResponse;
import com.tak.app_media.entity.Media;
import com.tak.app_media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
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
                .ownerId(userId)
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

    public String getMediaUrl(Long mediaId) {
        // 1) DB에 실제 media가 있는지 확인 (없으면 예외)
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 mediaId: " + mediaId));

        // 2) S3 객체 key는 업로드 때와 동일하게 mediaId 문자열 사용
        String key = String.valueOf(media.getId());

        // 3) GetObject용 요청 생성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // 4) presigned GET URL 생성 (유효기간은 원하는 대로 조정)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);

        // 5) 최종 URL 반환
        return presigned.url().toString();
    }

}

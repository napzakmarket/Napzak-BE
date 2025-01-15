package com.napzak.domain.product.api.service;

import com.napzak.domain.product.api.ProductInterestFacade;
import com.napzak.domain.product.core.ProductPhotoRetriever;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPhotoRetriever productPhotoRetriever;
    private final ProductInterestFacade productInterestFacade;

    //사용자가 특정 product에 좋아요를 눌렀는지 boolean 값을 받아오는 메서드
    public Boolean getIsInterested(Long storeId, Long productId){
        return productInterestFacade.getIsInterested(storeId, productId);
    }

    //첫번째 순서의 사진(대표사진)의 url을 String으로 가져오는 메서드
    public String getFirstSequencePhoto(Long productId){
        return productPhotoRetriever.findFirstSequencePicture(productId);
    }

    //LocalDateTime createdAt을 대략적으로 표현하는(e.g. n일전) 메서드
    public String calculateUploadTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(createdAt, now);
        long months = ChronoUnit.MONTHS.between(createdAt, now);
        long weeks = ChronoUnit.WEEKS.between(createdAt, now);
        long days = ChronoUnit.DAYS.between(createdAt, now);
        long hours = ChronoUnit.HOURS.between(createdAt, now);
        long minutes = ChronoUnit.MINUTES.between(createdAt, now);

        if (years > 0 || months >= 11) {
            if (months == 11){
                years = 1;
            }
            return years + "년 전";
        }

        if (months > 0 || weeks >= 4) {
            if (weeks == 4){
                months = 1;
            }
            return months + "달 전";
        }

        if (weeks > 0 || days >= 6) {
            if (days == 6){
                weeks = 1;
            }
            return weeks + "주 전";
        }

        if (days > 0 || hours >= 23) {
            if (hours == 23){
                days = 1;
            }
            return days + "일 전";
        }


        if (hours > 0) {
            return hours + "시간 전";
        }

        if (minutes > 0) {
            return minutes + "분 전";
        }

        return "방금 전";
    }


}

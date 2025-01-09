package com.napzak.domain.member.repository;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query("SELECT u FROM MemberEntity u WHERE u.socialId = :socialId AND u.socialType = :socialType")
    Optional<MemberEntity> findBySocialTypeAndSocialId(@Param("socialId") Long socialId,
                                                 @Param("socialType") SocialType socialType);
}

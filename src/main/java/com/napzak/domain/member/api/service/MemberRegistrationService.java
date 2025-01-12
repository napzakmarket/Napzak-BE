package com.napzak.domain.member.api.service;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.Role;
import com.napzak.domain.member.core.MemberRepository;
import com.napzak.global.auth.client.dto.MemberSocialInfoResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRegistrationService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long registerMemberWithUserInfo(final MemberSocialInfoResponse memberSocialInfoResponse) {

        MemberEntity member = MemberEntity.create(
                null,
                null,
                Role.MEMBER,
                memberSocialInfoResponse.socialId(),
                memberSocialInfoResponse.socialType(),
                null
        );

        memberRepository.save(member);

        log.info("Member registered with memberId: {}, role: {}", member.getId(), member.getRole());

        return member.getId();
    }
}
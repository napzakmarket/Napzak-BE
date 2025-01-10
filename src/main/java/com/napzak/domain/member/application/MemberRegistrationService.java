package com.napzak.domain.member.application;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.Role;
import com.napzak.domain.member.repository.MemberRepository;
import com.napzak.global.auth.client.dto.MemberInfoResponse;
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
    public Long registerMemberWithUserInfo(final MemberInfoResponse memberInfoResponse) {
        //log.info("Registering new user with role: {}", users.getRole());

        MemberEntity member = MemberEntity.create(
                memberInfoResponse.nickname(),
                null,
                Role.MEMBER,
                memberInfoResponse.socialId(),
                memberInfoResponse.socialType(),
                null
        );

        memberRepository.save(member);

        //log.info("Member registered with memberId: {}, role: {}", member.getId(), users.getRole());

        return member.getId();
    }
}
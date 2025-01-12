package com.napzak.domain.member.api.service;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;
import com.napzak.domain.member.core.exception.MemberErrorCode;
import com.napzak.domain.member.core.MemberRepository;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberEntity findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NapzakException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean checkMemberExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return memberRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }

    @Transactional(readOnly = true)
    public MemberEntity findMemberBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return memberRepository.findBySocialTypeAndSocialId(socialId, socialType)
                .orElseThrow(() -> new NapzakException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
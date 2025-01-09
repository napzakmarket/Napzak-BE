package com.napzak.domain.member.application;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;
import com.napzak.domain.member.exception.MemberErrorCode;
import com.napzak.domain.member.port.MemberUseCase;
import com.napzak.domain.member.repository.MemberRepository;
import com.napzak.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService implements MemberUseCase {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberEntity findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkMemberExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return memberRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberEntity findMemberBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return memberRepository.findBySocialTypeAndSocialId(socialId, socialType)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
package com.napzak.domain.member.port;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;

public interface MemberUseCase {
    MemberEntity findMemberByMemberId(Long memberId);

    boolean checkMemberExistsBySocialIdAndSocialType(Long socialId, SocialType socialType);

    MemberEntity findMemberBySocialIdAndSocialType(Long socialId, SocialType socialType);
}

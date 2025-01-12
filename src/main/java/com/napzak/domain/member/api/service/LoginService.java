package com.napzak.domain.member.api.service;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;
import com.napzak.domain.member.api.dto.LoginSuccessResponse;
import com.napzak.global.auth.client.dto.MemberSocialInfoResponse;
import com.napzak.global.auth.client.dto.MemberLoginRequest;
import com.napzak.global.auth.client.service.SocialService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final SocialService kakaoSocialService;
    private final MemberRegistrationService memberRegistrationService;
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    @Transactional
    public LoginSuccessResponse login(
            String authorizationCode,
            MemberLoginRequest request){
        try {
            // 소셜 서비스로부터 사용자 정보 조회
            MemberSocialInfoResponse memberInfo = findMemberInfo(authorizationCode, request);

            // 회원 정보를 DB에서 확인
            Long memberId = findOrRegisterMember(memberInfo);

            // 토큰 발급, 로그인 응답
            return returnLoginSuccessResponse(memberId, memberInfo);
        } catch (Exception e) {
            log.error("Login failed: ", e);
            throw e;
        }
    }
    //소셜 타입에 따라 사용자 정보 조회
    private MemberSocialInfoResponse findMemberInfo(
            String authorizationCode,
            MemberLoginRequest request){

        SocialService socialService = getSocialService(request.socialType());
        return socialService.login(authorizationCode, request);
    }

    //해당하는 소셜 서비스 반환
    private SocialService getSocialService(SocialType socialType){
        return switch (socialType){
            case KAKAO -> kakaoSocialService;
            default -> throw new RuntimeException();
        };
    }

    //기존 회원을 찾거나, 없으면 새로 멤버 등록
    private Long findOrRegisterMember(final MemberSocialInfoResponse memberSocialInfoResponse){
        boolean memberExits = memberService.checkMemberExistsBySocialIdAndSocialType(memberSocialInfoResponse.socialId(),
                memberSocialInfoResponse.socialType());

        if (memberExits){
            MemberEntity member = memberService.findMemberBySocialIdAndSocialType(memberSocialInfoResponse.socialId(),
                    memberSocialInfoResponse.socialType());
            return member.getId();
        }

        return memberRegistrationService.registerMemberWithUserInfo(memberSocialInfoResponse);
    }

    private LoginSuccessResponse returnLoginSuccessResponse(
            Long memberId,
            MemberSocialInfoResponse memberSocialInfoResponse){

        MemberEntity member = memberService.findMemberByMemberId(memberId);
        return authenticationService.generateLoginSuccessResponse(memberId, memberSocialInfoResponse);

    }

}

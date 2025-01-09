package com.napzak.domain.member.application;

import com.napzak.domain.member.core.MemberEntity;
import com.napzak.domain.member.core.SocialType;
import com.napzak.domain.member.dto.LoginSuccessResponse;
import com.napzak.domain.member.dto.MemberLoginResponse;
import com.napzak.domain.member.port.MemberUseCase;
import com.napzak.global.auth.client.dto.MemberInfoResponse;
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
    private final MemberUseCase memberUseCase;
    private final MemberRegistrationService memberRegistrationService;
    private final AuthenticationService authenticationService;

    @Transactional
    public LoginSuccessResponse login(
            String authorizationCode,
            MemberLoginRequest request){
        try {
            // 소셜 서비스로부터 사용자 정보 조회
            MemberInfoResponse memberInfo = findMemberInfo(authorizationCode, request);

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
    private MemberInfoResponse findMemberInfo(
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
    private Long findOrRegisterMember(final MemberInfoResponse memberInfoResponse){
        boolean memberExits = memberUseCase.checkMemberExistsBySocialIdAndSocialType(memberInfoResponse.socialId(),
                memberInfoResponse.socialType());

        if (memberExits){
            MemberEntity member = memberUseCase.findMemberBySocialIdAndSocialType(memberInfoResponse.socialId(),
                    memberInfoResponse.socialType());
            return member.getId();
        }

        return memberRegistrationService.registerMemberWithUserInfo(memberInfoResponse);
    }

    private LoginSuccessResponse returnLoginSuccessResponse(
            Long memberId,
            MemberInfoResponse memberInfoResponse){

        MemberEntity member = memberUseCase.findMemberByMemberId(memberId);
        return authenticationService.generateLoginSuccessResponse(memberId, memberInfoResponse);

    }

}

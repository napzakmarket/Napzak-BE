package com.napzak.api.domain.store.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
	GENRE_PREPERENCE_REGISTER_SUCCESS(HttpStatus.CREATED, "장르 정보 저장 성공"),
	GET_MYPAGE_INFO_SUCCESS(HttpStatus.OK, "마이페이지 정보 조회 성공"),
	GET_STORE_ID_SUCCESS(HttpStatus.OK, "상점 id 조회 성공"),
	GET_STORE_INFO_SUCCESS(HttpStatus.OK, "상점 정보 조회 성공"),
	GET_SETTING_LINK_SUCCESS(HttpStatus.OK, "설정 링크 조회 성공"),
	ACCESS_TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "액세스 토큰 재발급 성공"),
	GET_ONBOARDING_TERMS_SUCCESS(HttpStatus.OK, "온보딩 약관 정보 조회 성공"),
	VALID_NICKNAME_SUCCESS(HttpStatus.OK, "사용할 수 있는 닉네임이에요!"),
	NICKNAME_REGISTER_SUCCESS(HttpStatus.OK, "닉네임이 등록되었습니다."),
	SLANG_REDIS_UPDATE_SUCCESS(HttpStatus.OK, "redis 비속어 업데이트 성공"),
	GET_PROFILE_SUCCESS(HttpStatus.OK, "상점 프로필이 성공적으로 조회되었습니다."),
	PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "상점 프로필이 성공적으로 수정되었습니다."),
	CHANGE_STORE_ROLE_SUCCESS(HttpStatus.OK, "유저 role 변경이 완료되었습니다."),
	STORE_PHOTO_DELETE_SUCCESS(HttpStatus.OK, "사용하지 않는 S3 유저 이미지가 삭제되었습니다."),
	STORE_UNBLOCK_SUCCESS(HttpStatus.OK, "유저 차단이 해제되었습니다."),
	/*
	201 Created
	 */
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	STORE_REPORT_SUCCESS(HttpStatus.CREATED, "상점이 신고되었습니다."),
	STORE_WITHDRAW_SUCCESS(HttpStatus.CREATED, "상점 탈퇴가 완료되었습니다."),
	REGISTER_TERMS_AGREEMENT_SUCCESS(HttpStatus.CREATED, "약관 동의 내용이 저장되었습니다."),
	TOKENS_REISSUE_SUCCESS(HttpStatus.OK, "토큰 재발행에 성공하였습니다."),
	STORE_BLOCK_SUCCESS(HttpStatus.CREATED, "유저 차단에 성공하였습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}

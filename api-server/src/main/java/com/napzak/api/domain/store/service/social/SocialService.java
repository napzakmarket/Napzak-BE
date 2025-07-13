package com.napzak.api.domain.store.service.social;

import com.napzak.common.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.common.auth.client.dto.StoreSocialLoginRequest;

public interface SocialService {
	StoreSocialInfoResponse login(
		final String authorizationToken,
		final StoreSocialLoginRequest storeSocialLoginRequest);
}

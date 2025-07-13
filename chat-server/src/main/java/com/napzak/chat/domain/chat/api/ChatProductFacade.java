package com.napzak.chat.domain.chat.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.product.crud.product.ProductRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatProductFacade {

	private final ProductRetriever productRetriever;
}

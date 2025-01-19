package com.napzak.domain.product.api.dto.response;

import java.util.List;

public record ProductListResponse(
        List<ProductBuyDto> productBuyList,
        List<ProductSellDto> productSellList
) {
}
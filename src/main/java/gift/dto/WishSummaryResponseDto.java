package gift.dto;

import gift.domain.WishSummary;

public record WishSummaryResponseDto(
        String productName,
        int count
) {
    public static WishSummaryResponseDto from(WishSummary wishSummary) {
        return new WishSummaryResponseDto(
                wishSummary.getProductName(),
                wishSummary.getCount()
        );
    }
}

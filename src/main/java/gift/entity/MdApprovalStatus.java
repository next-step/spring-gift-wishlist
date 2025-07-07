package gift.entity;

import gift.exception.MdApprovalException;

public class MdApprovalStatus {

    private final boolean approved;

    private MdApprovalStatus(boolean approved) {
        this.approved = approved;
    }

    public static MdApprovalStatus of(String productName) {
        if (productName.contains("카카오")) {
            throw new MdApprovalException("'카카오'가 포함된 상품명은 MD 승인 후 등록 가능합니다.");
        }
        return new MdApprovalStatus(false);
    }

    public static MdApprovalStatus approved() {
        return new MdApprovalStatus(true);
    }

    public static MdApprovalStatus notApproved() {
        return new MdApprovalStatus(false);
    }

    public boolean isApproved() {
        return approved;
    }
}


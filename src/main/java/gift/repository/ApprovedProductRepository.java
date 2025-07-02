package gift.repository;

public interface ApprovedProductRepository {

    void saveApprovedProductName(String name);

    boolean existApprovedProductName(String name);


}

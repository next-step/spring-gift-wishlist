package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Item;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(ItemRepository.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 및 ID로 조회 테스트")
    void saveAndFindById() {

        Item newItem = new Item(null, "테스트 상품", 10000, "test.jpg");

        Item savedItem = itemRepository.save(newItem);
        Optional<Item> foundItemOptional = itemRepository.findById(savedItem.getId());

        assertThat(savedItem.getId()).isNotNull();
        assertThat(foundItemOptional).isPresent();

        Item foundItem = foundItemOptional.get();
        assertThat(foundItem.getName()).isEqualTo(newItem.getName());
        assertThat(foundItem.getPrice()).isEqualTo(newItem.getPrice());
    }
}
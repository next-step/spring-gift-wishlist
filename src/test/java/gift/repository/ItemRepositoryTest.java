package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Item;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
    private Item item1, item2, item3;

    @BeforeEach
    void setUp() {
        itemRepository.save(new Item(null, "C-Item", 3000, "c.jpg"));
        itemRepository.save(new Item(null, "A-Item", 1000, "a.jpg"));
        itemRepository.save(new Item(null, "B-Item", 2000, "b.jpg"));
    }

    @Test
    @DisplayName("상품 저장 및 ID로 조회 테스트")
    void saveAndFindById() {

        Item newItem = new Item(null, "테스트 상품", 10000, "test.jpg");

        Item savedItem = itemRepository.save(newItem);
        Optional<Item> foundItemOptional = itemRepository.findById(savedItem.getId());

        assertThat(savedItem.getId()).isNotNull();
        assertThat(foundItemOptional).isPresent();
        assertThat(foundItemOptional.get().getName()).isEqualTo("새로운 상품");
    }

    @Test
    @DisplayName("findAll - 이름 오름차순 정렬 및 페이지네이션 테스트")
    void findAll_with_pagination_and_sorting() {
        int page = 0;
        int size = 2;
        String sortProperty = "name";
        String sortDirection = "asc";

        List<Item> items = itemRepository.findAll(page, size, sortProperty, sortDirection);

        assertThat(items).hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("A-Item");
        assertThat(items.get(1).getName()).isEqualTo("B-Item");
    }

    @Test
    @DisplayName("상품 정보 수정 테스트")
    void update() {
        Item itemToUpdate = itemRepository.findAll(0, 1, "name", "asc").getFirst();
        Long itemId = itemToUpdate.getId();
        Item updatedInfo = new Item(itemId, "수정된 A-Item", 1500, "updated_a.jpg");

        itemRepository.update(updatedInfo);
        Optional<Item> foundItemOptional = itemRepository.findById(itemId);

        assertThat(foundItemOptional).isPresent();
        assertThat(foundItemOptional.get().getName()).isEqualTo("수정된 A-Item");
        assertThat(foundItemOptional.get().getPrice()).isEqualTo(1500);
    }

    @Test
    @DisplayName("상품 정보 삭제 테스트")
    void deleteById() {
        Item itemToDelete = itemRepository.findAll(0, 1, "name", "desc").getFirst();
        Long itemId = itemToDelete.getId();

        itemRepository.deleteById(itemId);
        Optional<Item> foundItemOptional = itemRepository.findById(itemId);

        assertThat(foundItemOptional).isNotPresent();
    }
}
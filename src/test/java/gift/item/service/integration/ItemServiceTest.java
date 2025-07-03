package gift.item.service.integration;

import gift.global.exception.CustomException;
import gift.item.dto.CreateItemDto;
import gift.item.dto.ItemDto;
import gift.item.repository.ItemRepository;
import gift.item.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional  // 테스트 후 데이터 롤백
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    void 아이템저장성공() {
        CreateItemDto dto = new CreateItemDto("상품A", 1000, "img.jpg");

        ItemDto result = itemService.saveItem(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("상품A");
    }

    @Test
    void 아이템조회성공() {
        CreateItemDto dto = new CreateItemDto("상품B", 2000, "img2.jpg");
        ItemDto saved = itemService.saveItem(dto);

        ItemDto result = itemService.findItem(saved.getId());

        assertThat(result.getName()).isEqualTo("상품B");
    }

    @Test
    void 아이템조회_실패_예외발생() {
        Long 없는아이템Id = 999L;

        assertThrows(CustomException.class, () -> itemService.findItem(없는아이템Id));
    }
}
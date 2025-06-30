package gift.model;

import java.util.List;

public record CustomPage<T> (
    List<T> contents,
    Integer page,
    Integer size,
    Integer totalElements,
    Integer totalPages
) {
    public static class Builder<T> {
        final private List<T> contents;
        final private Integer totalElements;
        private Integer page = 0;
        private Integer size = 5; // 기본값 설정

        public Builder(List<T> contents, Integer totalElements) {
            if (contents == null || totalElements == null) {
                throw new IllegalArgumentException("contents와 totalElements는 null일 수 없습니다!");
            }
            this.contents = contents;
            this.totalElements = totalElements;
        }

        public Builder<T> page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder<T> size(Integer size) {
            this.size = size;
            return this;
        }

        public CustomPage<T> build() {
            if (this.page < 0 || this.size <= 0) {
                throw new IllegalArgumentException("페이지 번호는 0 이상이어야 하고, 크기는 1 이상이어야 합니다!");
            }
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (!(totalPages == 0 && page == 0) && this.page >= totalPages) {
                throw new IllegalArgumentException("페이지 번호가 총 페이지 수를 초과했습니다!");
            }

            return new CustomPage<>(
                this.contents,
                this.page,
                this.size,
                this.totalElements,
                totalPages
            );
        }
    }
}

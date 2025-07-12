package gift.common.pagination;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        int pageSize,
        int totalElements
) {}
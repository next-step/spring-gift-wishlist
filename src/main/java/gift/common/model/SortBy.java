package gift.common.model;

public record SortBy(
        String field,
        Boolean ascending
) {
    public SortBy {
        if (ascending == null) {
            ascending = true;
        }
    }

    static SortBy from(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return new SortBy("id", true); // 기본값 설정
        }
        try{
            String[] parts = sortParam.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("정렬 파라미터는 '필드,방향' 형식이어야 합니다.");
            }
            String field = parts[0].trim();
            String direction = parts[1].trim().toLowerCase();
            Boolean ascending = switch (direction) {
                case "asc", "ascending" -> true;
                case "desc", "descending" -> false;
                default -> throw new IllegalArgumentException("정렬 방향은 'asc', 'desc', 'ascending', 'descending' 중 하나여야 합니다.");
            };
            return new SortBy(field, ascending);
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 정렬 파라미터 형식입니다: " + sortParam, e);
        }
    }

    public String toSql() {
        return field + (ascending ? " ASC" : " DESC");
    }
}

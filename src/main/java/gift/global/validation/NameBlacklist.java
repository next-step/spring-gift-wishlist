package gift.global.validation;

import java.util.List;
import java.util.Map;

public class NameBlacklist {

  private static final Map<NameType, List<String>> blacklistMap = Map.of(
      NameType.PRODUCT, List.of("카카오")
  );

  public static List<String> getBlacklist(NameType type) {
    return blacklistMap.getOrDefault(type, List.of());
  }
}

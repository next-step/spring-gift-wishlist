package gift.wishlist.repository;

import gift.member.entity.Member;
import gift.item.entity.Item;
import gift.wishlist.entity.Wishlist;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishlistRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Wishlist> findByMember(Member member) {
        String sql = "SELECT w.id, w.member_id, w.item_id, i.name, i.price, i.image_url " +
                "FROM wishlist w JOIN items i ON w.item_id = i.id " +
                "WHERE w.member_id = ?";

        return jdbcTemplate.query(sql, new Object[]{member.getId()}, (rs, rowNum) -> {
            return new Wishlist(
                    rs.getLong("id"),
                    member,
                    new Item(
                            rs.getLong("item_id"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getString("image_url")
                    )
            );
        });
    }

    public boolean existsByMemberAndItem(Member member, Item item) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE member_id = ? AND item_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, member.getId(), item.getId());
        return count != null && count > 0;
    }

    public void save(Wishlist wishlist) {
        String sql = "INSERT INTO wishlist (member_id, item_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, wishlist.getMember().getId(), wishlist.getItem().getId());
    }

    public void deleteByMemberAndItem(Member member, Item item) {
        String sql = "DELETE FROM wishlist WHERE member_id = ? AND item_id = ?";
        jdbcTemplate.update(sql, member.getId(), item.getId());
    }
}
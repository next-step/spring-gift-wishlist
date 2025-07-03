package gift.item.repository;

import gift.item.dto.UpdateItemDto;
import gift.item.entity.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class itemRepositoryImpl implements ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public itemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Item saveItem(Item item) {
        String sql = "INSERT INTO item (name, price, image_url) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, item.getName());
            ps.setInt(2, item.getPrice());
            ps.setString(3, item.getImageUrl());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Item(id, item.getName(), item.getPrice(), item.getImageUrl());
    }

    @Override
    public List<Item> findAllItems() {
        String sql = "SELECT * FROM item";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                )
        );
    }

    @Override
    public Item findItem(Long id) {
        String sql = "SELECT * FROM item WHERE id = ?";
        List<Item> result = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) ->
                new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                ));
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void updateItem(Long id, UpdateItemDto dto) {
        String sql = "UPDATE item SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, dto.getName(), dto.getPrice(), dto.getImageUrl(), id);
    }

    @Override
    public void deleteItem(Long id) {
        String sql = "DELETE FROM item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
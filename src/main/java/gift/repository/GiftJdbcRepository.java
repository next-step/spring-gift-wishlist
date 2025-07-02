package gift.repository;

import gift.entity.Gift;
import gift.exception.InValidSpecialCharException;
import gift.exception.NeedAcceptException;
import gift.exception.NoGiftException;
import gift.exception.NoValueException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GiftJdbcRepository implements GiftRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AtomicLong id = new AtomicLong(0);

    public GiftJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Gift save(Gift gift) {
        if (gift == null) {
            throw new NoGiftException("해당 상품을 찾을 수 없습니다.");
        }
        if(!gift.isGiftNameValid()){
            throw new InValidSpecialCharException("특수문자는 ( ), [ ], +, -, &, /, _ 만 허용됩니다.");
        }
        gift.isKakaoMessageInclude();

        gift.setId(id.getAndIncrement());
        String sql = "INSERT INTO Gift(id, giftId, giftName, giftPrice, giftPhotoUrl, isKakaoMDAccepted) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] args = new Object[]{
                gift.getId(),
                gift.getGiftId(),
                gift.getGiftName(),
                gift.getGiftPrice(),
                gift.getGiftPhotoUrl(),
                gift.getIsKakaoMDAccepted()
        };
        int[] argTypes = {Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.BOOLEAN};
        jdbcTemplate.update(sql, args, argTypes);

        if(!gift.getIsKakaoMDAccepted()){
            throw new NeedAcceptException("MD 의 승인이 필요합니다.");
        }

        return gift;
    }

    @Override
    public Optional<Gift> findById(Long id) {
        String sql = "SELECT * FROM Gift WHERE id = ?";
        Object[] args = {id};
        int[] argTypes = {Types.BIGINT};
        return jdbcTemplate.query(sql, args, argTypes, rs -> {
                if (rs.next()) {
                    Gift gift = new Gift();
                    gift.setId(rs.getLong("id"));
                    gift.setGiftId(rs.getLong("giftId"));
                    gift.setGiftName(rs.getString("giftName"));
                    gift.setGiftPrice(rs.getInt("giftPrice"));
                    gift.setGiftPhotoUrl(rs.getString("giftPhotoUrl"));
                    gift.setKakaoMDAccepted(rs.getBoolean("isKakaoMDAccepted"));
                    return Optional.of(gift);
                }
                return Optional.empty();
            }
        );
    }

    @Override
    public List<Gift> findAll() {
        String sql = "SELECT * FROM Gift WHERE isKakaoMDAccepted = ?";
        Object[] args = {true};
        int[] argTypes = {Types.BOOLEAN};
        return jdbcTemplate.query(sql, args, argTypes, (rs, rowNum) -> {
            Gift gift = new Gift();
            gift.setId(rs.getLong("id"));
            gift.setGiftId(rs.getLong("giftId"));
            gift.setGiftName(rs.getString("giftName"));
            gift.setGiftPrice(rs.getInt("giftPrice"));
            gift.setGiftPhotoUrl(rs.getString("giftPhotoUrl"));
            gift.setKakaoMDAccepted(rs.getBoolean("isKakaoMDAccepted"));
            return gift;
        });
    }

    @Override
    public Gift modify(Long id, Gift gift) {
        StringBuilder sql = new StringBuilder("UPDATE Gift SET ");
        List<Object> params = new ArrayList<>();
        if(gift.getGiftId() != null){
            sql.append("giftId = ?, ");
            params.add(gift.getGiftId());
        }
        if(gift.getGiftName() != null){
            sql.append("giftName = ?, ");
            params.add(gift.getGiftName());
        }
        if(gift.getGiftPrice() != null){
            sql.append("giftPrice = ?, ");
            params.add(gift.getGiftPrice());
        }
        if(gift.getGiftPhotoUrl() != null){
            sql.append("giftPhotoUrl = ?, ");
            params.add(gift.getGiftPhotoUrl());
        }
        if(params.isEmpty()){
            throw new NoValueException("수정할 내용이 없습니다.");
        }
        sql.deleteCharAt((sql.length()-2));
        sql.append(" WHERE id = ?");

        params.add(id);
        jdbcTemplate.update(sql.toString(), params.toArray());

        gift.setId(id);
        gift.isKakaoMessageInclude();
        return gift;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Gift WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

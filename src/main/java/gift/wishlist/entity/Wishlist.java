package gift.wishlist.entity;

import gift.item.entity.Item;
import gift.member.entity.Member;

public class Wishlist {

    private Long id;
    private Member member;
    private Item item;

    public Wishlist(Long id, Member member, Item item) {
        this.id = id;
        this.member = member;
        this.item = item;
    }

    public Wishlist(Member member, Item item) {
        this(null, member, item);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Item getItem() {
        return item;
    }
}
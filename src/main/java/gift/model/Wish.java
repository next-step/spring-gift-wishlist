package gift.model;

public class Wish {
    private Long id;
    private Long userid;
    private Long productid;
    private Long count;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Long getUserid() {return userid;}
    public void setUserid(Long userid) {this.userid = userid;}
    public Long getProductid() {return productid;}
    public void setProductid(Long productid) {this.productid = productid;}
    public Long getCount() {return count;}
    public void setCount(Long count) {this.count = count;}
}
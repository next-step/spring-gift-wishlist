package gift.controller;

import gift.annotation.LoginUser;
import gift.repository.WishDao;
import gift.model.User;
import gift.service.WishService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class WishController {
    private final WishDao wishDao;
    private final WishService wishService;

    public WishController(WishDao wishDao, WishService wishService){
        this.wishDao = wishDao;
        this.wishService = wishService;
    }

    @PostMapping("/wish/add")
    public void addWish(@RequestBody Long productId, @LoginUser User user){
        wishDao.addWish(user.getId(),productId);
    }
}
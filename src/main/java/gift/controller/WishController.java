package gift.controller;

import gift.DTO.WishRequestDTO;
import gift.annotation.LoginUser;
import gift.repository.WishDao;
import gift.model.User;
import gift.service.WishService;
import org.springframework.web.bind.annotation.*;

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
    public void addWish(@RequestBody WishRequestDTO request, @LoginUser User user){
        wishDao.addWish(user.getId(), request.getProductid());
    }

    @DeleteMapping("/wish/delete")
    public void deleteWish(@RequestBody WishRequestDTO request,@LoginUser User user){
        wishDao.deleteWish(user.getId(), request.getProductid());

    }
}
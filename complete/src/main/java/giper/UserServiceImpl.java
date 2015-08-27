package giper;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by prochiy on 8/27/15.
 */
public class UserServiceImpl implements UserService{

    @Resource
    private UserRepository userRepository;

    @Override
    @Transactional
    public User create(User user) {
        User createdUser = user;
        return userRepository.save(createdUser);
    }

    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findOne((long) id);
    }

    @Override
    @Transactional(rollbackFor=UserNotFound.class)
    public User delete(long id) throws UserNotFound {
        User deletedUser = userRepository.findOne(id);

        if (deletedUser == null)
            throw new UserNotFound();
        38

        39
        shopRepository.delete(deletedShop);
        40
        return deletedShop;
        41
    }
    42

            43
    @Override
    44
    @Transactional
    45
    public List findAll() {
        46
        return shopRepository.findAll();
        47
    }
    48

            49
    @Override
    50
    @Transactional(rollbackFor=ShopNotFound.class)
    51
    public Shop update(Shop shop) throws ShopNotFound {
        52
        Shop updatedShop = shopRepository.findOne(shop.getId());
        53

        54
        if (updatedShop == null)
            55
        throw new ShopNotFound();
        56

        57
        updatedShop.setName(shop.getName());
        58
        updatedShop.setEmplNumber(shop.getEmplNumber());
        59
        return updatedShop;
        60
    }
    61

}

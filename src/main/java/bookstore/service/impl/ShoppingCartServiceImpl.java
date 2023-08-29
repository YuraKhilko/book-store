package bookstore.service.impl;

import bookstore.dto.cartitem.CreateCartItemRequestDto;
import bookstore.dto.cartitem.UpdateCartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.repository.CartItemRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getById(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    @Override
    public ShoppingCartResponseDto addCartItem(
            Long userId,
            CreateCartItemRequestDto requestDto
    ) {
        ShoppingCart shoppingCart = getShoppingCartById(userId);
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequestDto requestDto
    ) {
        CartItem cartItemFromDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id " + cartItemId));
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItemFromDb.setQuantity(cartItem.getQuantity());
        cartItemRepository.save(cartItemFromDb);
        return shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    private ShoppingCart getShoppingCartById(Long userId) {
        return shoppingCartRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart by userId "
                                + userId));

    }
}

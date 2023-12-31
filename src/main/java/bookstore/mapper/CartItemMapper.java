package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.cartitem.CartItemResponseDto;
import bookstore.dto.cartitem.CreateCartItemRequestDto;
import bookstore.dto.cartitem.UpdateCartItemRequestDto;
import bookstore.model.Book;
import bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CreateCartItemRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    CartItem toEntity(UpdateCartItemRequestDto requestDto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBook(@MappingTarget CartItem cartItem, CreateCartItemRequestDto requestDto) {
        Book book = new Book();
        book.setId(requestDto.getBookId());
        cartItem.setBook(book);
    }
}

package bookstore.service.impl;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.BookSearchParametersDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.repository.CategoryRepository;
import bookstore.repository.book.BookRepository;
import bookstore.repository.book.BookSpecificationBuilderImpl;
import bookstore.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilderImpl bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(b -> bookMapper.toDto(b))
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(findBookById(id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("Can't find Category by id " + categoryId));
        return category.getBooks().stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
    }
}

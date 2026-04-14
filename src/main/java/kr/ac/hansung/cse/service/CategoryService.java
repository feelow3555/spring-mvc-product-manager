package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Spring한테 "이 클래스는 서비스 빈이야" 라고 알려줌 Spring이 자동으로 객체 생성해서 관리
@Transactional(readOnly = true) // 클래스 전체에 읽기 전용 트랜잭션 적용 읽기만 하는 메서드는 DB 최적화가 돼서 성능이 좋아짐
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 그냥 전체 목록 반환, readOnly 트랜잭션 상속받음
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //@Transactional로 쓰기 허용 오버라이드 (ifPresent로 중복 이름 있으면 DuplicateCategoryException 던짐)
    @Transactional
    public Category createCategory(String name) {
        categoryRepository.findByName(name)
                .ifPresent(c -> { throw new DuplicateCategoryException(name); });
        return categoryRepository.save(new Category(name));
    }

    // 삭제 전에 연결된 상품 수 확인 1개라도 있으면 IllegalStateException 던져서 삭제 막음
    @Transactional
    public void deleteCategory(Long id) {
        long count = categoryRepository.countProductsByCategoryId(id);
        if (count > 0) throw new IllegalStateException(
                "상품 " + count + "개가 연결되어 있어 삭제할 수 없습니다.");
        categoryRepository.delete(id);
    }
}

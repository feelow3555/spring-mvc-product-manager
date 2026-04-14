package kr.ac.hansung.cse.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.CategoryForm;
import kr.ac.hansung.cse.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 이 클래스가 MVC의 Controller임을 Spring에 알려준다. View 이름을 반환하는 방식으로 동작
@RequestMapping("/categories") // 이 Controller의 모든 URL이 /categories로 시작
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /categories 요청 처리 Model에 카테고리 목록 담아서 categoryList.html로 전달
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categoryList";
    }

    // GET /categories/create 요청 처리 빈 CategoryForm 객체를 모델에 담아서 폼 화면 보여줌 Thymeleaf가 이 객체에 입력값을 바인딩
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categoryForm";
    }


    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute CategoryForm categoryForm, // @Valid — CategoryForm의 @NotBlank, @Size 검증 실행, @ModelAttribute — 폼에서 넘어온 데이터를 CategoryForm 객체에 자동 바인딩
            BindingResult bindingResult, // BindingResult — 검증 오류 담는 객체. @Valid 바로 뒤에 와야 함
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "categoryForm"; // bindingResult.hasErrors() — 오류 있으면 폼 다시 보여줘
        try {
            categoryService.createCategory(categoryForm.getName());
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 등록되었습니다.");
        } catch (DuplicateCategoryException e) { // DuplicateCategoryException 잡아서 rejectValue()로 폼에 오류 메시지 표시
            bindingResult.rejectValue("name", "duplicate", e.getMessage());
            return "categoryForm";
        }
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) { //@PathVariable — URL의 {id} 부분을 변수로 받아
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
        } catch (IllegalStateException e) { //IllegalStateException 잡아서 redirectAttributes.addFlashAttribute()로 오류 메시지 전달
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Flash Attribute — 리다이렉트 후 딱 한 번만 표시되고 사라짐
        }
        return "redirect:/categories"; // 처리 후 목록 페이지로 이동 return "categoryList" 랑 다르게 브라우저가 새 GET 요청을 보냄
    }
}

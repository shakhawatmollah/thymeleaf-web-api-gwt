package com.shakhawat.authapp.controller;

import com.shakhawat.authapp.entity.Product;
import com.shakhawat.authapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductService productService;

    private static final int DEFAULT_PAGE_SIZE = 5;

    @GetMapping
    public String listProducts(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("query") Optional<String> query,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<Product> productPage;

        if (query.isPresent() && !query.get().isEmpty()) {
            productPage = productService.searchProducts(query.get(),
                    PageRequest.of(currentPage - 1, pageSize));
            model.addAttribute("query", query.get());
        } else {
            productPage = (Page<Product>) productService.getAllProducts(
                    PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("products", productPage);
        model.addAttribute("pageTitle", "Product Management");

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers",
                    IntStream.rangeClosed(1, totalPages).boxed().toList());
        }

        return "products/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Create Product");
        return "products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            redirectAttributes.addFlashAttribute("message", product.getId() == null ? "Product created successfully!" : "Product updated successfully!");
            productService.saveProduct(product);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("pageTitle", "Edit Product");
        return "products/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }
}

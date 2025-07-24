package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Product;
import com.endorodrigo.eComerce.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


/**
 * Controlador para la gestión de productos.
 * Permite listar, crear y actualizar productos.
 */
@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String getProduct(@RequestParam(value = "code", required = false) String id, Model model) {
        List<Product> listProduct = productService.getAll();
        logger.info("Listado de productos: {}", listProduct);
        Product product = (id != null) ? productService.findId(id) : new Product();
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("product", product);
        return "product";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listProduct", productService.getAll());
            return "product";
        }
        productService.insert(product);
        logger.info("Creando producto: {}", product);
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/update", method = RequestMethod.POST)
    public String updateProduct(@ModelAttribute("product") Product product, Model model) {
        productService.insert(product);
        logger.info("Actualizando producto: {}", product);
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/delite/{code}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable String code, Model model) {
        Product product = productService.findId(code);
        logger.info("buscando product por id para eliminar: " + code);
        productService.delete(product);
        return "redirect:/product";
    }


}

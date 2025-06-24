package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Product;
import com.endorodrigo.eComerce.service.CustomerService;
import com.endorodrigo.eComerce.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ProductController {

    Logger logger = Logger.getLogger(ProductService.class.getName());

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String getProduct(@RequestParam(value = "code", required = false) String id, Model model) {
        List<Product> listProduct = productService.getAll();
        logger.info("listado de product: " + listProduct);
        Product article;
        if (id != null) {
            logger.info("buscando product por id: " + id);
            article = productService.findId(id);
            logger.info("buscando product por id: " + article);
        }else {
            article =  new Product();
        }

        model.addAttribute("listProduct", listProduct);
        model.addAttribute("article", article);
        return "product";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public String createProduct(@ModelAttribute("productForm") Product product, Model model) {
        productService.insert(product);
        logger.info("Creando product " + product);
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/update", method = RequestMethod.POST)
    public String updateProduct(@ModelAttribute("productForm") Product product, Model model) {
        productService.insert(product);
        logger.info("Creando product " + product);
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

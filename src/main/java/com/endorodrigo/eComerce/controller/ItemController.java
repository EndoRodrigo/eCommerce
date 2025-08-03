package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Item;
import com.endorodrigo.eComerce.service.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controlador para la gestión de productos.
 * Permite listar, crear y actualizar productos.
 */
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService productService;

    public ItemController(ItemService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String getProduct(@RequestParam(value = "code", required = false) String id, Model model) {
        List<Item> listProduct = productService.getAll();
        logger.info("Listado de productos: {}", listProduct);
        logger.info("Id: {}", id);
        Item product = (id != null) ? productService.findId(id) : new Item();
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("product", product);
        return "product";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public String createProduct(@Valid @ModelAttribute("product") Item product, BindingResult result, Model model) {
        logger.info("Creando producto: {}", product);
        if (result.hasErrors()) {
            model.addAttribute("listProduct", productService.getAll());
            return "product";
        }
        try {
            productService.insert(product);
        } catch (RuntimeException e) {
            model.addAttribute("listProduct", productService.getAll());
            model.addAttribute("errorMsg", e.getMessage());
            return "product";
        }
    
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/update", method = RequestMethod.POST)
    public String updateProduct(@Valid @ModelAttribute("product") Item product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listProduct", productService.getAll());
            return "product";
        }
        try {
            productService.update(product);
        } catch (RuntimeException e) {
            model.addAttribute("listProduct", productService.getAll());
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("product", product);
            return "product";
        }
        logger.info("Actualizando producto: {}", product);
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/delite/{code}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable String code, Model model) {
        Item product = productService.findId(code);
        logger.info("buscando product por id para eliminar: " + code);
        productService.delete(product);
        return "redirect:/product";
    }


}

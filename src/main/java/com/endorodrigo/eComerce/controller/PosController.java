package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.Cart;
import com.endorodrigo.eComerce.model.Product;
import com.endorodrigo.eComerce.service.PosService;
import com.endorodrigo.eComerce.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class PosController {

    public static Logger logger = (Logger) LoggerFactory.getLogger(PosController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private PosService posService;

    private final Cart cart = new Cart();

    @RequestMapping(value = "/pos", method = RequestMethod.GET)
    public String pos(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("product", new Product());
        return "pos";
    }

    @RequestMapping(value = "/pos/add", method = RequestMethod.POST)
    public String addProduct(Model model,@Valid @ModelAttribute("product") Product cartForm ) {
        logger.info("addProduct for id. "+ cartForm.getCode());
        Product product = productService.findId(cartForm.getCode());

        logger.info("product = " + product);
        cart.addProduct(product);
        logger.info("list cart = " + cart);
        return "redirect:/pos";

    }

    @RequestMapping(value = "/pos/clear", method =  RequestMethod.GET)
    public String clearCart(Model model) {
        logger.info("clearCart for id. "+ cart.toString());
        cart.clear();
        logger.info("list cart = " + cart);
        return "redirect:/pos";
    }

    @RequestMapping(value = "/pos/cash", method =  RequestMethod.GET)
    public String Order(){
        logger.info("Order for id. "+ cart);
        posService.insert(cart);
        return "redirect:/pos";
    }
}

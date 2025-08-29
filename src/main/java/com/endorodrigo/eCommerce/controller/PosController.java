package com.endorodrigo.eCommerce.controller;


import com.endorodrigo.eCommerce.exception.FacturaException;
import com.endorodrigo.eCommerce.exception.PosException;
import com.endorodrigo.eCommerce.exception.ValidationException;
import com.endorodrigo.eCommerce.model.Item;
import com.endorodrigo.eCommerce.service.PosService;
import com.endorodrigo.eCommerce.service.ItemService;
import com.endorodrigo.eCommerce.service.WebClientService;
import com.endorodrigo.eCommerce.model.Cart;
import com.endorodrigo.eCommerce.model.Customer;
import com.endorodrigo.eCommerce.model.Data;
import com.endorodrigo.eCommerce.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




/**
 * Controlador para la gestión del punto de venta (POS).
 * Permite agregar productos y clientes al carrito y procesar ventas.
 */
@Controller
@RequestMapping("/pos")
@SessionAttributes("data")
public class PosController {

    private static final Logger logger = LoggerFactory.getLogger(PosController.class);

    @Autowired
    private ItemService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PosService posService;

    @Autowired
    private WebClientService webClientService;

    private final Cart cart = new Cart();

    @GetMapping
    public String pos(@RequestParam(defaultValue = "1") int step, Model model) {
        cart.setReference_code(posService.generateIdCard());
        model.addAttribute("cart", cart);
        model.addAttribute("data", new Data());
        model.addAttribute("currentStep", step);
        // Muestra la vista POS
        return "pos";
    }

    @PostMapping(params = "action=saveClient")
    public String saveCliente(@ModelAttribute("data") Data data, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (data == null || data.getClient() == null || data.getClient().trim().isEmpty()) {
                throw new ValidationException("El documento del cliente no puede estar vacío", "client");
            }
            
            Customer customer = customerService.findIdIntification(data.getClient());
            logger.info("Information customer -> {}", customer);
            
            if (customer == null) {
                throw new ValidationException("Cliente no encontrado con el documento: " + data.getClient(), "client");
            }
            
            cart.setCustomer(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente seleccionado: " + customer.getNames());
            return "redirect:/pos?step=2";
            
        } catch (ValidationException e) {
            logger.error("Error de validación al guardar cliente: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("errorField", e.getField());
            return "redirect:/pos";
        } catch (Exception e) {
            logger.error("Error inesperado al guardar cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al buscar el cliente. Por favor, inténtelo de nuevo.");
            return "redirect:/pos";
        }
    }

    @PostMapping(params = "action=addProduct")
    public String addProduct(@ModelAttribute("data") Data data, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (data == null || data.getCode() == null || data.getCode().trim().isEmpty()) {
                throw new ValidationException("El código del producto no puede estar vacío", "code");
            }
            
            Item product = productService.findReferenteCode(data.getCode());
            logger.info("Information product = {}", product);
            
            if (product == null) {
                throw new ValidationException("Producto no encontrado con el código: " + data.getCode(), "code");
            }
            
            cart.addProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Producto agregado: " + product.getName());
            return "redirect:/pos?step=2";
            
        } catch (ValidationException e) {
            logger.error("Error de validación al agregar producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("errorField", e.getField());
            return "redirect:/pos";
        } catch (Exception e) {
            logger.error("Error inesperado al agregar producto: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al buscar el producto. Por favor, inténtelo de nuevo.");
            return "redirect:/pos";
        }
    }


    @PostMapping(params = "action=nextToPayment")
    public String nextToPayment() {
        return "redirect:/pos?step=3";
    }

    @PostMapping(params = "action=nextAddPayment")
    public String nextToPayment(@ModelAttribute("data") Data data, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (data == null || data.getPaymentMethod() == null || data.getPaymentMethod().trim().isEmpty()) {
                throw new ValidationException("Debe seleccionar un método de pago", "paymentMethod");
            }
            
            cart.setPayment_method_code(data.getPaymentMethod());
            logger.info("Information pago = {}", data.getPaymentMethod());
            
            redirectAttributes.addFlashAttribute("successMessage", "Método de pago seleccionado: " + data.getPaymentMethod());
            return "redirect:/pos?step=3";
            
        } catch (ValidationException e) {
            logger.error("Error de validación al seleccionar método de pago: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("errorField", e.getField());
            return "redirect:/pos";
        } catch (Exception e) {
            logger.error("Error inesperado al seleccionar método de pago: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al seleccionar el método de pago. Por favor, inténtelo de nuevo.");
            return "redirect:/pos";
        }
    }

    /**
     * Procesa la venta del carrito, guardándola localmente y enviándola a Factus.
     * @param status Estado de la sesión
     * @param redirectAttributes Atributos para redirección
     * @return Redirección a la página POS
     */
    @PostMapping("/generate")
    public String submit(SessionStatus status, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Information cart (antes de enviar) = {}", cart);

            // Validaciones del carrito antes de procesar
            if (cart == null) {
                throw new ValidationException("El carrito no puede ser nulo", "cart");
            }
            
            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new ValidationException("El carrito debe contener al menos un producto", "items");
            }
            
            if (cart.getCustomer() == null) {
                throw new ValidationException("El carrito debe tener un cliente asociado", "customer");
            }
            
            if (cart.getPayment_method_code() == null || cart.getPayment_method_code().trim().isEmpty()) {
                throw new ValidationException("El carrito debe tener un método de pago seleccionado", "payment_method");
            }

            // Guardar venta local
            logger.info("Guardando venta en base de datos local...");
            Cart savedCart = posService.insert(cart);
            logger.info("Venta guardada exitosamente con ID: {}", savedCart.getId());

            // Enviar a Factus
            logger.info("Enviando factura a Factus...");
            String factusResult = webClientService.createPost(cart);
            logger.info("Factura enviada a Factus: {}", factusResult);

            // Limpiar sesión después del éxito
            status.setComplete();
            cart.clear();
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("successMessage", "Venta procesada exitosamente. Factura enviada a Factus.");
            
            return "redirect:/pos";
            
        } catch (ValidationException e) {
            logger.error("Error de validación en submit: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("errorField", e.getField());
            return "redirect:/pos";
            
        } catch (PosException e) {
            logger.error("Error en POS durante submit: {} - Código: {}", e.getMessage(), e.getErrorCode());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al procesar la venta: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorCode", e.getErrorCode());
            return "redirect:/pos";
            
        } catch (FacturaException e) {
            logger.error("Error de facturación durante submit: {} - Código: {}", e.getMessage(), e.getErrorCode());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al enviar la factura: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorCode", e.getErrorCode());
            redirectAttributes.addFlashAttribute("warningMessage", "La venta se guardó localmente pero no se pudo enviar a Factus.");
            return "redirect:/pos";
            
        } catch (Exception e) {
            logger.error("Error inesperado durante submit: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al procesar la venta. Por favor, inténtelo de nuevo.");
            return "redirect:/pos";
        }
    }

    @PostMapping("/pos/clear")
    public void clearCart() {
        cart.clear();
    }
}

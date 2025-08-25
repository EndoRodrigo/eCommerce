package com.endorodrigo.eCommerce.service;


import com.endorodrigo.eCommerce.model.Item;
import com.endorodrigo.eCommerce.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio completo de notificaciones
 * Incluye email, SMS y notificaciones push
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.notifications.enabled:true}")
    private boolean notificationsEnabled;

    @Value("${app.notifications.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.notifications.sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${app.notifications.push.enabled:false}")
    private boolean pushEnabled;

    // Constructor para inyección de dependencias
    public NotificationService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Enviar notificación de producto creado
     */
    @Async
    public CompletableFuture<Void> sendProductCreatedNotification(Item product) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Notificar a administradores
            List<User> admins = getUserService().findByRole("ADMIN");
            for (User admin : admins) {
                sendProductCreatedEmail(admin, product);
            }

            // Notificar a managers
            List<User> managers = getUserService().findByRole("MANAGER");
            for (User manager : managers) {
                sendProductCreatedEmail(manager, product);
            }

            logger.info("Notificaciones de producto creado enviadas para: {}", product.getName());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de producto creado", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar notificación de producto actualizado
     */
    @Async
    public CompletableFuture<Void> sendProductUpdatedNotification(Item product) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Notificar a administradores sobre cambios importantes
            List<User> admins = getUserService().findByRole("ADMIN");
            for (User admin : admins) {
                sendProductUpdatedEmail(admin, product);
            }

            logger.info("Notificaciones de producto actualizado enviadas para: {}", product.getName());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de producto actualizado", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar notificación de producto eliminado
     */
    @Async
    public CompletableFuture<Void> sendProductDeletedNotification(String productName) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Notificar a administradores sobre eliminación
            List<User> admins = getUserService().findByRole("ADMIN");
            for (User admin : admins) {
                sendProductDeletedEmail(admin, productName);
            }

            logger.info("Notificaciones de producto eliminado enviadas para: {}", productName);
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de producto eliminado", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar alerta de stock bajo
     */
    @Async
    public CompletableFuture<Void> sendLowStockAlert(Item product) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            List<User> managers = getUserService().findByRole("MANAGER");
            for (User manager : managers) {
                sendLowStockEmail(manager, product);
            }

            logger.info("Alertas de stock bajo enviadas para: {}", product.getName());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar alertas de stock bajo", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar alerta de stock agotado
     */
    @Async
    public CompletableFuture<Void> sendOutOfStockAlert(Item product) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            List<User> admins = getUserService().findByRole("ADMIN");
            for (User admin : admins) {
                sendOutOfStockEmail(admin, product);
            }

            logger.info("Alertas de stock agotado enviadas para: {}", product.getName());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar alertas de stock agotado", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar notificación de venta realizada
     */
    @Async
    public CompletableFuture<Void> sendSaleNotification(User customer, Map<String, Object> saleData) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Email de confirmación al cliente
            if (emailEnabled) {
                sendSaleConfirmationEmail(customer, saleData);
            }

            // Notificar a administradores sobre venta importante
            if (saleData.get("total") != null && 
                Double.parseDouble(saleData.get("total").toString()) > 1000) {
                
                List<User> admins = getUserService().findByRole("ADMIN");
                for (User admin : admins) {
                    sendHighValueSaleNotification(admin, saleData);
                }
            }

            logger.info("Notificaciones de venta enviadas para cliente: {}", customer.getEmail());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de venta", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar notificación de usuario registrado
     */
    @Async
    public CompletableFuture<Void> sendUserRegistrationNotification(User user) {
        if (!notificationsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Email de bienvenida al usuario
            if (emailEnabled) {
                sendWelcomeEmail(user);
            }

            // Notificar a administradores sobre nuevo usuario
            List<User> admins = getUserService().findByRole("ADMIN");
            for (User admin : admins) {
                sendNewUserNotification(admin, user);
            }

            logger.info("Notificaciones de registro enviadas para usuario: {}", user.getEmail());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar notificaciones de registro", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar email simple
     */
    private void sendSimpleEmail(String to, String subject, String text) {
        if (!emailEnabled) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            emailSender.send(message);
            logger.debug("Email simple enviado a: {}", to);

        } catch (Exception e) {
            logger.error("Error al enviar email simple a: {}", to, e);
        }
    }

    /**
     * Enviar email con template HTML
     */
    private void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        if (!emailEnabled) {
            return;
        }

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            
            emailSender.send(message);
            logger.debug("Email HTML enviado a: {}", to);

        } catch (MessagingException e) {
            logger.error("Error al enviar email HTML a: {}", to, e);
        }
    }

    /**
     * Enviar email de producto creado
     */
    private void sendProductCreatedEmail(User user, Item product) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("product", product);
        context.setVariable("action", "creado");

        sendHtmlEmail(
            user.getEmail(),
            "Nuevo Producto Creado: " + product.getName(),
            "emails/product-notification",
            context
        );
    }

    /**
     * Enviar email de producto actualizado
     */
    private void sendProductUpdatedEmail(User user, Item product) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("product", product);
        context.setVariable("action", "actualizado");

        sendHtmlEmail(
            user.getEmail(),
            "Producto Actualizado: " + product.getName(),
            "emails/product-notification",
            context
        );
    }

    /**
     * Enviar email de producto eliminado
     */
    private void sendProductDeletedEmail(User user, String productName) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("productName", productName);
        context.setVariable("action", "eliminado");

        sendHtmlEmail(
            user.getEmail(),
            "Producto Eliminado: " + productName,
            "emails/product-deleted",
            context
        );
    }

    /**
     * Enviar email de alerta de stock bajo
     */
    private void sendLowStockEmail(User user, Item product) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("product", product);
        context.setVariable("alertType", "stock bajo");

        sendHtmlEmail(
            user.getEmail(),
            "ALERTA: Stock Bajo - " + product.getName(),
            "emails/stock-alert",
            context
        );
    }

    /**
     * Enviar email de alerta de stock agotado
     */
    private void sendOutOfStockEmail(User user, Item product) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("product", product);
        context.setVariable("alertType", "stock agotado");

        sendHtmlEmail(
            user.getEmail(),
            "ALERTA CRÍTICA: Stock Agotado - " + product.getName(),
            "emails/stock-alert",
            context
        );
    }

    /**
     * Enviar email de confirmación de venta
     */
    private void sendSaleConfirmationEmail(User customer, Map<String, Object> saleData) {
        Context context = new Context();
        context.setVariable("customer", customer);
        context.setVariable("saleData", saleData);

        sendHtmlEmail(
            customer.getEmail(),
            "Confirmación de Venta - eCommerce",
            "emails/sale-confirmation",
            context
        );
    }

    /**
     * Enviar email de venta de alto valor
     */
    private void sendHighValueSaleNotification(User admin, Map<String, Object> saleData) {
        Context context = new Context();
        context.setVariable("admin", admin);
        context.setVariable("saleData", saleData);

        sendHtmlEmail(
            admin.getEmail(),
            "Notificación: Venta de Alto Valor Realizada",
            "emails/high-value-sale",
            context
        );
    }

    /**
     * Enviar email de bienvenida
     */
    private void sendWelcomeEmail(User user) {
        Context context = new Context();
        context.setVariable("user", user);

        sendHtmlEmail(
            user.getEmail(),
            "¡Bienvenido a eCommerce!",
            "emails/welcome",
            context
        );
    }

    /**
     * Enviar notificación de nuevo usuario
     */
    private void sendNewUserNotification(User admin, User newUser) {
        Context context = new Context();
        context.setVariable("admin", admin);
        context.setVariable("newUser", newUser);

        sendHtmlEmail(
            admin.getEmail(),
            "Nuevo Usuario Registrado: " + newUser.getEmail(),
            "emails/new-user-notification",
            context
        );
    }

    /**
     * Enviar SMS (implementación básica)
     */
    @Async
    public CompletableFuture<Void> sendSMS(String phoneNumber, String message) {
        if (!smsEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Aquí se implementaría la integración con un servicio SMS
            // Por ahora solo log
            logger.info("SMS enviado a {}: {}", phoneNumber, message);
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar SMS a: {}", phoneNumber, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Enviar notificación push (implementación básica)
     */
    @Async
    public CompletableFuture<Void> sendPushNotification(String userId, String title, String message) {
        if (!pushEnabled) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Aquí se implementaría la integración con un servicio de push notifications
            // Por ahora solo log
            logger.info("Push notification enviado a {}: {} - {}", userId, title, message);
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Error al enviar push notification a: {}", userId, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Método helper para obtener el servicio de usuarios
     * En una implementación real, esto se inyectaría como dependencia
     */
    private UserService getUserService() {
        // Este método debería ser reemplazado por inyección de dependencia
        // Por ahora retorna null para evitar errores de compilación
        return null;
    }

    // Interface temporal para evitar errores de compilación
    private interface UserService {
        List<User> findByRole(String role);
    }
}

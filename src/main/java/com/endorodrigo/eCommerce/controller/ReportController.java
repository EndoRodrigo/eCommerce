package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.service.ReportService;
import com.endorodrigo.eComerce.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador para reportes y analytics del dashboard
 * Incluye métricas de ventas, inventario, clientes y exportación
 */
@Controller
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;
    private final DashboardService dashboardService;

    public ReportController(ReportService reportService, DashboardService dashboardService) {
        this.reportService = reportService;
        this.dashboardService = dashboardService;
    }

    /**
     * Dashboard principal de reportes
     */
    @GetMapping
    public String reportsDashboard(Model model) {
        try {
            // Métricas generales
            model.addAttribute("totalSales", dashboardService.getTotalVentas());
            model.addAttribute("totalProducts", dashboardService.getTotalProductos());
            model.addAttribute("totalCustomers", dashboardService.getTotalClientes());
            model.addAttribute("totalUsers", dashboardService.getTotalUsuarios());

            // Datos para gráficos
            model.addAttribute("salesChartData", reportService.getSalesChartData());
            model.addAttribute("productsChartData", reportService.getProductsChartData());
            model.addAttribute("customersChartData", reportService.getCustomersChartData());

            // Top productos y categorías
            model.addAttribute("topProducts", reportService.getTopProducts(10));
            model.addAttribute("topCategories", reportService.getTopCategories(5));

            return "reports/dashboard";

        } catch (Exception e) {
            logger.error("Error al cargar dashboard de reportes", e);
            model.addAttribute("error", "Error al cargar los reportes");
            return "reports/dashboard";
        }
    }

    /**
     * Reporte de ventas
     */
    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "daily") String period,
            Model model) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            Map<String, Object> salesData = reportService.getSalesReport(start, end, period);
            
            model.addAttribute("salesData", salesData);
            model.addAttribute("startDate", start);
            model.addAttribute("endDate", end);
            model.addAttribute("period", period);
            model.addAttribute("periods", reportService.getAvailablePeriods());

            return "reports/sales";

        } catch (Exception e) {
            logger.error("Error al generar reporte de ventas", e);
            model.addAttribute("error", "Error al generar el reporte de ventas");
            return "reports/sales";
        }
    }

    /**
     * Reporte de inventario
     */
    @GetMapping("/inventory")
    public String inventoryReport(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "all") String stockStatus,
            Model model) {
        
        try {
            Map<String, Object> inventoryData = reportService.getInventoryReport(category, stockStatus);
            
            model.addAttribute("inventoryData", inventoryData);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("selectedStockStatus", stockStatus);
            model.addAttribute("categories", reportService.getAllCategories());
            model.addAttribute("stockStatuses", reportService.getStockStatuses());

            return "reports/inventory";

        } catch (Exception e) {
            logger.error("Error al generar reporte de inventario", e);
            model.addAttribute("error", "Error al generar el reporte de inventario");
            return "reports/inventory";
        }
    }

    /**
     * Reporte de clientes
     */
    @GetMapping("/customers")
    public String customersReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "all") String customerType,
            Model model) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(90);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            Map<String, Object> customersData = reportService.getCustomersReport(start, end, customerType);
            
            model.addAttribute("customersData", customersData);
            model.addAttribute("startDate", start);
            model.addAttribute("endDate", end);
            model.addAttribute("selectedCustomerType", customerType);
            model.addAttribute("customerTypes", reportService.getCustomerTypes());

            return "reports/customers";

        } catch (Exception e) {
            logger.error("Error al generar reporte de clientes", e);
            model.addAttribute("error", "Error al generar el reporte de clientes");
            return "reports/customers";
        }
    }

    /**
     * Reporte de productos
     */
    @GetMapping("/products")
    public String productsReport(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "all") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            Model model) {
        
        try {
            Map<String, Object> productsData = reportService.getProductsReport(category, sortBy, sortDirection);
            
            model.addAttribute("productsData", productsData);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("selectedSortBy", sortBy);
            model.addAttribute("selectedSortDirection", sortDirection);
            model.addAttribute("categories", reportService.getAllCategories());
            model.addAttribute("sortOptions", reportService.getProductSortOptions());

            return "reports/products";

        } catch (Exception e) {
            logger.error("Error al generar reporte de productos", e);
            model.addAttribute("error", "Error al generar el reporte de productos");
            return "reports/products";
        }
    }

    /**
     * Reporte financiero
     */
    @GetMapping("/financial")
    public String financialReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "monthly") String period,
            Model model) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(12);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            Map<String, Object> financialData = reportService.getFinancialReport(start, end, period);
            
            model.addAttribute("financialData", financialData);
            model.addAttribute("startDate", start);
            model.addAttribute("endDate", end);
            model.addAttribute("selectedPeriod", period);
            model.addAttribute("periods", reportService.getFinancialPeriods());

            return "reports/financial";

        } catch (Exception e) {
            logger.error("Error al generar reporte financiero", e);
            model.addAttribute("error", "Error al generar el reporte financiero");
            return "reports/financial";
        }
    }

    /**
     * API REST para datos de gráficos
     */
    @GetMapping("/api/chart/sales")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSalesChartData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
            LocalDate end = endDate != null ? endDate : LocalDate.now();
            
            Map<String, Object> chartData = reportService.getSalesChartData(start, end);
            return ResponseEntity.ok(chartData);
            
        } catch (Exception e) {
            logger.error("Error al obtener datos del gráfico de ventas", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para datos de productos
     */
    @GetMapping("/api/chart/products")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductsChartData() {
        try {
            Map<String, Object> chartData = reportService.getProductsChartData();
            return ResponseEntity.ok(chartData);
            
        } catch (Exception e) {
            logger.error("Error al obtener datos del gráfico de productos", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para datos de clientes
     */
    @GetMapping("/api/chart/customers")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCustomersChartData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(90);
            LocalDate end = endDate != null ? endDate : LocalDate.now();
            
            Map<String, Object> chartData = reportService.getCustomersChartData(start, end);
            return ResponseEntity.ok(chartData);
            
        } catch (Exception e) {
            logger.error("Error al obtener datos del gráfico de clientes", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Exportar reporte de ventas
     */
    @GetMapping("/sales/export")
    public String exportSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(defaultValue = "daily") String period,
            RedirectAttributes redirectAttributes) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            String filename = reportService.exportSalesReport(start, end, period, format);
            
            redirectAttributes.addFlashAttribute("success", 
                "Reporte de ventas exportado exitosamente: " + filename);
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de ventas", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar el reporte");
        }
        
        return "redirect:/reports/sales";
    }

    /**
     * Exportar reporte de inventario
     */
    @GetMapping("/inventory/export")
    public String exportInventoryReport(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "pdf") String format,
            RedirectAttributes redirectAttributes) {
        
        try {
            String filename = reportService.exportInventoryReport(category, format);
            
            redirectAttributes.addFlashAttribute("success", 
                "Reporte de inventario exportado exitosamente: " + filename);
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de inventario", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar el reporte");
        }
        
        return "redirect:/reports/inventory";
    }

    /**
     * Exportar reporte de clientes
     */
    @GetMapping("/customers/export")
    public String exportCustomersReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(defaultValue = "all") String customerType,
            RedirectAttributes redirectAttributes) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(90);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            String filename = reportService.exportCustomersReport(start, end, customerType, format);
            
            redirectAttributes.addFlashAttribute("success", 
                "Reporte de clientes exportado exitosamente: " + filename);
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de clientes", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar el reporte");
        }
        
        return "redirect:/reports/customers";
    }

    /**
     * Exportar reporte de productos
     */
    @GetMapping("/products/export")
    public String exportProductsReport(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            RedirectAttributes redirectAttributes) {
        
        try {
            String filename = reportService.exportProductsReport(category, sortBy, sortDirection, format);
            
            redirectAttributes.addFlashAttribute("success", 
                "Reporte de productos exportado exitosamente: " + filename);
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de productos", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar el reporte");
        }
        
        return "redirect:/reports/products";
    }

    /**
     * Exportar reporte financiero
     */
    @GetMapping("/financial/export")
    public String exportFinancialReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(defaultValue = "monthly") String period,
            RedirectAttributes redirectAttributes) {
        
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(12);
            LocalDate end = endDate != null ? endDate : LocalDate.now();

            String filename = reportService.exportFinancialReport(start, end, period, format);
            
            redirectAttributes.addFlashAttribute("success", 
                "Reporte financiero exportado exitosamente: " + filename);
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte financiero", e);
            redirectAttributes.addFlashAttribute("error", "Error al exportar el reporte");
        }
        
        return "redirect:/reports/financial";
    }

    /**
     * Configuración de reportes
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String reportSettings(Model model) {
        try {
            model.addAttribute("reportSettings", reportService.getReportSettings());
            model.addAttribute("exportFormats", reportService.getAvailableExportFormats());
            model.addAttribute("emailTemplates", reportService.getEmailTemplates());
            
            return "reports/settings";
            
        } catch (Exception e) {
            logger.error("Error al cargar configuración de reportes", e);
            model.addAttribute("error", "Error al cargar la configuración");
            return "reports/settings";
        }
    }

    /**
     * Guardar configuración de reportes
     */
    @PostMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveReportSettings(
            @RequestParam Map<String, String> settings,
            RedirectAttributes redirectAttributes) {
        
        try {
            reportService.saveReportSettings(settings);
            redirectAttributes.addFlashAttribute("success", "Configuración guardada exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al guardar configuración de reportes", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar la configuración");
        }
        
        return "redirect:/reports/settings";
    }
}

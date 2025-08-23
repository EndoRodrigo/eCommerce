package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Item;
import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.model.Payment;
import com.endorodrigo.eComerce.repository.IProductRepository;
import com.endorodrigo.eComerce.repository.ICustomerRepository;
import com.endorodrigo.eComerce.repository.IPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes y analytics del dashboard
 * Incluye reportes de ventas, inventario, clientes y exportación
 */
@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    
    @Autowired
    private IProductRepository productRepository;
    
    @Autowired
    private ICustomerRepository customerRepository;
    
    @Autowired
    private IPayment paymentRepository;

    /**
     * Obtener datos para gráfico de ventas
     */
    public Map<String, Object> getSalesChartData() {
        return getSalesChartData(LocalDate.now().minusDays(30), LocalDate.now());
    }

    public Map<String, Object> getSalesChartData(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // Simular datos de ventas por día
            List<String> labels = new ArrayList<>();
            List<Double> data = new ArrayList<>();
            
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                labels.add(current.format(DateTimeFormatter.ofPattern("MMM dd")));
                
                // Simular ventas aleatorias entre 1000 y 10000
                double sales = Math.random() * 9000 + 1000;
                data.add(Math.round(sales * 100.0) / 100.0);
                
                current = current.plusDays(1);
            }
            
            chartData.put("labels", labels);
            chartData.put("data", data);
            chartData.put("totalSales", data.stream().mapToDouble(Double::doubleValue).sum());
            chartData.put("averageSales", data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            
        } catch (Exception e) {
            logger.error("Error al generar datos del gráfico de ventas", e);
            chartData.put("error", "Error al generar datos");
        }
        
        return chartData;
    }

    /**
     * Obtener datos para gráfico de productos
     */
    public Map<String, Object> getProductsChartData() {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // Simular datos de productos por categoría
            List<String> labels = Arrays.asList("Electrónicos", "Ropa", "Hogar", "Deportes", "Libros");
            List<Integer> data = Arrays.asList(150, 200, 120, 80, 95);
            
            chartData.put("labels", labels);
            chartData.put("data", data);
            chartData.put("totalProducts", data.stream().mapToInt(Integer::intValue).sum());
            
        } catch (Exception e) {
            logger.error("Error al generar datos del gráfico de productos", e);
            chartData.put("error", "Error al generar datos");
        }
        
        return chartData;
    }

    /**
     * Obtener datos para gráfico de clientes
     */
    public Map<String, Object> getCustomersChartData() {
        return getCustomersChartData(LocalDate.now().minusDays(90), LocalDate.now());
    }

    public Map<String, Object> getCustomersChartData(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // Simular datos de clientes por mes
            List<String> labels = new ArrayList<>();
            List<Integer> data = new ArrayList<>();
            
            LocalDate current = startDate.withDayOfMonth(1);
            while (!current.isAfter(endDate)) {
                labels.add(current.format(DateTimeFormatter.ofPattern("MMM yyyy")));
                
                // Simular nuevos clientes entre 10 y 50 por mes
                int customers = (int) (Math.random() * 40 + 10);
                data.add(customers);
                
                current = current.plusMonths(1);
            }
            
            chartData.put("labels", labels);
            chartData.put("data", data);
            chartData.put("totalCustomers", data.stream().mapToInt(Integer::intValue).sum());
            chartData.put("averageCustomers", data.stream().mapToInt(Integer::intValue).average().orElse(0.0));
            
        } catch (Exception e) {
            logger.error("Error al generar datos del gráfico de clientes", e);
            chartData.put("error", "Error al generar datos");
        }
        
        return chartData;
    }

    /**
     * Obtener top productos
     */
    public List<Map<String, Object>> getTopProducts(int limit) {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        
        try {
            // Simular top productos por ventas
            String[] productNames = {"iPhone 15 Pro", "MacBook Air", "AirPods Pro", "iPad Air", "Apple Watch"};
            double[] sales = {125000.0, 98000.0, 75000.0, 65000.0, 55000.0};
            
            for (int i = 0; i < Math.min(limit, productNames.length); i++) {
                Map<String, Object> product = new HashMap<>();
                product.put("name", productNames[i]);
                product.put("sales", sales[i]);
                product.put("rank", i + 1);
                topProducts.add(product);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener top productos", e);
        }
        
        return topProducts;
    }

    /**
     * Obtener top categorías
     */
    public List<Map<String, Object>> getTopCategories(int limit) {
        List<Map<String, Object>> topCategories = new ArrayList<>();
        
        try {
            // Simular top categorías por ventas
            String[] categoryNames = {"Electrónicos", "Ropa", "Hogar", "Deportes", "Libros"};
            double[] sales = {450000.0, 320000.0, 280000.0, 220000.0, 180000.0};
            
            for (int i = 0; i < Math.min(limit, categoryNames.length); i++) {
                Map<String, Object> category = new HashMap<>();
                category.put("name", categoryNames[i]);
                category.put("sales", sales[i]);
                category.put("rank", i + 1);
                topCategories.add(category);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener top categorías", e);
        }
        
        return topCategories;
    }

    /**
     * Generar reporte de ventas
     */
    public Map<String, Object> getSalesReport(LocalDate startDate, LocalDate endDate, String period) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // Simular datos de ventas
            double totalSales = Math.random() * 500000 + 100000;
            int totalOrders = (int) (Math.random() * 1000 + 200);
            double averageOrderValue = totalSales / totalOrders;
            
            // Simular ventas por período
            Map<String, Double> salesByPeriod = new HashMap<>();
            LocalDate current = startDate;
            
            while (!current.isAfter(endDate)) {
                String periodKey = getPeriodKey(current, period);
                double dailySales = Math.random() * 5000 + 1000;
                
                salesByPeriod.merge(periodKey, dailySales, Double::sum);
                current = current.plusDays(1);
            }
            
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("period", period);
            report.put("totalSales", Math.round(totalSales * 100.0) / 100.0);
            report.put("totalOrders", totalOrders);
            report.put("averageOrderValue", Math.round(averageOrderValue * 100.0) / 100.0);
            report.put("salesByPeriod", salesByPeriod);
            
        } catch (Exception e) {
            logger.error("Error al generar reporte de ventas", e);
            report.put("error", "Error al generar reporte");
        }
        
        return report;
    }

    /**
     * Generar reporte de inventario
     */
    public Map<String, Object> getInventoryReport(String category, String stockStatus) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // Simular datos de inventario
            int totalProducts = (int) (Math.random() * 500 + 200);
            int lowStockProducts = (int) (totalProducts * 0.15);
            int outOfStockProducts = (int) (totalProducts * 0.05);
            double totalValue = totalProducts * (Math.random() * 100 + 50);
            
            // Simular productos por categoría
            Map<String, Integer> productsByCategory = new HashMap<>();
            String[] categories = {"Electrónicos", "Ropa", "Hogar", "Deportes", "Libros"};
            
            for (String cat : categories) {
                if (category == null || "all".equals(category) || cat.equals(category)) {
                    productsByCategory.put(cat, (int) (Math.random() * 100 + 20));
                }
            }
            
            report.put("category", category);
            report.put("stockStatus", stockStatus);
            report.put("totalProducts", totalProducts);
            report.put("lowStockProducts", lowStockProducts);
            report.put("outOfStockProducts", outOfStockProducts);
            report.put("totalValue", Math.round(totalValue * 100.0) / 100.0);
            report.put("productsByCategory", productsByCategory);
            
        } catch (Exception e) {
            logger.error("Error al generar reporte de inventario", e);
            report.put("error", "Error al generar reporte");
        }
        
        return report;
    }

    /**
     * Generar reporte de clientes
     */
    public Map<String, Object> getCustomersReport(LocalDate startDate, LocalDate endDate, String customerType) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // Simular datos de clientes
            int totalCustomers = (int) (Math.random() * 2000 + 500);
            int newCustomers = (int) (Math.random() * 200 + 50);
            int activeCustomers = (int) (totalCustomers * 0.7);
            double averageCustomerValue = Math.random() * 500 + 100;
            
            // Simular clientes por tipo
            Map<String, Integer> customersByType = new HashMap<>();
            customersByType.put("Regular", (int) (totalCustomers * 0.6));
            customersByType.put("Premium", (int) (totalCustomers * 0.3));
            customersByType.put("VIP", (int) (totalCustomers * 0.1));
            
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("customerType", customerType);
            report.put("totalCustomers", totalCustomers);
            report.put("newCustomers", newCustomers);
            report.put("activeCustomers", activeCustomers);
            report.put("averageCustomerValue", Math.round(averageCustomerValue * 100.0) / 100.0);
            report.put("customersByType", customersByType);
            
        } catch (Exception e) {
            logger.error("Error al generar reporte de clientes", e);
            report.put("error", "Error al generar reporte");
        }
        
        return report;
    }

    /**
     * Generar reporte de productos
     */
    public Map<String, Object> getProductsReport(String category, String sortBy, String sortDirection) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // Simular datos de productos
            int totalProducts = (int) (Math.random() * 1000 + 300);
            double totalValue = totalProducts * (Math.random() * 200 + 100);
            int activeProducts = (int) (totalProducts * 0.85);
            
            // Simular productos por categoría
            Map<String, Integer> productsByCategory = new HashMap<>();
            String[] categories = {"Electrónicos", "Ropa", "Hogar", "Deportes", "Libros"};
            
            for (String cat : categories) {
                if (category == null || "all".equals(category) || cat.equals(category)) {
                    productsByCategory.put(cat, (int) (Math.random() * 200 + 50));
                }
            }
            
            report.put("category", category);
            report.put("sortBy", sortBy);
            report.put("sortDirection", sortDirection);
            report.put("totalProducts", totalProducts);
            report.put("totalValue", Math.round(totalValue * 100.0) / 100.0);
            report.put("activeProducts", activeProducts);
            report.put("productsByCategory", productsByCategory);
            
        } catch (Exception e) {
            logger.error("Error al generar reporte de productos", e);
            report.put("error", "Error al generar reporte");
        }
        
        return report;
    }

    /**
     * Generar reporte financiero
     */
    public Map<String, Object> getFinancialReport(LocalDate startDate, LocalDate endDate, String period) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // Simular datos financieros
            double totalRevenue = Math.random() * 1000000 + 200000;
            double totalCosts = totalRevenue * (Math.random() * 0.4 + 0.3);
            double grossProfit = totalRevenue - totalCosts;
            double netProfit = grossProfit * (Math.random() * 0.3 + 0.6);
            
            // Simular ingresos por período
            Map<String, Double> revenueByPeriod = new HashMap<>();
            LocalDate current = startDate;
            
            while (!current.isAfter(endDate)) {
                String periodKey = getPeriodKey(current, period);
                double monthlyRevenue = Math.random() * 100000 + 20000;
                
                revenueByPeriod.merge(periodKey, monthlyRevenue, Double::sum);
                current = current.plusMonths(1);
            }
            
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("period", period);
            report.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
            report.put("totalCosts", Math.round(totalCosts * 100.0) / 100.0);
            report.put("grossProfit", Math.round(grossProfit * 100.0) / 100.0);
            report.put("netProfit", Math.round(netProfit * 100.0) / 100.0);
            report.put("profitMargin", Math.round((netProfit / totalRevenue) * 10000.0) / 100.0);
            report.put("revenueByPeriod", revenueByPeriod);
            
        } catch (Exception e) {
            logger.error("Error al generar reporte financiero", e);
            report.put("error", "Error al generar reporte");
        }
        
        return report;
    }

    /**
     * Obtener períodos disponibles para reportes
     */
    public List<String> getAvailablePeriods() {
        return Arrays.asList("daily", "weekly", "monthly", "quarterly", "yearly");
    }

    /**
     * Obtener categorías disponibles
     */
    public List<String> getAllCategories() {
        return Arrays.asList("all", "Electrónicos", "Ropa", "Hogar", "Deportes", "Libros");
    }

    /**
     * Obtener estados de stock disponibles
     */
    public List<String> getStockStatuses() {
        return Arrays.asList("all", "in_stock", "low_stock", "out_of_stock");
    }

    /**
     * Obtener tipos de cliente disponibles
     */
    public List<String> getCustomerTypes() {
        return Arrays.asList("all", "new", "returning", "vip", "inactive");
    }

    /**
     * Obtener opciones de ordenamiento para productos
     */
    public List<String> getProductSortOptions() {
        return Arrays.asList("name", "price", "quantity", "created_date", "sales_count");
    }

    /**
     * Obtener períodos financieros disponibles
     */
    public List<String> getFinancialPeriods() {
        return Arrays.asList("monthly", "quarterly", "yearly");
    }

    /**
     * Obtener formatos de exportación disponibles
     */
    public List<String> getAvailableExportFormats() {
        return Arrays.asList("pdf", "excel", "csv", "json");
    }

    /**
     * Obtener configuración de reportes
     */
    public Map<String, Object> getReportSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("defaultPeriod", "monthly");
        settings.put("defaultFormat", "pdf");
        settings.put("autoExport", false);
        settings.put("emailNotifications", true);
        return settings;
    }

    /**
     * Obtener plantillas de email disponibles
     */
    public List<String> getEmailTemplates() {
        return Arrays.asList("daily_report", "weekly_summary", "monthly_analysis", "custom");
    }

    /**
     * Guardar configuración de reportes
     */
    public void saveReportSettings(Map<String, String> settings) {
        // Implementación básica - guardar en memoria o base de datos
        logger.info("Configuración de reportes guardada: {}", settings);
    }

    /**
     * Exportar reporte de ventas
     */
    public String exportSalesReport(LocalDate startDate, LocalDate endDate, String period, String format) {
        try {
            String filename = String.format("sales_report_%s_%s_%s.%s", 
                startDate, endDate, period, format);
            
            // Simular exportación
            String content = generateSalesReportContent(startDate, endDate, period);
            exportToFile(filename, content, format);
            
            return filename;
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de ventas", e);
            throw new RuntimeException("Error al exportar reporte", e);
        }
    }

    /**
     * Exportar reporte de inventario
     */
    public String exportInventoryReport(String category, String format) {
        try {
            String filename = String.format("inventory_report_%s_%s.%s", 
                category != null ? category : "all", LocalDate.now(), format);
            
            // Simular exportación
            String content = generateInventoryReportContent(category);
            exportToFile(filename, content, format);
            
            return filename;
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de inventario", e);
            throw new RuntimeException("Error al exportar reporte", e);
        }
    }

    /**
     * Exportar reporte de clientes
     */
    public String exportCustomersReport(LocalDate startDate, LocalDate endDate, String customerType, String format) {
        try {
            String filename = String.format("customers_report_%s_%s_%s.%s", 
                startDate, endDate, customerType, format);
            
            // Simular exportación
            String content = generateCustomersReportContent(startDate, endDate, customerType);
            exportToFile(filename, content, format);
            
            return filename;
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de clientes", e);
            throw new RuntimeException("Error al exportar reporte", e);
        }
    }

    /**
     * Exportar reporte de productos
     */
    public String exportProductsReport(String category, String sortBy, String sortDirection, String format) {
        try {
            String filename = String.format("products_report_%s_%s_%s.%s", 
                category != null ? category : "all", sortBy, LocalDate.now(), format);
            
            // Simular exportación
            String content = generateProductsReportContent(category, sortBy, sortDirection);
            exportToFile(filename, content, format);
            
            return filename;
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte de productos", e);
            throw new RuntimeException("Error al exportar reporte", e);
        }
    }

    /**
     * Exportar reporte financiero
     */
    public String exportFinancialReport(LocalDate startDate, LocalDate endDate, String period, String format) {
        try {
            String filename = String.format("financial_report_%s_%s_%s.%s", 
                startDate, endDate, period, format);
            
            // Simular exportación
            String content = generateFinancialReportContent(startDate, endDate, period);
            exportToFile(filename, content, format);
            
            return filename;
            
        } catch (Exception e) {
            logger.error("Error al exportar reporte financiero", e);
            throw new RuntimeException("Error al exportar reporte", e);
        }
    }

    /**
     * Métodos auxiliares privados
     */
    private String getPeriodKey(LocalDate date, String period) {
        switch (period) {
            case "daily":
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            case "weekly":
                return "Week " + date.get(DateTimeFormatter.ISO.WEEK_BASED_YEAR);
            case "monthly":
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            case "quarterly":
                return "Q" + ((date.getMonthValue() - 1) / 3 + 1) + " " + date.getYear();
            case "yearly":
                return String.valueOf(date.getYear());
            default:
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    private String generateSalesReportContent(LocalDate startDate, LocalDate endDate, String period) {
        StringBuilder content = new StringBuilder();
        content.append("REPORTE DE VENTAS\n");
        content.append("================\n");
        content.append("Período: ").append(startDate).append(" a ").append(endDate).append("\n");
        content.append("Frecuencia: ").append(period).append("\n");
        content.append("Generado: ").append(LocalDateTime.now()).append("\n\n");
        
        // Simular contenido del reporte
        content.append("Resumen de Ventas:\n");
        content.append("- Total de ventas: $").append(Math.random() * 500000 + 100000).append("\n");
        content.append("- Total de pedidos: ").append((int)(Math.random() * 1000 + 200)).append("\n");
        content.append("- Valor promedio por pedido: $").append(Math.random() * 500 + 100).append("\n");
        
        return content.toString();
    }

    private String generateInventoryReportContent(String category) {
        StringBuilder content = new StringBuilder();
        content.append("REPORTE DE INVENTARIO\n");
        content.append("=====================\n");
        content.append("Categoría: ").append(category != null ? category : "Todas").append("\n");
        content.append("Generado: ").append(LocalDateTime.now()).append("\n\n");
        
        // Simular contenido del reporte
        content.append("Resumen de Inventario:\n");
        content.append("- Total de productos: ").append((int)(Math.random() * 500 + 200)).append("\n");
        content.append("- Productos con stock bajo: ").append((int)(Math.random() * 100 + 20)).append("\n");
        content.append("- Productos sin stock: ").append((int)(Math.random() * 50 + 10)).append("\n");
        
        return content.toString();
    }

    private String generateCustomersReportContent(LocalDate startDate, LocalDate endDate, String customerType) {
        StringBuilder content = new StringBuilder();
        content.append("REPORTE DE CLIENTES\n");
        content.append("==================\n");
        content.append("Período: ").append(startDate).append(" a ").append(endDate).append("\n");
        content.append("Tipo: ").append(customerType).append("\n");
        content.append("Generado: ").append(LocalDateTime.now()).append("\n\n");
        
        // Simular contenido del reporte
        content.append("Resumen de Clientes:\n");
        content.append("- Total de clientes: ").append((int)(Math.random() * 2000 + 500)).append("\n");
        content.append("- Nuevos clientes: ").append((int)(Math.random() * 200 + 50)).append("\n");
        content.append("- Clientes activos: ").append((int)(Math.random() * 1500 + 300)).append("\n");
        
        return content.toString();
    }

    private String generateProductsReportContent(String category, String sortBy, String sortDirection) {
        StringBuilder content = new StringBuilder();
        content.append("REPORTE DE PRODUCTOS\n");
        content.append("===================\n");
        content.append("Categoría: ").append(category != null ? category : "Todas").append("\n");
        content.append("Ordenado por: ").append(sortBy).append(" ").append(sortDirection).append("\n");
        content.append("Generado: ").append(LocalDateTime.now()).append("\n\n");
        
        // Simular contenido del reporte
        content.append("Resumen de Productos:\n");
        content.append("- Total de productos: ").append((int)(Math.random() * 1000 + 300)).append("\n");
        content.append("- Productos activos: ").append((int)(Math.random() * 800 + 200)).append("\n");
        content.append("- Valor total del inventario: $").append(Math.random() * 200000 + 50000).append("\n");
        
        return content.toString();
    }

    private String generateFinancialReportContent(LocalDate startDate, LocalDate endDate, String period) {
        StringBuilder content = new StringBuilder();
        content.append("REPORTE FINANCIERO\n");
        content.append("==================\n");
        content.append("Período: ").append(startDate).append(" a ").append(endDate).append("\n");
        content.append("Frecuencia: ").append(period).append("\n");
        content.append("Generado: ").append(LocalDateTime.now()).append("\n\n");
        
        // Simular contenido del reporte
        content.append("Resumen Financiero:\n");
        content.append("- Ingresos totales: $").append(Math.random() * 1000000 + 200000).append("\n");
        content.append("- Costos totales: $").append(Math.random() * 500000 + 100000).append("\n");
        content.append("- Beneficio bruto: $").append(Math.random() * 500000 + 100000).append("\n");
        content.append("- Beneficio neto: $").append(Math.random() * 300000 + 50000).append("\n");
        
        return content.toString();
    }

    private void exportToFile(String filename, String content, String format) throws IOException {
        // Crear directorio de exportación si no existe
        Path exportDir = Paths.get("exports");
        if (!Files.exists(exportDir)) {
            Files.createDirectories(exportDir);
        }
        
        // Escribir archivo
        Path filePath = exportDir.resolve(filename);
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(content);
        }
        
        logger.info("Reporte exportado exitosamente: {}", filePath);
    }
}

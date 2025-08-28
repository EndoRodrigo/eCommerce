package com.endorodrigo.eCommerce.service;

import com.endorodrigo.eCommerce.model.Cart;
import com.endorodrigo.eCommerce.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final IProductRepository productRepository;
    private final ICustomerRepository customerRepository;
    private final IUserRepository userRepository;
    private final IPosRepository posRepository;
    private final IPayment paymentRepository;

    public DashboardService(IProductRepository productRepository, ICustomerRepository customerRepository, 
                          IUserRepository userRepository, IPosRepository posRepository, IPayment paymentRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.posRepository = posRepository;
        this.paymentRepository = paymentRepository;
    }

    // Métodos para tarjetas de información
    public long getTotalVentas() {
        return paymentRepository.count();
    }

    public long getTotalProductos() {
        return productRepository.count();
    }

    public long getTotalClientes() {
        return customerRepository.count();
    }

    public long getTotalUsuarios() {
        return userRepository.count();
    }

    public long getTotalCarritos() {
        return posRepository.count();
    }

    public List<Cart> getCart() {
        return (List<Cart>) posRepository.findAll();
    }

    // Nuevos métodos para gráficas de seguimiento
    
    /**
     * Obtiene datos para el gráfico de ventas mensuales
     */
    public Map<String, Object> getVentasMensuales() {
        Map<String, Object> chartData = new HashMap<>();
        
        // Generar datos de los últimos 6 meses
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        
        for (int i = 5; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            labels.add(date.format(formatter));
            
            // Simular datos de ventas (en producción esto vendría de la base de datos)
            long ventas = new Random().nextInt(100) + 20; // Entre 20 y 120 ventas
            data.add(ventas);
        }
        
        chartData.put("labels", labels);
        chartData.put("data", data);
        chartData.put("backgroundColor", "rgba(78, 115, 223, 0.1)");
        chartData.put("borderColor", "rgba(78, 115, 223, 1)");
        
        return chartData;
    }

    /**
     * Obtiene datos para el gráfico de productos por categoría
     */
    public Map<String, Object> getProductosPorCategoria() {
        Map<String, Object> chartData = new HashMap<>();
        
        // Simular datos de productos por categoría
        List<String> labels = Arrays.asList("Electrónicos", "Ropa", "Hogar", "Deportes", "Otros");
        List<Long> data = Arrays.asList(
            new Random().nextInt(50) + 20L,  // Electrónicos
            new Random().nextInt(40) + 15L,  // Ropa
            new Random().nextInt(30) + 10L,  // Hogar
            new Random().nextInt(25) + 8L,   // Deportes
            new Random().nextInt(20) + 5L    // Otros
        );
        
        List<String> colors = Arrays.asList(
            "#4e73df", "#1cc88a", "#36b9cc", "#f6c23e", "#e74a3b"
        );
        
        chartData.put("labels", labels);
        chartData.put("data", data);
        chartData.put("colors", colors);
        
        return chartData;
    }

    /**
     * Obtiene estadísticas de rendimiento del sistema
     */
    public Map<String, Object> getEstadisticasRendimiento() {
        Map<String, Object> stats = new HashMap<>();
        
        // Calcular porcentajes de completitud
        long totalProductos = getTotalProductos();
        long totalClientes = getTotalClientes();
        long totalUsuarios = getTotalUsuarios();
        long totalVentas = getTotalVentas();
        
        // Simular métricas de rendimiento
        stats.put("productosCompletitud", Math.min(100, (totalProductos * 100) / 200)); // Meta: 200 productos
        stats.put("clientesCompletitud", Math.min(100, (totalClientes * 100) / 150));   // Meta: 150 clientes
        stats.put("usuariosCompletitud", Math.min(100, (totalUsuarios * 100) / 50));    // Meta: 50 usuarios
        stats.put("ventasCompletitud", Math.min(100, (totalVentas * 100) / 100));       // Meta: 100 ventas
        
        return stats;
    }

    /**
     * Obtiene datos para el gráfico de tendencias de usuarios
     */
    public Map<String, Object> getTendenciasUsuarios() {
        Map<String, Object> chartData = new HashMap<>();
        
        // Generar datos de los últimos 7 días
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            labels.add(date.format(formatter));
            
            // Simular datos de usuarios activos
            long usuarios = new Random().nextInt(20) + 5; // Entre 5 y 25 usuarios
            data.add(usuarios);
        }
        
        chartData.put("labels", labels);
        chartData.put("data", data);
        chartData.put("backgroundColor", "rgba(28, 200, 138, 0.1)");
        chartData.put("borderColor", "rgba(28, 200, 138, 1)");
        
        return chartData;
    }
}

package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.Customer;
import com.endorodrigo.eComerce.repository.ICustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de clientes.
 * Proporciona métodos CRUD para la entidad Customer.
 */

/**
 * Servicio para la gestión de clientes.
 * Proporciona métodos CRUD para la entidad Customer.
 */
@Service
public class CustomerService implements IGenericService<Customer, Integer> {

    private final ICustomerRepository customerRepository;

    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    /**
     * Busca un cliente por su ID.
     * @param id Identificador del cliente
     * @return Cliente encontrado o null si no existe
     */
    public Customer findId(Integer id) {
        if (id == null) return null;
        return customerRepository.findById(id).orElse(null);
    }

    public Customer findIdIntification(String s) {
        return customerRepository.findByIdentification(s);
    }

    @Override
    /**
     * Obtiene todos los clientes registrados.
     * @return Lista de clientes
     */
    public List<Customer> getAll() {

        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    /**
     * Inserta o actualiza un cliente en la base de datos.
     * @param entity Cliente a guardar
     * @return Cliente guardado
     */
    public Customer insert(Customer entity) {
        if (entity == null) {
            System.out.println("[CustomerService] Entidad nula, no se inserta");
            return null;
        }
        System.out.println("[CustomerService] Insertando cliente: " + entity);
        Customer saved = customerRepository.save(entity);
        System.out.println("[CustomerService] Cliente guardado: " + saved);
        return saved;
    }

    @Override
    /**
     * Elimina un cliente de la base de datos si no es nulo.
     * @param entity Cliente a eliminar
     */
    public void delete(Customer entity) {
        if (entity != null) customerRepository.delete(entity);
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param entity Cliente con la información actualizada
     * @return Cliente actualizado o null si el cliente no existe
     */
    public Customer update(Customer entity) {
        return customerRepository.save(entity);
    }
}

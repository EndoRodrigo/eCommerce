package com.endorodrigo.eCommerce.service;


/**
 * Servicio para la gestión de pagos.
 * Proporciona métodos CRUD para la entidad Payment.
 */
@Service
public class PaymentService implements IGenericService<Payment, Integer> {

    /**
     * Repositorio para operaciones CRUD sobre la entidad Payment.
     */
    private IPayment paymentRepository;

    /**
     * Constructor que inyecta el repositorio de pagos.
     * @param paymentRepository Repositorio de pagos
     */
    public PaymentService(IPayment paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Busca un pago por su ID.
     * Actualmente no implementado, retorna null.
     * @param integer Identificador del pago
     * @return El pago si existe, null si no se encuentra
     */
    @Override
    public Payment findId(Integer integer) {
        return null;
    }

    /**
     * Obtiene todos los pagos registrados en el sistema.
     * Actualmente no implementado, retorna lista vacía.
     * @return Lista de pagos
     */
    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    /**
     * Inserta o actualiza un pago en la base de datos.
     * @param entity Pago a guardar
     * @return Pago guardado
     */
    @Override
    public Payment insert(Payment entity) {
        return paymentRepository.save(entity);
    }

    /**
     * Inserta o actualiza un pago en la base de datos.
     * @param entity Pago a guardar
     * @return Pago guardado
     */
    @Override
    public Payment update(Payment entity) {
        return paymentRepository.save(entity);
    }

    /**
     * Elimina un pago de la base de datos.
     * Actualmente no implementado.
     * @param entity Pago a eliminar
     */
    @Override
    public void delete(Payment entity) {

    }
}

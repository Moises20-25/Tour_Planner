package com.planes_tours.Tour_Planner.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.planes_tours.Tour_Planner.dto.ResultadoResponse;
import com.planes_tours.Tour_Planner.model.DetalleReserva;
import com.planes_tours.Tour_Planner.model.Reserva;
import com.planes_tours.Tour_Planner.model.ReservaForm;
import com.planes_tours.Tour_Planner.repository.DetalleReservaRepository;
import com.planes_tours.Tour_Planner.repository.PaqueteRepository;
import com.planes_tours.Tour_Planner.repository.ReservaRepository;
import com.planes_tours.Tour_Planner.repository.ServicioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final DetalleReservaRepository detalleReservaRepository;
    private final PaqueteRepository paqueteRepository;
    private final ServicioRepository servicioRepository;

    @Transactional
    public ResultadoResponse create(Integer idPaquete, ReservaForm reservaForm, Integer idUsuario) {
        try {
            var paquete = paqueteRepository.findById(idPaquete).orElse(null);
            if (paquete == null) {
                return new ResultadoResponse(false, "No se encontró el paquete");
            }

            var lstServicios = servicioRepository.findByPaquete(idPaquete);
            if (lstServicios.isEmpty()) {
                return new ResultadoResponse(false, "El paquete no tiene servicios asociados");
            }
                        
            var viajeros = reservaForm.getViajeros();

            // Validar ell stock antes de grabar
            for (var servicio : lstServicios) {
                if (servicio.getStock() < viajeros) {
                    var mensaje = String.format("No hay stock suficiente en %s", servicio.getNombre());
                    return new ResultadoResponse(false, mensaje);
                }
            }

            // Registrr la reserva 
            var reserva = new Reserva();
            reserva.setIdUsuario(idUsuario);
            reserva.setFechaReserva(LocalDateTime.now());
            reserva.setFechaInicio(reservaForm.getFechaViaje());
            reserva.setFechaFin(reservaForm.getFechaViaje().plusDays(paquete.getDias()));
            reserva.setEstado("pendiente");
            reserva.setPasajeros(viajeros);
            reserva.setMontoTotal(paquete.getPrecio().multiply(BigDecimal.valueOf(viajeros)));
            reserva.setCodigoReserva("RES-" + System.currentTimeMillis());
            var registrado = reservaRepository.save(reserva);

            // Registrar el detalle y  descontar el stock
            for (var servicio : lstServicios) {
                var subtotal = servicio.getPrecio().multiply(BigDecimal.valueOf(viajeros));
                detalleReservaRepository.save(new DetalleReserva(null, registrado.getIdReserva(),
                        servicio.getIdServicio(), viajeros, servicio.getPrecio(), subtotal));

                servicio.setStock(servicio.getStock() - viajeros);
                servicioRepository.save(servicio);
            }

            var mensaje = String.format("Reserva %s registrada", registrado.getCodigoReserva());

            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }
}
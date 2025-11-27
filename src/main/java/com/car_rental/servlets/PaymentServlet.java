package com.car_rental.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Payment;
import com.car_rental.repository.PaymentRepository;
import com.car_rental.serializer.JsonDataSerializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/payments", "/payments/*"}, loadOnStartup = 1)
public class PaymentServlet extends BaseServlet {

    private JsonDataSerializer serializer;
    private PaymentRepository paymentRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Initializing PaymentServlet...");
        serializer = new JsonDataSerializer();
        paymentRepository = (PaymentRepository) getServletContext().getAttribute("paymentRepository");

        if (paymentRepository == null) {
            logger.error("PaymentRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }
        logger.info("PaymentServlet initialized successfully.");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        response.setContentType(CONTENT_TYPE_JSON);

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetWithFilters(req, response);
            } else {
                String identity = decodePathParam(pathInfo.substring(1));
                handleGetByIdentity(identity, response);
            }
        } catch (DataSerializationException e) {
            logger.error("Serialization error in doGet", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        try {
            String requestBody = getRequestBody(req);
            Payment payment = (Payment) serializer.fromString(requestBody, Payment.class);

            boolean res = paymentRepository.add(payment);
            if (!res) {
                logger.warn("Payment already exists: {}", payment.getId());
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST,
                        "Payment already exists: " + payment.getId());
                return;
            }
            logger.info("Added new payment: {}", payment.getId());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(payment));
        } catch (InvalidDataException e) {
            logger.warn("Invalid data in doPost", e);
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DataSerializationException e) {
            logger.error("Serialization error in doPost", e);
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE_JSON);

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Payment ID is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));

        try {
            String requestBody = getRequestBody(req);
            Payment updatedPayment = (Payment) serializer.fromString(requestBody, Payment.class);

            boolean updated = paymentRepository.update(updatedPayment);

            if (!updated) {
                logger.warn("Payment not found: {}", identity);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Payment not found: " + identity);
                return;
            }

            logger.info("Payment updated: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedPayment));

        } catch (DataSerializationException e) {
            logger.error("Serialization error in doPut", e);
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Payment ID is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));
        boolean removed = paymentRepository.removeByIdentity(identity);

        if (!removed) {
            logger.warn("Payment not found for deletion: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Payment not found: " + identity);
            return;
        }

        logger.info("Payment deleted: {}", identity);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetWithFilters(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DataSerializationException {

        String id = req.getParameter("id");
        String rentalId = req.getParameter("rental_id");

        List<Payment> payments;

        if (id != null && !id.isBlank()) {
            payments = paymentRepository.findById(id).map(p -> List.of(p)).orElse(List.of());
            logger.info("Filter by ID '{}': {} payments", id, payments.size());
        } else if (rentalId != null && !rentalId.isBlank()) {
            payments = paymentRepository.findByRentalId(rentalId);
            logger.info("Filter by rental ID '{}': {} payments", rentalId, payments.size());
        } else {
            payments = paymentRepository.getAll();
            logger.info("No filters applied: {} payments", payments.size());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(payments));
    }

    private void handleGetByIdentity(String identity, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Payment> payment = paymentRepository.findByIdentity(identity);

        if (payment.isPresent()) {
            logger.info("Found payment: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(payment.get()));
        } else {
            logger.warn("Payment not found: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Payment not found: " + identity);
        }
    }

    private String decodePathParam(String param) {
        try {
            return java.net.URLDecoder.decode(param, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Failed to decode path param: {}", param);
            return param;
        }
    }
}

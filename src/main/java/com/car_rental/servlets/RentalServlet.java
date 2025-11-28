package com.car_rental.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Rental;
import com.car_rental.repository.RentalRepository;
import com.car_rental.serializer.JsonDataSerializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RentalServlet", urlPatterns = {"/rentals", "/rentals/*"}, loadOnStartup = 1)
public class RentalServlet extends BaseServlet {

    private JsonDataSerializer serializer;
    private RentalRepository rentalRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Initializing RentalServlet...");
        serializer = new JsonDataSerializer();
        rentalRepository = (RentalRepository) getServletContext().getAttribute("rentalRepository");

        if (rentalRepository == null) {
            logger.error("RentalRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }
        logger.info("RentalServlet initialized successfully.");
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
            Rental rental = (Rental) serializer.fromString(requestBody, Rental.class);

            boolean res = rentalRepository.add(rental);
            if (!res) {
                logger.warn("Rental already exists: {}", rental.getId());
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST,
                        "Rental already exists: " + rental.getId());
                return;
            }
            logger.info("Added new rental: {}", rental.getId());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(rental));
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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Rental ID is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));

        try {
            String requestBody = getRequestBody(req);
            Rental updatedRental = (Rental) serializer.fromString(requestBody, Rental.class);

            boolean updated = rentalRepository.update(updatedRental);

            if (!updated) {
                logger.warn("Rental not found: {}", identity);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Rental not found: " + identity);
                return;
            }

            logger.info("Rental updated: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedRental));

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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Rental ID is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));
        boolean removed = rentalRepository.removeByIdentity(identity);

        if (!removed) {
            logger.warn("Rental not found for deletion: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Rental not found: " + identity);
            return;
        }

        logger.info("Rental deleted: {}", identity);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetWithFilters(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DataSerializationException {

        String id = req.getParameter("id");
        String carLicensePlate = req.getParameter("car_license_plate");
        String customerDriverLicense = req.getParameter("customer_driver_license");

        List<Rental> rentals;

        if (id != null && !id.isBlank()) {
            rentals = rentalRepository.findById(id).map(r -> List.of(r)).orElse(List.of());
            logger.info("Filter by ID '{}': {} rentals", id, rentals.size());
        } else if (carLicensePlate != null && !carLicensePlate.isBlank()) {
            rentals = rentalRepository.findByCarLicensePlate(carLicensePlate);
            logger.info("Filter by car license plate '{}': {} rentals", carLicensePlate, rentals.size());
        } else if (customerDriverLicense != null && !customerDriverLicense.isBlank()) {
            rentals = rentalRepository.findByCustomerDriverLicense(customerDriverLicense);
            logger.info("Filter by customer driver license '{}': {} rentals", customerDriverLicense, rentals.size());
        } else {
            rentals = rentalRepository.getAll();
            logger.info("No filters applied: {} rentals", rentals.size());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(rentals));
    }

    private void handleGetByIdentity(String identity, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Rental> rental = rentalRepository.findByIdentity(identity);

        if (rental.isPresent()) {
            logger.info("Found rental: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(rental.get()));
        } else {
            logger.warn("Rental not found: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Rental not found: " + identity);
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

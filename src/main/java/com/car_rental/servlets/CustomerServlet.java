package com.car_rental.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Customer;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.serializer.JsonDataSerializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/customers", "/customers/*"}, loadOnStartup = 1)
public class CustomerServlet extends BaseServlet {

    private JsonDataSerializer serializer;
    private CustomerRepository customerRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Initializing CustomerServlet...");
        serializer = new JsonDataSerializer();
        customerRepository = (CustomerRepository) getServletContext().getAttribute("customerRepository");

        if (customerRepository == null) {
            logger.error("CustomerRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }
        logger.info("CustomerServlet initialized successfully.");
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
            Customer customer = (Customer) serializer.fromString(requestBody, Customer.class);

            boolean res = customerRepository.add(customer);
            if (!res) {
                logger.warn("Customer already exists: {}", customer.driverLicense());
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST,
                        "Customer already exists: " + customer.driverLicense());
                return;
            }
            logger.info("Added new customer: {}", customer.driverLicense());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(customer));
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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer driver license is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));

        try {
            String requestBody = getRequestBody(req);
            Customer updatedCustomer = (Customer) serializer.fromString(requestBody, Customer.class);

            boolean updated = customerRepository.update(updatedCustomer);

            if (!updated) {
                logger.warn("Customer not found: {}", identity);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found: " + identity);
                return;
            }

            logger.info("Customer updated: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedCustomer));

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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer driver license is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));
        boolean removed = customerRepository.removeByIdentity(identity);

        if (!removed) {
            logger.warn("Customer not found for deletion: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found: " + identity);
            return;
        }

        logger.info("Customer deleted: {}", identity);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetWithFilters(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DataSerializationException {

        String driverLicense = req.getParameter("driver_license");
        String birthDate = req.getParameter("birth_date");

        List<Customer> customers;

        if (driverLicense != null && !driverLicense.isBlank()) {
            customers = customerRepository.findByDriverLicense(driverLicense).map(c -> List.of(c)).orElse(List.of());
            logger.info("Filter by driver license '{}': {} customers", driverLicense, customers.size());
        } else if (birthDate != null && !birthDate.isBlank()) {
            customers = customerRepository.findByBirthDate(birthDate);
            logger.info("Filter by birth date '{}': {} customers", birthDate, customers.size());
        } else {
            customers = customerRepository.getAll();
            logger.info("No filters applied: {} customers", customers.size());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(customers));
    }

    private void handleGetByIdentity(String identity, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Customer> customer = customerRepository.findByIdentity(identity);

        if (customer.isPresent()) {
            logger.info("Found customer: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(customer.get()));
        } else {
            logger.warn("Customer not found: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found: " + identity);
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

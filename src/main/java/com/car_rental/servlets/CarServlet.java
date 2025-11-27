package com.car_rental.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Car;
import com.car_rental.repository.CarRepository;
import com.car_rental.serializer.JsonDataSerializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CarServlet", urlPatterns = {"/cars", "/cars/*"}, loadOnStartup = 1)
public class CarServlet extends BaseServlet {

    private JsonDataSerializer serializer;
    private CarRepository carRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Initializing CarServlet...");
        serializer = new JsonDataSerializer();
        carRepository = (CarRepository) getServletContext().getAttribute("carRepository");

        if (carRepository == null) {
            logger.error("CarRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }
        logger.info("CarServlet initialized successfully.");
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
            Car car = (Car) serializer.fromString(requestBody, Car.class);

            boolean res = carRepository.add(car);
            if (!res) {
                logger.warn("Car already exists: {}", car.getLicensePlate());
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST,
                        "Car already exists: " + car.getLicensePlate());
                return;
            }
            logger.info("Added new car: {}", car.getLicensePlate());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(car));
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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Car license plate is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));

        try {
            String requestBody = getRequestBody(req);
            Car updatedCar = (Car) serializer.fromString(requestBody, Car.class);

            boolean updated = carRepository.update(updatedCar);

            if (!updated) {
                logger.warn("Car not found: {}", identity);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Car not found: " + identity);
                return;
            }

            logger.info("Car updated: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedCar));

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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Car license plate is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));
        boolean removed = carRepository.removeByIdentity(identity);

        if (!removed) {
            logger.warn("Car not found for deletion: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Car not found: " + identity);
            return;
        }

        logger.info("Car deleted: {}", identity);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetWithFilters(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DataSerializationException {

        String licensePlate = req.getParameter("license_plate");
        String model = req.getParameter("model");

        List<Car> cars;

        if (licensePlate != null && !licensePlate.isBlank()) {
            cars = carRepository.findByLicensePlate(licensePlate).map(c -> List.of(c)).orElse(List.of());
            logger.info("Filter by license plate '{}': {} cars", licensePlate, cars.size());
        } else if (model != null && !model.isBlank()) {
            cars = carRepository.findByModel(model);
            logger.info("Filter by model '{}': {} cars", model, cars.size());
        } else {
            cars = carRepository.getAll();
            logger.info("No filters applied: {} cars", cars.size());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(cars));
    }

    private void handleGetByIdentity(String identity, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Car> car = carRepository.findByIdentity(identity);

        if (car.isPresent()) {
            logger.info("Found car: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(car.get()));
        } else {
            logger.warn("Car not found: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Car not found: " + identity);
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

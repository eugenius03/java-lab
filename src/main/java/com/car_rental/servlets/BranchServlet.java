package com.car_rental.servlets;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Branch;
import com.car_rental.repository.BranchRepository;
import com.car_rental.serializer.JsonDataSerializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BranchServlet", urlPatterns = {"/branches", "/branches/*"}, loadOnStartup = 1)
public class BranchServlet extends BaseServlet {

    private JsonDataSerializer serializer;
    private BranchRepository branchRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Initializing BranchServlet...");
        serializer = new JsonDataSerializer();
        branchRepository = (BranchRepository) getServletContext().getAttribute("branchRepository");

        if (branchRepository == null) {
            logger.error("CourseRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }
        logger.info("BranchServlet initialized successfully.");
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
            Branch branch = (Branch) serializer.fromString(requestBody, Branch.class);

            boolean res = branchRepository.add(branch);
            if (!res) {
                logger.warn("Branch already exists: {}", branch.name());
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST,
                        "Branch already exists: " + branch.name());
                return;
            }
            logger.info("Added new branch: {}", branch.name());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(branch));
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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Branch identity is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));

        try {
            String requestBody = getRequestBody(req);
            Branch updatedBranch = (Branch) serializer.fromString(requestBody, Branch.class);

            boolean updated = branchRepository.update(updatedBranch);

            if (!updated) {
                logger.warn("Branch not found: {}", identity);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Branch not found: " + identity);
                return;
            }

            logger.info("Branch updated: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedBranch));

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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Branch identity is required");
            return;
        }

        String identity = decodePathParam(pathInfo.substring(1));
        boolean removed = branchRepository.removeByIdentity(identity);

        if (!removed) {
            logger.warn("Branch not found for deletion: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Branch not found: " + identity);
            return;
        }

        logger.info("Branch deleted: {}", identity);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    private void handleGetWithFilters(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DataSerializationException {

        String name = req.getParameter("name");
        String location = req.getParameter("location");

        List<Branch> branches;

        if (name != null && !name.isBlank()) {
            branches = branchRepository.findByName(name).map(b -> List.of(b)).orElse(List.of());
            logger.info("Filter by name '{}': {} branches", name, branches.size());
        } else if (location != null && !location.isBlank()) {
            branches = branchRepository.findByLocation(location);
            logger.info("Filter by location '{}': {} branches", location, branches.size());
        } else {
            branches = branchRepository.getAll();
            logger.info("No filters applied: {} branches", branches.size());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(branches));
    }

    private void handleGetByIdentity(String identity, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Branch> branch = branchRepository.findByIdentity(identity);

        if (branch.isPresent()) {
            logger.info("Found branch: {}", identity);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(branch.get()));
        } else {
            logger.warn("Branch not found: {}", identity);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Branch not found: " + identity);
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

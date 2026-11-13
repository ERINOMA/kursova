package com.kursova.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursova.model.Enrollment;
import com.kursova.model.EnrollmentStatus;
import com.kursova.service.EnrollmentService;
import com.kursova.util.LoggerUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EnrollmentServlet extends HttpServlet {
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            objectMapper.writeValue(resp.getWriter(), enrollments);
            LoggerUtil.info("HTTP GET: Список зарахувань успішно відправлено");
        } catch (Exception e) {
            LoggerUtil.error("HTTP GET: Помилка", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Внутрішня помилка сервера: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                int courseId = Integer.parseInt(req.getParameter("courseId"));
                int studentId = Integer.parseInt(req.getParameter("studentId"));

                Enrollment enrollment = new Enrollment();
                enrollment.setCourseId(courseId);
                enrollment.setStudentId(studentId);

                enrollmentService.registerNewEnrollment(enrollment);
                objectMapper.writeValue(resp.getWriter(), enrollment);

            } else if ("changeStatus".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                EnrollmentStatus newStatus = EnrollmentStatus.valueOf(req.getParameter("status"));

                enrollmentService.changeStatus(id, newStatus);
                resp.getWriter().write("{\"status\":\"success\"}");

            } else if ("updateFactors".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                int f1 = Integer.parseInt(req.getParameter("f1"));
                int f2 = Integer.parseInt(req.getParameter("f2"));
                int f3 = Integer.parseInt(req.getParameter("f3"));

                enrollmentService.updateFactors(id, f1, f2, f3);
                resp.getWriter().write("{\"status\":\"success\"}");

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Невідома дія (action)\"}");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            LoggerUtil.error("Помилка при обробці HTTP", e);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Внутрішня помилка сервера\"}");
            LoggerUtil.error("Критична помилка", e);
        }
    }
}

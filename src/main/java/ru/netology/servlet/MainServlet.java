package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      switch (method) {
        case "GET":
          if (path.equals("/api/posts")) {
            controller.all(resp);
          } else if (path.matches("/api/posts/\\d+")) {
            final var id = parseIdFromPath(path);
            controller.getById(id, resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        case "POST":
          if (path.equals("/api/posts")) {
            controller.save(req.getReader(), resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        case "DELETE":
          if (path.matches("/api/posts/\\d+")) {
            final var id = parseIdFromPath(path);
            controller.removeById(id, resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        default:
          resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parseIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}
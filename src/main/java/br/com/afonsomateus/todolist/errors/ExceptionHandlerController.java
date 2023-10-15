package br.com.afonsomateus.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleJdbcSQLDataException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O campo title deve ter no máximo 50 caracteres");
  }
}

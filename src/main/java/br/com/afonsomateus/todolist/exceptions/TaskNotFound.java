package br.com.afonsomateus.todolist.exceptions;

public class TaskNotFound extends Exception {
  public TaskNotFound(String message) {
    super(message);
  }
}

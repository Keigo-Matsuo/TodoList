package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	private final HttpSession session;
	
	// ToDo一覧表示
	@GetMapping("/todo")
	public String showTodoList(Model model) {
		List<Todo> todoList = todoRepository.findAll();
		model.addAttribute("todoList", todoList);
		model.addAttribute("todoQuery", new TodoQuery());
		return "todoList";
	}
	
	@PostMapping("/todo/query")
	public String queryTodo(@ModelAttribute TodoQuery todoQuery, BindingResult bindingResult, Model model) {
		List<Todo> todoList = null;
		
		if (todoService.isValid(todoQuery, bindingResult)) {
			// エラーがなければ検索
			todoList = todoService.doQuery(todoQuery);
		}
		
		model.addAttribute("todoList", todoList);
		
		return "todoList";
	}
	
	// ToDo入力フォーム表示
	@GetMapping("/todo/create")
	public String createTodo(Model model) {
		model.addAttribute("todoData", new TodoData());
		session.setAttribute("mode", "create");
		return "todoForm";
	}
	
	// ToDo追加処理
	@PostMapping("/todo/create")
	public String createTodo(@Valid TodoData todoData, BindingResult bindingResult, Model model) {
		// エラーチェック
		boolean isValid = todoService.isValid(todoData, bindingResult);
		
		if (!bindingResult.hasErrors() && isValid) {
			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			// エラーあり
//			model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}
	
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}
	
	@GetMapping("/todo/{id}")
	public String todoById(@PathVariable(name = "id") int id, Model model) {
		Todo todo = todoRepository.findById(id).get();
		model.addAttribute("todoData", todo);
		session.setAttribute("mode", "update");
		return "todoForm";
	}
	
	@PostMapping("/todo/update")
	public String updateTodo(@Valid TodoData todoData, BindingResult bindingResult, Model model) {
		
		// エラーチェック
		boolean isValid = todoService.isValid(todoData, bindingResult);
		
		if (!bindingResult.hasErrors() && isValid) {
			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			// エラーあり
//			model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}
	
	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoData todoData) {
		todoRepository.deleteById(todoData.getId());
		return "redirect:/todo";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

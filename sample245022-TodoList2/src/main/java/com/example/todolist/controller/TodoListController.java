package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.todolist.dao.TodoDaoImpl;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // final指定されたメンバ変数だけを対象としたコンストラクタを生成
//@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	private final HttpSession session;
	
	@PersistenceContext
	private EntityManager entityManager;
	TodoDaoImpl todoDaoImpl;
	
	@PostConstruct
	public void init() {
		todoDaoImpl = new TodoDaoImpl(entityManager);
	}
	
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
//			todoList = todoService.doQuery(todoQuery);
			
			// JPQLによる検索
			todoList = todoDaoImpl.findByJPQL(todoQuery);
			
			// Criteria APIによる検索
//			todoList = todoDaoImpl.findByCriteria(todoQuery);
		}
		
		model.addAttribute("todoList", todoList);
		
		return "todoList";
	}
	
	// ToDo入力フォーム表示
	@PostMapping("/todo/create/form")
	public String createTodo(Model model) {
		model.addAttribute("todoData", new TodoData());
		session.setAttribute("mode", "create");
		return "todoForm";
	}
	
	// ToDo追加処理
	@PostMapping("/todo/create/do")
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

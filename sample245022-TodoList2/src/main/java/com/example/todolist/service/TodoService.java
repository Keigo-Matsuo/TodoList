package com.example.todolist.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodoService {
	
	private final TodoRepository todoRepository;
	
	// 各種検索メソッド
	public List<Todo> doQuery(TodoQuery todoQuery) {
		List<Todo> todoList = null;
		
		if (todoQuery.getTitle().length() > 0) {
		
			// タイトルで検索
			todoList = todoRepository.findByTitleLike("%" + todoQuery.getTitle() + "%");

		} else if (todoQuery.getImportance() != null && todoQuery.getImportance() != -1) {
		
			// 重要度で検索
			todoList = todoRepository.findByImportance(todoQuery.getImportance());

		} else if (todoQuery.getUrgency() != null && todoQuery.getUrgency() != -1) {
	
			// 緊急度で検索
			todoList = todoRepository.findByUrgency(todoQuery.getUrgency());

		} else if (!todoQuery.getDeadlineFrom().equals("") && todoQuery.getDeadlineTo().equals("")) {
		
			// 期限 開始～
			todoList = todoRepository.findByDeadlineGreaterThanEqualOrderByDeadlineAsc(
					Utils.str2date(todoQuery.getDeadlineFrom())
			);

		} else if (todoQuery.getDeadlineFrom().equals("") && !todoQuery.getDeadlineTo().equals("")) {
		
			// 期限 ～終了
			todoList = todoRepository.findByDeadlineLessThanEqualOrderByDeadlineAsc(
					Utils.str2date(todoQuery.getDeadlineTo())
			);

		} else if (!todoQuery.getDeadlineFrom().equals("") && !todoQuery.getDeadlineTo().equals("")) {
			
			// 期限 開始～終了
			todoList = todoRepository.findByDeadlineBetweenOrderByDeadlineAsc(
					Utils.str2date(todoQuery.getDeadlineFrom()),
					Utils.str2date(todoQuery.getDeadlineTo())
			);
			
		} else if (todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
			
			// 完了（OK）で検索
			todoList = todoRepository.findByDone("Y");

		} else {
			
			// 入力条件がなければ全件検索
			todoList = todoRepository.findAll();
		}
		
		return todoList;
	}
	
	
	
	// 自作入力値チェック（※自作バリデーションではない）
	// 入力値がバインドされたtodoDataをチェックして、エラーがあればFieldErrorオブジェクトをbindingResultに追加
	public boolean isValid(TodoData todoData, BindingResult bindingResult) {
		boolean ans = true;
		
		// titleが全角スペースのみの場合エラー
		String title = todoData.getTitle();
		if (title != null && !title.equals("")) {
			boolean isAllDoubleSpace = true;
			for (int i = 0; i < title.length(); i++) {
				if (title.charAt(i) != '　') {
					isAllDoubleSpace = false;
					break;
				}
			}
			
			if (isAllDoubleSpace) {
				FieldError fieldError = new FieldError(
					bindingResult.getObjectName(),
					"title",
					"The title is a full-width space."
				);
				
				bindingResult.addError(fieldError);
				ans = false;
			}
		}
		
		// deadlineが過去の日付ならエラー
		String deadline = todoData.getDeadline();
		if (!deadline.equals("")) {
			LocalDate today = LocalDate.now();
			LocalDate deadlineDate = null;
			
			try {
				deadlineDate = LocalDate.parse(deadline);
				if (deadlineDate.isBefore(today)) {
					FieldError fieldError = new FieldError(
						bindingResult.getObjectName(),
						"deadline",
						"Please enter the date after today"
					);
					
					bindingResult.addError(fieldError);
					ans = false;
				}
			} catch (DateTimeException e) {
				FieldError fieldError = new FieldError(
					bindingResult.getObjectName(),
					"deadline",
					"Please enter the date in the format yyyy-mm-dd."
				);
					
				bindingResult.addError(fieldError);
				ans = false;
			}
		}
		
		return ans;
	}
	
	// 検索フォームチェック
	// 期限開始と期限終了に対するチェック処理
	public boolean isValid(TodoQuery todoQuery, BindingResult bindingResult) {
		boolean ans = true;
		
		// 期限開始の形式チェック
		String date = todoQuery.getDeadlineFrom();
		if (!date.equals("")) {
			try {
				LocalDate.parse(date);
			} catch (DateTimeException e) {
				// parseできない場合
				FieldError fieldError = new FieldError(
					bindingResult.getObjectName(),
					"deadlineFrom",
					"Please enter the date in the format yyyy-mm-dd."
				);
				
				bindingResult.addError(fieldError);
				ans = false;
			}
		}
		
		// 期限終了の形式チェック
		date = todoQuery.getDeadlineTo();
		if (!date.equals("")) {
			try {
				LocalDate.parse(date);
			} catch (DateTimeException e) {
				// parseできない場合
				FieldError fieldError = new FieldError(
					bindingResult.getObjectName(),
					"deadlineTo",
					"Please enter the date in the format yyyy-mm-dd."
				);
				
				bindingResult.addError(fieldError);
				ans = false;
			}
		}
		
		return ans;		
	}
}

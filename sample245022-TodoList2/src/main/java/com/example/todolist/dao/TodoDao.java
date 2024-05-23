package com.example.todolist.dao;

import java.util.List;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoQuery;

public interface TodoDao {

	/*
	 * JPQL : Spring Boot 内部で使用しているクエリ言語。
	 * 操作対象はエンティティオブジェクト。
	 * エンティティはテーブルに紐づけられているので最終的にテーブルの検索となる。
	 */
	// JPQLによる検索
	List<Todo> findByJPQL(TodoQuery todoQuery);
	
	/*
	 * Criteria API : JPQLを生成するAPI
	 * JPQLは文字列として組み立てられるのでプログラムを実行するまで誤りがわからない。（コンパイラは文字列の内部までチェックしない）
	 * Criteria APIはメソッドやメタクラスを使いJPQLを作成することでエラーが起こらないようにする
	 */
	// Criteria API
	List<Todo> findByCriteria(TodoQuery todoQuery);
}

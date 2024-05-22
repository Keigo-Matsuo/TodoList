package com.example.todolist.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * Todoクラスのプロパティはtodoテーブルの列と１対１で対応する。
 * つまりTodoオブジェクト１つでtodoテーブルの１レコードを表せる。
 * このようにテーブルのレコードを表すクラスをエンティティと呼ぶ。
 */

@Entity               // このクラスがエンティティであることを示す。
@Table(name = "todo") // テーブル紐づけ。Todoオブジェクトへの操作は自動的にtodoテーブルのレコードに対する操作になる。
@Data
public class Todo {
	
	@Id // 主キー宣言
	@GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL用の自動採番。strategyに指定する値は、使用するデータベースシステムや自動採番の方法により異なる。
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "importance")
	private Integer importance;
	
	@Column(name = "urgency")
	private Integer urgency;
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "done")
	private String done;
}

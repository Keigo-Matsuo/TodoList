package com.example.todolist.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;

// JpaRepositoryの型引数
// 第１引数：このリポジトリが対象とするエンティティ（クラス）。
// 第２引数：対象エンティティで@Idが指定されているプロパティのクラス

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
	List<Todo> findByTitleLike(String title);
	List<Todo> findByImportance(Integer importance);
	List<Todo> findByUrgency(Integer urgency);
	List<Todo> findByDeadlineBetweenOrderByDeadlineAsc(Date from, Date to);
	List<Todo> findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Date from);
	List<Todo> findByDeadlineLessThanEqualOrderByDeadlineAsc(Date from);
	List<Todo> findByDone(String done);
}

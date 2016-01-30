package com.townscript.todo.dao.task;

import java.util.List;

import com.townscript.todo.model.Task;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDaoHnateImpl extends HibernateDaoSupport implements TaskDao{

	@Override
	public int addTask(Task task) {
		getHibernateTemplate().save(task);
		return task.getId();
	}

	@Override
	public Task loadTask(int taskId) {
		String queryString = "FROM "+ Task.class.getName() +" WHERE id = :taskId";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("taskId", taskId);
		List<Task> tasksList = query.list();
		return tasksList.get(0);	
	}

	@Override
	public List<Task> loadTasksofUsers(int userId) {
		String queryString = "FROM "+ Task.class.getName() +" WHERE user_id = :userid AND mark = :markv";
		boolean markv = false;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("userid", userId);
		query.setParameter("markv", markv);
		List<Task> tasksList = query.list();
		return tasksList;	
	}

	@Override
	public void updateTask(Task task) {
		getHibernateTemplate().update(task);
	}

	@Override
	public void removeTask(int taskId) {
		Task task = new Task();
		task.setId(taskId);
		getHibernateTemplate().delete(task);
	}

	@Override
	public List<Task> loadMarkedTasksofUsers(int userId) {
		String queryString = "FROM "+ Task.class.getName() +" WHERE user_id = :userid AND mark = :markv";
		boolean markv = true;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("userid", userId);
		query.setParameter("markv", markv);
		List<Task> tasksList = query.list();
		return tasksList;	
	}

}

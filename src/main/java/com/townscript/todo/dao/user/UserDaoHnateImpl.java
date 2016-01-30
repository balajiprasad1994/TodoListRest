package com.townscript.todo.dao.user;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.townscript.todo.model.User;

@Repository
public class UserDaoHnateImpl extends HibernateDaoSupport implements UserDao{

	@Override
	public int addUser(User user) {
		getHibernateTemplate().save(user);
		return user.getId();
	}

	@Override
	public void deleteUser(int userId) {
		User user = new User();
		user.setId(userId);
		getHibernateTemplate().delete(user);
	}

	@Override
	public void updateUser(User user) {
		getHibernateTemplate().update(user);
	}

	@Override
	public User isAuthenticUser(String username, String password) {
		String queryString = "FROM "+ User.class.getName() +" WHERE username = :Username AND password = :Password";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("Username", username);
		query.setParameter("Password", password);

		List<User> userList = query.list();
		if(userList == null || userList.isEmpty()){
			return null;
		}
		return userList.get(0);	
	}

	@Override
	public User loadUser(int userId) {
		String queryString = "FROM "+ User.class.getName() +" WHERE id = :userId";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("userId", userId);
		List<User> userList = query.list();
		return userList.get(0);	
	}

}

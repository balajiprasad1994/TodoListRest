package com.townscript.todo.dao.category;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.townscript.todo.model.Category;

@Repository
public class CategoryDaoHnateImpl extends HibernateDaoSupport implements CategoryDao{

	@Override
	public int addCategory(Category category) {
		getHibernateTemplate().save(category);
		return category.getId();
	}

	@Override
	public void removeCategory(int categoryId) {
		Category category = new Category();
		category.setId(categoryId);
		getHibernateTemplate().delete(category);
		
	}

	@Override
	public Category loadCategory(int categoryId) {
		String queryString = "FROM "+ Category.class.getName() +" WHERE id = :categoryId";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("categoryId", categoryId);
		List<Category> categoriesList = query.list();
		if(categoriesList!= null && !categoriesList.isEmpty()){
		return categoriesList.get(0);	
		}
		else 
			return null;
	}

	@Override
	public void updateCategory(Category category) {
		getHibernateTemplate().update(category);
	}

	@Override
	public Category getCategoryofTask(int taskId) {
		String queryString = "FROM "+ Category.class.getName() +" WHERE taskids like :taskid";
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(queryString);

		query.setParameter("taskid", "%"+taskId+"%");
		List<Category> categoriesList = query.list();
		if(categoriesList!= null && !categoriesList.isEmpty()){
		return categoriesList.get(0);
		}
		else 
			return null;
	}

}

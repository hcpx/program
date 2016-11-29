package com.hysd.qbe;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.junit.Test;

import com.hysd.domain.Emp;
import com.hysd.util.HibernateUtils;

/**
 * 测试QBE查询
 */
public class TestQBE {

	/** QBE查询 适合条件检索 */
	@Test
	public void test_qbe() {
		Emp emp = new Emp();
		emp.setDeptno(10);
		emp.setJob("MANAGER");

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Emp.class);
		criteria.add(Example.create(emp));// 注意
		List<Emp> list = criteria.list();
		show(list);
		session.close();
	}

	// 遍历集合
	private void show(List<Emp> list) {
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
	
}

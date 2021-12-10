package edu.neu.crm.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.ibatis.session.SqlSession;

public class TransactionInvocationHandler implements InvocationHandler{
	
	private Object target;
	
	public TransactionInvocationHandler(Object target){
		
		this.target = target;
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		SqlSession session = null;
		
		Object obj = null;
		
		try{
			//这里获得的SqlSession对象是线程唯一的，也就是真正执行sql语句的那个SqlSession对象
			session = SqlSessionUtil.getSqlSession();
			
			obj = method.invoke(target, args);
			//在代理类中进行事务处理
			session.commit();
		}catch(Exception e){
			//业务处理中一旦发生异常，立刻回滚事务
			session.rollback();
			e.printStackTrace();
			
			//如果被代理的业务类中发生了异常会被在这里直接处理
			// 如果要在控制层处理就继续往上抛，处理的是什么异常，继续往上抛什么异常
			throw e.getCause();
		}finally{
			SqlSessionUtil.myClose(session);
		}
		
		return obj;
	}
	
	public Object getProxy(){
		
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
		
	}
	
}












































package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedis(){
		
		//创建一个jedis对象，构造方法，两个参数host、port
		Jedis jedis=new Jedis("192.168.25.128",6379);
		//直接使用jedis对象操作redis，对应redis命令都有一个同名方法
		jedis.set("mytest", "10000");
		String result = jedis.get("mytest");
		System.out.println(result);
		//关闭连接
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
	//创建一个jedisPool对象，两个参数host、post
	JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
	//从连接池中获得连接，得到一个jedis对象
	Jedis jedis = jedisPool.getResource();
	//使用jedis对象获取数据
	String string = jedis.get("mytest");
	//打印结果
	System.out.println(string);
	//关闭jedis,让连接池回收连接，每次使用用完毕后都需要关闭
	jedis.close();
	//系统结束之前关闭jedisPool
	jedisPool.close();
	}
	
	@Test
	public void testJedisCluster(){
		//创建JedisCluster对象，构造方法参数只有一个nodes是一个Set，set中有多个HostaAndPort对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用JedisCluster对象管理redis
		jedisCluster.set("mytest", "abcdef");
		String string = jedisCluster.get("mytest");
		//打印结果
		System.out.println(string);
		//关闭JedisCluster（在系统中可以是单利的）
		jedisCluster.close();
	}
	
	
}

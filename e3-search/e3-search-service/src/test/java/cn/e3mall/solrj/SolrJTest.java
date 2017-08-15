package cn.e3mall.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {

	@Test
	public void addDucoment() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		//创建一个文本对象
		SolrInputDocument document=new SolrInputDocument();
		//向文档对象中添加域
		document.addField("id",2);
		document.addField("item_title", "2商品测试");
		document.addField("item_price", 1000);
		//将对象写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	
	}
	

	@Test
	public void deleteDucomentBid() throws Exception{
		
		//创建一个SolrServer对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		//根据ID删除
		solrServer.deleteById("1");
		//提交
		solrServer.commit();
		
	}
	
	@Test
	public void deleteDucomentByQuery() throws Exception{
		
		//创建一个SolrServer对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		//根据查询删除
		solrServer.deleteByQuery("*:*");
		//提交
		solrServer.commit();
		
	}
	
	@Test
	public void searchIndex()throws Exception{
		
		//创建一个SolrServer对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		//创建一个solrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("*:*");
		//执行查询得到一个QueryResponse
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取查询结果
		SolrDocumentList results = queryResponse.getResults();
		//查询总记录数
		System.out.println(results.getNumFound());
		//打印结果
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	
	
	@Test
	public void searchIndexHightLight()throws Exception{

		//创建一个SolrServer对象
				SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
				//创建一个SolrQuery对象
				SolrQuery solrQuery = new SolrQuery();
				//设置查询条件
				solrQuery.setQuery("手机");
				solrQuery.set("df", "item_title");
				solrQuery.setHighlight(true);
				solrQuery.addHighlightField("item_title");
				solrQuery.setHighlightSimplePre("<em>");
				solrQuery.setHighlightSimplePost("</em>");
				//执行查询
				QueryResponse queryResponse = solrServer.query(solrQuery);
				//取查询结果
				SolrDocumentList solrDocumentList = queryResponse.getResults();
				//取查询结果总记录数
				System.out.println(solrDocumentList.getNumFound());
				//取高亮显示
				Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
				//遍历列表，取高亮结果
				for (SolrDocument solrDocument : solrDocumentList) {
					System.out.println(solrDocument.get("id"));
					//取高亮结果
					String title = "";
					List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
					if (list != null && list.size() > 0) {
						title = list.get(0);
					} else {
						title = solrDocument.get("item_title").toString();
					}
					System.out.println(title);
					System.out.println(solrDocument.get("item_price"));
					System.out.println(solrDocument.get("item_sell_point"));
				}
						
			}
	
	
}

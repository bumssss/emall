package cn.e3mall.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;

/*
 * 查询索引库的dao
 */
@Repository
public class SearchDaoImpl implements SearchDao{

	@Autowired
	private SolrServer solrServer;
	
	
	@Override
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		// 1、根据SolrQuery执行查询
				QueryResponse queryResponse = solrServer.query(solrQuery);
				// 2、取查询结果
				SolrDocumentList solrDocumentList = queryResponse.getResults();
				// 3、取查询结果的总记录数
				long numFound = solrDocumentList.getNumFound();
				// 4、取高亮结果
				Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
				// 5、取商品列表
				List<SearchItem> itemList = new ArrayList<>();
				for (SolrDocument solrDocument : solrDocumentList) {
					SearchItem searchItem = new SearchItem();
					searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
					searchItem.setId((String) solrDocument.get("id"));
					searchItem.setImage((String) solrDocument.get("item_image"));
					searchItem.setPrice((long) solrDocument.get("item_price"));
					searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
					//取高亮结果
					String itemTitle = "";
					List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
					if (list !=null && list.size() > 0) {
						itemTitle = list.get(0);
					} else {
						itemTitle = (String) solrDocument.get("item_title");
					}
					searchItem.setTitle(itemTitle);
					//添加到商品列表
					itemList.add(searchItem);
				}
				// 6、把结果封装到SearchResult对象中
				SearchResult searchResult = new SearchResult();
				searchResult.setItemList(itemList);
				searchResult.setRecourdCount(numFound);
				// 7、返回结果
				return searchResult;
	}

}

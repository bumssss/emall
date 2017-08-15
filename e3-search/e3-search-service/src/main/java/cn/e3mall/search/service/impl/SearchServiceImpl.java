package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/*
 * 商品搜索服务
 */
@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		// 创建SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//向SolrQuery对象中设置查询条件
		solrQuery.setQuery(keyWord);
		//分页条件
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		//设置默认搜索域
		solrQuery.set("df","item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		// 3、调用dao执行查询
		SearchResult searchResult = searchDao.search(solrQuery);
		// 4、需要计算总页数
		long recourdCount = searchResult.getRecourdCount();
		long pageCount = recourdCount / rows;
		if (recourdCount % rows != 0) {
			pageCount++;
		}
		searchResult.setTotalPages(pageCount);
		// 5、返回查询结果
		return searchResult;
	}

}

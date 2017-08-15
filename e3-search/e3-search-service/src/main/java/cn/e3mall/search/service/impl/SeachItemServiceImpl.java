package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SeachItemService;
@Service
public class SeachItemServiceImpl implements SeachItemService{

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public E3Result importindex() {
		try {
			// 1、查询所有的商品数据
			List<SearchItem> itemList = searchItemMapper.getItemList();
			// 2、遍历商品数据，全部插入到索引库中。需要使用solrJ
			for (SearchItem searchItem : itemList) {
				// 1）创建一个SolrServer对象，可以在spring的配置文件中定义。
				// 2）创建一个文档对象
				SolrInputDocument document = new SolrInputDocument();
				// 3）向文档对象中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				// 4）把文档对象写入索引库
				solrServer.add(document);
			}
			// 5）提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, e.getMessage());
		}
		// 3、返回导入成功
		return E3Result.ok();
	}
}

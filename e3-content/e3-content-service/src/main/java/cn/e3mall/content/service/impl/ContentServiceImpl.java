package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public DataGridResult getContentList(long categoryId, int page, int rows) {
		//1、设置分页信息。使用mybateis分页插件
		PageHelper.startPage(page, rows);
		//2、执行查询，根据分类ID查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//3、取分页结果total
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		//4封装到DataGridResult对象中
		DataGridResult result = new DataGridResult();
		result.setTotal(total);
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result addContent(TbContent content) {
		// 1、补全pojo对象属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//2、插入到数据库
		contentMapper.insert(content);
		//缓存同步
		//删除cid对应的缓存
		jedisClient.hdel("content-info", content.getCategoryId().toString());
		
		
		//3返回成功
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long categoryId) {
		//查询数据库之前查询缓存
		try {
			//如果查询到结果直接返回
			String json = jedisClient.hget("content-info", categoryId+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
			return list;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		//如果没有结果查询数据库
		
		// 创建一个查询条件，根据内容分类ID查询
		TbContentExample contentExample = new TbContentExample();
		Criteria createCriteria = contentExample.createCriteria();
		createCriteria.andCategoryIdEqualTo(categoryId);
		//2、执行查询
		List<TbContent> list = contentMapper.selectByExample(contentExample);
		//把结果添加到缓存
		try {
			jedisClient.hset("content-info", categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//3/返回结果
		return list;
	}

}

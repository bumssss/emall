package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/*
 * 内容分类接口service
 */
@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<TreeNode> getContentList(long parentId) {
		// 1、创建查询条件
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//2、执行查询，根据parentId查询内容分类列表
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		
		//3、把内容分类列表转换成TreeNode列表
		List<TreeNode> resultList = new ArrayList<TreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setText(tbContentCategory.getName());
			treeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(treeNode);
			
		}
		//返回结果
		return resultList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 1创建一个TbContentCategory对象
		TbContentCategory contentCategory = new TbContentCategory();
		//2、补全属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1正常     2删除
		contentCategory.setStatus(1);
		//默认排序值是1
		contentCategory.setSortOrder(1);
		//1位true  0为false  新添加节点一定是false
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//3、插入表中
		contentCategoryMapper.insert(contentCategory);
		//4、需要判断父节点的isparent
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(parentId);
		//5如果父节点的isparent为false应改为true
		if(!category.getIsParent()){
			
			category.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(category);
		}
		//6返回E3Result其中包含TbContentCategory对象
		
		return E3Result.ok(contentCategory);
	}

}

package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
/*
 * 商品分类管理service
 */

@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	
	@Override
	public List<TreeNode> getItemCatList(long parentId) {
		//1 根据parebtid查询子节点列表
		TbItemCatExample catExample = new TbItemCatExample();
		Criteria criteria = catExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(catExample);
		//2把列表转换成list<TreeNode>
		List<TreeNode> treeNodes = new ArrayList<>();
		
		for (TbItemCat tbItemCat : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			
		//如果是父节点“colsed”,如果是子节点“open”
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到节点列表
			treeNodes.add(treeNode);
		}
		//返回结果
		return treeNodes;
	}

}

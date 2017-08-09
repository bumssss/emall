package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;

/*
 * 商品分类管理controller
 */


@Controller
public class itemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> getItemCatList(@RequestParam(value="id",defaultValue="0")long parentId){
		
		List<TreeNode> catList = itemCatService.getItemCatList(parentId);
		
		return catList;
		
	}
	
}

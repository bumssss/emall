package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/*
 * 商城首页处理
 */
@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${index.slider.cid}")
	private Long indexSliderCid;
	@RequestMapping("/default")
	public String showIndex(Model model){
		//展示页面之前查询内容列表
		List<TbContent> listByCid = contentService.getContentListByCid(indexSliderCid);
		//把结果传递给jsp页面
		model.addAttribute("ad1List",listByCid);
		//返回逻辑视图
		return "index";
	}
	
}

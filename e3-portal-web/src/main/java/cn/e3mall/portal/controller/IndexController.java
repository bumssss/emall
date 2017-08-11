package cn.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 商城首页处理
 */
@Controller
public class IndexController {

	
	@RequestMapping("/default")
	public String showIndex(){
		
		return "index";
	}
	
}

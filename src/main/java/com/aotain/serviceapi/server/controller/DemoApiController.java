package com.aotain.serviceapi.server.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apidemo")
public class DemoApiController {
    @Value("${spring.application.name}")
    private String appname;
    @Value("${server.port}")
    private String port;

//	@Autowired
//	private DemoServiceImpl demoServiceImpl;

	@RequestMapping(value="/testhello",method = { RequestMethod.POST })
	public boolean testhello(){
//		demoServiceImpl.hello();
		return true;
	}
    @RequestMapping(value="/sayhello",method = { RequestMethod.POST })
    public String sayHello(@RequestBody @ApiParam(required = true, name = "ubean", value="用户实体UserBean")UserBean ubean){
        return "{\"url\":  \"http://"+appname+":"+port+"/"+ubean.getName()+"/\"}";
    }
    
    @RequestMapping(value="/sayhello1",method = { RequestMethod.POST })
    public UrlBean sayHello1(@RequestBody @ApiParam(required = true, name = "ubean", value="用户实体UserBean")UserBean ubean){
    	// "{\"url\":  \"http://"+appname+":"+port+"/"+ubean.getName()+"/\"}"
    	UrlBean b = new UrlBean();
    	b.setUrl("http://"+appname+":"+port+"/"+ubean.getName()+"/");
        return b;
    }

    public String callback(){
        return "sorry";
    }
    
    static class UrlBean{
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return "UrlBean [url=" + url + "]";
		}
		
	}
    
    static class UserBean {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}
